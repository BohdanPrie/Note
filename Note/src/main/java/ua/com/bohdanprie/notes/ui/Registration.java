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
import ua.com.bohdanprie.notes.domain.UserManager;

@WebServlet("/reg")
public class Registration extends HttpServlet {
	private static final long serialVersionUID = -5450629669073582225L;
	private static final Logger LOG = LogManager.getLogger(Registration.class.getName());
	private UserManager userManager = null;
	private NoteManager noteManager = null;

	public Registration() {
		super();
		LOG.info("Servlet Registration initialized");
		noteManager = ManagerFactory.getInstance().getNoteManager();
		userManager = ManagerFactory.getInstance().getUserManager();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		WebUtils.loadResource("Registration.html", response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String login = request.getParameter("login");
		String password = request.getParameter("password");

		try {
			User user = userManager.createAccount(login, password);
			noteManager.createNote(0, user);
			noteManager.createNote(1, user);

			request.getSession().setAttribute("user", user);
			request.getSession().setAttribute("User-Agent", request.getHeader("User-Agent"));

			response.setStatus(HttpServletResponse.SC_CREATED);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			e.printStackTrace();
		}
	}
}