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
import ua.com.bohdanprie.notes.domain.exceptions.AuthorisationException;
import ua.com.bohdanprie.notes.domain.exceptions.NoSuchUserException;
import ua.com.bohdanprie.notes.domain.managers.UserManager;
import ua.com.bohdanprie.notes.ui.WebUtils;

@WebServlet("/profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 4722434442834385696L;
	private static final Logger LOG = LogManager.getLogger(Profile.class.getName());

	public Profile() {
		super();
		LOG.debug("Servlet Profile initialized");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.trace("Get request to Profile");
		if ("getPassword".equals(request.getParameter("action"))) {
			User user = (User) request.getSession().getAttribute("user");
			response.getWriter().write(user.getPassword());
			return;
		}
		LOG.trace("Loading page Profile");
		WebUtils.loadResource("Profile.html", response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserManager userManager = WebUtils.getUserManager();
		
		LOG.trace("Post request to Profile");
		String action = request.getParameter("action");
		User user = (User) request.getSession().getAttribute("user");

		if ("exit".equals(action)) {
			LOG.info("Invalidating session for user " + user.getLogin());
			request.getSession().invalidate();
			response.sendRedirect("/main");
		}

		try {
			if ("changeLogin".equals(action)) {
				String newLogin = request.getParameter("login");
				try {
					LOG.trace("Changing login for user " + user.getLogin());
					userManager.changeLogin(newLogin, user);
				} catch (AuthorisationException e) {
					LOG.warn("User " + newLogin + " already exist", e);
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				}
			} else if ("changePassword".equals(action)) {
				LOG.trace("Changing password for user " + user.getLogin());
				String password = request.getParameter("password");
				userManager.changePassword(user, password);
			} else if ("deleteUser".equals(action)) {
				LOG.trace("Deleting user " + user.getLogin());
				userManager.deleteUser(user);
				request.getSession().invalidate();
			}
		} catch (NoSuchUserException e) {
			LOG.warn("Current user was not found");
			LOG.info("Invalidating session for user " + user.getLogin());
			request.getSession().invalidate();
			response.sendRedirect("/main");
		}
	}
}