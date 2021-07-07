package ua.com.bohdanprie.notes.domain.service;

import ua.com.bohdanprie.notes.domain.entity.User;
/**
 * Interface that represent work with {@link User}'s collections elements
 * <br>All services that work with user's collections elements must implement this interface
 * <br>Contains basic methods to work with elements
 * <br>Type of elements depends on realization
 * @author bohda
 *
 */
public interface TextService {
	
	/**
	 * Deletes all elements from given {@link User}.
	 * @param user
	 */
	public void deleteAll(User user);
	
	/**
	 * Creates new element with given id at {@link User} of the type. 
	 * @param id
	 * @param user
	 */
	public void create(int id, User user);
	
	/**
	 * Deletes element by id from given {@link User}. 
	 * @param id
	 * @param user
	 */
	public void delete(int id, User user);
	
	/**
	 * Changes data of the element at given {@link User}. Data contains at given {@link String}
	 * @param JSON
	 * @param user
	 */
	public void change(String JSON, User user);
	
	/**
	 * Returns all elements from given {@link User}.
	 * @param user
	 * @return {@link String} representation of returned data
	 */
	public String getAll(User user);
	
	/**
	 * Returns all elements from given {@link User} and sort data by time creation.
	 * @param user
	 * @return {@link String} representation of returned data, sorted by time creation
	 */
	public String getSortedByCreation(User user);
	
	/**
	 * Returns all elements from given {@link User}, that satisfy given pattern.
	 * @param user
	 * @param pattern
	 * @return {@link String} representation of returned data, that satisfy given pattern
	 */
	public String searchByPattern(User user, String pattern);
}