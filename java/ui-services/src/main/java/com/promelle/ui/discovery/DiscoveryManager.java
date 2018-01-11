package com.promelle.ui.discovery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.client.http.HttpRequest.Verb;
import com.promelle.exception.AbstractException;
import com.promelle.utils.RestUtils;
import com.promelle.utils.RestUtils.HttpRequestMode;

/**
 * This filter is intended for handling service discovery & load balancing.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class DiscoveryManager {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DiscoveryManager.class.getName());
	private Map<String, String> registry;

	public DiscoveryManager(Map<String, String> registry) {
		this.registry = registry;
	}

	public String loadBalancedRestCall(HttpServletRequest req, String path)
			throws AbstractException {
		try {
			Verb verb = Verb.valueOf(req.getMethod());
			int endPos = path.indexOf("/", 9);
			String moduleName = endPos > 0 ? path.substring(9, endPos) : path
					.substring(9);
			String host = registry.get(moduleName);
			String url = req.getScheme() + "://" + host + path;
			Map<String, String> headers = new HashMap<String, String>();
			Enumeration<String> paramsEnum = req.getParameterNames();
			String requestData = null;
			if (Verb.GET == verb) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				while (paramsEnum.hasMoreElements()) {
					String key = paramsEnum.nextElement();
					params.add(new BasicNameValuePair(key, req
							.getParameter(key)));
				}
				if (!params.isEmpty()) {
					url += "?" + RestUtils.getQuery(params);
				}
				params.clear();
			} else if (Verb.DELETE != verb) {
				InputStream is;
				try {
					is = req.getInputStream();
				} catch (IOException e) {
					throw new AbstractException(e);
				}
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				String line;
				try {
					line = br.readLine();
				} catch (IOException e) {
					throw new AbstractException(e);
				}
				String data = "";
				while (line != null) {
					data += line;
					try {
						line = br.readLine();
					} catch (IOException e) {
						throw new AbstractException(e);
					}
				}
				if (StringUtils.isNotBlank(data)) {
					requestData = data;
				} else {
					List<String> params = new ArrayList<>();
					while (paramsEnum.hasMoreElements()) {
						String key = paramsEnum.nextElement();
						params.add(key + "=" + req.getParameter(key));
					}
					requestData = StringUtils.join(params, '&');
				}
			}
			// headers.put("requestId", (String) req.getAttribute("requestId"));
			// headers.put("portalName", (String)
			// req.getAttribute("portalName"));
			// headers.put("requestBy", (String) req.getAttribute("requestBy"));
			// headers.put("domain", (String) req.getAttribute("domain"));
			// headers.put("timeZone", (String) req.getAttribute("timeZone"));
			switch (verb) {
			case GET:
				return RestUtils.restCall(url, HttpRequestMode.GET, null,
						headers);
			case POST:
				return RestUtils.restCall(url, HttpRequestMode.POST, null,
						headers, requestData);
			case PUT:
				return RestUtils.restCall(url, HttpRequestMode.PUT, null,
						headers, requestData);
			case DELETE:
				return RestUtils.restCall(url, HttpRequestMode.DELETE, null,
						headers, requestData);
			default:
				break;
			}
			return null;
		} catch (Exception e) {
			throw new AbstractException(e);
		}
	}

	public void writeResponse(String resStr, HttpServletResponse res)
			throws AbstractException {
		res.setHeader("Content-Length",
				String.valueOf(resStr.getBytes().length));
		res.setContentType(MediaType.APPLICATION_JSON);
		res.setCharacterEncoding("UTF-8");
		try (PrintWriter out = res.getWriter()) {
			out.println(resStr);
			res.setStatus(200);
		} catch (IOException e) {
			res.setStatus(500);
			throw new AbstractException(e);
		}
	}

}
