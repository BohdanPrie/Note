package ua.com.bohdanprie.notes.domain.service.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.dao.DaoManager;
import ua.com.bohdanprie.notes.dao.entityDao.UserDao;
import ua.com.bohdanprie.notes.dao.exception.DaoException;
import ua.com.bohdanprie.notes.domain.ServiceManager;
import ua.com.bohdanprie.notes.domain.entity.User;
import ua.com.bohdanprie.notes.domain.exception.AuthorisationException;
import ua.com.bohdanprie.notes.domain.exception.NoSuchUserException;
import ua.com.bohdanprie.notes.domain.service.UserService;

public class UserServiceImpl implements UserService {
	private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class.getName());
	private final NoteService noteService;
	private final ToDoLineService toDoLineService;
	private final UserDao userDao;

	public UserServiceImpl() {
		LOG.trace("Getting ToDoLineService instance");
		toDoLineService = ServiceManager.getInstance().getToDoLineService();
		LOG.trace("Getting NoteService instance");
		noteService = ServiceManager.getInstance().getNoteService();
		LOG.trace("Getting UserDao instance");
		userDao = DaoManager.getInstance().getUserDao();
		LOG.debug("UserService initialized");
	}

	@Override
	public User authorisation(String login, String password) {
		LOG.trace("Authorisation user  " + login);
		User user = null;

		try {
			user = userDao.find(login);
		} catch (DaoException e) {
			LOG.error("Fail to find user " + login, e);
		} catch (IllegalArgumentException e) {
			LOG.warn("No user " + login, e);
			throw new NoSuchUserException(e);
		}

		if (password == null || !password.equals(user.getPassword())) {
			LOG.warn("Wrong password");
			throw new AuthorisationException("Wrong password");
		}
		LOG.info("User " + user.getLogin() + " was found");
		return user;
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
		LOG.info("User " + user.getLogin() + " was created");
		LOG.trace("Creating basic elements for user " + user.getLogin());
		createForNewUser(user);
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

	private void createForNewUser(User user) {
		LOG.trace("Creating basic elements for user " + user.getLogin());
		for (int id = 0; id < 2; id++) {
			LOG.trace("Creating note for user " + user.getLogin());
			noteService.create(id, user);
			LOG.trace("Creating toDoLine for user " + user.getLogin());
			toDoLineService.create(id, user);
		}
		LOG.trace("Basic elements created for user " + user.getLogin());
	}
}