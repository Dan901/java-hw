package hr.fer.zemris.webapps.webapp2;

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
 * Filters all requests and sets session attribute for background color if none
 * is set.
 * 
 * @author Dan
 */
@WebFilter("/*")
public class BgColorFilter implements Filter {

	/** Default background color. */
	private static final String COLOR = "white";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		if (req.getSession().getAttribute("pickedBgCol") == null) {
			req.getSession().setAttribute("pickedBgCol", COLOR);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}
