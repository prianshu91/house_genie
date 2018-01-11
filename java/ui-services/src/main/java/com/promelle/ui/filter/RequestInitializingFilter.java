package com.promelle.ui.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.promelle.constants.Punctuation;
import com.promelle.utils.UUIDUtils;

/**
 * This filter is intended for acting as an edge service with support for
 * service discovery & load balancing.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class RequestInitializingFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RequestInitializingFilter.class.getName());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// do nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String sessionId = req.getHeader("PROMELLE_SESS_UUID");
        LOGGER.info("PROMELLE_SESS_UUID : " + sessionId);
		req.setAttribute("requestId", UUIDUtils.getUUID());
		String serverName = req.getServerName();
		req.setAttribute("portalName", "promelle");
		// req.setAttribute(
		// "portalName",
		// serverName.substring(0,
		// serverName.indexOf(Punctuation.DOT.toString())));
		req.setAttribute("domain", serverName.substring(serverName
				.indexOf(Punctuation.DOT.toString()) + 1));
		request.setAttribute("requestBy", req.getHeader("requestBy"));
		request.setAttribute("scheme", req.getScheme());
		if (StringUtils.isBlank(req.getParameter("timeZone"))) {
			request.setAttribute("timeZone", req.getHeader("timeZone"));
		} else {
			request.setAttribute("timeZone", req.getParameter("timeZone"));
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// do nothing
	}

}
