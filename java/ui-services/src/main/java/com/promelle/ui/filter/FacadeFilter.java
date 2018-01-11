package com.promelle.ui.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.promelle.exception.AbstractException;
import com.promelle.ui.discovery.DiscoveryManager;

/**
 * This filter is intended for acting as an edge service with support for
 * service discovery & load balancing.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class FacadeFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FacadeFilter.class.getName());
	private DiscoveryManager discoveryManager;

	public FacadeFilter(DiscoveryManager discoveryManager) {
		super();
		this.discoveryManager = discoveryManager;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// do nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String path = req.getRequestURI().substring(
				req.getContextPath().length());
		if (path.startsWith("/service")) {
			try {
				discoveryManager.writeResponse(
						discoveryManager.loadBalancedRestCall(req, path), res);
			} catch (AbstractException e) {
				LOGGER.error("Error in facade", e);
				res.setStatus(500);
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		// do nothing
	}

}
