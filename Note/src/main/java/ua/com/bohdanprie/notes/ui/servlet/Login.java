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

@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = -5979986946771364505L;
	private static final Logger LOG = LogManager.getLogger(Login.class.getName());
	private UserService userService;

	public Login() {
		userService = ServiceManager.getInstance().getUserService();
		LOG.debug("Servlet Login initialized");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.trace("Get request to Login");
		LOG.trace("Loading page Login");
		WebUtils.loadResource("Authorisation.html", response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.trace("Post request to Login");
		
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		
		try {
			LOG.trace("Authorisation user with login = " + login);
			User user = userService.authorisation(login, password);
			response.setStatus(HttpServletResponse.SC_CREATED);
			LOG.info("Creating session for user " + user.getLogin());
			request.getSession().setAttribute("user", user);
			request.getSession().setAttribute("User-Agent", request.getHeader("User-Agent"));
		} catch (AuthorisationException e) {
			LOG.warn("Wrong password for user with login = " + login, e);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (NoSuchUserException e) {
			LOG.warn("No user with login = " + login, e);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}