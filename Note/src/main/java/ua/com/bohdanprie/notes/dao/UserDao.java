package ua.com.bohdanprie.notes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

import ua.com.bohdanprie.notes.domain.User;

public class UserDao {
	private static final Logger LOG = LogManager.getLogger(UserDao.class.getName());
	private DaoFactory daoFactory = DaoFactory.getInstance();

	public User create(String login, String password) {
		LOG.trace("Creating user with login = " + login);
		String SQL = "insert into notes.users (login, password) values (?, ?);";

		User user = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			LOG.trace("Creating connection");
			connection = daoFactory.getConnection();
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
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
		} finally {
			LOG.trace("Closing elements");
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
		LOG.info("Returning created user");
		return user;
	}

	public void changeLogin(User user, String newLogin) {
		LOG.trace("Changing login for user with login = " + user.getLogin());

		String oldLogin = user.getLogin();
		String SQL = "UPDATE notes.users SET login = ? WHERE login = ?;";

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			LOG.trace("Creating connection");
			connection = daoFactory.getConnection();
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, newLogin);
				statement.setString(2, oldLogin);
				try {
					LOG.trace("Executing SQL");
					statement.execute();
				} catch (SQLException e) {
					LOG.warn("Fail to change login", e);
					throw new DaoException("Fail to change login", e);
				}
			} catch (SQLException e) {
				LOG.error("Fail to prepare statement", e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} finally {
			LOG.trace("Closing elements");
			try {
				if (connection != null) {
					connection.close();
				}
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				LOG.error("Fail at closing", e);
			}
		}
		LOG.info("Login changed from " + user.getLogin() + " to " + newLogin);
	}

	public void changePassword(User user, String newPassword) {
		LOG.trace("Changing password for user with login = " + user.getLogin());

		String login = user.getLogin();
		String SQL = "UPDATE notes.users SET password = ? WHERE login = ?;";
		
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			LOG.trace("Creating connection");
			connection = daoFactory.getConnection();
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, newPassword);
				statement.setString(2, login);
				try {
					LOG.trace("Executing SQL");
					statement.execute();
				} catch (SQLException e) {
					LOG.warn("Fail to change password", e);
					throw new DaoException("Fail to change password", e);
				}
			} catch (SQLException e) {
				LOG.error("Fail to prepare statement", e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} finally {
			LOG.trace("Closing elements");
			try {
				if (connection != null) {
					connection.close();
				}
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				LOG.error("Fail at closing", e);
			}
		}
		LOG.info("Password changed for user " + user.getLogin());
	}

	public void delete(User user) {
		LOG.trace("Deleting user with login = " + user.getLogin());

		String SQL = "DELETE FROM notes.users WHERE login = ?";
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			LOG.trace("Creating connection");
			connection = daoFactory.getConnection();
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				try {
					LOG.trace("Executing SQL");
					statement.execute();
				} catch (SQLException e) {
					LOG.warn("Fail to delete user", e);
					throw new DaoException("Fail to delete user", e);
				}
			} catch (SQLException e) {
				LOG.error("Fail to prepare statement", e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} finally {
			LOG.trace("Closing elements");
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
		LOG.info("User with login = " + user.getLogin() + " deleted");
	}

	public User find(String login) {
		LOG.trace("Authorisation user with login = " + login);
		String SQL = "SELECT * FROM notes.users where login = ?;";

		User user = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			LOG.trace("Creating connection");
			connection = daoFactory.getConnection();
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
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
		} finally {
			LOG.trace("Closing elements");
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
		LOG.info("Returning user with login = " + user.getLogin());
		return user;
	}
}