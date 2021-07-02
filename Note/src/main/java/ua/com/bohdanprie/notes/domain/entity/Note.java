package ua.com.bohdanprie.notes.domain.entity;

import java.util.Objects;

public class Note extends AbstractTextContainer {
	
	private String body;
    
    public Note(String title, String body, int id) {
    	this();
    	this.body = body;
    	changeTitle(title);
    	setId(id);
    }
    
    public Note() {
		super();
    }

	public String getBody() {
		return body;
	}

	public void changeBody(String body) {
		this.body = body;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(body, super.hashCode());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if (obj == null || (getClass() != obj.getClass())) return false;
		Note note = (Note) obj;
		return body.equals(note.body) && super.equals(note);
	}
}