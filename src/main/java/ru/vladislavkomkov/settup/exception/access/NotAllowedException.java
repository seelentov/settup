package ru.vladislavkomkov.settup.exception.access;

public class NotAllowedException extends RuntimeException {
    public NotAllowedException(String message) {
        super(message);
    }
}
