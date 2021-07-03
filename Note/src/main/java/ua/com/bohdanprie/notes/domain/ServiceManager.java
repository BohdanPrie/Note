package ua.com.bohdanprie.notes.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.domain.service.TextService;
import ua.com.bohdanprie.notes.domain.service.UserService;
import ua.com.bohdanprie.notes.domain.service.serviceImpl.NoteService;
import ua.com.bohdanprie.notes.domain.service.serviceImpl.ToDoLineService;
import ua.com.bohdanprie.notes.domain.service.serviceImpl.UserServiceImpl;

public final class ServiceManager {
	private static final Logger LOG = LogManager.getLogger(ServiceManager.class.getName());
    private static ServiceManager managerFactory;
    private UserService userService;
    private NoteService noteService;
    private ToDoLineService toDoLineService;

	private ServiceManager() {
		
	}
    
    public static ServiceManager getInstance() {
        if(managerFactory == null) {
            managerFactory = new ServiceManager();
    		LOG.debug("ServiceManager initialized");
        }
        return managerFactory;
    }

    public UserService getUserService(){
    	if(userService == null) {
    		userService = new UserServiceImpl();
    		LOG.debug("UserService initialized");
    	}
        return userService;
    }

    public NoteService getNoteService(){
    	if(noteService == null) {
    		noteService = new NoteService();
    		LOG.debug("NoteService initialized");
    	}
        return noteService;
    }
    
    public ToDoLineService getToDoLineService() {
    	if(toDoLineService == null) {
    		toDoLineService = new ToDoLineService();
    		LOG.debug("ToDoLineService initialized");
    	}
    	return toDoLineService;
    }
    
	public TextService getTextService(String need) {
		LOG.trace("Getting TextService depending on need");
		TextService service = null;
    	if("notes".equals(need)) {
    		service = getNoteService();
    	} else if("toDos".equals(need)) {
    		service = getToDoLineService();
    	}
    	LOG.trace("Returning TextService");
    	return service;
    }
}
