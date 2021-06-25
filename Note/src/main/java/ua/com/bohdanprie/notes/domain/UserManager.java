package ua.com.bohdanprie.notes.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.dao.DaoException;
import ua.com.bohdanprie.notes.dao.DaoFactory;
import ua.com.bohdanprie.notes.dao.UserDao;

public class UserManager {
	private static final Logger LOG = LogManager.getLogger(NoteManager.class.getName());
	private UserDao userDao;
	private NoteManager noteManager;

	private UserManager() {
		userDao = DaoFactory.getInstance().getUserDao();
		noteManager = ManagerFactory.getInstance().getNoteManager();
	}

	public static UserManager getInstance() {
		return new UserManager();
	}

	public User authorisation(String login, String password) throws NullPointerException {
		User tempUser = null;
		LOG.trace("Authorisation user with login = " + login);

		try {
			tempUser = userDao.find(login);
		} catch (DaoException e) {
			
		}
		if (tempUser == null) {
			throw new AuthorisationException("No user with this login");
		}

		if (password == null || !password.equals(tempUser.getPassword())) {
			LOG.warn("Wrong password");
			throw new AuthorisationException("Wrong password");
		}
		noteManager.getAll(tempUser);
		return tempUser;
	}

	public User createAccount(String login, String password) {
		User user = userDao.create(login, password);
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
		userDao.delete(user);
	}
}