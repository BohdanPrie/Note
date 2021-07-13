package ua.com.bohdanprie.notes.dao.entityDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.dao.DaoManager;
import ua.com.bohdanprie.notes.dao.DaoUtils;
import ua.com.bohdanprie.notes.dao.exception.DBException;
import ua.com.bohdanprie.notes.dao.exception.DaoException;
import ua.com.bohdanprie.notes.domain.entity.Note;
import ua.com.bohdanprie.notes.domain.entity.User;
/**
 * Class provide work with CRUD operations with {@link Note}
 * @author bohda
 *
 */
public class NoteDao {
	private final DaoManager daoManager;
	private static final Logger LOG = LogManager.getLogger(NoteDao.class.getName());

	public NoteDao() {
		LOG.trace("Getting DaoManager instance");
		daoManager = DaoManager.getInstance();
		LOG.debug("NoteDao initialized");
	}

	/**
	 * Method changes given {@link Note} at {@link User} in DataBase
	 * @throws DaoException if failed to change {@link Note}
	 * @param note
	 * @param user
	 */
	public void change(Note note, User user) {
		LOG.trace("Changing note of user " + user.getLogin());
		String SQL = "UPDATE notes.notes SET title = ?, body = ?, time_change = ? where user_login = ? AND id = ?;";

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, note.getTitle());
				statement.setString(2, note.getBody());
				statement.setTimestamp(3, new Timestamp(note.getTimeChange().getTime()));
				statement.setString(4, user.getLogin());
				statement.setInt(5, note.getId());
				LOG.trace("Executing SQL");
				statement.executeUpdate();
			} catch (SQLException e) {
				LOG.error("Fail to change the note at user " + user.getLogin(), e);
				throw new DaoException("Fail to change the note at user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Note was changed at user " + user.getLogin());
	}

	/**
	 * Method deletes all {@link Note}s from given {@link User} in DataBase
	 * @throws DaoException if failed to delete all {@link Note}s
	 * @param user
	 */
	public void deleteAll(User user) {
		LOG.trace("Deleting all notes from user " + user.getLogin());
		String SQL = "DELETE FROM notes.notes WHERE user_login = ?;";

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				LOG.trace("Executing SQL");
				statement.execute();
			} catch (SQLException e) {
				LOG.error("Fail to delete all Notes from user " + user.getLogin(), e);
				throw new DaoException("Fail to delete all Notes from user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("All notes were deleted from user " + user.getLogin());
	}

	/**
	 * Method deletes given {@link Note} from {@link User} in DataBase
	 * @throws DaoException if failed to delete given {@link Note}
	 * @param id
	 * @param user
	 */
	public void delete(int id, User user) {
		LOG.trace("Deleting note by id from user " + user.getLogin());
		String SQL = "DELETE FROM notes.notes WHERE user_login = ? AND id = ?;";

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.setInt(2, id);
				LOG.trace("Executing SQL");
				statement.execute();
			} catch (SQLException e) {
				LOG.error("Fail to delete note by id from user " + user.getLogin(), e);
				throw new DaoException("Fail to delete note by id from user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Note was deleted from user " + user.getLogin());
	}

	/**
	 * Create new {@link Note} with given id at given {@link User}
	 * @throws DaoException if failed to create new {@link Note} 
	 * @param id
	 * @param user
	 * @return created {@link Note}
	 */
	public Note create(int id, User user) {
		LOG.trace("Creating new note for user " + user.getLogin());
		StringBuffer SQLInsert = new StringBuffer("INSERT INTO notes.notes (user_login, title, body, id, time_creation, time_change) VALUES ");
		DaoUtils.buildInsertValuesQuery(1, 6, SQLInsert);
		String SQL = SQLInsert.append(";").toString();

		Note note = null;

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, user.getLogin());
				statement.setString(2, "Title");
				statement.setString(3, "Note");
				statement.setInt(4, id);
				statement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
				statement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
				LOG.trace("Executing SQL");
				statement.execute();
				LOG.trace("Getting result set");
				resultSet = statement.getGeneratedKeys();
				if (resultSet.next()) {
					note = new Note(resultSet.getString("title"), 
							resultSet.getString("body"), 
							resultSet.getInt("id"));
					note.setTimeCreation(resultSet.getTimestamp("time_creation"));
					note.setTimeChange(resultSet.getTimestamp("time_change"));
				}
			} catch (SQLException e) {
				LOG.warn("Fail to create note at user " + user.getLogin(), e);
				throw new DaoException("Fail to create note at user " + user.getLogin());
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Returning created note for user " + user.getLogin());
		return note;
	}

	/**
	 * Method search in DataBase {@link Note}s at {@link User} that satisfy given pattern {@link String} 
	 * @throws DaoException if failed to search {@link Note}s by given pattern {@link String}
	 * @param user
	 * @param pattern
	 * @return Collection of {@link Note}s
	 */
	public ArrayList<Note> searchByPattern(User user, String pattern) {
		LOG.trace("Getting all notes by pattern from user " + user.getLogin());
		ArrayList<Note> notes = new ArrayList<>();

		String SQL = "SELECT * FROM notes.notes WHERE user_login = ? AND (title ILIKE ? OR body ILIKE ?);";

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.setString(2, "%" + pattern + "%");
				statement.setString(3, "%" + pattern + "%");
				LOG.trace("Getting result set");
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					Note note = new Note(resultSet.getString("title"), 
							resultSet.getString("body"),
							resultSet.getInt("id"));
					note.setTimeCreation(resultSet.getTimestamp("time_creation"));
					note.setTimeChange(resultSet.getTimestamp("time_change"));
					notes.add(note);
				}
			} catch (SQLException e) {
				LOG.warn("Fail to get notes by pattern from user " + user.getLogin(), e);
				throw new DaoException("Fail to get notes by pattern from user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Returning notes by pattern from user " + user.getLogin());
		return notes;
	}

	/**
	 * Method returns all {@link Note}s from given {@link User}
	 * @throws DaoException if failed to get all {@link Note}s
	 * @param user
	 * @return Collection of returned {@link Note}s from DataBase 
	 */
	public ArrayList<Note> getAll(User user) {
		LOG.trace("Getting all notes from user " + user.getLogin());
		ArrayList<Note> notes = new ArrayList<>();

		String SQL = "SELECT * FROM notes.notes WHERE user_login = ?;";

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				LOG.trace("Getting result set");
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					Note note = new Note(resultSet.getString("title"), 
							resultSet.getString("body"),
							resultSet.getInt("id"));
					note.setTimeChange(resultSet.getTimestamp("time_change"));
					note.setTimeCreation(resultSet.getTimestamp("time_creation"));
					notes.add(note);
				}
			} catch (SQLException e) {
				LOG.warn("Fail to get all notes from user " + user.getLogin(), e);
				throw new DaoException("Fail to get all notes from user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Returning all notes from user " + user.getLogin());
		return notes;
	}
}