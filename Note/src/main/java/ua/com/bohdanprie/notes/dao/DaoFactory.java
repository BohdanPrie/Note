package ua.com.bohdanprie.notes.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DaoFactory {
	private static final Logger LOG = LogManager.getLogger(DaoFactory.class.getName());
	private static DaoFactory daoFactory;
	private UserDao userDao;
	private NoteDao noteDao;

	private DaoFactory() {

	}

	public UserDao getUserDao() {
		if (userDao == null) {
			LOG.info("Creating UserDao");
			userDao = new UserDao();
		}
		return userDao;
	}

	public NoteDao getNoteDao() {
		if (noteDao == null) {
			LOG.info("Creating NoteDao");
			noteDao = new NoteDao();
		}
		return noteDao;
	}

	public static DaoFactory getInstance() {
		if (daoFactory == null) {
			LOG.info("Creating DaoFactory");
			daoFactory = new DaoFactory();
		}
		return daoFactory;
	}

	public Connection getConnection() {
		LOG.trace("Creating connection");
		InitialContext context = null;
		Connection connection = null;
		DataSource source = null;
		try {
			context = new InitialContext();
			source = (DataSource) context.lookup("java:comp/env/jdbc/Notes");
			connection = source.getConnection();
		} catch (NamingException e) {
			LOG.error("Fail ", e);
			throw new DBException("Fail lookup" , e);
		} catch (SQLException e) {
			LOG.error("Fail to create connection", e);
			throw new DBException("Fail connection", e);
		}
		LOG.info("Returning connection " + connection);
		return connection;
	}
}