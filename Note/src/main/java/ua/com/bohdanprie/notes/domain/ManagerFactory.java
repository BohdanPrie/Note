package ua.com.bohdanprie.notes.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.domain.managers.UserManager;
import ua.com.bohdanprie.notes.domain.managers.managersImpl.NoteManager;
import ua.com.bohdanprie.notes.domain.managers.managersImpl.ToDoLineManager;
import ua.com.bohdanprie.notes.domain.managers.managersImpl.UserManagerImpl;

public final class ManagerFactory {
	private static final Logger LOG = LogManager.getLogger(ManagerFactory.class.getName());
    private static ManagerFactory managerFactory;
    private UserManager userManager;
    private NoteManager noteManager;
    private ToDoLineManager toDoLineManager;

	private ManagerFactory() {
		
    }
    
    public static ManagerFactory getInstance() {
        if(managerFactory == null) {
            managerFactory = new ManagerFactory();
    		LOG.debug("ManagerFactory initialized");
        }
        return managerFactory;
    }

    public UserManager getUserManager(){
    	if(userManager == null) {
    		userManager = new UserManagerImpl();
    		LOG.debug("UserManager initialized");
    	}
        return userManager;
    }

    public NoteManager getNoteManager(){
    	if(noteManager == null) {
    		noteManager = new NoteManager();
    		LOG.debug("NoteManager initialized");
    	}
        return noteManager;
    }
    
    public ToDoLineManager getToDoLineManager() {
    	if(toDoLineManager == null) {
    		toDoLineManager = new ToDoLineManager();
    		LOG.debug("ToDoLineManager initialized");
    	}
    	return toDoLineManager;
    }
}
