package ua.com.bohdanprie.notes.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/main")
public class StartPage extends HttpServlet {
	private static final long serialVersionUID = 8037199713142569021L;
	private static final Logger LOG = LogManager.getLogger(StartPage.class.getName());

	public StartPage() {
		super();
		LOG.info("Servlet StartPage initialized");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.trace("User enter the main page");
		if (request.getSession(false) == null) {
			WebUtils.loadResource("StartPage.html", response);
		} else {
			WebUtils.loadResource("AfterAuthStartPage.html", response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}