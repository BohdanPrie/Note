package ua.com.bohdanprie.notes.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.domain.managers.NoteManager;
import ua.com.bohdanprie.notes.domain.managers.UserManager;

public class ManagerFactory {
	private static final Logger LOG = LogManager.getLogger(ManagerFactory.class.getName());
    private static ManagerFactory managerFactory;
    private UserManager userManager;
    private NoteManager noteManager;

    private ManagerFactory() {
    	
    }
    
    public static ManagerFactory getInstance() {
        if(managerFactory == null) {
            managerFactory = new ManagerFactory();
    		LOG.debug("ManagerFactory was initialized");
        }
        return managerFactory;
    }

    public UserManager getUserManager(){
    	if(userManager == null) {
    		userManager = new UserManager();
    		LOG.debug("UserManager was initialized");
    	}
        return userManager;
    }

    public NoteManager getNoteManager(){
    	if(noteManager == null) {
        	noteManager = new NoteManager();
        	LOG.debug("NoteManager was initialized");
    	}
        return noteManager;
    }
}
