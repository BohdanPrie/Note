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

public class ToDoDao {
	private final DaoManager daoFactory;
	private static final Logger LOG = LogManager.getLogger(ToDoDao.class.getName());

	public ToDoDao() {
		LOG.trace("Getting DaoFactory instance");
		daoFactory = DaoManager.getInstance();
		LOG.debug("ToDoDao initialized");
	}

	public void deleteAll(User user) {
		LOG.trace("Deleting all toDos at user " + user.getLogin());
		String SQL = "DELETE FROM notes.to_do as to_do WHERE to_do.user_login = ?;";

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

	public void deleteFromLine(int listId, User user) {
		LOG.trace("Deleting all toDos from toDoline by id at user "+ user.getLogin());
		String SQL = "DELETE FROM notes.to_do AS to_do WHERE to_do.user_login = ? AND to_do.list_id = ?;";

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.setInt(2, listId);
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

	public void change(int listId, List<ToDo> newValue, User user) {
		LOG.trace("Changing toDos at user " + user.getLogin());
		deleteFromLine(listId, user);
		add(listId, newValue, user);
	}

	public Map<Integer, ArrayList<ToDo>> getAll(User user) {
		LOG.trace("Getting all toDos from user " + user.getLogin());
		HashMap<Integer, ArrayList<ToDo>> valuesAndListId = new HashMap<>();
		String SQL = "SELECT body, id, list_id, marked FROM notes.to_do WHERE user_login = ?;";

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
					ToDo toDo = new ToDo(resultSet.getInt("id"), 
							resultSet.getString("body"),
							resultSet.getBoolean("marked"));
					int listId = resultSet.getInt("list_id");
					if (!valuesAndListId.containsKey(listId)) {
						valuesAndListId.put(listId, new ArrayList<ToDo>());
					}
					valuesAndListId.get(listId).add(toDo);
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
		return valuesAndListId;
	}

	public Map<Integer, ArrayList<ToDo>> getById(Integer[] id, User user) {
		LOG.trace("Getting all toDos by toDoLines id from user " + user.getLogin());
		HashMap<Integer, ArrayList<ToDo>> valuesAndListId = new HashMap<>();
		String SQL = "SELECT body, id, list_id, marked FROM notes.to_do WHERE user_login = ? AND list_id = ANY(?);";

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
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
					int listId = resultSet.getInt("list_id");
					if (!valuesAndListId.containsKey(listId)) {
						valuesAndListId.put(listId, new ArrayList<ToDo>());
					}
					valuesAndListId.get(listId).add(toDo);
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
		return valuesAndListId;
	}
	
	public void add(int listId, List<ToDo> toDos, User user) {
		LOG.trace("Adding toDos to toDoLine to user " + user.getLogin());
		StringBuffer SQLInsert = new StringBuffer("INSERT INTO notes.to_do (user_login, body, id, list_id, marked) VALUES ");
		DaoUtils.buildInsertValuesQuery(toDos.size(), 5, SQLInsert);
		String SQL = SQLInsert.append(";").toString();

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				addValues(statement, listId, toDos, user);
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

	private void addValues(PreparedStatement statement, int listId, List<ToDo> newValues, User user) throws SQLException {
		LOG.trace("Adding values to PreparedStatement for user " + user.getLogin());
		int valuePosition = 1;
		for (ToDo toDo : newValues) {
			statement.setString(valuePosition++, user.getLogin());
			statement.setString(valuePosition++, toDo.getBody());
			statement.setInt(valuePosition++, toDo.getId());
			statement.setInt(valuePosition++, listId);
			statement.setBoolean(valuePosition++, toDo.isMarked());
		}
		LOG.trace("Values to PreparedStatement were added for user " + user.getLogin());
	}
}