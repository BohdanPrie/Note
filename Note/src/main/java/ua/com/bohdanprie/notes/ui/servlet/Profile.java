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
import ua.com.bohdanprie.notes.domain.exception.AuthorisationException;
import ua.com.bohdanprie.notes.domain.exception.NoSuchUserException;
import ua.com.bohdanprie.notes.domain.service.UserService;
import ua.com.bohdanprie.notes.ui.WebUtils;

@WebServlet("/profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 4722434442834385696L;
	private static final Logger LOG = LogManager.getLogger(Profile.class.getName());
	UserService userService;

	public Profile() {
		userService = ServiceManager.getInstance().getUserService();
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
		LOG.trace("Post request to Profile");
		String action = request.getParameter("action");
		User user = (User) request.getSession().getAttribute("user");

		if ("exit".equals(action)) {
			LOG.info("Invalidating session for user " + user.getLogin());
			request.getSession().invalidate();
			response.sendRedirect("/main");
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.trace("Delete request to Profile");
		User user = (User) request.getSession().getAttribute("user");
		LOG.trace("Deleting user " + user.getLogin());
		userService.deleteUser(user);
		request.getSession().invalidate();
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.trace("Put request to Profile");
		String action = request.getParameter("action");
		String newLogin = request.getParameter("login");
		String password = request.getParameter("password");
		User user = (User) request.getSession().getAttribute("user");
		
		try {
			if ("changeLogin".equals(action)) {
				LOG.trace("Changing login for user " + user.getLogin());
				userService.changeLogin(newLogin, user);
			} else if ("changePassword".equals(action)) {
				LOG.trace("Changing password for user " + user.getLogin());
				userService.changePassword(user, password);
			}
		} catch (NoSuchUserException e) {
			LOG.warn("Current user was not found");
			LOG.info("Invalidating session for user " + user.getLogin());
			request.getSession().invalidate();
			response.sendRedirect("/main");
		} catch (AuthorisationException e) {
			LOG.warn("User " + newLogin + " already exist", e);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
	}
}