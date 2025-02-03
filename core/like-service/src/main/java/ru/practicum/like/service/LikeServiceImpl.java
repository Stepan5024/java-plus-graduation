package ru.practicum.like.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.core.api.dto.event.EventFullDto;
import ru.practicum.core.api.enums.Status;
import ru.practicum.core.api.error.NotFoundException;
import ru.practicum.core.api.error.RestrictionsViolationException;
import ru.practicum.like.dto.EventDto;
import ru.practicum.like.dto.UserRequestDto;
import ru.practicum.like.dto.mapper.EventMapper;
import ru.practicum.like.exchange.EventFeignClient;
import ru.practicum.like.exchange.RequestFeignClient;
import ru.practicum.like.exchange.UserFeignClient;
import ru.practicum.like.model.Like;
import ru.practicum.like.model.StatusLike;
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


    @Override
    @Transactional
    public EventFullDto addLike(long eventId, long userId, StatusLike statusLike) {
        log.info("The beginning of the process of adding like to an event");

        // Получаем информацию о пользователе через Feign Client
        UserRequestDto userDto = userFeignClient.getUserById(userId);
        if (userDto == null) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        // Получаем информацию о событии через Feign Client
        EventDto eventDto = eventFeignClient.getEventById(eventId);
        if (eventDto == null) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        // Проверяем, является ли пользователь участником события
        if (!requestFeignClient.existsByEventAndRequesterAndStatus(eventId, userId, Status.CONFIRMED)) {
            throw new RestrictionsViolationException("In order to like, you must be a participant in the event");
        }

        // Проверяем, не поставил ли пользователь уже лайк
        if (likeRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new RestrictionsViolationException("You have already rated this event");
        }

        // Проверяем, не является ли пользователь инициатором события
        if (eventDto.getInitiatorId().equals(userId)) {
            throw new RestrictionsViolationException("The initiator of the event cannot rate himself");
        }

        // Создаем объект Like, заполняем его минимально необходимыми данными
        Like like = new Like();
        like.setEventId(eventId); // Используем ID события
        like.setUserId(userId);   // Используем ID пользователя
        like.setStatus(statusLike);
        like.setCreated(LocalDateTime.now());

        // Сохраняем лайк в базе данных
        likeRepository.save(like);

        // Обновляем рейтинг пользователя и события
        changeRatingUserAndEvent(eventDto, statusLike, DIFFERENCE_RATING_BY_ADD);

        log.info("The {} was added", statusLike);
        return eventMapper.eventToEventFullDto(eventDto);
    }

    @Override
    @Transactional
    public EventFullDto updateLike(long eventId, long userId, StatusLike statusLike) {
        log.info("The beginning of the process of updating like for eventId={} and userId={}", eventId, userId);

        // Fetch User information from User service using Feign Client
        UserRequestDto userDto = userFeignClient.getUserById(userId);
        if (userDto == null) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        // Fetch Event information from Event service using Feign Client
        EventDto eventDto = eventFeignClient.getEventById(eventId);
        if (eventDto == null) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        // Check if the like exists based on eventId and userId
        Optional<Like> likeOptional = likeRepository.findByEventIdAndUserId(eventId, userId);

        if (likeOptional.isPresent()) {
            Like like = likeOptional.get();

            // Check if the like already has the same status
            if (like.getStatus() == statusLike) {
                throw new RestrictionsViolationException("You have already " + statusLike + " this event");
            }

            // Update the like's status
            like.setStatus(statusLike);
            like.setCreated(LocalDateTime.now());
            likeRepository.save(like);  // Save the updated like entity

            // Update ratings for the user and the event
            changeRatingUserAndEvent(eventDto, statusLike, DIFFERENCE_RATING_BY_UPDATE);
        } else {
            throw new NotFoundException("You didn't rate this event");
        }

        // Return the updated Event DTO
        return eventMapper.eventToEventFullDto(eventDto);
    }

    @Override
    @Transactional
    public void deleteLike(long eventId, long userId) {
        log.info("The beginning of the process of deleting like for eventId={} and userId={}", eventId, userId);

        // Fetch User information from User service using Feign Client
        UserRequestDto userDto = userFeignClient.getUserById(userId);
        if (userDto == null) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        // Fetch Event information from Event service using Feign Client
        EventDto eventDto = eventFeignClient.getEventById(eventId);
        if (eventDto == null) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        // Check if the like exists based on eventId and userId
        Optional<Like> likeOptional = likeRepository.findByEventIdAndUserId(eventId, userId);

        if (likeOptional.isPresent()) {
            Like like = likeOptional.get();
            StatusLike statusLike = like.getStatus();

            // Delete the like
            likeRepository.delete(like);

            // Update ratings for the user and the event
            changeRatingUserAndEvent(eventDto, statusLike, DIFFERENCE_RATING_BY_DELETE);
        } else {
            throw new RestrictionsViolationException("You haven't reacted yet");
        }

        log.info("The reaction was deleted successfully for eventId={} and userId={}", eventId, userId);
    }

    private void changeRatingUserAndEvent(EventDto eventDto, StatusLike statusLike, int difference) {
        // Получаем инициатора события по ID через Feign Client
        UserRequestDto initiatorDto = userFeignClient.getUserById(eventDto.getInitiatorId());
        if (initiatorDto == null) {
            throw new NotFoundException("Initiator with id=" + eventDto.getInitiatorId() + " was not found");
        }

        // Изменяем рейтинг инициатора и события в зависимости от статуса лайка
        if (statusLike == StatusLike.LIKE) {
            // Обновляем рейтинг инициатора события
            userFeignClient.updateUserRating(initiatorDto.getId(), initiatorDto.getRating() + difference);

            // Обновляем рейтинг события
            eventFeignClient.updateEventRating(eventDto.getId(), eventDto.getRating() + difference);
        } else if (statusLike == StatusLike.DISLIKE) {
            // Обновляем рейтинг инициатора события
            userFeignClient.updateUserRating(initiatorDto.getId(), initiatorDto.getRating() - difference);

            // Обновляем рейтинг события
            eventFeignClient.updateEventRating(eventDto.getId(), eventDto.getRating() - difference);
        }
    }

}
