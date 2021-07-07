package ua.com.bohdanprie.notes.dao.entityDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.dao.DaoManager;
import ua.com.bohdanprie.notes.dao.DaoUtils;
import ua.com.bohdanprie.notes.dao.exception.DBException;
import ua.com.bohdanprie.notes.dao.exception.DaoException;
import ua.com.bohdanprie.notes.domain.entity.ToDo;
import ua.com.bohdanprie.notes.domain.entity.ToDoLine;
import ua.com.bohdanprie.notes.domain.entity.User;
/**
 * Class provide work with CRUD operations with {@link ToDoLine}
 * @author bohda
 *
 */
public class ToDoLineDao {
	private final DaoManager daoManager;
	private static final Logger LOG = LogManager.getLogger(ToDoLineDao.class.getName());

	public ToDoLineDao() {
		LOG.trace("Getting DaoManager instance");
		daoManager = DaoManager.getInstance();
		LOG.debug("ToDoLineDao initialized");
	}

	/**
	 * Method deletes all {@link ToDoLine}s from given {@link User} in DataBase
	 * @throws DaoException if failed to delete all {@link ToDoLine}s
	 * @param user
	 */
	public void deleteAll(User user) {
		LOG.trace("Deleting all toDoLists at user " + user.getLogin());
		String SQL = "DELETE FROM notes.to_do_line WHERE user_login = ?;";

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
				LOG.error("Fail to delete all toDoLists at user " + user.getLogin(), e);
				throw new DaoException("Fail to delete all toDoLists at user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("All toDoLists were deleted at user " + user.getLogin());
	}

	/**
	 * Method deletes given {@link ToDoLine} from {@link User} in DataBase
	 * @throws DaoException if failed to delete {@link ToDoLine} by id
	 * @param id
	 * @param user
	 */
	public void delete(int id, User user) {
		LOG.trace("Deleting toDoLine from user " + user.getLogin());
		String SQL = "DELETE FROM notes.to_do_line WHERE user_login = ? AND id = ?;";

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
				LOG.error("Fail to delete toDoLine from user " + user.getLogin(), e);
				throw new DaoException("Fail to delete toDoLine from user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("ToDoLine was deleted from user " + user.getLogin());
	}

	/**
	 * Method changes given {@link ToDoLine} at {@link User} in DataBase
	 * @throws DaoException if failed to change {@link ToDoLine}
	 * @param note
	 * @param user
	 */
	public void change(ToDoLine toDoLine, User user) {
		LOG.trace("Changing toDoLine at user " + user.getLogin());
		String SQL = "UPDATE notes.to_do_line set title = ?, time_change = ? WHERE user_login = ? AND id = ?;";

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, toDoLine.getTitle());
				statement.setTimestamp(2, new Timestamp(toDoLine.getTimeChange().getTime()));
				statement.setString(3, user.getLogin());
				statement.setInt(4, toDoLine.getId());
				LOG.trace("Executing SQL");
				statement.execute();
			} catch (SQLException e) {
				LOG.error("Fail to change toDoLine at user " + user.getLogin(), e);
				throw new DaoException("Fail to change toDoLine at user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("ToDoLine was changed at user " + user.getLogin());
	}

	/**
	 * Create new {@link ToDoLine} with given id at given {@link User}
	 * @throws DaoException if failed to create new {@link ToDoLine} 
	 * @param id
	 * @param user
	 * @return created {@link ToDoLine}
	 */
	public ToDoLine create(int id, User user) {
		LOG.trace("Creating new toDoLine for user " + user.getLogin());
		StringBuffer SQLInsert = new StringBuffer("INSERT INTO notes.to_do_line (user_login, title, id, time_change, time_creation) VALUES ");
		DaoUtils.buildInsertValuesQuery(1, 5, SQLInsert);
		String SQL = SQLInsert.append(";").toString();

		ToDoLine toDoLine = null;

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, user.getLogin());
				statement.setString(2, "Title");
				statement.setInt(3, id);
				statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
				statement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
				LOG.trace("Executing SQL");
				statement.execute();
				LOG.trace("Getting result set");
				resultSet = statement.getGeneratedKeys();
				if(resultSet.next()) {
					toDoLine = new ToDoLine(resultSet.getString("title"));
					toDoLine.setTimeCreation(resultSet.getTimestamp("time_creation"));
					toDoLine.setTimeChange(resultSet.getTimestamp("time_change"));
				}
			} catch (SQLException e) {
				LOG.warn("Fail to create toDoLine at user " + user.getLogin(), e);
				throw new DaoException("Fail to create toDoLine at user " + user.getLogin());
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Returning created toDoLine from user " + user.getLogin());
		return toDoLine;
	}

	/**
	 * Method returns all {@link ToDoLine}s from given {@link User}
	 * @throws DaoException if failed to get all {@link ToDoLine}s
	 * @param user
	 * @return Collection of returned {@link ToDoLine}s from DataBase 
	 */
	public ArrayList<ToDoLine> getAll(User user) {
		LOG.trace("Getting all toDoLines from user " + user.getLogin());
		String SQL = "SELECT * FROM notes.to_do_line WHERE user_login = ?;";

		ArrayList<ToDoLine> toDoLines = new ArrayList<>();

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
					ToDoLine toDoLine = new ToDoLine(resultSet.getString("title"));
					toDoLine.setId(resultSet.getInt("id"));
					toDoLine.setTimeChange(resultSet.getTimestamp("time_change"));
					toDoLine.setTimeCreation(resultSet.getTimestamp("time_creation"));
					toDoLines.add(toDoLine);
				}
			} catch (SQLException e) {
				LOG.error("Fail to get all toDoLines from user " + user.getLogin(), e);
				throw new DaoException("Fail to get all toDoLines from user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Returning all toDoLines from user " + user.getLogin());
		return toDoLines;
	}

