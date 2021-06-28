package ua.com.bohdanprie.notes.domain.entities;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ToDoLine {
	private static final Logger LOG = LogManager.getLogger(ToDoLine.class.getName());
	@JsonIgnore
	private Date timeCreation;

	@JsonIgnore
	private Date timeChange;

	private int id;
	
	private List<ToDo> toDo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public List<ToDo> getToDo() {
		return toDo;
	}

	public void setToDo(List<ToDo> toDo) {
		if (toDo == null) {
			LOG.trace("ToDo list is null");
		} else {
			this.toDo = toDo;
		}
	}
}