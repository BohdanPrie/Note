package ua.com.bohdanprie.notes.dao.exception;
/**
 * Thrown when fail in connection with database occurs
 * <br>For example: fail to connect to DB, fail to get connection from connection pool
 * @author bohda
 *
 */
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