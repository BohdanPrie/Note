package ua.com.bohdanprie.notes.ui.servlet;

import ua.com.bohdanprie.notes.domain.ManagerFactory;
import ua.com.bohdanprie.notes.domain.entities.User;
import ua.com.bohdanprie.notes.domain.exceptions.AuthorisationException;
import ua.com.bohdanprie.notes.domain.managers.UserManager;
import ua.com.bohdanprie.notes.ui.WebUtils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = -5979986946771364505L;
	private static final Logger LOG = LogManager.getLogger(Login.class.getName());
	private UserManager userManager = null;

	public Login() {
		super();
		LOG.info("Servlet Login initialized");
		userManager = ManagerFactory.getInstance().getUserManager();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.trace("Loading page Login");
		WebUtils.loadResource("Authorisation.html", response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.trace("Post request");
		
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		
		try {
			LOG.trace("Authorisation user with login = " + login);
			User user = userManager.authorisation(login, password);
			request.getSession().setAttribute("user", user);
			request.getSession().setAttribute("User-Agent", request.getHeader("User-Agent"));
			response.setStatus(HttpServletResponse.SC_CREATED);
		} catch (AuthorisationException e) {
			LOG.warn("Authorisation user with login = " + login + " failed", e);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write(e.getMessage());
		}
	}
}