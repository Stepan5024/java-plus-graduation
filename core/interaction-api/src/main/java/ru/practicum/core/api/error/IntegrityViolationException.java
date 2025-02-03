package ru.practicum.core.api.error;

public class IntegrityViolationException extends RuntimeException {
    public IntegrityViolationException(String message) {
        super(message);
    }
}
