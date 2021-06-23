package ua.com.bohdanprie.notes.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtils {
	public static void loadResource(String resource, HttpServletResponse response) throws IOException {
		File file = null;

		if (resource.endsWith(".html")) {
			file = new File("src/main/webapp/html/" + resource);
		} else if (resource.endsWith(".css")) {
			file = new File("src/main/webapp/css/" + resource);
		} else if (resource.endsWith(".js")) {
			file = new File("src/main/webapp/js/" + resource);
		}
		response.getWriter().append(trimResourse(file));
	}

	public static String trimResourse(File file) throws IOException {
		StringBuilder response = new StringBuilder();

		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();

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
		return response.toString();
	}

	public static void loadJSON(HttpServletResponse response, String JSON) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(JSON);
	}

	public static String readData(HttpServletRequest request) throws IOException {
		StringBuffer data = new StringBuffer();
		String line = null;
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			data.append(line);
		}
		return data.toString();
	}
}
