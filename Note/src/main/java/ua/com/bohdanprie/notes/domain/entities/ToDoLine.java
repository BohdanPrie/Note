package ua.com.bohdanprie.notes.domain.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ToDoLine extends AbstractText{
	private static final Logger LOG = LogManager.getLogger(ToDoLine.class.getName());
	
	private List<ToDo> toDo;

	public ToDoLine(String title) {
		this();
		changeTitle(title);
	}
	
	public ToDoLine() {
		super();
		toDo = new ArrayList<>();		
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

	@Override
	public int hashCode() {
		return Objects.hash(toDo, super.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if (obj == null || (getClass() != obj.getClass())) return false;
		ToDoLine toDoLine = (ToDoLine) obj;
		return super.equals(toDoLine) && toDo.equals(toDoLine.toDo);
	}
}