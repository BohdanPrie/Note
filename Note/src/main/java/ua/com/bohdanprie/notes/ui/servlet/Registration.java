package ua.com.bohdanprie.notes.ui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.domain.ManagerFactory;
import ua.com.bohdanprie.notes.domain.entities.User;
import ua.com.bohdanprie.notes.domain.exceptions.AuthorisationException;
import ua.com.bohdanprie.notes.domain.managers.UserManager;
import ua.com.bohdanprie.notes.ui.WebUtils;

@WebServlet("/reg")
public class Registration extends HttpServlet {
	private static final long serialVersionUID = -5450629669073582225L;
	private static final Logger LOG = LogManager.getLogger(Registration.class.getName());
	private UserManager userManager = null;

	public Registration() {
		super();
		userManager = ManagerFactory.getInstance().getUserManager();
		LOG.debug("Servlet Registration initialized");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.trace("Get request to Registration");
		LOG.trace("Loading page Registration");
		WebUtils.loadResource("Registration.html", response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.trace("Post request to Registration");
		String login = request.getParameter("login");
		String password = request.getParameter("password");

		try {
			LOG.trace("Creating user with login = " + login);
			User user = userManager.createAccount(login, password);

			response.setStatus(HttpServletResponse.SC_CREATED);
			request.getSession().setAttribute("user", user);
			request.getSession().setAttribute("User-Agent", request.getHeader("User-Agent"));
		} catch (AuthorisationException e) {
			LOG.warn("User with login " + login + " already exist", e);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
}