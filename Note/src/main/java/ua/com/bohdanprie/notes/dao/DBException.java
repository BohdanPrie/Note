package ua.com.bohdanprie.notes.dao;

public class DBException extends RuntimeException {
    public DBException() {
        super();
    }

    public DBException(String message) {
        super(message);
    }
    
    public DBException(String message, Throwable cause) {
        super(message, cause);
    }
}