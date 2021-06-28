package ua.com.bohdanprie.notes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.dao.exceptions.DBException;
import ua.com.bohdanprie.notes.dao.exceptions.DaoException;
import ua.com.bohdanprie.notes.domain.entities.ToDoLine;
import ua.com.bohdanprie.notes.domain.entities.User;

public class ToDoLineDao {
	private DaoFactory daoFactory = DaoFactory.getInstance();
	private static final Logger LOG = LogManager.getLogger(ToDoLineDao.class.getName());
	
	public void deleteAll(User user) {
		LOG.trace("Deleting all toDoLists from user " + user.getLogin());
		String SQL = "DELETE FROM notes.to_do_list WHERE user_login = ?";

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
				LOG.error("Fail to delete all toDoLists from user " + user.getLogin(), e);
				throw new DaoException("Fail to delete all toDoLists from user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("All toDoLists were deleted from user " + user.getLogin());
	}
	
	public void delete(int id, User user) {
		LOG.trace("Deleting toDoLine from user " + user.getLogin());
		String SQL = "DELETE FROM notes.to_do_list WHERE user_login = ? AND id = ?;";

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
				LOG.error("Fail to delete toDoLine of user " + user.getLogin(), e);
				throw new DaoException("Fail to delete toDoLine of user " + user.getLogin(), e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("toDoLine was deleted from user " + user.getLogin());
	}
	
	public void change(ToDoLine toDoLine, User user) {
		
	}
	
	public ToDoLine create(int id, User user) {
		return null;
	}
	
	public ArrayList<ToDoLine> getAll(User user){
		return null;
	}
	
	public ArrayList<ToDoLine> searchByPattern(User user, String pattern){
		return null;
	}
}
