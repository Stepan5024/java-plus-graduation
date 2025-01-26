package ru.practicum.like.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import ru.practicum.compilation.dto.EventDto;
import ru.practicum.compilation.dto.EventFullDto;
import ru.practicum.compilation.model.Status;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RestrictionsViolationException;
import ru.practicum.like.dto.UserDto;
import ru.practicum.like.dto.UserMapper;
import ru.practicum.like.dto.UserRequestDto;
import ru.practicum.like.dto.mapper.EventMapper;
import ru.practicum.like.exchange.EventFeignClient;
import ru.practicum.like.exchange.RequestFeignClient;
import ru.practicum.like.exchange.UserFeignClient;
import ru.practicum.like.model.Event;
import ru.practicum.like.model.Like;
import ru.practicum.like.model.StatusLike;
import ru.practicum.like.model.User;
import ru.practicum.like.repository.LikeRepository;


import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private static final int DIFFERENCE_RATING_BY_ADD = 1;
    private static final int DIFFERENCE_RATING_BY_DELETE = -1;
    private static final int DIFFERENCE_RATING_BY_UPDATE = 2;

    private final EventFeignClient eventFeignClient;
    private final LikeRepository likeRepository;
    private final UserFeignClient userFeignClient;
    private final RequestFeignClient requestFeignClient;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public EventFullDto addLike(long eventId, long userId, StatusLike statusLike) {
        log.info("The beginning of the process of adding like to an event");
        UserRequestDto userDto = userFeignClient.getUserById(userId);
        if (userDto == null) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        User user = userMapper.userRequestDtoToUser(userDto);

        EventDto eventDto = eventFeignClient.getEventById(eventId);
        if (eventDto == null) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        Event event = eventMapper.fromDto(eventDto);
        if (!requestFeignClient.existsByEventAndRequesterAndStatus(eventId, userId, Status.CONFIRMED)) {
            throw new RestrictionsViolationException("In order to like, you must be a participant in the event");
        }

        if (likeRepository.existsByEventAndUser(event, user)) {
            throw new RestrictionsViolationException("You have already rated this event");
        }

        if (event.getInitiator().getId() == userId) {
            throw new RestrictionsViolationException("The initiator of the event cannot rate himself");
        }

        Like like = new Like();
        like.setUser(user);
        like.setEvent(event);
        like.setStatus(statusLike);
        like.setCreated(LocalDateTime.now());
        likeRepository.save(like);
        changeRatingUserAndEvent(event, statusLike, DIFFERENCE_RATING_BY_ADD);

        log.info("The {} was added", statusLike);
        return eventMapper.eventToEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateLike(long eventId, long userId, StatusLike statusLike) {
        log.info("The beginning of the process of updating like to an event");
        UserRequestDto userDto = userFeignClient.getUserById(userId);
        if (userDto == null) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
        User user = userMapper.userRequestDtoToUser(userDto);

        EventDto eventDto = eventFeignClient.getEventById(eventId);
        if (eventDto == null) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        Event event = eventMapper.fromDto(eventDto);

        Optional<Like> likeOptional = likeRepository.findByEventAndUser(event, user);

        if (likeOptional.isPresent()) {
            Like like = likeOptional.get();
            if (like.getStatus() == statusLike) {
                throw new RestrictionsViolationException("You have already " + statusLike + " this event");
            }
            like.setStatus(statusLike);
            like.setCreated(LocalDateTime.now());
            changeRatingUserAndEvent(event, statusLike, DIFFERENCE_RATING_BY_UPDATE);
        } else {
            throw new NotFoundException("You didn't rate this event");
        }

        return eventMapper.eventToEventFullDto(event);
    }

    @Override
    @Transactional
    public void deleteLike(long eventId, long userId) {
        log.info("The beginning of the process of deleting like to an event");
        UserRequestDto userDto = userFeignClient.getUserById(userId);
        if (userDto == null) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
        User user = userMapper.userRequestDtoToUser(userDto);
        EventDto eventDto = eventFeignClient.getEventById(eventId);
        if (eventDto == null) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        Event event = eventMapper.fromDto(eventDto);

        Optional<Like> likeOptional = likeRepository.findByEventAndUser(event, user);

        if (likeOptional.isPresent()) {
            Like like = likeOptional.get();
            StatusLike statusLike = like.getStatus();
            likeRepository.delete(like);
            changeRatingUserAndEvent(event, statusLike, DIFFERENCE_RATING_BY_DELETE);
        } else {
            throw new RestrictionsViolationException("You haven't reacted it yet");
        }
        log.info("The reaction was deleted");
    }

    private void changeRatingUserAndEvent(Event event, StatusLike statusLike, int difference) {
        UserRequestDto initiatorDto = userFeignClient.getUserById(event.getInitiator().getId());
        if (initiatorDto == null) {
            throw new NotFoundException("Initiator with id=" + event.getInitiator().getId() + " was not found");
        }
        User initiatorEvent = userMapper.userRequestDtoToUser(initiatorDto);
        if (statusLike == StatusLike.LIKE) {
            initiatorEvent.setRating(initiatorEvent.getRating() + difference);
            event.setRating(event.getRating() + difference);
        } else if (statusLike == StatusLike.DISLIKE) {
            initiatorEvent.setRating(initiatorEvent.getRating() - difference);
            event.setRating(event.getRating() - difference);
        }
    }
}
