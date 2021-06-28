package ua.com.bohdanprie.notes.domain.entities;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Note extends AbstractText{
    private String title;
    
    @JsonIgnore
    private Date timeCreation;
    
    @JsonIgnore
    private Date timeChange;
    
    public Note(String title, String body, int id) {
    	this();
    	this.title = title;
    	changeBody(body);
    	setId(id);
    }
    
    public Note() {
		timeCreation = new Date();
		timeChange = new Date();
    }
    
    public Date getTimeChange() {
		return timeChange;
	}

	public void setTimeChange(Date timeChange) {
		this.timeChange = timeChange;
	}

	public Date getTimeCreation() {
		return timeCreation;
	}

	public void setTimeCreation(Date timeCreation) {
		this.timeCreation = timeCreation;
	}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return getId() == note.getId() && Objects.equals(title, note.title) && Objects.equals(getBody(), note.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, getBody(), getId());
    }
}
