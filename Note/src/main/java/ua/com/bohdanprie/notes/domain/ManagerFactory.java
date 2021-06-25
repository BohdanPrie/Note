package ua.com.bohdanprie.notes.domain;

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
    		userManager = UserManager.getInstance();
    	}
        return userManager;
    }

    public NoteManager getNoteManager(){
    	if(noteManager == null) {
        	noteManager = NoteManager.getInstance();
    	}
        return noteManager;
    }
}
