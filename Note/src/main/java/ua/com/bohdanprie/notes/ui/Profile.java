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

@WebServlet("/profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 4722434442834385696L;
	private UserManager userManager = null;

    public Profile() {
        super();
		userManager = ManagerFactory.getInstance().getUserManager();
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if("getPassword".equals(request.getParameter("action"))) {
			response.getWriter().write(((User) request.getSession().getAttribute("user")).getPassword());
			return;
		}
		WebUtils.loadResource("Profile.html", response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		User user = ((User) request.getSession().getAttribute("user"));
		
		if("exit".equals(action)) {
			request.getSession(false).invalidate();
		} else if("changeLogin".equals(action)) {
			String newLogin = request.getParameter("login");
			if(newLogin != null && ! newLogin.trim().equals("")) {
				try {
				userManager.changeLogin(newLogin, user);
				user.changeLogin(newLogin);
				} catch(Exception e) {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				}
			}
		} else if("changePassword".equals(action)) {
			String password = request.getParameter("password");
			if(password != null && ! password.trim().equals("")) {
				userManager.changePassword(user, password);
				user.changePassword(password);
			}
		} else if("deleteUser".equals(action)) {
			userManager.deleteUser(user);
			request.getSession(false).invalidate();
		}
	}
}
