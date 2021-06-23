package ua.com.bohdanprie.notes.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DaoFactory {
	private static DaoFactory daoFactory;
	private UserDao userDao;
	private NoteDao noteDao;
	
	{ // delete later
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private DaoFactory() {
		
	}

	public static DaoFactory getInstance() {
		if(daoFactory == null) {
			daoFactory = new DaoFactory();
		}
		return daoFactory;
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
	
	public Connection getConnection() {
		Connection connection = null;
			try {
				Properties properties = new Properties();
				File file = new File("src/main/java/ua/com/bohdanprie/notes/db.properties");		
				InputStream fis = new FileInputStream(file);
				
				properties.load(fis);
				String url = properties.getProperty("URL");
				String owner = properties.getProperty("owner");
				String password = properties.getProperty("password");
				
				connection = DriverManager.getConnection(url, owner, password);
			} catch (IOException e) {
				throw new DBException("Fail to load properties", e);
			} catch (SQLException e){
				throw new DBException("Fail connection", e);
			}
		return connection;
	}
}