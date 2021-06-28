package ua.com.bohdanprie.notes.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.domain.ManagerFactory;
import ua.com.bohdanprie.notes.domain.managers.TextManager;
import ua.com.bohdanprie.notes.domain.managers.UserManager;

public class WebUtils {
	private static final Logger LOG = LogManager.getLogger(WebUtils.class.getName());
	
	public static void loadResource(String resource, HttpServletResponse response) throws IOException {
		LOG.trace("Loading resource " + resource);
		File file = null;
		response.setCharacterEncoding("UTF-8");

		if (resource.endsWith(".html")) {
			file = new File("src/main/webapp/html/" + resource);
			response.setContentType("text/html");
		} else if (resource.endsWith(".css")) {
			file = new File("src/main/webapp/css/" + resource);
			response.setContentType("text/css");
		} else if (resource.endsWith(".js")) {
			file = new File("src/main/webapp/js/" + resource);
			response.setContentType("application/javascript");
		}
		LOG.trace("Trimming resource " + resource);
		String trimmedResource  = trimResourse(file);
		LOG.trace("Writing resource to response");
		response.getWriter().append(trimmedResource);
	}
	
	public static String trimResourse(File file) throws IOException {
		StringBuffer response = new StringBuffer();

		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();

		LOG.trace("Removing useless elements from file");
		while (line != null) {
			for (int i = 0; i < line.length(); i++) {
				char ch = line.charAt(i);
				if (ch != '\n' && ch != '\t' && ch != '\r') {
					response.append(ch);
				}
			}
			line = reader.readLine();
		}
		reader.close();
		LOG.trace("Returning trimmed file");
		return response.toString();
	}

	public static void loadJSON(HttpServletResponse response, String JSON) throws IOException {
		LOG.trace("Loading JSON to response");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(JSON);
	}

	public static String readData(HttpServletRequest request) throws IOException {
		LOG.trace("Reading data from request");
		StringBuffer data = new StringBuffer();
		String line = null;
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			data.append(line);
		}
		LOG.trace("Returning data, read from request");
		return data.toString();
	}
	
	public static TextManager getTextManager(HttpServletRequest request) {
		String elementsNeeded = request.getParameter("need");
		TextManager manager = null;
		if("note".equals(elementsNeeded)) {
			manager = ManagerFactory.getInstance().getNoteManager();
		} else if("toDo".equals(elementsNeeded)) {
			manager = ManagerFactory.getInstance().getToDoLineManager();
		}
		return manager;
	}
	
	public static UserManager getUserManager() {
		return ManagerFactory.getInstance().getUserManager();
	}
}
