package ua.com.bohdanprie.notes.ui;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.com.bohdanprie.notes.domain.ManagerFactory;
import ua.com.bohdanprie.notes.domain.NoteManager;
import ua.com.bohdanprie.notes.domain.User;

@WebServlet("/notes")
public class Notes extends HttpServlet {
	private static final long serialVersionUID = -1614888115806476030L;
	private NoteManager noteManager;

	public Notes() {
		super();
		noteManager = ManagerFactory.getInstance().getNoteManager();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if ("getAll".equals(request.getParameter("action"))) {
			User user = ((User) request.getSession().getAttribute("user"));
			String JSONNotes = null;
						
			JSONNotes = noteManager.getAll(user);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(JSONNotes);
			
		} else {
			WebUtils.loadResource("Notes.html", response.getWriter());
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		
		String action = request.getParameter("action");
		String q = request.getParameter("q");
		
		User user = ((User) request.getSession().getAttribute("user"));
		
		if(q != null) {
			String JSONNotes = null;
			
			StringBuffer pattern = new StringBuffer();
			String line = null;
			try {
				BufferedReader reader = request.getReader();
				while ((line = reader.readLine()) != null)
					pattern.append(line);
			} catch (Exception e) { /* report an error */
				
			}
			JSONNotes = noteManager.searchByPattern(user, pattern.toString());
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(JSONNotes);
		}
		
		if ("create".equals(action)) {
			int maxId = Integer.parseInt(request.getParameter("maxId"));
			noteManager.createNote(maxId, user);
		} else if ("delete".equals(action)) {
			int id = Integer.parseInt(request.getParameter("id"));
			noteManager.deleteNote(id, user);
		} else if ("save".equals(action)) {
			StringBuffer jb = new StringBuffer();
			String line = null;
			try {
				BufferedReader reader = request.getReader();
				while ((line = reader.readLine()) != null)
					jb.append(line);
			} catch (Exception e) { /* report an error */
				
			}
			noteManager.changeNote(jb.toString(), user);
		} else if ("sByDateCreation".equals(action)) {
			String JSONNotes = null;

			JSONNotes = noteManager.sortByTimeCreation(user);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(JSONNotes);
		} else if("deleteAllNotes".equals(action)) {
			noteManager.deleteAll(user);
		}
	}
}