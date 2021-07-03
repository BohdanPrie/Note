package ua.com.bohdanprie.notes.dao.exception;

@SuppressWarnings("serial")
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
	
	public DBException(Throwable cause) {
		super(cause);
	}
}