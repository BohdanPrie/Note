package ua.com.bohdanprie.notes.ui;

import ua.com.bohdanprie.notes.domain.ManagerFactory;
import ua.com.bohdanprie.notes.domain.User;
import ua.com.bohdanprie.notes.domain.UserManager;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = -5979986946771364505L;
	private UserManager userManager = null;

	public Login() {
		super();
		userManager = ManagerFactory.getInstance().getUserManager();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		WebUtils.loadResource("Authorisation.html", response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String login = request.getParameter("login");
		String password = request.getParameter("password");

		try {
			User user = userManager.authorisation(login, password);
			
			request.getSession().setAttribute("user", user);
			request.getSession().setAttribute("User-Agent", request.getHeader("User-Agent"));
			response.setStatus(HttpServletResponse.SC_CREATED);
		} catch (NullPointerException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write(e.getMessage());
			e.printStackTrace();
		}
	}
}