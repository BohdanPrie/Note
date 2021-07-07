package ua.com.bohdanprie.notes.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Class provide methods to work with {@link HttpServletRequest} and {@link HttpServletResponse}
 * @author bohda
 *
 */
public final class WebUtils {
	private static final Logger LOG = LogManager.getLogger(WebUtils.class.getName());
	
	/**
	 * Method loads given resource to {@link HttpServletResponse}
	 * @param resource
	 * @param response
	 * @throws IOException
	 */
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
		} else {
			return;
		}
		LOG.trace("Trimming resource " + resource);
		String trimmedResource  = trimResourse(file);
		LOG.info("Writing resource to response");
		response.getWriter().append(trimmedResource);
	}
	
	/**
	 * Reads file from system (usually .html / .css / .js) and removes unnecessary elements
	 * for loading resource optimization
	 * For example:
	 * <table summary="Examples of Returned Values">
     * <th>File</th>
     * <th><td>Trimmed file</th>
     * <tr><td>&ltdiv&gt<br><td>&ltinput&gt<br>&lt/div&gt<td><td>&ltdiv&gt&ltinput&gt&lt/div&gt
     * </table>
	 * @param file
	 * @return String representation of a file
	 * @throws IOException
	 */
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
		LOG.info("Returning trimmed file");
		return response.toString();
	}

	/**
	 * Method loads JSON to a response (Can be converted to load other types of String data) 
	 * @param response
	 * @param JSON
	 * @throws IOException
	 */
	public static void loadJSON(HttpServletResponse response, String JSON) throws IOException {
		LOG.trace("Loading JSON to response");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(JSON);
		LOG.info("JSON was loaded to response");
	}
	
	/**
	 * Method reads data, that came in a request and return a String representation of data
	 * @param request
	 * @return data, that came in a request
	 * @throws IOException
	 */
	public static String readData(HttpServletRequest request) throws IOException {
		LOG.trace("Reading data from request");
		StringBuffer data = new StringBuffer();
		String line = null;
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null) {
			data.append(line);
		}
		LOG.info("Returning data, read from request");
		return data.toString();
	}
	
	/**
	 * Returns last element of requested path 
	 * For example:
	 * <table summary="Examples of Returned Values">
     * <tr align=left><th>Requested Path      </th>
     * <th>     Returned Value</th>
     * <tr><td>/somePath/path/anotherPath<td><td>/anotherPath
     * <tr><td>/main/page/main.css
     * <td><td>/main.css
     * </table>
	 * @param request
	 * @return a last String element of path 
	 */
	public static String getRequestedPath(HttpServletRequest request) {
		String requestURI = request.getRequestURI();		
		LOG.info("Returning curent path");
		return request.getRequestURI().substring(requestURI.lastIndexOf("/"));
	}
}
