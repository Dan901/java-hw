package hr.fer.zemris.webapps.blog.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Sets the boolean flag in the request indicating if the user is logged in.
 *
 * @author Dan
 */
@WebFilter("/*")
public class LoggedInFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		request.setAttribute("loggedIn", httpReq.getSession().getAttribute("user_id") != null);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}
