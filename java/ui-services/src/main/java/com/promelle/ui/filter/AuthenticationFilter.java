package com.promelle.ui.filter;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.ui.dao.LoginSessionDao;
import com.promelle.ui.discovery.DiscoveryManager;
import com.promelle.ui.dto.LoginSession;
import com.promelle.utils.UUIDUtils;

/**
 * This filter is intended for handling the authentication process.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class AuthenticationFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuthenticationFilter.class.getName());
	private static final String PATH = ";Path=/;";
	private static final String DOMAIN = ";Domain=%s;";

	private static final String PROMELLE_SESS_UUID = "PROMELLE_SESS_UUID=";
	private static final String SET_COOKIE = "Set-Cookie";
	private static final String ID = "id";
	private static final String PERMISSIONS = "permissions";
//	private static final String PORTAL_NAME = "portalName";
	private static final String DATA = "data";
	private static final String ERROR_CODE = "errorCode";

	private DiscoveryManager discoveryManager;
	private LoginSessionDao loginSessionDao;

	public AuthenticationFilter(DiscoveryManager discoveryManager,
			LoginSessionDao loginSessionDao) {
		super();
		this.discoveryManager = discoveryManager;
		this.loginSessionDao = loginSessionDao;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// do nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String path = req.getRequestURI().substring(
				req.getContextPath().length());
		if (path.startsWith("/service/user/login")) {
			userLogin(req, res);
		} else {
			try {
				chain.doFilter(request, response);
			} catch (IOException e) {
				throw new ServletException(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void userLogin(HttpServletRequest req, HttpServletResponse res)
			throws ServletException {
		try {
			String path = req.getRequestURI().substring(
					req.getContextPath().length());
//			String serverName = req.getServerName();
//			String portalName = serverName.substring(0,
//					serverName.indexOf(Punctuation.DOT.toString()));
			String resStr = discoveryManager.loadBalancedRestCall(req, path);
			Map<String, Object> responseMap = new ObjectMapper().readValue(
					resStr, Map.class);
			LOGGER.info("RESPONSEMAP =>> " + responseMap);
			if (responseMap.containsKey(ERROR_CODE)) {
				if(((String) responseMap.get(ERROR_CODE)).equalsIgnoreCase("429")){
					res.setStatus(429);
					return;					
				}else{
					discoveryManager.writeResponse(resStr, res);
					return;
				}
				
//				if (Integer.valueOf((String) responseMap.get(ERROR_CODE)) == 429) {
//					res.setStatus(429);
//					return;
//				}
//				LOGGER.error("Error code found");
//				res.setStatus(401);
//				return;
			}
			Map<String, Object> dataMap = (Map<String, Object>) responseMap
					.get(DATA);
//			String userPortalName = (String) dataMap.get(PORTAL_NAME);
//			if (!userPortalName.equalsIgnoreCase(portalName)) {
//				LOGGER.error(String.format("Portal Mismatch (%s, %s)", userPortalName, portalName));
//				res.setStatus(401);
//				return;
//			}

			LoginSession loginSession = saveSession(new AbstractRequestTracker(
					req), (String) dataMap.get(ID),
					(List<String>) dataMap.get(PERMISSIONS), resStr);
			res.setHeader(SET_COOKIE, PROMELLE_SESS_UUID + loginSession.getId()
					+ PATH + String.format(DOMAIN, req.getServerName()));
			res.setHeader("PROMELLE_SESS_UUID", loginSession.getId());

			discoveryManager.writeResponse(resStr, res);
		} catch (Exception e) {
			LOGGER.error("Error in userLogin()", e);
			res.setStatus(500);
		}
	}

	public LoginSession saveSession(AbstractRequestTracker requestTracker,
			String userId, List<String> permissions, String response) {
		LoginSession loginSession = new LoginSession();
		loginSession.setPermissions(permissions);
		loginSession.setUserId(userId);
		loginSession.setId(UUIDUtils.getUUID());
		loginSession.setCreatedOn(Calendar.getInstance().getTimeInMillis());
		loginSession.setModifiedOn(loginSession.getCreatedOn());
		loginSession.setResponse(response);
		loginSession.setTimeZone(requestTracker.getTimeZone());
		loginSessionDao.save(requestTracker, loginSession);
		return loginSession;
	}

	@Override
	public void destroy() {
		// do nothing
	}

}
