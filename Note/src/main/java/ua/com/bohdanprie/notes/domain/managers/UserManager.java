package ua.com.bohdanprie.notes.domain.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.dao.DaoException;
import ua.com.bohdanprie.notes.dao.DaoFactory;
import ua.com.bohdanprie.notes.dao.UserDao;
import ua.com.bohdanprie.notes.domain.entities.User;
import ua.com.bohdanprie.notes.domain.exceptions.AuthorisationException;

public class UserManager {
	private static final Logger LOG = LogManager.getLogger(NoteManager.class.getName());
	private UserDao userDao;

	public UserManager() {
		userDao = DaoFactory.getInstance().getUserDao();
	}

	public User authorisation(String login, String password) {
		LOG.trace("Authorisation user with login = " + login);
		User tempUser = null;
		
		try {
			tempUser = userDao.find(login);
		} catch (DaoException e) {
			LOG.warn("No user with login = " + login);
			throw new AuthorisationException("No user with login = " + login);
		}
		
		if (!password.equals(tempUser.getPassword())) {
			LOG.warn("Wrong password");
			throw new AuthorisationException("Wrong password");
		}
		return tempUser;
	}

	public User createAccount(String login, String password) {
		User user = null;

		try {
			user = userDao.create(login, password);
		} catch (DaoException e) {
			LOG.warn("Fail to create user", e);
			throw new AuthorisationException("");
		}
		return user;
	}

	public String changeLogin(String newLogin, User user) {
		userDao.changeLogin(user, newLogin);
		user.changeLogin(newLogin);
		return null;
	}

	public void changePassword(User user, String newPassword) {
		userDao.changePassword(user, newPassword);
		user.changePassword(newPassword);
	}

	public void deleteUser(User user) {
		try {
			userDao.delete(user);
		} catch (DaoException e) {
			
		}
	}
}