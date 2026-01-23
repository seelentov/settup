package ru.vladislavkomkov.settup.exception;

public class StaticException extends RuntimeException {
    public StaticException(String message) {
        super(message);
    }
    
    public StaticException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
