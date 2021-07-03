package ua.com.bohdanprie.notes.ui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.domain.ServiceManager;
import ua.com.bohdanprie.notes.domain.entity.User;
import ua.com.bohdanprie.notes.domain.service.TextService;
import ua.com.bohdanprie.notes.ui.WebUtils;

@WebServlet({ "/notes", "/toDos" })
public class UserElements extends HttpServlet {
	private static final long serialVersionUID = -1614888115806476030L;
	private static final Logger LOG = LogManager.getLogger(UserElements.class.getName());

	public UserElements() {
		LOG.debug("Servlet Notes initialized");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		TextService manager = ServiceManager.getInstance().getTextService(request.getParameter("need"));

		if ("getAll".equals(request.getParameter("action"))) {
			User user = ((User) request.getSession().getAttribute("user"));
			LOG.trace("Getting all elements for user " + user.getLogin());
			String JSON = manager.getAll(user);
			LOG.trace("Loading elements for user " + user.getLogin());
			WebUtils.loadJSON(response, JSON);
		} else {
			if (WebUtils.getRequestedPath(request).equals("/notes")) {
				LOG.trace("Loading page Notes");
				WebUtils.loadResource("Notes.html", response);
			} else if (WebUtils.getRequestedPath(request).equals("/toDos")) {
				LOG.trace("Loading page ToDo");
				WebUtils.loadResource("ToDo.html", response);
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.trace("Post request to UserElements");
		TextService manager = ServiceManager.getInstance().getTextService(request.getParameter("need"));
		String action = request.getParameter("action");
		String search = request.getParameter("q");
		User user = ((User) request.getSession().getAttribute("user"));

		if (search != null) {
			LOG.trace("Searching elements by pattern for user " + user.getLogin());
			String JSONNotes = manager.searchByPattern(user, search);
			WebUtils.loadJSON(response, JSONNotes);
		}

		if ("create".equals(action)) {
			LOG.trace("Creating new element for user " + user.getLogin());
			int maxId = Integer.parseInt(request.getParameter("maxId"));
			manager.create(maxId, user);
		} else if ("save".equals(action)) {
			LOG.trace("Saving element for user " + user.getLogin());
			manager.change(WebUtils.readData(request), user);
		} else if ("sByDateCreation".equals(action)) {
			LOG.trace("Getting sorted by date creation elements for user " + user.getLogin());
			String JSON = manager.getSortedByCreation(user);
			WebUtils.loadJSON(response, JSON);
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.trace("Delete request to UserElements");
		TextService manager = ServiceManager.getInstance().getTextService(request.getParameter("need"));

		String action = request.getParameter("action");
		User user = ((User) request.getSession().getAttribute("user"));

		if ("deleteAllNotes".equals(action)) {
			LOG.trace("Deleting all elements for user " + user.getLogin());
			manager.deleteAll(user);
		} else if(action != null){
			LOG.trace("Deleting element for user " + user.getLogin());
			int id = Integer.parseInt(request.getParameter("id"));
			manager.delete(id, user);			
		}
	}
}