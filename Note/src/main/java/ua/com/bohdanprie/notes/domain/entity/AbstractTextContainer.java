package ua.com.bohdanprie.notes.domain.entity;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Parent class for all user's elements as Notes, ToDoLines etc.
 * <br>Have basic fields for all elements: id, title, timeCreation, timeChange.
 * @author bohda
 *
 */
public abstract class AbstractTextContainer {
	private String title;
	private int id;

	@JsonIgnore
	private Date timeCreation;

	@JsonIgnore
	private Date timeChange;

	public AbstractTextContainer() {
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

	public void changeTitle(String title) {
		if (title != null) {
			this.title = title;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, timeChange, timeCreation);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || (getClass() != obj.getClass())) return false;
		AbstractTextContainer other = (AbstractTextContainer) obj;
		return Objects.equals(title, other.title) && id == other.id && timeChange == other.timeChange && timeCreation == other.timeCreation;
	}

}