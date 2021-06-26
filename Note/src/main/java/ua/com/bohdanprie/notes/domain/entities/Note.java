package ua.com.bohdanprie.notes.domain.entities;

import java.util.Date;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Note {
    private String title;
    private String body;
    private int id;
    
    @JsonIgnore
    private Date timeCreation;
    
    @JsonIgnore
    private Date timeChange;
    
    public Note(String title, String body, int id) {
    	this();
    	this.title = title;
    	this.body = body;
    	this.id = id;
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
    
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.id && Objects.equals(title, note.title) && Objects.equals(body, note.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, body, id);
    }
}
