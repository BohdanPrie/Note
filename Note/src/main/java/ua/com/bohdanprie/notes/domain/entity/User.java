package ua.com.bohdanprie.notes.domain.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class User {
	private static final Logger LOG = LogManager.getLogger(User.class.getName());
	private String login;
	private String password;
	private List<Note> notes = new ArrayList<>();
	private List<ToDoLine> toDoLines = new ArrayList<>();

	public User(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public List<ToDoLine> getToDoLines() {
		return toDoLines;
	}

	public void setToDoLines(List<ToDoLine> toDoLines) {
		if (toDoLines == null) {
			LOG.debug("ToDoLines list is null");
		} else {
			this.toDoLines = toDoLines;
		}
	}

	public void setNotes(List<Note> notes) {
		if (notes == null) {
			LOG.debug("Notes list is null");
		} else {
			this.notes = notes;
		}
	}

	public void changeLogin(String login) {
		if (login == null || login.isEmpty()) {
			LOG.debug("Login is null or empty");
		} else {
			this.login = login;
		}
	}

	public void changePassword(String password) {
		if (password == null || password.isEmpty()) {
			LOG.debug("Password is null or empty");
		} else {
			this.password = password;
		}
	}
}
