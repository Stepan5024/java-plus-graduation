package ru.practicum.core.api.error;

public class RestrictionsViolationException extends RuntimeException {
    public RestrictionsViolationException(String message) {
        super(message);
    }
}
