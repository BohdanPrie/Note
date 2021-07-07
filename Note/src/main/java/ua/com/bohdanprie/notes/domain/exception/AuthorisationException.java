package ua.com.bohdanprie.notes.domain.exception;
/**
 * Thrown when user failed his authentication
 * <br>For example: wrong password, wrong login etc.
 * @author bohda
 *
 */
@SuppressWarnings("serial")
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
	
	public AuthorisationException(Throwable cause) {
		super(cause);
	}
}
