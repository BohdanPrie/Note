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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.bohdanprie.notes.ui.WebUtils;

@WebFilter(filterName = "FileLoadFilter")
public class FileLoadFilter implements Filter {
	private static final Logger LOG = LogManager.getLogger(FileLoadFilter.class.getName());

	public FileLoadFilter() {

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest requestFilter, ServletResponse responseFilter, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) requestFilter;
		HttpServletResponse response = (HttpServletResponse) responseFilter;

		String page = WebUtils.getRequestedPath(request);

		if (page.endsWith(".css") || page.endsWith(".js")) {
			LOG.trace("Loading resource " + page);
			WebUtils.loadResource(page, response);
			return;
		}

		LOG.trace("Continue performing filters");
		chain.doFilter(requestFilter, response);
	}

}
