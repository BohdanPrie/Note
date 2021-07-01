package ua.com.bohdanprie.notes.dao.entitiesDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.dao.DaoFactory;
import ua.com.bohdanprie.notes.dao.DaoUtils;
import ua.com.bohdanprie.notes.dao.exceptions.DBException;
import ua.com.bohdanprie.notes.dao.exceptions.DaoException;
import ua.com.bohdanprie.notes.domain.entities.ToDo;
import ua.com.bohdanprie.notes.domain.entities.User;

public class ToDoDao {
	private DaoFactory daoFactory;
	private static final Logger LOG = LogManager.getLogger(ToDoDao.class.getName());

	public ToDoDao() {
		daoFactory = DaoFactory.getInstance();
	}

	public void deleteAll(User user) {
		String SQL = "DELETE FROM notes.to_do as to_do USING notes.to_do_line AS list WHERE to_do.user_login = ?;";

		PreparedStatement statement = null;

		try (Connection connection = daoFactory.getConnection()) {
			try {
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.execute();
			} catch (SQLException e) {
				throw new DaoException("Fail to delete all toDos from user " + user.getLogin() ,e);
			}
		} catch (DBException e) {

		} catch (SQLException e) {

		}
	}

	public void deleteFromLine(int listId, User user) {
		String SQL = "DELETE FROM notes.to_do AS to_do USING notes.to_do_line AS list WHERE to_do.user_login = ? AND to_do.list_id = ?;";

		PreparedStatement statement = null;

		try (Connection connection = daoFactory.getConnection()) {
			try {
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				statement.setInt(2, listId);
				statement.execute();
			} catch (SQLException e) {
				throw new DaoException("Fail to add toDos to toDoLine at user " + user.getLogin(), e);
			}
		} catch (DBException e) {

		} catch (SQLException e) {

		}
	}

	public void change(int listId, List<ToDo> newValue, User user) {
		deleteFromLine(listId, user);
		add(listId, newValue, user);
	}

	public Map<Integer, ArrayList<ToDo>> getAll(User user) {
		HashMap<Integer, ArrayList<ToDo>> valuesAndListId = new HashMap<>();
		String SQL = "SELECT body, id, list_id, marked FROM notes.to_do WHERE user_login = ?;";
		
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try (Connection connection = daoFactory.getConnection()) {
			try {
				statement = connection.prepareStatement(SQL);
				resultSet = statement.executeQuery();
				while(resultSet.next()) {
					ToDo toDo = new ToDo(resultSet.getInt("id"), resultSet.getString("body"), resultSet.getBoolean("marked"));
					int listId = resultSet.getInt("list_id");
					if(valuesAndListId.containsKey(listId)) {
						valuesAndListId.get(listId).add(toDo);
					} else {
						valuesAndListId.put(listId, new ArrayList<ToDo>());
						valuesAndListId.get(listId).add(toDo);
					}
				}
			} catch (SQLException e) {
				throw new DaoException("Fail to get all toDos from user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			
		} catch (SQLException e) {
			
		}
		
		return valuesAndListId;
	}
	
	public Map<Integer, ArrayList<ToDo>> getById(Integer[] id, User user) {
		return null;
	}

	public void add(int listId, List<ToDo> toDos, User user) {
		StringBuffer SQLInsert = new StringBuffer("INSERT INTO notes.to_do (user_login, body, id, list_id, marked) VALUES ");
		String SQL = SQLInsert.append(DaoUtils.buildInsertValuesQuery(toDos.size(), 5)).append(";").toString();

		PreparedStatement statement = null;

		try (Connection connection = daoFactory.getConnection()) {
			try {
				statement = connection.prepareStatement(SQL);
				addValues(statement, listId, toDos, user);
				statement.execute();
			} catch (SQLException e) {
				throw new DaoException("Fail to add toDos to toDoLine at user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			
		} catch (SQLException e) {
			
		}
	}

	private void addValues(PreparedStatement statement, int listId, List<ToDo> newValues, User user) throws SQLException {
		int valuePosition = 1;
		for(ToDo toDo : newValues) {
			statement.setString(valuePosition++, user.getLogin());
			statement.setString(valuePosition++, toDo.getBody());
			statement.setInt(valuePosition++, toDo.getId());
			statement.setInt(valuePosition++, listId);
			statement.setBoolean(valuePosition++, toDo.isMarked());
		}
	}
}