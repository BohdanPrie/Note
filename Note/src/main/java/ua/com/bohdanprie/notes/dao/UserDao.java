package ua.com.bohdanprie.notes.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.dao.exceptions.DBException;
import ua.com.bohdanprie.notes.dao.exceptions.DaoException;
import ua.com.bohdanprie.notes.domain.entities.User;

import java.sql.SQLException;

public class UserDao {
	private static final Logger LOG = LogManager.getLogger(UserDao.class.getName());
	private DaoFactory daoFactory = DaoFactory.getInstance();

	public User create(String login, String password) {
		LOG.trace("Creating user " + login);
		String SQL = "INSERT INTO notes.users (login, password) VALUES (?, ?);";

		User user = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, login);
				statement.setString(2, password);
				statement.execute();
				LOG.trace("Getting result set");
				resultSet = statement.getGeneratedKeys();
				if (resultSet.next()) {
					LOG.trace("Creating user " + login);
					user = new User(resultSet.getString("login"), resultSet.getString("password"));
				}
			} catch (SQLException e) {
				LOG.warn("User " + login + " was not created", e);
				throw new DaoException("User " + login + " was not created", e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Returning created user " + user.getLogin());
		return user;
	}

	public void changeLogin(User user, String newLogin) {
		LOG.trace("Changing login for user " + user.getLogin());

		String oldLogin = user.getLogin();
		String SQL = "UPDATE notes.users SET login = ? WHERE login = ?;";

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, newLogin);
				statement.setString(2, oldLogin);

				LOG.trace("Executing SQL");
				int rowsAffected = statement.executeUpdate();
				if (rowsAffected == 0) {
					LOG.warn("Fail to change login");
					throw new IllegalArgumentException("Fail to change login");
				}
			} catch (SQLException e) {
				LOG.warn("Fail to change login", e);
				throw new DaoException("Fail to change login", e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Login changed from " + user.getLogin() + " to " + newLogin);
	}

	public void changePassword(User user, String newPassword) {
		LOG.trace("Changing password for user " + user.getLogin());

		String login = user.getLogin();
		String SQL = "UPDATE notes.users SET password = ? WHERE login = ?;";

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, newPassword);
				statement.setString(2, login);

				LOG.trace("Executing SQL");
				int rowsAffected = statement.executeUpdate();
				if (rowsAffected == 0) {
					LOG.warn("Fail to change password");
					throw new IllegalArgumentException("Fail to change password");
				}
			} catch (SQLException e) {
				LOG.warn("Fail to change password");
				throw new DaoException("Fail to change password");
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Password changed for user " + user.getLogin());
	}

	public void delete(User user) {
		LOG.trace("Deleting user " + user.getLogin());

		String SQL = "DELETE FROM notes.users WHERE login = ?";
		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());

				LOG.trace("Executing SQL");
				int rowsAffected = statement.executeUpdate();
				if (rowsAffected == 0) {
					LOG.warn("Fail to delete user");
					throw new IllegalArgumentException("Fail to delete user");
				}
			} catch (SQLException e) {
				LOG.warn("Fail to delete user");
				throw new DaoException("Fail to delete user");
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("User " + user.getLogin() + " was deleted");
	}

	public User find(String login) {
		LOG.trace("Authorisation user " + login);
		String SQL = "SELECT * FROM notes.users WHERE login = ?;";

		User user = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, login);

				LOG.trace("Getting result set");
				resultSet = statement.executeQuery();
				if (resultSet.next()) {
					user = new User(resultSet.getString("login"), resultSet.getString("password"));
				}
			} catch (SQLException e) {
				LOG.warn("User " + login + " not found", e);
				throw new DaoException("User " + login + " not found", e);
			}

			if (user == null) {
				LOG.warn("User " + login + " not found");
				throw new IllegalArgumentException("User " + login + " not found");
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Returning created user " + user.getLogin());
		return user;
	}
}