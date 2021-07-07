package ua.com.bohdanprie.notes.domain.entity;

import java.util.Objects;
/**
 * Element represents single user's task
 * <br>Fields: id (int), body {@link String}, marked (boolean)  
 * @author bohda
 *
 */
public class ToDo {
	private int id;
	private String body;
	private boolean marked;

	public ToDo(int id, String body, boolean marked) {
		this.id = id;
		this.body = body;
		this.marked = marked;
	}
	
	public ToDo() {
		
	}

	public boolean isMarked() {
		return marked;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void changeBody(String body) {
		this.body = body;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(body, marked, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || (getClass() != obj.getClass())) return false;
		ToDo other = (ToDo) obj;
		return Objects.equals(body, other.body) && marked == other.marked && id == other.id;
	}
}