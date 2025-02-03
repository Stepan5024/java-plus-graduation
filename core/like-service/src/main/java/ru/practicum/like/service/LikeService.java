package ru.practicum.like.service;


import ru.practicum.like.dto.EventDto;
import ru.practicum.like.model.StatusLike;

public interface LikeService {
    EventDto addLike(long eventId, long userId, StatusLike statusLike);

    EventDto updateLike(long eventId, long userId, StatusLike statusLike);

    void deleteLike(long eventId, long userId);
}
