package ua.com.bohdanprie.notes.dao.exception;
/**
 * Thrown when fail in work with database occurs
 * @author bohda
 *
 */
@SuppressWarnings("serial")
public class DaoException extends RuntimeException{
    public DaoException() {
        super();
    }

    public DaoException(String message) {
        super(message);
    }
    
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DaoException(Throwable cause) {
		super(cause);
	}
}