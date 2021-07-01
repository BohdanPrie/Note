package ua.com.bohdanprie.notes.domain.managers.managersImpl;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.bohdanprie.notes.dao.DaoFactory;
import ua.com.bohdanprie.notes.dao.entitiesDao.ToDoDao;
import ua.com.bohdanprie.notes.dao.entitiesDao.ToDoLineDao;
import ua.com.bohdanprie.notes.dao.exceptions.DaoException;
import ua.com.bohdanprie.notes.domain.ManagerUtils;
import ua.com.bohdanprie.notes.domain.entities.ToDoLine;
import ua.com.bohdanprie.notes.domain.entities.User;
import ua.com.bohdanprie.notes.domain.managers.TextManager;

public class ToDoLineManager implements TextManager{
	private static final Logger LOG = LogManager.getLogger(ToDoLineManager.class.getName());
	private ToDoDao toDoDao;
	private ToDoLineDao toDoLineDao;

	public ToDoLineManager() {
		toDoLineDao = DaoFactory.getInstance().getToDoLineDao();
		toDoDao = DaoFactory.getInstance().getToDoDao();
	}
	
	@Override
	public void deleteAll(User user) {
		LOG.trace("Deleting all toDoLists from user " + user.getLogin());
		try {
			toDoDao.deleteAll(user);
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
			toDoDao.deleteFromLine(id, user);
			toDoLineDao.delete(id, user);
		} catch (DaoException e) {
			LOG.warn("Fail to delete toDoLine");
		}
	}

	@Override
	public void change(String JSON, User user) {
		ObjectMapper mapper = new ObjectMapper();
		ToDoLine toDoLine = null;
		try {
			toDoLine = mapper.readValue(JSON, ToDoLine.class);
			toDoDao.change(toDoLine.getId(), toDoLine.getToDo(), user);
			toDoLineDao.change(toDoLine, user);
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
		toDoLines = ManagerUtils.sortByLastChange(toDoLines);
		user.setToDoLines(toDoLines);
		return ManagerUtils.toJSON(toDoLines);
	}

	@Override
	public String getSortedByCreation(User user) {
		List<ToDoLine> toDoLines = null;
		
		try {
			toDoLines = toDoLineDao.getAll(user);
		} catch (DaoException e) {
			
		}
		toDoLines = ManagerUtils.sortByTimeCreation(toDoLines);
		user.setToDoLines(toDoLines);
		return ManagerUtils.toJSON(toDoLines);
	}

	@Override
	public String searchByPattern(User user, String pattern) {
		List<ToDoLine> toDoLines = null;
		
		try {
			toDoLines = toDoLineDao.searchByPattern(user, pattern);
		} catch (DaoException e) {
			
		}
		user.setToDoLines(toDoLines);
		return ManagerUtils.toJSON(toDoLines);
	}
}
