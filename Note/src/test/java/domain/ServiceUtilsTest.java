package domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ua.com.bohdanprie.notes.domain.ServiceUtils;
import ua.com.bohdanprie.notes.domain.entity.Note;
import ua.com.bohdanprie.notes.domain.entity.ToDo;
import ua.com.bohdanprie.notes.domain.entity.ToDoLine;

public class ServiceUtilsTest {
	private static ArrayList<Note> elements;
	private static ArrayList<ToDoLine> elementsToDoLines;

	public static void addNotes() {
		elements = new ArrayList<>();

		Note note1 = new Note("Title 3", "Body 3", 3);
		Note note2 = new Note("Title 2", "Body 2", 2);
		Note note3 = new Note("Title 1", "Body 1", 1);

		note1.setTimeCreation(new Date(12000));
		note2.setTimeCreation(new Date(10000));
		note3.setTimeCreation(new Date(89000));

		note1.setTimeChange(new Date(85));
		note2.setTimeChange(new Date(20));
		note3.setTimeChange(new Date(9));

		elements.add(note1);
		elements.add(note2);
		elements.add(note3);
	}

	public static void addToDoLines() {
		elementsToDoLines = new ArrayList<>();

		ToDoLine toDoLine1 = new ToDoLine("Title 1");
		ToDoLine toDoLine2 = new ToDoLine("Title 2");
		ToDoLine toDoLine3 = new ToDoLine("Title 3");

		toDoLine1.setTimeCreation(new Date(14));
		toDoLine2.setTimeCreation(new Date(948));
		toDoLine3.setTimeCreation(new Date(225));

		toDoLine1.setTimeChange(new Date(12222));
		toDoLine2.setTimeChange(new Date(7833));
		toDoLine3.setTimeChange(new Date(96));

		toDoLine1.setId(4);
		toDoLine2.setId(2);
		toDoLine3.setId(9);

		elementsToDoLines.add(toDoLine1);
		elementsToDoLines.add(toDoLine2);
		elementsToDoLines.add(toDoLine3);
	}

	@Test
	public void sortingNotesByTimeCreation() {
		addNotes();
		ArrayList<Note> testelements = ServiceUtils.sortByTimeCreation(elements);

		assertEquals("Title 2", testelements.get(0).getTitle());
		assertEquals("Title 1", testelements.get(2).getTitle());
		assertEquals("Title 3", testelements.get(1).getTitle());
	}

	@Test
	public void sortingNotesByLastChange() {
		addNotes();
		ArrayList<Note> testelements = ServiceUtils.sortByLastChange(elements);

		assertEquals("Title 1", testelements.get(0).getTitle());
		assertEquals("Title 2", testelements.get(1).getTitle());
		assertEquals("Title 3", testelements.get(2).getTitle());
	}

	@Test
	public void notesToJson() {
		addNotes();
		String JSON = ServiceUtils.toJSON(elements);

		assertFalse(JSON.contains("timeChange"));
		assertFalse(JSON.contains("timeCreation"));
		assertTrue(JSON.contains("title"));
		assertTrue(JSON.contains("body"));
		assertTrue(JSON.contains("id"));

		int positionNote1 = JSON.indexOf("Title 3");
		int positionNote2 = JSON.indexOf("Title 2");
		int positionNote3 = JSON.indexOf("Title 1");

		assertTrue(positionNote1 < positionNote2);
		assertTrue(positionNote2 < positionNote3);
		assertTrue(positionNote1 < positionNote3);
	}

	@Test
	public void combineResults() {
		addToDoLines();
		Map<Integer, List<ToDo>> arrays = new HashMap<Integer, List<ToDo>>() 
		{{
				put(2, Arrays.asList(new ToDo(1, "Body 1", true), new ToDo(7, "Body 7", false)));
				put(9, Arrays.asList(new ToDo(9, "Body 9", false), new ToDo(10, "Body 10", true),new ToDo(666, "Body 666", true), new ToDo(42, "Body 42", true)));
				put(4, Arrays.asList(new ToDo(5, "Body 5", true)));
				put(5, Arrays.asList(new ToDo(2, "Body 2", false), new ToDo(14, "Body 14", true),new ToDo(33, "Body 33", false)));
		}};
		ServiceUtils.combineResult(elementsToDoLines, arrays);

		assertTrue(elementsToDoLines.get(0).getToDo().size() == 1);
		assertTrue(elementsToDoLines.get(1).getToDo().size() == 2);
		assertTrue(elementsToDoLines.get(2).getToDo().size() == 4);
	}

	@Test
	public void sortingToDoLinesByTimeCreation() {
		addToDoLines();

		ArrayList<ToDoLine> lines = ServiceUtils.sortByTimeCreation(elementsToDoLines);

		assertEquals("Title 1", lines.get(0).getTitle());
		assertEquals("Title 3", lines.get(1).getTitle());
		assertEquals("Title 2", lines.get(2).getTitle());
	}

	@Test
	public void sortingToDoLinesByLastChange() {
		addToDoLines();

		ArrayList<ToDoLine> lines = ServiceUtils.sortByLastChange(elementsToDoLines);

		assertEquals("Title 3", lines.get(0).getTitle());
		assertEquals("Title 2", lines.get(1).getTitle());
		assertEquals("Title 1", lines.get(2).getTitle());
	}

	@Test
	public void toDoLinesToJSON() {
		addToDoLines();

		String JSON = ServiceUtils.toJSON(elementsToDoLines);
		
		assertFalse(JSON.contains("timeChange"));
		assertFalse(JSON.contains("timeCreation"));
		assertTrue(JSON.contains("title"));
		assertTrue(JSON.contains("id"));
		assertTrue(JSON.contains("toDo"));

		int positionNote1 = JSON.indexOf("Title 1");
		int positionNote2 = JSON.indexOf("Title 2");
		int positionNote3 = JSON.indexOf("Title 3");

		assertTrue(positionNote1 < positionNote2);
		assertTrue(positionNote2 < positionNote3);
		assertTrue(positionNote1 < positionNote3);
	}

	@Test
	public void sortById() {
		List<ToDo> toDo = Arrays.asList(
		new ToDo(7, "Body 1", true), 
		new ToDo(1, "Body 7", false),
		new ToDo(10, "Body 9", false), 
		new ToDo(9, "Body 10", true),
		new ToDo(666, "Body 666", true), 
		new ToDo(42, "Body 42", true));

		toDo = ServiceUtils.sortById(toDo);
		
		assertTrue(toDo.get(0).getId() == 1);
		assertTrue(toDo.get(1).getId() == 7);
		assertTrue(toDo.get(2).getId() == 9);
		assertTrue(toDo.get(3).getId() == 10);
		assertTrue(toDo.get(4).getId() == 42);
		assertTrue(toDo.get(5).getId() == 666);
	}
}