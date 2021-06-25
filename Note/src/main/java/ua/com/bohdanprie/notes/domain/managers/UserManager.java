package ua.com.bohdanprie.notes.domain.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.dao.DaoException;
import ua.com.bohdanprie.notes.dao.DaoFactory;
import ua.com.bohdanprie.notes.dao.UserDao;
import ua.com.bohdanprie.notes.domain.ManagerFactory;
import ua.com.bohdanprie.notes.domain.entities.User;
import ua.com.bohdanprie.notes.domain.exceptions.AuthorisationException;
import ua.com.bohdanprie.notes.domain.exceptions.NoSuchUserException;

public class UserManager {
	private static final Logger LOG = LogManager.getLogger(NoteManager.class.getName());
	private NoteManager noteManager;
	private UserDao userDao;

	public UserManager() {
		noteManager = ManagerFactory.getInstance().getNoteManager();
		userDao = DaoFactory.getInstance().getUserDao();
	}

	public User authorisation(String login, String password) {
		LOG.trace("Authorisation user with login = " + login);
		User tempUser = null;

		try {
			tempUser = userDao.find(login);
		} catch (DaoException e) {
			LOG.warn("No user with login = " + login, e);
			throw new NoSuchUserException(e);
		}

		if (!password.equals(tempUser.getPassword())) {
			LOG.warn("Wrong password");
			throw new AuthorisationException("Wrong password");
		}
		return tempUser;
	}

	public User createAccount(String login, String password) {
		LOG.trace("Creating user with login = " + login);
		User user = null;

		try {
			user = userDao.create(login, password);
		} catch (DaoException e) {
			LOG.warn("User with login " + login + " already exist", e);
			throw new AuthorisationException("User with login " + login + " already exist", e);
		}
		noteManager.createNote(0, user);
		noteManager.createNote(1, user);
		LOG.info("User with login = " + login + " was created");
		return user;
	}

	public void changeLogin(String newLogin, User user) {
		LOG.trace("Changing login for user with login = " + user.getLogin());
		try {
			userDao.changeLogin(user, newLogin);
		} catch (DaoException e) {
			LOG.warn("No user with login = " + user.getLogin(), e);
			throw new NoSuchUserException(e);
		}
		user.changeLogin(newLogin);
		LOG.info("Login changed from " + user.getLogin() + " to " + newLogin);
	}

	public void changePassword(User user, String newPassword) {
		LOG.trace("Changing password for user with login = " + user.getLogin());
		try {
			userDao.changePassword(user, newPassword);
		} catch (DaoException e) {
			LOG.warn("No user with login = " + user.getLogin(), e);
			throw new NoSuchUserException(e);
		}
		user.changePassword(newPassword);
		LOG.info("Password changed for user " + user.getLogin());
	}

	public void deleteUser(User user) {
		LOG.trace("Deleting user with login = " + user.getLogin());
		try {
			userDao.delete(user);
		} catch (DaoException e) {
			LOG.warn("No user with login = " + user.getLogin(), e);
			throw new NoSuchUserException(e);
		}
		LOG.info("User with login = " + user.getLogin() + " was deleted");
	}
}