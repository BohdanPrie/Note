package ua.com.bohdanprie.notes.domain.managers.managersImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.dao.DaoFactory;
import ua.com.bohdanprie.notes.dao.UserDao;
import ua.com.bohdanprie.notes.dao.exceptions.DaoException;
import ua.com.bohdanprie.notes.domain.ManagerFactory;
import ua.com.bohdanprie.notes.domain.entities.User;
import ua.com.bohdanprie.notes.domain.exceptions.AuthorisationException;
import ua.com.bohdanprie.notes.domain.exceptions.NoSuchUserException;
import ua.com.bohdanprie.notes.domain.managers.UserManager;

public class UserManagerImpl implements UserManager{
	private static final Logger LOG = LogManager.getLogger(UserManagerImpl.class.getName());
	private NoteManager noteManager;
	private UserDao userDao;

	public UserManagerImpl() {
		noteManager = ManagerFactory.getInstance().getNoteManager();
		userDao = DaoFactory.getInstance().getUserDao();
	}

	@Override
	public User authorisation(String login, String password) {
		LOG.trace("Authorisation user  " + login);
		User tempUser = null;

		try {
			tempUser = userDao.find(login);
		} catch (DaoException e) {
			LOG.error("Fail to find user " + login, e);
		} catch (IllegalArgumentException e) {
			LOG.warn("No user " + login, e);
			throw new NoSuchUserException(e);
		}

		if (password == null || !password.equals(tempUser.getPassword())) {
			LOG.warn("Wrong password");
			throw new AuthorisationException("Wrong password");
		}
		LOG.info("User " + login + " was found");
		return tempUser;
	}

	@Override
	public User createAccount(String login, String password) {
		LOG.trace("Creating user " + login);
		User user = null;

		try {
			user = userDao.create(login, password);
		} catch (DaoException e) {
			LOG.warn("User  " + login + " already exist", e);
			throw new AuthorisationException("User " + login + " already exist", e);
		}
		LOG.info("User " + login + " was created");
		LOG.trace("Creating two notes for new user " + login);
		noteManager.create(0, user);
		noteManager.create(1, user);
		return user;
	}

	@Override
	public void changeLogin(String newLogin, User user) {
		LOG.trace("Changing login for user " + user.getLogin());
		try {
			userDao.changeLogin(user, newLogin);
		} catch (DaoException e) {
			LOG.warn("User  " + user.getLogin() + " already exist", e);
			throw new AuthorisationException("User " + user.getLogin() + " already exist", e);
		} catch (IllegalArgumentException e) {
			LOG.warn("No user " + user.getLogin());
			throw new NoSuchUserException(e);
		}
		LOG.info("Login changed from " + user.getLogin() + " to " + newLogin);
		user.changeLogin(newLogin);
	}

	@Override
	public void changePassword(User user, String newPassword) {
		LOG.trace("Changing password for user " + user.getLogin());
		try {
			userDao.changePassword(user, newPassword);
		} catch (DaoException e) {
			LOG.error("Fail to change password at user " + user.getLogin());
		} catch (IllegalArgumentException e) {
			LOG.warn("No user " + user.getLogin());
			throw new NoSuchUserException(e);
		}
		user.changePassword(newPassword);
		LOG.info("Password changed for user " + user.getLogin());
	}

	@Override
	public void deleteUser(User user) {
		LOG.trace("Deleting user " + user.getLogin());
		try {
			userDao.delete(user);
		} catch (DaoException e) {
			LOG.error("Fail to delete user " + user.getLogin());
		} catch (IllegalArgumentException e) {
			LOG.warn("No user " + user.getLogin());
			throw new NoSuchUserException(e);
		}
		LOG.info("User " + user.getLogin() + " was deleted");
	}
}