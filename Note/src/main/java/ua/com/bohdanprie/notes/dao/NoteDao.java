package ua.com.bohdanprie.notes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.domain.Note;
import ua.com.bohdanprie.notes.domain.User;

public class NoteDao {
	private DaoFactory daoFactory = DaoFactory.getInstance();
	private static final Logger LOG = LogManager.getLogger(NoteDao.class.getName());

	public void changeNote(Note note, User user) {
		LOG.info("Changing note of user's login = " + user.getLogin());
		String SQL = "UPDATE notes.notes SET title = ?, body = ?, time_change = ? where user_login = ? AND id = ?";

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			LOG.trace("");
			connection = daoFactory.getConnection();
			try {
				statement = connection.prepareStatement(SQL);
				statement.setString(1, note.getTitle());
				statement.setString(2, note.getBody());
				statement.setTimestamp(3, new Timestamp(note.getTimeChange().getTime()));
				statement.setString(4, user.getLogin());
				statement.setInt(5, note.getId());
				statement.execute();

			} catch (SQLException e) {
				LOG.error("Fail to change the note in user = " + user.getLogin(), e);
				throw new DaoException("Fail to change Note", e);
			}
		} catch (DBException e) {
			LOG.error("Connection fail", e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e) {
				LOG.error("Fail at closing", e);
			}
		}
		LOG.info("Note was changed at user with login = " + user.getLogin());
	}

	public void deleteAll(User user) {
		String SQL = "DELETE FROM notes.notes WHERE user_login = ?";

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = daoFactory.getConnection();
			try {
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.execute();

			} catch (SQLException e) {
				LOG.error("Fail to delete all Notes", e);
			}
		} catch (DBException e) {
			LOG.error("Connection fail", e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e) {
				LOG.error("Fail at closing", e);
			}
		}
	}
	
	public void deleteNote(int id, User user) {
		String SQL = "DELETE FROM notes.notes WHERE user_login = ? AND id = ?;";

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = daoFactory.getConnection();
			try {
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.setInt(2, id);
				statement.execute();

			} catch (SQLException e) {
				throw new DaoException("Fail to delete Note", e);
			}
		} catch (DBException e) {
			throw new DaoException("Connection fail", e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e) {
				throw new DaoException("Fail at closing", e);
			}
		}
	}

	public Note createNote(int id, User user) {
		String SQL = "INSERT INTO notes.notes (user_login, title, body, id, time_creation, time_change) values (?, 'Title', 'Note', ?, ?, ?);";

		Note note = new Note("Title", "Body", id);

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = daoFactory.getConnection();
			try {
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.setInt(2, id);
				statement.setTimestamp(3, new Timestamp(note.getTimeCreation().getTime()));
				statement.setTimestamp(4, new Timestamp(note.getTimeChange().getTime()));
				statement.execute();

			} catch (SQLException e) {
				throw new DaoException("Fail to create Note", e);
			}
		} catch (DBException e) {
			throw new DaoException("Connection fail", e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				throw new DaoException("Fail at closing", e);
			}
		}
		return note;
	}
	
	public ArrayList<Note> searchByPattern(User user, String pattern){
		ArrayList<Note> notes = new ArrayList<>();
		
		String SQL = "SELECT * FROM notes.notes WHERE user_login = ? AND (title ILIKE ? OR body ILIKE ?);";
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			try {
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.setString(2, "%" + pattern + "%");
				statement.setString(3, "%" + pattern + "%");

				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					Note note = new Note(resultSet.getString("title"), 
							resultSet.getString("body"),
							resultSet.getInt("id"));
					note.setTimeCreation(resultSet.getTimestamp("time_creation"));
					note.setTimeCreation(resultSet.getTimestamp("time_creation"));
					notes.add(note);
				}
			} catch (SQLException e) {
				throw new DaoException("Fail to get all notes", e);
			}
		} catch (DBException e) {
			throw new DaoException("Connection fail", e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e) {
				throw new DaoException("Fail at closing", e);
			}
		}
		return notes;
	}

	public ArrayList<Note> getAllNotes(User user) {
		ArrayList<Note> notes = new ArrayList<>();

		String SQL = "SELECT * FROM notes.notes WHERE user_login = ?;";

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = daoFactory.getConnection();
			try {
				statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, user.getLogin());
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					Note note = new Note(resultSet.getString("title"), resultSet.getString("body"),
							resultSet.getInt("id"));
					note.setTimeChange(resultSet.getTimestamp("time_change"));
					note.setTimeCreation(resultSet.getTimestamp("time_creation"));
					notes.add(note);
				}
			} catch (SQLException e) {
				throw new DaoException("Fail to get all notes", e);
			}
		} catch (DBException e) {
			throw new DaoException("Connection fail", e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e) {
				throw new DaoException("Fail at closing", e);
			}
		}
		return notes;
	}
}