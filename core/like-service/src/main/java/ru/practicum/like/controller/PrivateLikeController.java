package ru.practicum.like.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.like.service.LikeService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PrivateLikeController {

    private static final String USERS_PATH = "/users/{userId}";
    private static final String EVENTS_LIKES_PATH = USERS_PATH + "/events/{eventId}/likes";
    private static final String LOCATIONS_LIKES_PATH = USERS_PATH + "/locations/{locationId}/likes";

    private final LikeService likeService;

    @PutMapping(EVENTS_LIKES_PATH)
    public Long addEventLike(@PathVariable long userId, @PathVariable long eventId) {
        log.info("==> PUT {} - Adding like for event with id: {} by user with id: {}", EVENTS_LIKES_PATH, eventId, userId);
        Long countOfLikes = likeService.addEventLike(eventId, userId);
        log.info("<== PUT {} - Like added. Current count of likes: {}", EVENTS_LIKES_PATH, countOfLikes);
        return countOfLikes;
    }

    @DeleteMapping(EVENTS_LIKES_PATH)
    public Long deleteEventLike(@PathVariable long userId, @PathVariable long eventId) {
        log.info("==> DELETE {} - Deleting like for event with id: {} by user with id: {}", EVENTS_LIKES_PATH, eventId, userId);
        Long countOfLikes = likeService.deleteEventLike(eventId, userId);
        log.info("<== DELETE {} - Like deleted. Current count of likes: {}", EVENTS_LIKES_PATH, countOfLikes);
        return countOfLikes;
    }

    @PutMapping(LOCATIONS_LIKES_PATH)
    public Long addLocationLike(@PathVariable long userId, @PathVariable long locationId) {
        log.info("==> PUT {} - Adding like for location with id: {} by user with id: {}", LOCATIONS_LIKES_PATH, locationId, userId);
        Long locationLikesCount = likeService.addLocationLike(locationId, userId);
        log.info("<== PUT {} - Like added. Current count of likes: {}", LOCATIONS_LIKES_PATH, locationLikesCount);
        return locationLikesCount;
    }

    @DeleteMapping(LOCATIONS_LIKES_PATH)
    public Long deleteLocationLike(@PathVariable long userId, @PathVariable long locationId) {
        log.info("==> DELETE {} - Deleting like for location with id: {} by user with id: {}", LOCATIONS_LIKES_PATH, locationId, userId);
        Long locationLikesCount = likeService.deleteLocationLike(locationId, userId);
        log.info("<== DELETE {} - Like deleted. Current count of likes: {}", LOCATIONS_LIKES_PATH, locationLikesCount);
        return locationLikesCount;
    }
}