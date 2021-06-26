package ua.com.bohdanprie.notes.domain.managers;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.bohdanprie.notes.dao.DaoFactory;
import ua.com.bohdanprie.notes.dao.NoteDao;
import ua.com.bohdanprie.notes.dao.exceptions.DaoException;
import ua.com.bohdanprie.notes.domain.entities.Note;
import ua.com.bohdanprie.notes.domain.entities.User;

public class NoteManager {
	private static final Logger LOG = LogManager.getLogger(NoteManager.class.getName());
	private NoteDao noteDao;

	public NoteManager() {
		LOG.info("Getting NoteDao instance");
		noteDao = DaoFactory.getInstance().getNoteDao();
	}

	public void changeNote(String JSONData, User user) {
		LOG.trace("Changing note at user " + user.getLogin());
		ObjectMapper mapper = new ObjectMapper();
		Note note = null;
		try {
			note = mapper.readValue(JSONData, Note.class);
			noteDao.changeNote(note, user);
		} catch (IOException e) {
			LOG.warn("Fail to convert JSON to note");
		} catch (DaoException e) {
			LOG.warn("Fail to change note", e);
		}
	}

	public void deleteAll(User user) {
		LOG.trace("Deleting all notes from user " + user.getLogin());
		try {
			noteDao.deleteAll(user);
		} catch (DaoException e) {
			LOG.warn("Fail to delete note", e);
		}
	}

	public void deleteNote(int id, User user) {
		try {
			noteDao.deleteNote(id, user);
		} catch (DaoException e) {
			LOG.warn("Fail to delete all notes", e);
		}
	}

	public void createNote(int id, User user) {
		Note note = null;
		try {
			note = noteDao.createNote(id, user);
		} catch (DaoException e) {
			LOG.warn("Fail to create note", e);
		}
		user.getNotes().add(note);
	}

	public String getAll(User user) {
		LOG.trace("Getting all notes from at user " + user.getLogin());
		List<Note> notes = null;
		try {
			notes = noteDao.getAllNotes(user);
		} catch (DaoException e) {
			LOG.warn("Fail to get all notes", e);
		}
		user.setNotes(notes);

		notes = sortByLastChange(notes);
		LOG.info("Returning notes");
		return notesToJSON(notes);
	}

	public String getSortedByCreation(User user) {
		LOG.trace("Getting sorted notes by creation at user " + user.getLogin());
		List<Note> notes = null;
		try {
			notes = noteDao.getAllNotes(user);
		} catch (DaoException e) {
			LOG.warn("Fail to get all notes", e);
		}
		user.setNotes(notes);
		notes = sortByTimeCreation(notes);
		LOG.info("Returning notes");
		return notesToJSON(notes);
	}

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
		return notesToJSON(notes);
	}

	private List<Note> sortByLastChange(List<Note> notes) {
		LOG.trace("Sorting notes by last change");
		return notes.stream().sorted(Comparator.comparing(note -> note.getTimeChange().getTime()))
				.collect(Collectors.toList());
	}

	private List<Note> sortByTimeCreation(List<Note> notes) {
		LOG.trace("Sorting notes by time creation");
		return notes.stream().sorted(Comparator.comparing(note -> note.getTimeCreation().getTime()))
				.collect(Collectors.toList());
	}

	public String notesToJSON(List<Note> notes) {
		LOG.trace("Converting notes to JSON Object");
		String JSON = null;

		ObjectMapper mapper = new ObjectMapper();
		try {
			JSON = mapper.writeValueAsString(notes);
		} catch (JsonProcessingException e) {
			LOG.error("Fail to convet notes to JSON", e);
		}
		LOG.info("Returning converted to JSON notes");
		return JSON;
	}
}