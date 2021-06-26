package ua.com.bohdanprie.notes.ui.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.ui.WebUtils;

@WebFilter("/*")
public class AuthFilter implements Filter {
	private static final Logger LOG = LogManager.getLogger(AuthFilter.class.getName());

	public AuthFilter() {
		LOG.debug("AuthFilter initialized");
	}

	public void destroy() {

	}

	public void doFilter(ServletRequest requestFilter, ServletResponse responseFilter, FilterChain chain)
			throws IOException, ServletException {
		LOG.trace("Performing filter");
		HttpServletRequest request = (HttpServletRequest) requestFilter;
		HttpServletResponse response = (HttpServletResponse) responseFilter;

		String[] path = request.getRequestURI().split("/");
		String page = path[path.length - 1];

		HttpSession session = request.getSession(false);
		String UserAgent = request.getHeader("User-Agent");
		
		if (session == null || !UserAgent.equals(session.getAttribute("User-Agent"))) {
			LOG.trace("User is not authorised");
			if ("profile".equals(page) || "notes".equals(page)) {
				LOG.trace("Sending redirect to main page");
				response.sendRedirect("/main");
				return;
			}
		} else {
			LOG.trace("User is authorised");
			if("login".equals(page) || "registration".equals(page)) {
				LOG.trace("Sending redirect to main page");
				response.sendRedirect("/main");
				return;
			}
		}
		
		if(page.endsWith(".css") || page.endsWith(".js")) {
			LOG.trace("Loading resource " + page);
			WebUtils.loadResource(page, response);
			return;
		}
		chain.doFilter(requestFilter, responseFilter);
	}

	public void init(FilterConfig fConfig) throws ServletException {

	}
}