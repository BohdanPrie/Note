package ua.com.bohdanprie.notes.domain.manager.managerImpl;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.bohdanprie.notes.dao.DaoFactory;
import ua.com.bohdanprie.notes.dao.entityDao.NoteDao;
import ua.com.bohdanprie.notes.dao.exception.DaoException;
import ua.com.bohdanprie.notes.domain.ManagerUtils;
import ua.com.bohdanprie.notes.domain.entity.Note;
import ua.com.bohdanprie.notes.domain.entity.User;
import ua.com.bohdanprie.notes.domain.manager.TextManager;

public class NoteManager implements TextManager {
	private static final Logger LOG = LogManager.getLogger(NoteManager.class.getName());
	private NoteDao noteDao;

	public NoteManager() {
		LOG.info("Getting NoteDao instance");
		noteDao = DaoFactory.getInstance().getNoteDao();
	}

	@Override
	public void change(String JSONData, User user) {
		LOG.trace("Changing note at user " + user.getLogin());
		ObjectMapper mapper = new ObjectMapper();
		Note note = null;
		try {
			note = mapper.readValue(JSONData, Note.class);
			noteDao.change(note, user);
		} catch (IOException e) {
			LOG.warn("Fail to convert JSON to note", e);
		} catch (DaoException e) {
			LOG.warn("Fail to change note", e);
		}
	}

	@Override
	public void deleteAll(User user) {
		LOG.trace("Deleting all notes from user " + user.getLogin());
		try {
			noteDao.deleteAll(user);
		} catch (DaoException e) {
			LOG.warn("Fail to delete all notes", e);
		}
	}

	@Override
	public void delete(int id, User user) {
		try {
			noteDao.delete(id, user);
		} catch (DaoException e) {
			LOG.warn("Fail to delete note", e);
		}
	}

	@Override
	public void create(int id, User user) {
		Note note = null;
		try {
			note = noteDao.create(id, user);
		} catch (DaoException e) {
			LOG.warn("Fail to create note", e);
		}
		user.getNotes().add(note);
	}

	@Override
	public String getAll(User user) {
		LOG.trace("Getting all notes from at user " + user.getLogin());
		List<Note> notes = null;
		try {
			notes = noteDao.getAll(user);
		} catch (DaoException e) {
			LOG.warn("Fail to get all notes", e);
		}
		notes = ManagerUtils.sortByLastChange(notes);
		user.setNotes(notes);
		LOG.info("Returning notes");
		return ManagerUtils.toJSON(notes);
	}
	
	@Override
	public String getSortedByCreation(User user) {
		LOG.trace("Getting sorted notes by creation at user " + user.getLogin());
		List<Note> notes = null;
		try {
			notes = noteDao.getAll(user);
		} catch (DaoException e) {
			LOG.warn("Fail to get all notes", e);
		}
		notes = ManagerUtils.sortByTimeCreation(notes);
		user.setNotes(notes);
		LOG.info("Returning notes");
		return ManagerUtils.toJSON(notes);
	}
	
	@Override
	public String searchByPattern(User user, String pattern) {
		LOG.trace("Getting all notes by pattern at user " + user.getLogin());
		List<Note> notes = null;
		try {
			notes = noteDao.searchByPattern(user, pattern);
		} catch (DaoException e) {
			LOG.warn("Fail to get notes by pattern", e);
		}
		user.setNotes(notes);
		LOG.info("Returning notes");
		return ManagerUtils.toJSON(notes);
	}
}