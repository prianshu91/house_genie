package com.promelle.request.tracker;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.promelle.constants.Punctuation;
import com.promelle.object.AbstractObject;

/**
 * This class is responsible for holding generic fields to be returned in
 * response.
 * 
 * Note: Setters removed so that nobody can alter request info.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class AbstractRequestTracker extends AbstractObject {
	private static final long serialVersionUID = 5782835785114529291L;
	private static final String F_TIME_ZONE = "timeZone";
	private static final String F_DOMAIN = "domain";
	private static final String F_REQUEST_BY = "requestBy";
	// private static final String F_PORTAL_NAME = "portalName";
	private static final String F_REQUEST_ID = "requestId";
	private static final String F_HTTP_SCHEME = "scheme";
	public static final String F_HTTP_SCHEME_HTTPS = "https";
	private String requestId;
	private String portalName;
	private Long requestTimestamp;
	private String requestBy;
	private String domain;
	private String locale = "en_US";
	private String timeZone;
	private String scheme = "https";

	public static final String DEFAULT_USER = "system";
	public static final String DEFAULT_DOMAIN = "system";

	public AbstractRequestTracker() {
		requestTimestamp = Calendar.getInstance().getTimeInMillis();
		requestBy = DEFAULT_USER;
	}

	public AbstractRequestTracker(String requestId, String portalName,
			String requestBy, String domain, String timeZone, String scheme) {
		super();
		this.requestId = requestId;
		this.portalName = "promelle";// portalName;
		this.requestTimestamp = Calendar.getInstance().getTimeInMillis();
		this.requestBy = requestBy;
		this.domain = domain;
		this.scheme = scheme;
		this.timeZone = StringUtils.isNotBlank(timeZone) ? timeZone : Calendar
				.getInstance().getTimeZone().getID();
	}

	public AbstractRequestTracker(HttpServletRequest request) {
		super();
		String serverName = request.getServerName();
		this.requestTimestamp = Calendar.getInstance().getTimeInMillis();
		this.requestId = (String) request.getAttribute(F_REQUEST_ID);
		if (StringUtils.isBlank(requestId)) {
			this.requestId = request.getHeader(F_REQUEST_ID);
		}
		this.portalName = "promelle";// (String)
										// request.getAttribute(F_PORTAL_NAME);
		// if (StringUtils.isBlank(portalName)) {
		// this.portalName = request.getHeader(F_PORTAL_NAME);
		// }
		// if (StringUtils.isBlank(portalName)) {
		// this.portalName = serverName.startsWith("localhost") ? "localhost"
		// : serverName.substring(0,
		// serverName.indexOf(Punctuation.DOT.toString()));
		// }
		this.requestBy = (String) request.getAttribute(F_REQUEST_BY);
		if (StringUtils.isBlank(requestBy)) {
			this.requestBy = request.getHeader(F_REQUEST_BY);
		}
		this.domain = (String) request.getAttribute(F_DOMAIN);
		if (StringUtils.isBlank(domain)) {
			this.domain = request.getHeader(F_DOMAIN);
		}
		if (StringUtils.isBlank(domain)) {
			this.domain = serverName.substring(serverName
					.indexOf(Punctuation.DOT.toString()) + 1);
		}
		this.timeZone = (String) request.getAttribute(F_TIME_ZONE);
		if (StringUtils.isBlank(timeZone)) {
			this.timeZone = request.getHeader(F_TIME_ZONE);
		}
		if (StringUtils.isBlank(timeZone)) {
			this.timeZone = request.getParameter(F_TIME_ZONE);
		}
		if (StringUtils.isBlank(this.timeZone)) {
			this.timeZone = Calendar.getInstance().getTimeZone().getID();
		}
		this.scheme = (String) request.getAttribute(F_HTTP_SCHEME);
		if (StringUtils.isBlank(this.scheme)) {
			this.scheme = request.getHeader(F_HTTP_SCHEME);
		}
		if (StringUtils.isBlank(this.scheme)) {
			this.scheme = "https";
		}
	}

	public String getRequestId() {
		return requestId;
	}

	public String getPortalName() {
		return portalName;
	}

	public Long getRequestTimestamp() {
		return requestTimestamp;
	}

	public String getRequestBy() {
		return requestBy;
	}

	public String getDomain() {
		return domain;
	}

	public String getLocale() {
		return locale;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public String getScheme() {
		return scheme;
	}

}
