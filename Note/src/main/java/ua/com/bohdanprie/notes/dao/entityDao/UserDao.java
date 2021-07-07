package ua.com.bohdanprie.notes.dao.entityDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.dao.DaoManager;
import ua.com.bohdanprie.notes.dao.exception.DBException;
import ua.com.bohdanprie.notes.dao.exception.DaoException;
import ua.com.bohdanprie.notes.domain.entity.User;
/**
 * Class provide work with CRUD operations with {@link User}
 * @author bohda
 *
 */
public class UserDao {
	private final DaoManager daoManager;
	private static final Logger LOG = LogManager.getLogger(UserDao.class.getName());

	public UserDao() {
		LOG.trace("Getting DaoManager instance");
		daoManager = DaoManager.getInstance();
		LOG.debug("UserDao initialized");
	}
	
	/**
	 * Create new {@link User} with given login and password
	 * @throws DaoException if failed to create {@link User}
	 * @param login
	 * @param password
	 * @return created {@link User}
	 */
	public User create(String login, String password) {
		LOG.trace("Creating user " + login);
		String SQL = "INSERT INTO notes.users (login, password) VALUES (?, ?);";

		User user = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
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
				LOG.error("User " + login + " was not created", e);
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

	/**
	 * Method changes {@link User}'s current login to given newLogin 
	 * @throws IllegalArgumentException if {@link User} wasn't found
	 * @throws DaoException if failed to change login at given {@link User}
	 * @param user
	 * @param newLogin
	 */
	public void changeLogin(User user, String newLogin) {
		LOG.trace("Changing login for user " + user.getLogin());

		String oldLogin = user.getLogin();
		String SQL = "UPDATE notes.users SET login = ? WHERE login = ?;";

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, newLogin);
				statement.setString(2, oldLogin);

				LOG.trace("Executing SQL");
				int rowsAffected = statement.executeUpdate();
				if (rowsAffected == 0) {
					LOG.warn("User " + user.getLogin() + " not found");
					throw new IllegalArgumentException("User " + user.getLogin() + " not found");
				}
			} catch (SQLException e) {
				LOG.error("Fail to change login", e);
				throw new DaoException("Fail to change login", e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Login changed from " + user.getLogin() + " to " + newLogin);
	}

	/**
	 * Method changes {@link User}'s current password to given newPassword 
	 * @throws IllegalArgumentException if {@link User} wasn't found
	 * @throws DaoException if failed to change password at given {@link User}
	 * @param user
	 * @param newPassword
	 */
	public void changePassword(User user, String newPassword) {
		LOG.trace("Changing password for user " + user.getLogin());

		String login = user.getLogin();
		String SQL = "UPDATE notes.users SET password = ? WHERE login = ?;";

		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, newPassword);
				statement.setString(2, login);
				LOG.trace("Executing SQL");
				int rowsAffected = statement.executeUpdate();
				if (rowsAffected == 0) {
					LOG.warn("User " + user.getLogin() + " not found");
					throw new IllegalArgumentException("User " + user.getLogin() + " not found");
				}
			} catch (SQLException e) {
				LOG.error("Fail to change password", e);
				throw new DaoException("Fail to change password", e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("Password changed for user " + user.getLogin());
	}

	/**
	 * Method deletes given {@link User}
	 * @throws IllegalArgumentException if {@link User} wasn't found
	 * @throws DaoException if failed to delete given {@link User}
	 * @param user
	 */
	public void delete(User user) {
		LOG.trace("Deleting user " + user.getLogin());

		String SQL = "DELETE FROM notes.users WHERE login = ?";
		PreparedStatement statement = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
			try {
				LOG.trace("Preparing statement");
				statement = connection.prepareStatement(SQL);
				statement.setString(1, user.getLogin());
				LOG.trace("Executing SQL");
				int rowsAffected = statement.executeUpdate();
				if (rowsAffected == 0) {
					LOG.warn("User " + user.getLogin() + " not found");
					throw new IllegalArgumentException("User " + user.getLogin() + " not found");
				}
			} catch (SQLException e) {
				LOG.error("Fail to delete user", e);
				throw new DaoException("Fail to delete user", e);
			}
		} catch (DBException e) {
			LOG.error("Fail to create connection", e);
		} catch (SQLException e) {
			LOG.error("Fail at closing", e);
		}
		LOG.info("User " + user.getLogin() + " was deleted");
	}

	/**
	 * Method looking for user by given login
	 * @throws IllegalArgumentException if {@link User} with given login wasn't found
	 * @throws DaoException if failed to find {@link User}
	 * @param login
	 * @return found {@link User}
	 */
	public User find(String login) {
		LOG.trace("Authorisation user " + login);
		String SQL = "SELECT * FROM notes.users WHERE login = ?;";

		User user = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LOG.trace("Creating connection");
		try (Connection connection = daoManager.getConnection()) {
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