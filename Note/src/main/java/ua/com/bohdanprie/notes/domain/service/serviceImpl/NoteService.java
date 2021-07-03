package ua.com.bohdanprie.notes.domain.service.serviceImpl;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.bohdanprie.notes.dao.DaoManager;
import ua.com.bohdanprie.notes.dao.entityDao.NoteDao;
import ua.com.bohdanprie.notes.dao.exception.DaoException;
import ua.com.bohdanprie.notes.domain.ServiceUtils;
import ua.com.bohdanprie.notes.domain.entity.Note;
import ua.com.bohdanprie.notes.domain.entity.User;
import ua.com.bohdanprie.notes.domain.service.TextService;

public class NoteService implements TextService {
	private static final Logger LOG = LogManager.getLogger(NoteService.class.getName());
	private final NoteDao noteDao;

	public NoteService() {
		LOG.trace("Getting NoteDao instance");
		noteDao = DaoManager.getInstance().getNoteDao();
		LOG.debug("NoteService initialized");
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
			LOG.error("Fail to change note", e);
		}
		LOG.info("Note changed at user " + user.getLogin());
	}

	@Override
	public void deleteAll(User user) {
		LOG.trace("Deleting all notes from user " + user.getLogin());
		try {
			noteDao.deleteAll(user);
		} catch (DaoException e) {
			LOG.error("Fail to delete all notes", e);
		}
		LOG.info("All notes were deleted from user " + user.getLogin());
	}

	@Override
	public void delete(int id, User user) {
		LOG.trace("Deleting note by id from user " + user.getLogin());
		try {
			noteDao.delete(id, user);
		} catch (DaoException e) {
			LOG.error("Fail to delete note", e);
		}
		LOG.info("Note was deleted from user " + user.getLogin());
	}

	@Override
	public void create(int id, User user) {
		LOG.trace("Creating note for user " + user.getLogin());
		Note note = null;
		try {
			note = noteDao.create(id, user);
		} catch (DaoException e) {
			LOG.error("Fail to create note", e);
		}
		LOG.info("Note was created for user " + user.getLogin());
		user.getNotes().add(note);
	}

	@Override
	public String getAll(User user) {
		LOG.trace("Getting all notes from  user " + user.getLogin());
		List<Note> notes = null;
		try {
			notes = noteDao.getAll(user);
		} catch (DaoException e) {
			LOG.error("Fail to get all notes", e);
		}
		LOG.trace("Sorting notes by last change at user " + user.getLogin());
		notes = ServiceUtils.sortByLastChange(notes);
		user.setNotes(notes);
		LOG.info("Returning JSON notes");
		return ServiceUtils.toJSON(notes);
	}

	@Override
	public String getSortedByCreation(User user) {
		LOG.trace("Getting sorted notes by creation at user " + user.getLogin());
		List<Note> notes = null;
		try {
			notes = noteDao.getAll(user);
		} catch (DaoException e) {
			LOG.error("Fail to get all notes", e);
		}
		LOG.trace("Sorting notes by time creation at user " + user.getLogin());
		notes = ServiceUtils.sortByTimeCreation(notes);
		user.setNotes(notes);
		LOG.info("Returning JSON notes");
		return ServiceUtils.toJSON(notes);
	}

	@Override
	public String searchByPattern(User user, String pattern) {
		LOG.trace("Getting all notes by pattern at user " + user.getLogin());
		List<Note> notes = null;
		try {
			notes = noteDao.searchByPattern(user, pattern);
		} catch (DaoException e) {
			LOG.error("Fail to get notes by pattern", e);
		}
		user.setNotes(notes);
		LOG.info("Returning JSON notes");
		return ServiceUtils.toJSON(notes);
	}
}