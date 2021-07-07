package ua.com.bohdanprie.notes.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.domain.service.TextService;
import ua.com.bohdanprie.notes.domain.service.UserService;
import ua.com.bohdanprie.notes.domain.service.serviceImpl.NoteService;
import ua.com.bohdanprie.notes.domain.service.serviceImpl.ToDoLineService;
import ua.com.bohdanprie.notes.domain.service.serviceImpl.UserServiceImpl;
/**
 * Class created to work with all services that are in the program
 * @author bohda
 *
 */
public final class ServiceManager {
	private static final Logger LOG = LogManager.getLogger(ServiceManager.class.getName());
    private static ServiceManager managerFactory;
    private UserService userService;
    private NoteService noteService;
    private ToDoLineService toDoLineService;

	private ServiceManager() {
		
	}
    /**
     * 
     * @return one object of ServiceManager
     */
    public static ServiceManager getInstance() {
        if(managerFactory == null) {
            managerFactory = new ServiceManager();
    		LOG.debug("ServiceManager initialized");
        }
        return managerFactory;
    }
    
    /**
     * 
     * @return one object of UserService
     */
    public UserService getUserService(){
    	if(userService == null) {
    		userService = new UserServiceImpl();
    		LOG.debug("UserService initialized");
    	}
        return userService;
    }

    /**
     * 
     * @return one object of NoteService
     */
    public NoteService getNoteService(){
    	if(noteService == null) {
    		noteService = new NoteService();
    		LOG.debug("NoteService initialized");
    	}
        return noteService;
    }
    
    /**
     * 
     * @return one object of ToDoLineService
     */
    public ToDoLineService getToDoLineService() {
    	if(toDoLineService == null) {
    		toDoLineService = new ToDoLineService();
    		LOG.debug("ToDoLineService initialized");
    	}
    	return toDoLineService;
    }
    
    /**
     * Method returns needed implementation of TextService, based on given String parameter,
     * parameter represent current user's page
     * @param need
     * @return Needed implementation of TextService
     */
	public TextService getTextService(String need) {
		LOG.trace("Getting TextService depending on need");
		TextService service = null;
    	if("/notes".equals(need)) {
    		service = getNoteService();
    	} else if("/toDos".equals(need)) {
    		service = getToDoLineService();
    	}
    	LOG.trace("Returning TextService");
    	return service;
    }
}
