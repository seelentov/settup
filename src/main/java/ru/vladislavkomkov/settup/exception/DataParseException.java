package ru.vladislavkomkov.settup.exception;

public class DataParseException extends RuntimeException {
    public DataParseException(Throwable throwable) {
        super(throwable);
    }
    
    public DataParseException(String message) {
        super(message);
    }
    
    public DataParseException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
