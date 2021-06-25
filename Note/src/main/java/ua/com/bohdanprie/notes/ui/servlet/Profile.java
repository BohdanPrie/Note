package ua.com.bohdanprie.notes.ui.servlet;

import ua.com.bohdanprie.notes.domain.ManagerFactory;
import ua.com.bohdanprie.notes.domain.NoSuchUserException;
import ua.com.bohdanprie.notes.domain.entities.User;
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

@WebServlet("/profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 4722434442834385696L;
	private static final Logger LOG = LogManager.getLogger(Profile.class.getName());
	private UserManager userManager = null;

	public Profile() {
		super();
		LOG.info("Servlet Profile initialized");
		userManager = ManagerFactory.getInstance().getUserManager();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if ("getPassword".equals(request.getParameter("action"))) {
			User user = (User) request.getSession().getAttribute("user");
			response.getWriter().write(user.getPassword());
			return;
		}
		WebUtils.loadResource("Profile.html", response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		User user = (User) request.getSession().getAttribute("user");

		if ("exit".equals(action)) {
			request.getSession().invalidate();
		}

		try {
			if ("changeLogin".equals(action)) {
				String newLogin = request.getParameter("login");
				userManager.changeLogin(newLogin, user); // if login exist set status to SC_FORBIDDEN (exception throws here)
			} else if ("changePassword".equals(action)) {
				String password = request.getParameter("password");
				userManager.changePassword(user, password);
			} else if ("deleteUser".equals(action)) {
				userManager.deleteUser(user);
				request.getSession().invalidate();
			}
		} catch (NoSuchUserException e) {
			request.getSession().invalidate();
		}
	}
}