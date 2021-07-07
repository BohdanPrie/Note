package ua.com.bohdanprie.notes.domain.service;

import ua.com.bohdanprie.notes.domain.entity.User;
/**
 * Interface that represents work with user's data.
 * Provide methods to work with user's data
 * @author bohda
 *
 */
public interface UserService {

	/**
	 * Search for {@link User} by given login
	 * @param login
	 * @param password
	 * @return {@link User}, that was found by the given login
	 */
	public User authorisation(String login, String password);

	/**
	 * Create new {@link User} by given data
	 * @param login
	 * @param password
	 * @return created {@link User}
	 */
	public User createAccount(String login, String password);

	/**
	 * Changes given {@link User} login with given new login
	 * @param newLogin
	 * @param user
	 */
	public void changeLogin(String newLogin, User user);

	/**
	 * Changes given {@link User} password with given new password
	 * @param newPassword
	 * @param user
	 */
	public void changePassword(User user, String newPassword);

	/**
	 * Deletes given {@link User} 
	 * @param user
	 */
	public void deleteUser(User user);
}