package ua.com.bohdanprie.notes.domain.exceptions;

public class NoSuchUserException extends RuntimeException {
	public NoSuchUserException() {
		super();
	}

	public NoSuchUserException(String message) {
		super(message);
	}

	public NoSuchUserException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoSuchUserException(Throwable cause) {
		super(cause);
	}
}
