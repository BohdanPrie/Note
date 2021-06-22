package ua.com.bohdanprie.notes.dao;

import java.sql.*;

import ua.com.bohdanprie.notes.domain.User;

public class UserDao {
	private DaoFactory daoFactory = DaoFactory.getInstance();
	
	public User create(String login, String password) {
		String SQL = "insert into notes.users (login, password) values (?, ?);";

		User user = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = daoFactory.getConnection();
			try {
				statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, login);
				statement.setString(2, password);
				statement.execute();
				resultSet = statement.getGeneratedKeys();
				if (resultSet.next()) {
					user = new User(resultSet.getString("login"), resultSet.getString("password"));
				}
			} catch (SQLException e) {
				throw new DaoException("Prepare statement fail", e);
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
		return user;
	}

	public void changeLogin(User user, String newLogin) {
		String oldLogin = user.getLogin();
		String SQL = "UPDATE notes.users SET login = ? WHERE login = ?;";
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = daoFactory.getConnection();
			try {
				statement = connection.prepareStatement(SQL);
				statement.setString(1, newLogin);
				statement.setString(2, oldLogin);
				try {
					statement.execute();
				} catch (SQLException e) {
					throw new DaoException("Fail change login, user already exist", e);
				}
			} catch (SQLException e) {
				throw new DaoException("Prepare statement fail", e);
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
	}

	public void changePassword(User user, String newPassword) {
		String login = user.getLogin();
		String SQL = "UPDATE notes.users SET password = ? WHERE login = ?;";
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = daoFactory.getConnection();
			try {
				statement = connection.prepareStatement(SQL);
				statement.setString(1, newPassword);
				statement.setString(2, login);
				statement.execute();
			} catch (SQLException e) {
				throw new DaoException("Prepare statement fail", e);
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
	}

	public void delete(User user) {
		String SQL = "DELETE FROM notes.users WHERE login = ?";

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
				throw new DaoException("Fail to delete user", e);
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

	public User find(String login) {
		String SQL = "SELECT * FROM notes.users where login = ?;";

		User user = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = daoFactory.getConnection();
			try {
				statement = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, login);
				resultSet = statement.executeQuery();
				if (resultSet.next()) {
					user = new User(resultSet.getString("login"), resultSet.getString("password"));
				}
			} catch (SQLException e) {
				throw new DaoException("Wrong login", e);
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
		return user;
	}
}