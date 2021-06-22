package ua.com.bohdanprie.notes.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class WebUtils {
	public static void loadResource(String resource, PrintWriter writer) throws IOException {

		File file = null;
		StringBuilder response = new StringBuilder();

		if (resource.endsWith(".html")) {
			file = new File("src/main/webapp/html/" + resource);
		} else if (resource.endsWith(".css")) {
			file = new File("src/main/webapp/css/" + resource);
		} else if (resource.endsWith(".js")) {
			file = new File("src/main/webapp/js/" + resource);
		}

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

		writer.append(response.toString());

		reader.close();
	}
}
