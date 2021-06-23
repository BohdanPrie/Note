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

import ua.com.bohdanprie.notes.ui.WebUtils;

@WebFilter("/*")
public class AuthFilter implements Filter {

	public AuthFilter() {

	}

	public void destroy() {

	}

	public void doFilter(ServletRequest requestFilter, ServletResponse responseFilter, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) requestFilter;
		HttpServletResponse response = (HttpServletResponse) responseFilter;

		String[] path = request.getRequestURI().split("/");
		String page = path[path.length - 1];
		
		HttpSession session = request.getSession(false);
		String UserAgent = request.getHeader("User-Agent");
		
		if (session == null || !UserAgent.equals(session.getAttribute("User-Agent"))) {
			if ("profile".equals(page) || "notes".equals(page)) {
				response.sendRedirect("/main");
				return;
			}
		}
		
		if(page.endsWith(".css") || page.endsWith(".js")) {
			WebUtils.loadResource(page, response);
			return;
		}
		
		chain.doFilter(requestFilter, responseFilter);
	}

	public void init(FilterConfig fConfig) throws ServletException {

	}

}
