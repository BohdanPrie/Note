package ua.com.bohdanprie.notes.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.domain.ManagerFactory;
import ua.com.bohdanprie.notes.domain.NoteManager;
import ua.com.bohdanprie.notes.domain.User;

@WebServlet("/notes")
public class Notes extends HttpServlet {
	private static final long serialVersionUID = -1614888115806476030L;
	private static final Logger LOG = LogManager.getLogger(Notes.class.getName());
	private NoteManager noteManager = null;

	public Notes() {
		super();
		LOG.info("Servlet Notes initialized");
		noteManager = ManagerFactory.getInstance().getNoteManager();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if ("getAll".equals(request.getParameter("action"))) {
			User user = ((User) request.getSession().getAttribute("user"));
			String JSONNotes = noteManager.getAll(user);
			WebUtils.loadJSON(response, JSONNotes);
		} else {
			WebUtils.loadResource("Notes.html", response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		String search = request.getParameter("q");
		
		User user = ((User) request.getSession().getAttribute("user"));

		if (search != null) {
			String JSONNotes = noteManager.searchByPattern(user, search);
			WebUtils.loadJSON(response, JSONNotes);
		}

		if ("create".equals(action)) {
			int maxId = Integer.parseInt(request.getParameter("maxId"));
			noteManager.createNote(maxId, user);
		} else if ("delete".equals(action)) {
			int id = Integer.parseInt(request.getParameter("id"));
			noteManager.deleteNote(id, user);
		} else if ("save".equals(action)) {
			noteManager.changeNote(WebUtils.readData(request), user);
		} else if ("sByDateCreation".equals(action)) {
			String JSONNotes = noteManager.sortByTimeCreation(user);
			WebUtils.loadJSON(response, JSONNotes);
		} else if ("deleteAllNotes".equals(action)) {
			noteManager.deleteAll(user);
		}
	}
}