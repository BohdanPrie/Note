package ua.com.bohdanprie.notes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.dao.exceptions.DBException;
import ua.com.bohdanprie.notes.dao.exceptions.DaoException;
import ua.com.bohdanprie.notes.domain.entities.Note;
import ua.com.bohdanprie.notes.domain.entities.User;

public class NoteDao {
	private DaoFactory daoFactory = DaoFactory.getInstance();
	private static final Logger LOG = LogManager.getLogger(NoteDao.class.getName());

	public void change(Note note, User user) {
		LOG.trace("Changing note of user with login = " + user.getLogin());
		String SQL = "UPDATE notes.notes SET title = ?, body = ?, time_change = ? where user_login = ? AND id = ?";

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
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
				LOG.error("Fail to change the note of user " + user.getLogin(), e);
				throw new DaoException("Fail to change the note of user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Note was changed at user " + user.getLogin());
	}

	public void deleteAll(User user) {
		LOG.trace("Deleting all notes from user " + user.getLogin());
		String SQL = "DELETE FROM notes.notes WHERE user_login = ?";

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
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

	public void delete(int id, User user) {
		LOG.trace("Deleting note from user " + user.getLogin());
		String SQL = "DELETE FROM notes.notes WHERE user_login = ? AND id = ?;";

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.setInt(2, id);

				LOG.trace("Executing SQL");
				statement.execute();
			} catch (SQLException e) {
				LOG.error("Fail to delete the note of user " + user.getLogin(), e);
				throw new DaoException("Fail to delete the note of user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Note was deleted from user " + user.getLogin());
	}

	public Note create(int id, User user) {
		LOG.trace("Creating new note for user " + user.getLogin());
		String SQL = "INSERT INTO notes.notes (user_login, title, body, id, time_creation, time_change) VALUES (?, 'Title', 'Note', ?, ?, ?);";

		Note note = new Note("Title", "Body", id);

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.setInt(2, id);
				statement.setTimestamp(3, new Timestamp(note.getTimeCreation().getTime()));
				statement.setTimestamp(4, new Timestamp(note.getTimeChange().getTime()));

				LOG.trace("Executing SQL");
				statement.execute();
			} catch (SQLException e) {
				LOG.warn("Fail to create note at user " + user.getLogin(), e);
				throw new DaoException("Fail to create note at user " + user.getLogin());
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Returning note from user " + user.getLogin());
		return note;
	}

	public ArrayList<Note> searchByPattern(User user, String pattern) {
		LOG.trace("Getting all notes by pattern from user " + user.getLogin());
		ArrayList<Note> notes = new ArrayList<>();

		String SQL = "SELECT * FROM notes.notes WHERE user_login = ? AND (title ILIKE ? OR body ILIKE ?);";

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
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
				LOG.warn("Fail to get notes by pattern at user " + user.getLogin(), e);
				throw new DaoException("Fail to get notes by pattern at user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Returning notes by pattern from user " + user.getLogin());
		return notes;
	}

	public ArrayList<Note> getAll(User user) {
		LOG.trace("Getting all notes from user " + user.getLogin());
		ArrayList<Note> notes = new ArrayList<>();

		String SQL = "SELECT * FROM notes.notes WHERE user_login = ?;";

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
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
				LOG.warn("Fail to get all notes at user " + user.getLogin(), e);
				throw new DaoException("Fail to get all notes at user " + user.getLogin(), e);
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