	/**
	 * Method returns all {@link ToDoLine}s from given {@link User} which id satisfy given array
	 * @throws DaoException if failed to get all {@link ToDoLine}s by given array of id
	 * @param id
	 * @param user
	 * @return Collection of returned {@link ToDoLine}s from DataBase
	 */
	public ArrayList<ToDoLine> searchByPattern(Integer[] id, User user) {
		LOG.trace("Getting all toDoLines by pattern from user " + user.getLogin());
		String SQL = "SELECT * FROM notes.to_do_line WHERE user_login = ? AND id = ANY(?);";
		
		ArrayList<ToDoLine> toDoLines = new ArrayList<>();

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.setObject(2, connection.createArrayOf("integer", id));
				LOG.trace("Getting result set");
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					ToDoLine toDoLine = new ToDoLine(resultSet.getString("title"));
					toDoLine.setId(resultSet.getInt("id"));
					toDoLine.setTimeChange(resultSet.getTimestamp("time_change"));
					toDoLine.setTimeCreation(resultSet.getTimestamp("time_creation"));
					toDoLines.add(toDoLine);
				}
			} catch (SQLException e) {
				LOG.error("Fail to get all toDoLines by pattern from user " + user.getLogin(), e);
				throw new DaoException("Fail to get toDoLines by pattern from user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Returning all toDoLines by pattern from user " + user.getLogin());
		return toDoLines;
	}

	/**
	 * Method selects all line_id from tables of {@link ToDoLine} and {@link ToDo} 
	 * where text of {@link ToDo}'s body and {@link ToDoLine}'s title satisfy given pattern {@link String}
	 * @throws DaoException if failed to get all id by given pattern
	 * @param user
	 * @param pattern
	 * @return array of line_id id
	 */
	public Integer[] getAllIdByPattern(User user, String pattern) {
		LOG.trace("Getting all id of toDoLines that match pattern from user " + user.getLogin());
		Set<Integer> allLinesId = new HashSet<>();
		String SQL = "SELECT id FROM notes.to_do_line WHERE (user_login = ? AND title ILIKE ?) OR id IN(SELECT list_id FROM notes.to_do WHERE user_login = ? AND body ILIKE ?);";

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.setString(2, "%" + pattern + "%");
				statement.setString(3, user.getLogin());
				statement.setString(4, "%" + pattern + "%");
				LOG.trace("Getting result set");
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					allLinesId.add(resultSet.getInt("id"));
				}
			} catch (SQLException e) {
				LOG.error("Fail to get all id of toDoLines that match pattern from user " + user.getLogin(), e);
				throw new DaoException("Fail to get all id of toDoLines that match pattern from user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Returning all id of toDoLines that match pattern from user " + user.getLogin());
 		return allLinesId.toArray(new Integer[]{});
	}
}