package ua.com.bohdanprie.notes.domain.entities;

public class ToDo extends AbstractText{
	private boolean marked;

	public boolean isMarked() {
		return marked;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}
}