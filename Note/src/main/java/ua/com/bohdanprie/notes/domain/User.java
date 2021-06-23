package ua.com.bohdanprie.notes.domain;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String login;
    private String password;
    private List<Note> notes = new ArrayList<>();

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
    	this.notes = notes;
    }
    
    public void changeLogin(String login) {
        this.login = login;
    }

    public void changePassword(String password) {
        this.password = password;
    }
}
