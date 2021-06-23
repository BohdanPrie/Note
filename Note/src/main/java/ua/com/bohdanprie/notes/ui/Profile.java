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
			User user = (User) request.getSession().getAttribute("user");
			response.getWriter().write(user.getPassword());
			return;
		}
		WebUtils.loadResource("Profile.html", response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		String action = request.getParameter("action");
		User user = (User) request.getSession().getAttribute("user");
		
		if("exit".equals(action)) {
			request.getSession().invalidate();
		} else if("changeLogin".equals(action)) {
			String newLogin = request.getParameter("login");
			userManager.changeLogin(newLogin, user); // if login exist set status to SC_FORBIDDEN (exception throws here)
		} else if("changePassword".equals(action)) {
			String password = request.getParameter("password");
			userManager.changePassword(user, password);
		} else if("deleteUser".equals(action)) {
			userManager.deleteUser(user);
			request.getSession().invalidate();
		}
	}
}
