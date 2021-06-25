package ua.com.bohdanprie.notes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.domain.entities.User;

import java.sql.SQLException;

public class UserDao {
	private static final Logger LOG = LogManager.getLogger(UserDao.class.getName());
	private DaoFactory daoFactory = DaoFactory.getInstance();

	public User create(String login, String password) {
		LOG.trace("Creating user with login = " + login);
		String SQL = "insert into notes.users (login, password) values (?, ?);";

		User user = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, login);
				statement.setString(2, password);
				try {
					LOG.trace("Getting result set");
					resultSet = statement.executeQuery();
					if (resultSet.next()) {
						LOG.trace("Creating user to return");
						user = new User(resultSet.getString("login"), resultSet.getString("password"));
					}
				} catch (SQLException e) {
					LOG.warn("Fail to create user", e);
					throw new DaoException("Fail to create user", e);
				}
			} catch (SQLException e) {
				LOG.error("Fail to prepare statement", e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Returning created user");
		return user;
	}

	public void changeLogin(User user, String newLogin) {
		LOG.trace("Changing login for user with login = " + user.getLogin());

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
					throw new DaoException("Fail to change login");
				}
			} catch (SQLException e) {
				LOG.error("Fail to prepare statement", e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Login changed from " + user.getLogin() + " to " + newLogin);
	}

	public void changePassword(User user, String newPassword) {
		LOG.trace("Changing password for user with login = " + user.getLogin());

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
					throw new DaoException("Fail to change password");
				}
			} catch (SQLException e) {
				LOG.error("Fail to prepare statement", e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Password changed for user " + user.getLogin());
	}

	public void delete(User user) {
		LOG.trace("Deleting user with login = " + user.getLogin());

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
					throw new DaoException("Fail to delete user");
				}
			} catch (SQLException e) {
				LOG.error("Fail to prepare statement", e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("User with login = " + user.getLogin() + " was deleted");
	}

	public User find(String login) {
		LOG.trace("Authorisation user with login = " + login);
		String SQL = "SELECT * FROM notes.users where login = ?;";

		User user = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoFactory.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, login);
				try {
					LOG.trace("Getting result set");
					resultSet = statement.executeQuery();
					if (resultSet.next()) {
						user = new User(resultSet.getString("login"), resultSet.getString("password"));
					}
				} catch (SQLException e) {
					LOG.warn("User with login = " + login + " not found");
				}
			} catch (SQLException e) {
				LOG.error("Fail to prepare statement", e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Returning user with login = " + user.getLogin());
		return user;
	}
}