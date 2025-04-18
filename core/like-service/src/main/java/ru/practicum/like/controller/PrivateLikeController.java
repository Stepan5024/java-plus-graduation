package ru.practicum.like.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.like.service.LikeService;

@RestController
@RequiredArgsConstructor
public class PrivateLikeController {

    private final LikeService likeService;
    private static final String EVENT_LIKE_PATH = "/users/{userId}/events/{eventId}/likes";
    private static final String LOCATION_LIKE_PATH = "/users/{userId}/locations/{locationId}/likes";

    @PutMapping(EVENT_LIKE_PATH)
    public Long addEventLike(@PathVariable long userId, @PathVariable long eventId) {
        return likeService.addEventLike(eventId, userId);
    }

    @DeleteMapping(EVENT_LIKE_PATH)
    public Long deleteEventLike(@PathVariable long userId, @PathVariable long eventId) {
        return likeService.deleteEventLike(eventId, userId);
    }

    @PutMapping(LOCATION_LIKE_PATH)
    public Long addLocationLike(@PathVariable long userId, @PathVariable long locationId) {
        return likeService.addLocationLike(locationId, userId);
    }

    @DeleteMapping(LOCATION_LIKE_PATH)
    public Long deleteLocationLike(@PathVariable long userId, @PathVariable long locationId) {
        return likeService.deleteLocationLike(locationId, userId);
    }

}
