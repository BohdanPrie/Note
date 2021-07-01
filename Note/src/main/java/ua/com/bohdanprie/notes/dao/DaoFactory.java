package ua.com.bohdanprie.notes.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.dao.entitiesDao.NoteDao;
import ua.com.bohdanprie.notes.dao.entitiesDao.ToDoDao;
import ua.com.bohdanprie.notes.dao.entitiesDao.ToDoLineDao;
import ua.com.bohdanprie.notes.dao.entitiesDao.UserDao;
import ua.com.bohdanprie.notes.dao.exceptions.DBException;

public final class DaoFactory {
	private static final Logger LOG = LogManager.getLogger(DaoFactory.class.getName());
	private static DaoFactory daoFactory;
	private UserDao userDao;
	private NoteDao noteDao;
	private ToDoDao toDoDao;
	private ToDoLineDao toDoLineDao;
	private DataSource source;

	private DaoFactory() {
		try {
			InitialContext context = new InitialContext();
			source = (DataSource) context.lookup("java:comp/env/jdbc/Notes");
		} catch (NamingException e) {
			LOG.error("Fail lookup", e);
			throw new DBException("Fail lookup", e);
		}
	}
	
	public static DaoFactory getInstance() {
		if (daoFactory == null) {
			daoFactory = new DaoFactory();
			LOG.debug("DaoFactory initialized");
		}
		return daoFactory;
	}
	
	public ToDoDao getToDoDao() {
		if(toDoDao == null) {
			toDoDao = new ToDoDao();
			LOG.debug("ToDoDao initialized");
		}
		return toDoDao;
	}

	public ToDoLineDao getToDoLineDao() {
		if (toDoLineDao == null) {
			toDoLineDao = new ToDoLineDao();
			LOG.debug("ToDoLineDao initialized");
		}
		return toDoLineDao;
	}
	
	public UserDao getUserDao() {
		if (userDao == null) {
			userDao = new UserDao();
			LOG.debug("UserDao initialized");
		}
		return userDao;
	}

	public NoteDao getNoteDao() {
		if (noteDao == null) {
			noteDao = new NoteDao();
			LOG.debug("NoteDao initialized");
		}
		return noteDao;
	}

	public Connection getConnection() {
		LOG.trace("Creating connection");
		Connection connection;
		try {
			connection = source.getConnection();
		} catch (SQLException e) {
			LOG.error("Fail to create connection", e);
			throw new DBException("Fail connection", e);
		}
		LOG.info("Returning connection " + connection);
		return connection;
	}
}