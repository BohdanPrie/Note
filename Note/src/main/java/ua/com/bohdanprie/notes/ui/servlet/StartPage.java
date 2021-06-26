package ua.com.bohdanprie.notes.ui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.ui.WebUtils;

@WebServlet("/main")
public class StartPage extends HttpServlet {
	private static final long serialVersionUID = 8037199713142569021L;
	private static final Logger LOG = LogManager.getLogger(StartPage.class.getName());

	public StartPage() {
		super();
		LOG.debug("Servlet StartPage initialized");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.trace("Get request to StartPage");
		if (request.getSession(false) == null) {
			LOG.trace("Loading page StartPage");
			WebUtils.loadResource("StartPage.html", response);
		} else {
			LOG.trace("Loading page AfterAuthStartPage");
			WebUtils.loadResource("AfterAuthStartPage.html", response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOG.trace("Post request to StartPage");
		doGet(request, response);
	}
}