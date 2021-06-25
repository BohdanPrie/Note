package ua.com.bohdanprie.notes.domain;

import ua.com.bohdanprie.notes.domain.managers.NoteManager;
import ua.com.bohdanprie.notes.domain.managers.UserManager;

public class ManagerFactory {
    private static ManagerFactory managerFactory;
    private UserManager userManager;
    private NoteManager noteManager;

    private ManagerFactory() {
    	
    }
    
    public static ManagerFactory getInstance() {
        if(managerFactory == null) {
            managerFactory = new ManagerFactory();
        }
        return managerFactory;
    }

    public UserManager getUserManager(){
    	if(userManager == null) {
    		userManager = new UserManager();
    	}
        return userManager;
    }

    public NoteManager getNoteManager(){
    	if(noteManager == null) {
        	noteManager = new NoteManager();
    	}
        return noteManager;
    }
}
