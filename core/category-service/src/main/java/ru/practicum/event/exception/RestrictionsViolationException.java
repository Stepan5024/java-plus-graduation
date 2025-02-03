package ru.practicum.event.exception;

public class RestrictionsViolationException extends RuntimeException {
    public RestrictionsViolationException(String message) {
        super(message);
    }
}
