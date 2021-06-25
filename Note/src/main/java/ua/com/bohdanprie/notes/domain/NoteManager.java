package ua.com.bohdanprie.notes.domain;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.bohdanprie.notes.dao.DaoException;
import ua.com.bohdanprie.notes.dao.DaoFactory;
import ua.com.bohdanprie.notes.dao.NoteDao;

public class NoteManager {
	private NoteDao noteDao;

	private NoteManager() {
		noteDao = DaoFactory.getInstance().getNoteDao();
	}

	public static NoteManager getInstance() {
		return new NoteManager();
	}

	public void changeNote(String JSONData, User user) {
		ObjectMapper mapper = new ObjectMapper();
		Note note = null;
		try {
			note = mapper.readValue(JSONData, Note.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		noteDao.changeNote(note, user);
	}

	public void deleteAll(User user) {
		noteDao.deleteAll(user);
	}

	public void deleteNote(int id, User user) {
		noteDao.deleteNote(id, user);
	}

	public void createNote(int id, User user) {
		Note note = null;
		try {
			note = noteDao.createNote(id, user);
		} catch (DaoException e) {

		}
		user.getNotes().add(note);
	}

	public String getAll(User user) {
		List<Note> notes = noteDao.getAllNotes(user);
		user.setNotes(notes);

		notes = notes.stream().sorted(Comparator.comparing(note -> note.getTimeChange().getTime()))
				.collect(Collectors.toList());
		return notesToJSON(notes);
	}

	public String notesToJSON(List<Note> notes) {
		String JSON = null;

		ObjectMapper mapper = new ObjectMapper();
		try {
			JSON = mapper.writeValueAsString(notes);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return JSON;
	}

	public String sortByTimeCreation(User user) {
		List<Note> notes = noteDao.getAllNotes(user);
		user.setNotes(notes);

		notes = notes.stream().sorted(Comparator.comparing(note -> note.getTimeCreation().getTime()))
				.collect(Collectors.toList());
		return notesToJSON(notes);
	}

	public String searchByPattern(User user, String pattern) {
		List<Note> notes = noteDao.searchByPattern(user, pattern);

		user.setNotes(notes);
		return notesToJSON(notes);
	}
}
