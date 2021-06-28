package ua.com.bohdanprie.notes.domain.entities;

public abstract class AbstractText {
	private String body;
	private int id;

	public String getBody() {
		return body;
	}

	public void changeBody(String body) {
		if (body != null) {
			this.body = body;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}