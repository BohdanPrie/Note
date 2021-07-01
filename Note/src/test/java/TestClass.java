import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import ua.com.bohdanprie.notes.domain.ManagerUtils;
import ua.com.bohdanprie.notes.domain.entities.Note;

public class TestClass {
	private static ArrayList<Note> notes;
	
	@Before
	public void addValues() {
		notes = new ArrayList<>();
		
		Note note1 = new Note("Title 3", "Body 3", 3);
		Note note2 = new Note("Title 2", "Body 2", 2);
		Note note3 = new Note("Title 1", "Body 1", 1);
		
		note1.setTimeCreation(new Date(12000));
		note2.setTimeCreation(new Date(10000));
		note3.setTimeCreation(new Date(89000));
		
		note1.setTimeChange(new Date(85));
		note2.setTimeChange(new Date(20));
		note3.setTimeChange(new Date(9));
		
		notes.add(note1);
		notes.add(note2);
		notes.add(note3);
		
		System.out.println(notes.get(0).getTimeCreation());
		System.out.println(notes.get(1).getTimeCreation());
		System.out.println(notes.get(2).getTimeCreation());
	}
	
	@Test
	public void sortingByTimeCreation() {
		
		ArrayList<Note> testNotes = ManagerUtils.sortByTimeCreation(notes);
		
		assertEquals(ArrayList.class, testNotes.getClass());
		
		System.out.println(testNotes.get(0).getTitle());
		System.out.println(testNotes.get(1).getTitle());
		System.out.println(testNotes.get(2).getTitle());
		
		assertEquals("Title 2", testNotes.get(0).getTitle());
		assertEquals("Title 1", testNotes.get(2).getTitle());
		assertEquals("Title 3", testNotes.get(1).getTitle());
	}
	
	@Test
	public void sortingByLastChange() {
		ArrayList<Note> testNotes = ManagerUtils.sortByLastChange(notes);
		
		assertEquals(ArrayList.class, testNotes.getClass());
		
		System.out.println(testNotes.get(0).getTitle());
		System.out.println(testNotes.get(1).getTitle());
		System.out.println(testNotes.get(2).getTitle());
		
		assertEquals("Title 1", testNotes.get(0).getTitle());
		assertEquals("Title 2", testNotes.get(1).getTitle());
		assertEquals("Title 3", testNotes.get(2).getTitle());
	}
	
	@Test
	public void toJson() {
		String JSON = ManagerUtils.toJSON(notes);
		
		assertEquals(false, JSON.contains("timeChange"));
		assertEquals(false, JSON.contains("timeCreation"));
		
		int positionNote1 = JSON.indexOf("Title 3");
		int positionNote2 = JSON.indexOf("Title 2");
		int positionNote3 = JSON.indexOf("Title 1");
		
		assertEquals(true, positionNote1 < positionNote2);
		assertEquals(true, positionNote2 < positionNote3);
		assertEquals(true, positionNote1 < positionNote3);
	}
}