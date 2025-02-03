package ru.practicum.user.exception;

public class RestrictionsViolationException extends RuntimeException {
    public RestrictionsViolationException(String message) {
        super(message);
    }
}
