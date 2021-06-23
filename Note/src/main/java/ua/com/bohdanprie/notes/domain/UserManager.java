package ua.com.bohdanprie.notes.domain;

import ua.com.bohdanprie.notes.dao.DaoFactory;
import ua.com.bohdanprie.notes.dao.UserDao;

public class UserManager {
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
		User tempUser = userDao.find(login);

		if (tempUser == null) {
			throw new NullPointerException("No user with this login");
		}

		if (password == null || !password.equals(tempUser.getPassword())) {
			throw new NullPointerException("Wrong password");
			// send message that password is not correct
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