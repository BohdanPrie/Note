package ua.com.bohdanprie.notes.domain.service;

import ua.com.bohdanprie.notes.domain.entity.User;

public interface TextService {
	
	public void deleteAll(User user);
	
	public void create(int id, User user);
	
	public void delete(int id, User user);
	
	public void change(String JSON, User user);
	
	public String getAll(User user);
	
	public String getSortedByCreation(User user);
	
	public String searchByPattern(User user, String pattern);
}