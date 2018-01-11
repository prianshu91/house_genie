package com.promelle.ui.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.promelle.ui.dao.LoginSessionDao;
import com.promelle.ui.dto.LoginSession;

/**
 * This filter is intended for handling the authorization process.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class AuthorizationFilter implements Filter {
	private static final String GET = "GET";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuthorizationFilter.class.getName());
	protected static final List<Pattern> ALLOWED_PATTERNS = new ArrayList<>();
	protected static final List<Pattern> ALLOWED_GET_PATTERNS = new ArrayList<>();
	protected static final String LOGOUT_PATH = "/service/user/logout";
	private Long sessionTimeOutMillis;
	private LoginSessionDao loginSessionDao;

	static {
		ALLOWED_PATTERNS.add(Pattern.compile("/service/account/register"));
		ALLOWED_PATTERNS.add(Pattern.compile("/service/user"));
		ALLOWED_PATTERNS.add(Pattern.compile("/service/user/vaidateOTP"));
		ALLOWED_PATTERNS.add(Pattern.compile("/service/user/activate"));
		ALLOWED_PATTERNS.add(Pattern.compile("/service/user/login"));
		ALLOWED_PATTERNS.add(Pattern.compile("/service/user/forgetPassword"));
		ALLOWED_PATTERNS.add(Pattern.compile("/service/user/resetPassword"));
		ALLOWED_PATTERNS.add(Pattern.compile("/service/product/list"));
		ALLOWED_PATTERNS.add(Pattern.compile("/service/product/lengths"));
		ALLOWED_PATTERNS.add(Pattern.compile("/service/product/types"));
		ALLOWED_PATTERNS.add(Pattern.compile("/service/product/occasions"));
		ALLOWED_PATTERNS.add(Pattern.compile("/service/product/colors"));
		ALLOWED_PATTERNS.add(Pattern.compile("/service/product/sizes"));
		ALLOWED_PATTERNS.add(Pattern.compile("/service/review/list"));
		ALLOWED_PATTERNS.add(Pattern.compile("/service/user/generateOTP"));
		ALLOWED_PATTERNS.add(Pattern
				.compile("/service/communication/email/admin"));
		ALLOWED_PATTERNS
				.add(Pattern.compile("/service/user/listActiveSchools"));

		ALLOWED_GET_PATTERNS.add(
				Pattern.compile("/service/user/[a-z0-9]{32}+"));
		ALLOWED_GET_PATTERNS.add(
				Pattern.compile("/service/product/[a-z0-9]{32}+"));
		ALLOWED_PATTERNS.add(
				Pattern.compile("/service/product/getDocumentContent"));
		ALLOWED_PATTERNS.add(
				Pattern.compile("/service/product/getDocumentUrl"));
		ALLOWED_PATTERNS.add(
				Pattern.compile("/service/product/getAPIVersion"));	
		ALLOWED_PATTERNS.add(Pattern.compile("/service/product/categories"));
		ALLOWED_PATTERNS.add(Pattern.compile("/service/product/categories2"));
		ALLOWED_PATTERNS.add(Pattern.compile("/service/product/colors"));
	}

	public AuthorizationFilter(LoginSessionDao loginSessionDao,
			Long sessionTimeOutMillis) {
		super();
		this.loginSessionDao = loginSessionDao;
		this.sessionTimeOutMillis = sessionTimeOutMillis;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// do nothing
	}

	public static boolean isAllowed(String url, String method) {
		for (Pattern pattern : ALLOWED_PATTERNS) {
			if (pattern.matcher(url).matches()) {
				return true;
			}
		}
		if (GET.equalsIgnoreCase(method)) {
			for (Pattern pattern : ALLOWED_GET_PATTERNS) {
				if (pattern.matcher(url).matches()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		Cookie[] cookies = req.getCookies();
		String sessionId = req.getHeader("PROMELLE_SESS_UUID");
		if (cookies != null && StringUtils.isBlank(sessionId)) {
			for (Cookie cookie : cookies) {
				if ("PROMELLE_SESS_UUID".equalsIgnoreCase(cookie.getName())) {
					sessionId = cookie.getValue();
				}
			}
		}
		LOGGER.info("PROMELLE_SESS_UUID : " + sessionId);
		String path = req.getRequestURI()
				.substring(req.getContextPath().length()).split("\\?")[0];
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		if (isAllowed(path, req.getMethod())) {
			chain.doFilter(request, response);
		} else if (LOGOUT_PATH.equalsIgnoreCase(path)) {
			loginSessionDao.deleteById(sessionId);
		} else {
			LoginSession loginSession = loginSessionDao.findById(sessionId);
			
			// All changes done for PEA-649 issue.
			if (loginSession != null) {
				// long now = Calendar.getInstance().getTimeInMillis();
				// if ((now - loginSession.getModifiedOn()) >
				// sessionTimeOutMillis) {
				// loginSessionDao.deleteById(sessionId);
				// res.setStatus(403);
				// } else {
				request.setAttribute("requestBy", loginSession.getUserId());
				request.setAttribute("timeZone", loginSession.getTimeZone());
				// loginSessionDao.updatedModifiedOn(sessionId, now);
				// do more authorization stuff here
				chain.doFilter(request, response);
				// }
			} else {
				res.setStatus(401);
			}
		}

	}

	@Override
	public void destroy() {
		// do nothing
	}

}
