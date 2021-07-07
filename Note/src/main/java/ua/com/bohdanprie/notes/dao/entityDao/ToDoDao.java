package ua.com.bohdanprie.notes.dao.entityDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.dao.DaoManager;
import ua.com.bohdanprie.notes.dao.DaoUtils;
import ua.com.bohdanprie.notes.dao.exception.DBException;
import ua.com.bohdanprie.notes.dao.exception.DaoException;
import ua.com.bohdanprie.notes.domain.entity.ToDo;
import ua.com.bohdanprie.notes.domain.entity.User;
/**
 * Class provide work with CRUD operations with {@link ToDo}
 * @author bohda
 *
 */
public class ToDoDao {
	private final DaoManager daoManager;
	private static final Logger LOG = LogManager.getLogger(ToDoDao.class.getName());

	public ToDoDao() {
		LOG.trace("Getting DaoManager instance");
		daoManager = DaoManager.getInstance();
		LOG.debug("ToDoDao initialized");
	}

	/**
	 * Method deletes all {@link ToDo}s from given {@link User} from DataBase
	 * @throws DaoException if failed to delete all {@link ToDo}s
	 * @param user
	 */
	public void deleteAll(User user) {
		LOG.trace("Deleting all toDos at user " + user.getLogin());
		String SQL = "DELETE FROM notes.to_do as to_do WHERE to_do.user_login = ?;";

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
				LOG.error("Fail to delete all toDos at user " + user.getLogin(), e);
				throw new DaoException("Fail to delete all toDos at user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("All toDos were deleted at user " + user.getLogin());
	}

	/**
	 * Method deletes all {@link ToDo}s from given 
	 * {@link ua.com.bohdanprie.notes.domain.entity.ToDoLine} id from given 
	 * {@link User} from DataBase.
	 * @throws DaoException if failed to delete all {@link ToDo}s from {@link ua.com.bohdanprie.notes.domain.entity.ToDoLine}
	 * @param lineId
	 * @param user
	 */
	public void deleteFromLine(int lineId, User user) {
		LOG.trace("Deleting all toDos from toDoline by id at user "+ user.getLogin());
		String SQL = "DELETE FROM notes.to_do AS to_do WHERE to_do.user_login = ? AND to_do.line_id = ?;";

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.setInt(2, lineId);
				LOG.trace("Executing SQL");
				statement.execute();
			} catch (SQLException e) {
				LOG.error("Fail to delete toDos from toDoLine at user " + user.getLogin(), e);
				throw new DaoException("Fail to delete toDos from toDoLine at user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("ToDos were deleted from toDoLine id at user " + user.getLogin());
	}

	/**
	 * Method refresh all {@link ToDo}s in given {@link ua.com.bohdanprie.notes.domain.entity.ToDoLine} id 
	 * <br>Note: it first deletes all {@link ToDo}, then it inserts new.
	 * <br>It was made this way to decrease Queries to DataBase.
	 * <br>Otherwise we had to first check which {@link ToDo}s there are, 
	 * then compare with given, then delete all that are not in given {@link ToDo}s, then update left with given data
	 * @throws DaoException if failed to change all {@link ToDo}s
	 * @param lineId
	 * @param newValue
	 * @param user
	 */
	public void change(int lineId, List<ToDo> newValue, User user) {
		LOG.trace("Changing toDos at user " + user.getLogin());
		deleteFromLine(lineId, user);
		if(newValue.size() != 0) {
			add(lineId, newValue, user);
		}
	}

	/**
	 * Method gets all {@link ToDo}s from given user.
	 * <br>As many {@link ToDo}s are connected to one {@link ua.com.bohdanprie.notes.domain.entity.ToDoLine}
	 * <br>method get line_id of {@link ua.com.bohdanprie.notes.domain.entity.ToDoLine} 
	 * and set it as a key of Map (as line_id is unique at user)
	 * and get all {@link ToDo}s, connected to this id and set it as a value of a Map
	 * @throws DaoException if failed to get all {@link ToDo}s
	 * @param user
	 * @return Map<Integer, List<ToDo>> where Integer represents line_id of {@link ToDo} 
	 * and List<ToDo> represents returned values in DataBase
	 */
	public Map<Integer, List<ToDo>> getAll(User user) {
		LOG.trace("Getting all toDos from user " + user.getLogin());
		HashMap<Integer, List<ToDo>> valuesAndLineId = new HashMap<>();
		String SQL = "SELECT body, id, line_id, marked FROM notes.to_do WHERE user_login = ?;";

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
					ToDo toDo = new ToDo(resultSet.getInt("id"), 
							resultSet.getString("body"),
							resultSet.getBoolean("marked"));
					int lineId = resultSet.getInt("line_id");
					if (!valuesAndLineId.containsKey(lineId)) {
						valuesAndLineId.put(lineId, new ArrayList<ToDo>());
					}
					valuesAndLineId.get(lineId).add(toDo);
				}
			} catch (SQLException e) {
				LOG.error("Fail to get all ToDos from user " + user.getLogin());
				throw new DaoException("Fail to get all toDos from user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.trace("Returning all toDos from user " + user.getLogin());
		return valuesAndLineId;
	}

	/**
	 * Method gets all {@link ToDo}s from given user.
	 * <br>As many {@link ToDo}s are connected to one {@link ua.com.bohdanprie.notes.domain.entity.ToDoLine}
	 * <br>method gets line_id, that is in given array of id, of {@link ua.com.bohdanprie.notes.domain.entity.ToDoLine} 
	 * and set it as a key of Map (as line_id is unique at user)
	 * and get all {@link ToDo}s, connected to this id and set it as a value of a Map
	 * @throws DaoException if failed to get all {@link ToDo}s by given array of id
	 * @param id
	 * @param user
	 * @return Map<Integer, List<ToDo>> where Integer represents line_id of {@link ToDo} 
	 * and List<ToDo> represents returned values in DataBase 
	 */
	public Map<Integer, List<ToDo>> getById(Integer[] id, User user) {
		LOG.trace("Getting all toDos by toDoLines id from user " + user.getLogin());
		HashMap<Integer, List<ToDo>> valuesAndLineId = new HashMap<>();
		String SQL = "SELECT body, id, line_id, marked FROM notes.to_do WHERE user_login = ? AND line_id = ANY(?);";

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.setArray(2, connection.createArrayOf("integer", id));;
				LOG.trace("Getting result set");
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					ToDo toDo = new ToDo(resultSet.getInt("id"), 
							resultSet.getString("body"),
							resultSet.getBoolean("marked"));
					int lineId = resultSet.getInt("line_id");
					if (!valuesAndLineId.containsKey(lineId)) {
						valuesAndLineId.put(lineId, new ArrayList<ToDo>());
					}
					valuesAndLineId.get(lineId).add(toDo);
				}
			} catch (SQLException e) {
				LOG.error("Fail to get all toDos by id from user " + user.getLogin(), e);
				throw new DaoException("Fail to get all toDos by id from user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.trace("Returning toDos by toDoLines id from user " + user.getLogin());
		return valuesAndLineId;
	}
	/**
	 * Method adds given Collection of {@link ToDo}s to given {@link ua.com.bohdanprie.notes.domain.entity.ToDoLine} id 
	 * at given {@link User}
	 * @throws DaoException if failed add given {@link ToDo}s to given {@link ua.com.bohdanprie.notes.domain.entity.ToDoLine} id
	 * @param lineId
	 * @param toDos
	 * @param user
	 */
	public void add(int lineId, List<ToDo> toDos, User user) {
		LOG.trace("Adding toDos to toDoLine to user " + user.getLogin());
		StringBuffer SQLInsert = new StringBuffer("INSERT INTO notes.to_do (user_login, body, id, line_id, marked) VALUES ");
		DaoUtils.buildInsertValuesQuery(toDos.size(), 5, SQLInsert);
		String SQL = SQLInsert.append(";").toString();

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				addValues(statement, lineId, toDos, user);
				LOG.trace("Executing SQL");
				statement.execute();
			} catch (SQLException e) {
				LOG.error("Fail to add toDos to toDoLine at user " + user.getLogin(), e);
				throw new DaoException("Fail to add toDos to toDoLine at user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("ToDos added to user " + user.getLogin());
	}

	/**
	 * Method inserts into given {@link PreparedStatement} given Collection of {@link ToDo}, {@link User} and LineId
	 * @param statement
	 * @param lineId
	 * @param newValues
	 * @param user
	 * @throws SQLException
	 */
	private void addValues(PreparedStatement statement, int lineId, List<ToDo> newValues, User user) throws SQLException {
		LOG.trace("Adding values to PreparedStatement for user " + user.getLogin());
		int valuePosition = 1;
		for (ToDo toDo : newValues) {
			statement.setString(valuePosition++, user.getLogin());
			statement.setString(valuePosition++, toDo.getBody());
			statement.setInt(valuePosition++, toDo.getId());
			statement.setInt(valuePosition++, lineId);
			statement.setBoolean(valuePosition++, toDo.isMarked());
		}
		LOG.trace("Values to PreparedStatement were added for user " + user.getLogin());
	}
}