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
		
	/*{ // delete later
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}*/
	
	private DaoFactory() {
		
	}

	public UserDao getUserDao() {
		if(userDao == null) {
			userDao = new UserDao();
		}
		return userDao;
	}
	
	public NoteDao getNoteDao() {
		if(noteDao == null) {
			noteDao = new NoteDao();
		}
		return noteDao;
	}
	
	public static DaoFactory getInstance() {
		if(daoFactory == null) {
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
           
		} catch(SQLException e) {
			LOG.error("Fail to create connection", e);
			throw new DBException("Fail connection", e);
		}
		LOG.trace("Returning connection " + connection);
		return connection;
	}
}