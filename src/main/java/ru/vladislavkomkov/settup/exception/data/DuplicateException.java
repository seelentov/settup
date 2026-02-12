package ru.vladislavkomkov.settup.exception.data;

public class DuplicateException extends RuntimeException {
    public DuplicateException(String message) {
        super(message);
    }
}
