package com.promelle.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("deprecation")
public abstract class RestUtils {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RestUtils.class.getName());

	public static final String ACCEPT = "Accept";
	public static final String ACCEPT_LANGUAGE = "Accept-Language";
	public static final String CONNECTION = "Connection";
	public static final String HOST = "Host";
	public static final String USER_AGENT = "User-Agent";
	public static final String REFERER = "Referer";
	public static final String CACHE_CONTROL = "Cache-Control";
	public static final String CONTENT_LENGTH = "Content-Length";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String ORIGIN = "Origin";
	public static final String PRAGMA = "Pragma";
	public static final Integer RESPONSE_CODE_MOVED = 301;
	public static final Integer RESPONSE_CODE_MOVED_TEMP = 302;
	public static final String ENCODING = "UTF-8";

	static class CustomSocketFactory extends SSLSocketFactory {

		private SSLSocketFactory sf = null;
		private String[] enabledCiphers = null;

		public CustomSocketFactory(SSLSocketFactory sf,
				String... enabledCiphers) {
			super();
			this.sf = sf;
			this.enabledCiphers = enabledCiphers;
		}

		private Socket getSocketWithEnabledCiphers(Socket socket) {
			if (getSupportedCipherSuites() != null
					&& socket instanceof SSLSocket) {
				((SSLSocket) socket)
						.setEnabledCipherSuites(getSupportedCipherSuites());
			}
			return socket;
		}

		@Override
		public Socket createSocket(Socket s, String host, int port,
				boolean autoClose) throws IOException {
			return getSocketWithEnabledCiphers(sf.createSocket(s, host, port,
					autoClose));
		}

		@Override
		public String[] getDefaultCipherSuites() {
			return sf.getDefaultCipherSuites();
		}

		@Override
		public String[] getSupportedCipherSuites() {
			return enabledCiphers == null ? sf.getSupportedCipherSuites()
					: enabledCiphers;
		}

		@Override
		public Socket createSocket(String host, int port) throws IOException {
			return getSocketWithEnabledCiphers(sf.createSocket(host, port));
		}

		@Override
		public Socket createSocket(InetAddress address, int port)
				throws IOException {
			return getSocketWithEnabledCiphers(sf.createSocket(address, port));
		}

		@Override
		public Socket createSocket(String host, int port,
				InetAddress localAddress, int localPort) throws IOException {
			return getSocketWithEnabledCiphers(sf.createSocket(host, port,
					localAddress, localPort));
		}

		@Override
		public Socket createSocket(InetAddress address, int port,
				InetAddress localaddress, int localport) throws IOException {
			return getSocketWithEnabledCiphers(sf.createSocket(address, port,
					localaddress, localport));
		}
	}

	private static SSLSocketFactory socketFactory;

	static {
		try {
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, null, null);
			SSLParameters params = context.getSupportedSSLParameters();
			List<String> enabledCiphers = new ArrayList<String>();
			String[] exludedCipherSuites = { "_DHE_", "_DH_" };
			for (String cipher : params.getCipherSuites()) {
				boolean exclude = false;
				if (exludedCipherSuites != null) {
					for (int i = 0; i < exludedCipherSuites.length && !exclude; i++) {
						exclude = cipher.indexOf(exludedCipherSuites[i]) >= 0;
					}
				}
				if (!exclude) {
					enabledCiphers.add(cipher);
				}
			}
			String[] cArray = new String[enabledCiphers.size()];
			enabledCiphers.toArray(cArray);
			socketFactory = new CustomSocketFactory(context.getSocketFactory(),
					cArray);
		} catch (Exception e) {
			LOGGER.error("Error loading socket factory", e);
		}
	}

	public static enum HttpRequestMode {
		GET, POST, PUT, DELETE;
	}

	public static class CookieHelper {
		private List<String> cookies;
		private Map<String, String> cookiesMap;
		private Map<String, List<String>> responseHeaders;

		public void setResponseHeaders(Map<String, List<String>> responseHeaders) {
			this.responseHeaders = responseHeaders;
		}

		public CookieHelper() {
			this.cookiesMap = new HashMap<String, String>();
			this.cookies = new ArrayList<String>();
		}

		public List<String> getCookies() {
			return cookies;
		}

		public void setCookies(List<String> cookies) {
			if (cookies == null) {
				return;
			}
			for (String cookie : cookies) {
				cookie = cookie.split(";", 2)[0];
				String key = cookie.split("=", 2)[0];
				String value = cookie.split("=", 2)[1];
				if (value.equals("deleted")) {
					if (this.cookiesMap.containsKey(key)) {
						this.cookiesMap.remove(key);
					}
					continue;
				}
				this.cookiesMap.put(key, value);
			}
			this.cookies.clear();
			for (String cookieKey : cookiesMap.keySet()) {
				this.cookies.add(cookieKey + "=" + cookiesMap.get(cookieKey));
			}
		}

		public String getCookieByName(String name) {
			return cookiesMap.containsKey(name) ? cookiesMap.get(name) : "";
		}

		public List<String> getHeaderByName(String name) {
			return responseHeaders.containsKey(name) ? responseHeaders
					.get(name) : Arrays.asList("");
		}

	}

	public static class RestResponse implements Serializable {
		private static final long serialVersionUID = 121312412L;
		private final String html;
		private final CookieStore cookieStore;
		private final StatusLine statusLine;

		public RestResponse(String html, StatusLine statusLine,
				CookieStore cookieStore) {
			this.html = html;
			this.cookieStore = cookieStore;
			this.statusLine = statusLine;
		}

		public String getHtml() {
			return html;
		}

		public CookieStore getCookieStore() {
			return cookieStore;
		}

		public List<Cookie> getCookies() {
			return cookieStore.getCookies();
		}

		public StatusLine getStatusLine() {
			return statusLine;
		}
	}

	private static void writeParams(HttpURLConnection urlc,
			List<NameValuePair> params) throws IOException {
		if (params != null && params.size() > 0) {
			OutputStream os = urlc.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					os, ENCODING));
			writer.write(getQuery(params));
			writer.close();
			os.close();
		}
	}

	private static void writeHeaders(HttpURLConnection urlc,
			Map<String, String> requestProperty) throws IOException {
		if (requestProperty != null && requestProperty.size() > 0) {
			Set<String> keySet = requestProperty.keySet();
			for (String key : keySet) {
				urlc.addRequestProperty(key, requestProperty.get(key));
			}
		}
	}

	private static StringBuffer getInputStreamText(HttpURLConnection urlc)
			throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				urlc.getInputStream(), ENCODING));
		StringBuffer sb = new StringBuffer();
		String str = br.readLine();
		while (str != null) {
			if (str.trim().length() > 0) {
				sb.append(str);
			}
			str = br.readLine();
		}
		br.close();
		return sb;
	}

	private static StringBuffer getErrorStreamText(HttpURLConnection urlc)
			throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				urlc.getErrorStream(), ENCODING));
		String str;
		StringBuffer sb = new StringBuffer();
		while ((str = br.readLine()) != null) {
			if (str.trim().length() > 0) {
				sb.append(str);
			}
		}
		br.close();
		return sb;
	}

	/**
	 * This function provides feature for rest call
	 * 
	 * @param urlString
	 * @param requestMode
	 * @param params
	 * @param requestProperty
	 * @return Response String
	 * @throws IOException
	 */
	public static String restCall(String urlString,
			HttpRequestMode requestMode, List<NameValuePair> params,
			Map<String, String> requestProperty) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
		urlc.setDoOutput(true);
		urlc.setRequestMethod(requestMode.name());
		if (urlc instanceof HttpsURLConnection && socketFactory != null) {
			((HttpsURLConnection) urlc).setSSLSocketFactory(socketFactory);
		}
		urlc.setAllowUserInteraction(false);
		writeHeaders(urlc, requestProperty);
		writeParams(urlc, params);
		urlc.connect();
		try {
			StringBuffer sb = getInputStreamText(urlc);
			urlc.disconnect();
			return sb.toString();
		} catch (IOException e) {
			if (urlc.getErrorStream() != null) {
				StringBuffer sb = getErrorStreamText(urlc);
				urlc.disconnect();
				throw new IOException(sb.toString(), e);
			}
			throw e;
		}
	}

	public static String getRedirectUrl(String url) throws IOException {
		HttpContext context = new BasicHttpContext();
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(new HttpGet(url), context);
		httpClient.close();
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			throw new IOException(response.getStatusLine().toString());
		}
		HttpUriRequest currentReq = (HttpUriRequest) context
				.getAttribute("http.request");
		HttpHost currentHost = (HttpHost) context
				.getAttribute("http.target_host");
		return (currentReq.getURI().isAbsolute()) ? currentReq.getURI()
				.toString() : (currentHost.toURI() + currentReq.getURI());
	}

	public static String getFinalURL(String url) throws IOException {
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(url)
					.openConnection();
			con.setInstanceFollowRedirects(false);
			con.connect();
			con.getInputStream();

			if (con.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM
					|| con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
				url = con.getHeaderField("Location");
				return getFinalURL(url);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return url;
	}

	/**
	 * This function prepares parameters query for rest call
	 * 
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getQuery(List<NameValuePair> params)
			throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (NameValuePair pair : params) {
			if (StringUtils.isBlank(pair.getValue())) {
				continue;
			}
			if (first) {
				first = false;
			} else {
				result.append("&");
			}
			result.append(URLEncoder.encode(pair.getName(), ENCODING));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), ENCODING));
		}
		return result.toString();
	}

	/**
	 * Creates HTTP client. Uses Firefox User-Agent to fool Facebook. Sets
	 * cookies from provided CookieStore.
	 */
	private static DefaultHttpClient createHttpClient(CookieStore cookieStore) {
		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpProtocolParams
				.setUserAgent(httpclient.getParams(),
						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:9.0.1) Gecko/20100101 Firefox/9.0.1");

		if (cookieStore != null) {
			httpclient.setCookieStore(cookieStore);
		}
		return httpclient;
	}

	/**
	 * Sends GET request.
	 */
	public static RestResponse get(String url, CookieStore cookieStore)
			throws IOException {
		DefaultHttpClient httpclient = createHttpClient(cookieStore);

		HttpGet httpget = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpget);

		HttpEntity entity = response.getEntity();
		if (entity == null) {
			return null;
		}

		String content = EntityUtils.toString(entity);
		httpclient.getConnectionManager().shutdown();
		return new RestResponse(content, response.getStatusLine(),
				httpclient.getCookieStore());
	}

	/**
	 * Sends POST request.
	 */
	public static RestResponse post(String action,
			Map<String, String> parameters, CookieStore cookieStore)
			throws IOException {
		DefaultHttpClient httpclient = createHttpClient(cookieStore);

		httpclient.setRedirectStrategy(new DefaultRedirectStrategy() {
			public boolean isRedirected(HttpRequest request,
					HttpResponse response, HttpContext context) {
				boolean isRedirect = false;
				try {
					isRedirect = super.isRedirected(request, response, context);
				} catch (ProtocolException e) {
					LOGGER.error("Error setting redirect", e);
				}
				if (!isRedirect) {
					int responseCode = response.getStatusLine().getStatusCode();
					if (responseCode == RESPONSE_CODE_MOVED
							|| responseCode == RESPONSE_CODE_MOVED_TEMP) {
						return true;
					}
				}
				return isRedirect;
			}
		});

		HttpPost httpPost = new HttpPost(action);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			String name = entry.getKey();
			if (name == null) {
				continue;
			}
			String value = entry.getValue();
			nvps.add(new BasicNameValuePair(name, value));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		HttpResponse response = httpclient.execute(httpPost);
		HttpEntity entity = response.getEntity();
		if (entity == null) {
			return null;
		}
		if (cookieStore != null) {
			for (Cookie cookie : httpclient.getCookieStore().getCookies()) {
				cookieStore.addCookie(cookie);
			}
		}
		String content = EntityUtils.toString(entity);
		httpclient.getConnectionManager().shutdown();
		return new RestResponse(content, response.getStatusLine(),
				cookieStore == null ? httpclient.getCookieStore() : cookieStore);
	}

	/**
	 * This function provides feature for rest call
	 * 
	 * @param urlString
	 * @param requestMode
	 * @param requestData
	 * @param requestProperty
	 * @return Response String
	 * @throws IOException
	 */
	public static String restCall(String urlString,
			HttpRequestMode requestMode, List<NameValuePair> params,
			Map<String, String> requestProperty, String requestData)
			throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
		urlc.setDoOutput(true);
		urlc.setRequestMethod(requestMode.name());
		if (urlc instanceof HttpsURLConnection && socketFactory != null) {
			((HttpsURLConnection) urlc).setSSLSocketFactory(socketFactory);
		}
		urlc.setAllowUserInteraction(false);
		writeHeaders(urlc, requestProperty);
		if (requestData != null) {
			OutputStream os = urlc.getOutputStream();
			os.write(requestData.getBytes());
			os.close();
		}
		urlc.connect();
		try {
			StringBuffer sb = getInputStreamText(urlc);
			urlc.disconnect();
			return sb.toString();
		} catch (IOException e) {
			if (urlc.getErrorStream() != null) {
				StringBuffer sb = getErrorStreamText(urlc);
				urlc.disconnect();
				throw new IOException(sb.toString(), e);
			}
			throw e;
		}
	}

}
