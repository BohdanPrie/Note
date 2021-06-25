package ua.com.bohdanprie.notes.domain;

public class AuthorisationException extends RuntimeException {
	public AuthorisationException() {
		super();
	}

	public AuthorisationException(String message) {
		super(message);
	}

	public AuthorisationException(String message, Throwable cause) {
		super(message, cause);
	}
}
