package ua.com.bohdanprie.notes.ui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.domain.entities.User;
import ua.com.bohdanprie.notes.domain.managers.TextManager;
import ua.com.bohdanprie.notes.ui.WebUtils;

@WebServlet("/notes")
public class Notes extends HttpServlet {
	private static final long serialVersionUID = -1614888115806476030L;
	private static final Logger LOG = LogManager.getLogger(Notes.class.getName());
	
	public Notes() {
		super();
		LOG.debug("Servlet Notes initialized");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		TextManager manager = WebUtils.getTextManager(request);
		
		if ("getAll".equals(request.getParameter("action"))) {
			User user = ((User) request.getSession().getAttribute("user"));
			String JSON = null;
			JSON = manager.getAll(user);
			WebUtils.loadJSON(response, JSON);
		} else {
			WebUtils.loadResource("Notes.html", response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		TextManager manager = WebUtils.getTextManager(request);
		
		String action = request.getParameter("action");
		String search = request.getParameter("q");
		
		User user = ((User) request.getSession().getAttribute("user"));

		if (search != null) {
			String JSONNotes = manager.searchByPattern(user, search);
			WebUtils.loadJSON(response, JSONNotes);
		}

		if ("create".equals(action)) {
			int maxId = Integer.parseInt(request.getParameter("maxId"));
			manager.create(maxId, user);
		} else if ("delete".equals(action)) {
			int id = Integer.parseInt(request.getParameter("id"));
			manager.delete(id, user);
		} else if ("save".equals(action)) {
			manager.change(WebUtils.readData(request), user);
		} else if ("sByDateCreation".equals(action)) {
			String JSON = manager.getSortedByCreation(user);
			WebUtils.loadJSON(response, JSON);
		} else if ("deleteAllNotes".equals(action)) {
			manager.deleteAll(user);
		}
	}
}