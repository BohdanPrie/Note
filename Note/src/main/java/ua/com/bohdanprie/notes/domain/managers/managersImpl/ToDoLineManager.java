package ua.com.bohdanprie.notes.domain.managers.managersImpl;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.bohdanprie.notes.dao.DaoFactory;
import ua.com.bohdanprie.notes.dao.ToDoLineDao;
import ua.com.bohdanprie.notes.dao.exceptions.DaoException;
import ua.com.bohdanprie.notes.domain.entities.ToDoLine;
import ua.com.bohdanprie.notes.domain.entities.User;
import ua.com.bohdanprie.notes.domain.managers.TextManager;

public class ToDoLineManager implements TextManager{
	private static final Logger LOG = LogManager.getLogger(ToDoLineManager.class.getName());
	private ToDoLineDao toDoLineDao;

	public ToDoLineManager() {
		toDoLineDao = DaoFactory.getInstance().getToDoLineDao();
	}
	
	@Override
	public void deleteAll(User user) {
		LOG.trace("Deleting all toDoLists from user " + user.getLogin());
		try {
			toDoLineDao.deleteAll(user);
		} catch (DaoException e) {
			LOG.warn("Fail to delete all toDoLists", e);
		}
	}

	@Override
	public void create(int id, User user) {
		ToDoLine toDoLine = null;
		try {
			toDoLine = toDoLineDao.create(id, user);
		} catch (DaoException e) {
			
		}
		user.getToDoLines().add(toDoLine);
	}

	@Override
	public void delete(int id, User user) {
		try {
			toDoLineDao.delete(id, user);
		} catch (DaoException e) {
			LOG.warn("Fail to delete toDoLine");
		}
	}

	@Override
	public void change(String JSON, User user) {
		ObjectMapper mapper = new ObjectMapper();
		ToDoLine line = null;
		try {
			line = mapper.readValue(JSON, ToDoLine.class);
			toDoLineDao.change(line, user);
		} catch (IOException e) {
			LOG.warn("Fail to convert JSON to ToDoLine", e);
		} catch (DaoException e) {
			LOG.warn("Fail to change ToDoLine", e);
		}
	}

	@Override
	public String getAll(User user) {
		List<ToDoLine> toDoLines = null;
		try {
			toDoLines = toDoLineDao.getAll(user);
		} catch (DaoException e) {
			
		}
		toDoLines = sortByLastChange(toDoLines);
		user.setToDoLines(toDoLines);
		return toJSON(toDoLines);
	}

	@Override
	public String getSortedByCreation(User user) {
		List<ToDoLine> toDoLines = null;
		
		try {
			toDoLines = toDoLineDao.getAll(user);
		} catch (DaoException e) {
			
		}
		toDoLines = sortByTimeCreation(toDoLines);
		user.setToDoLines(toDoLines);
		return toJSON(toDoLines);
	}

	@Override
	public String searchByPattern(User user, String pattern) {
		List<ToDoLine> toDoLines = null;
		
		try {
			toDoLines = toDoLineDao.searchByPattern(user, pattern);
		} catch (DaoException e) {
			
		}
		user.setToDoLines(toDoLines);
		return toJSON(toDoLines);
	}
	
	private List<ToDoLine> sortByLastChange(List<ToDoLine> lines){
		return lines.stream().sorted(Comparator.comparing(line -> line.getTimeChange().getTime()))
				.collect(Collectors.toList());
	}
	
	private List<ToDoLine> sortByTimeCreation(List<ToDoLine> lines) {
		LOG.trace("Sorting notes by time creation");
		return lines.stream().sorted(Comparator.comparing(note -> note.getTimeCreation().getTime()))
				.collect(Collectors.toList());
	}
	
	private String toJSON(List<ToDoLine> lines) {
		return null;
	}
}
