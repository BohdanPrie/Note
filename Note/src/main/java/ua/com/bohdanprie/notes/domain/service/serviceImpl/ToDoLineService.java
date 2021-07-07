package ua.com.bohdanprie.notes.domain.service.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.bohdanprie.notes.dao.DaoManager;
import ua.com.bohdanprie.notes.dao.entityDao.ToDoDao;
import ua.com.bohdanprie.notes.dao.entityDao.ToDoLineDao;
import ua.com.bohdanprie.notes.dao.exception.DaoException;
import ua.com.bohdanprie.notes.domain.ServiceUtils;
import ua.com.bohdanprie.notes.domain.entity.ToDo;
import ua.com.bohdanprie.notes.domain.entity.ToDoLine;
import ua.com.bohdanprie.notes.domain.entity.User;
import ua.com.bohdanprie.notes.domain.service.TextService;

public class ToDoLineService implements TextService {
	private static final Logger LOG = LogManager.getLogger(ToDoLineService.class.getName());
	private final ToDoDao toDoDao;
	private final ToDoLineDao toDoLineDao;

	public ToDoLineService() {
		LOG.trace("Getting ToDoLineDao instance");
		toDoLineDao = DaoManager.getInstance().getToDoLineDao();
		LOG.trace("Getting ToDoDao instance");
		toDoDao = DaoManager.getInstance().getToDoDao();
		LOG.debug("ToDoLineService initialized");
	}

	@Override
	public void deleteAll(User user) {
		LOG.trace("Deleting all toDoLines and toDos from user " + user.getLogin());
		try {
			toDoDao.deleteAll(user);
			toDoLineDao.deleteAll(user);
		} catch (DaoException e) {
			LOG.error("Fail to delete all toDoLines and toDos", e);
		}
		LOG.info("All toDoLines and toDos were deleted from user " + user.getLogin());
	}

	@Override
	public void create(int id, User user) {
		LOG.trace("Creating new toDoLine for user " + user.getLogin());
		ToDoLine toDoLine = null;
		try {
			toDoLine = toDoLineDao.create(id, user);
		} catch (DaoException e) {
			LOG.error("Fail to create toDoLine for user " + user.getLogin(), e);
		}
		user.getToDoLines().add(toDoLine);
		LOG.info("ToDoLine was created for user " + user.getLogin());
	}

	@Override
	public void delete(int id, User user) {
		LOG.trace("Deleting toDoLine and toDos by id from user " + user.getLogin());
		try {
			toDoDao.deleteFromLine(id, user);
			toDoLineDao.delete(id, user);
		} catch (DaoException e) {
			LOG.warn("Fail to delete toDoLine and toDos", e);
		}
		LOG.info("ToDoLine and toDos was deleted from user " + user.getLogin());
	}

	@Override
	public void change(String JSON, User user) {
		LOG.trace("Changing toDoLine and toDos at user " + user.getLogin());
		ObjectMapper mapper = new ObjectMapper();
		ToDoLine toDoLine = null;
		try {
			toDoLine = mapper.readValue(JSON, ToDoLine.class);
			toDoDao.change(toDoLine.getId(), toDoLine.getToDo(), user);
			toDoLineDao.change(toDoLine, user);
		} catch (IOException e) {
			LOG.warn("Fail to convert JSON to toDoLine", e);
		} catch (DaoException e) {
			LOG.warn("Fail to change toDoLine", e);
		}
		LOG.info("ToDoLine and toDos were changed at user " + user.getLogin());
	}

	@Override
	public String getAll(User user) {
		LOG.trace("Getting all toDoLines and toDos from user " + user.getLogin());
		ArrayList<ToDoLine> toDoLines = null;
		Map<Integer, ArrayList<ToDo>> arraysToDo = null;
		try {
			toDoLines = toDoLineDao.getAll(user);
			arraysToDo = toDoDao.getAll(user);
		} catch (DaoException e) {
			LOG.error("Fail to get all toDoLines and toDos from user " + user.getLogin(), e);
		}
		LOG.trace("Combining returned toDoLines and toDos at user " + user.getLogin());
		ServiceUtils.combineResult(toDoLines, arraysToDo);
		LOG.trace("Sorting toDoLines by last change at user " + user.getLogin());
		toDoLines = ServiceUtils.sortByLastChange(toDoLines);
		user.setToDoLines(toDoLines);
		LOG.info("Returning JSON toDoLines");
		return ServiceUtils.toJSON(toDoLines);
	}

	@Override
	public String getSortedByCreation(User user) {
		LOG.trace("Getting sorted toDoLines by creation at user " + user.getLogin());
		ArrayList<ToDoLine> toDoLines = null;
		Map<Integer, ArrayList<ToDo>> arraysToDo = null;
		try {
			toDoLines = toDoLineDao.getAll(user);
			arraysToDo = toDoDao.getAll(user);
		} catch (DaoException e) {
			LOG.error("Fail to get all toDoLines and toDos from user " + user.getLogin());
		}
		LOG.trace("Combining returned toDoLines and toDos at user " + user.getLogin());
		ServiceUtils.combineResult(toDoLines, arraysToDo);
		LOG.trace("Sorting toDoLines by time creation at user " + user.getLogin());
		toDoLines = ServiceUtils.sortByTimeCreation(toDoLines);
		user.setToDoLines(toDoLines);
		LOG.info("Returning JSON toDoLines");
		return ServiceUtils.toJSON(toDoLines);
	}

	@Override
	public String searchByPattern(User user, String pattern) {
		LOG.trace("Getting all toDoLines and toDos by pattern from user " + user.getLogin());
		ArrayList<ToDoLine> toDoLines = null;
		Map<Integer, ArrayList<ToDo>> arraysToDo = null;
		Integer[] patternId = null;
		try {
			patternId = toDoLineDao.getAllIdByPattern(user, pattern);
			toDoLines = toDoLineDao.searchByPattern(patternId, user);
			arraysToDo = toDoDao.getById(patternId, user);
		} catch (DaoException e) {
			LOG.error("Fail to get all toDoLines and toDos by pattern from user " + user.getLogin());
		}
		LOG.trace("Combining returned toDoLines and toDos from user " + user.getLogin());
		ServiceUtils.combineResult(toDoLines, arraysToDo);
		user.setToDoLines(toDoLines);
		LOG.info("Returning JSON toDoLines");
		return ServiceUtils.toJSON(toDoLines);
	}
}
