package com.promelle.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

import com.promelle.constants.Punctuation;
import com.promelle.exception.InitializationException;
import com.promelle.request.tracker.AbstractRequestTracker;

/**
 * This class is responsible for holding config.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class AbstractConfiguration {
	private static final String DEFAULT_APP = "promelle";
	private static final String APP = "APP";
	private static final String MESSAGE = "message";
	private static final String PROPS = "props";
	private static final String I18N = "i18n";
	public static final String CONFIG_HOME = "PROMELLE_CONFIG_HOME";
	private static final String DEFAULT_CONFIG_HOME = "/promelle/config";
	private static ClassLoader i18nLoader;

	private static final Map<String, String> PROPERTIES = new HashMap<>();

	private AbstractConfiguration() {
		// default constructor
	}

	public static String getAppName() {
		String app = System.getenv(APP);
		if (StringUtils.isBlank(app)) {
			app = System.getProperty(APP);
		}
		if (StringUtils.isBlank(app)) {
			app = DEFAULT_APP;
		}
		return app;
	}

	public static String getConfigHome() {
		String home = System.getenv(CONFIG_HOME);
		if (StringUtils.isBlank(home)) {
			home = System.getProperty(CONFIG_HOME);
		}
		if (StringUtils.isBlank(home)) {
			home = DEFAULT_CONFIG_HOME;
		}
		return home;
	}

	static {
		String home = getConfigHome();
		File configDir = new File(home);
		if (configDir == null || !configDir.exists()) {
			throw new InitializationException(
					"Missing config home directory. Please set CONFIG_HOME environment.");
		}

		String[] apps = new String[] { "common", getAppName() };
		for (String app : apps) {
			String appPath = home + Punctuation.FORWARD_SLASH.toString() + app
					+ Punctuation.FORWARD_SLASH.toString();
			String propsPath = appPath + PROPS;
			File propsDir = new File(propsPath);
			if (propsDir != null && propsDir.exists()) {
				List<File> files = Arrays.asList(propsDir.listFiles());
				Map<String, String> configFiles = files
						.stream()
						.flatMap(file -> createPropertyMap(file))
						.collect(
								Collectors.toMap(
										entry -> (String) entry.getKey(),
										entry -> (String) entry.getValue()));
				configFiles.forEach((k, v) -> PROPERTIES.put((String) k,
						(String) v));
			} else {
				throw new InitializationException(
						"Missing configuration properties.");
			}

			String messagePath = appPath + I18N;
			File messagePathDir = new File(messagePath);
			if (propsDir != null && propsDir.exists()) {
				try {
					i18nLoader = new URLClassLoader(new URL[] { messagePathDir
							.toURI().toURL() });
				} catch (MalformedURLException e) {
					throw new InitializationException("Error loading config", e);
				}
			} else {
				throw new InitializationException("Missing configuration messages.");
			}
		}
	}

	private static Stream<Entry<Object, Object>> createPropertyMap(File file) {
		try (FileInputStream fis = new FileInputStream(file)) {
			Properties properties = new Properties();
			properties.load(fis);
			return properties.entrySet().stream();
		} catch (IOException e) {
			throw new InitializationException(e);
		}
	}

	/**
	 * This function is responsible for returning the value for given property
	 * key.
	 * 
	 * @param key
	 * @return value
	 */
	public static String getProperty(String key) {
		return PROPERTIES.get(key);
	}

	/**
	 * This function is responsible for returning the value for given property
	 * key.
	 * 
	 * @param key
	 * @param defaultStr
	 * @return value
	 */
	public static String getProperty(String key, String defaultStr) {
		return PROPERTIES.containsKey(key)
				&& StringUtils.isNotBlank(PROPERTIES.get(key)) ? PROPERTIES
				.get(key) : defaultStr;
	}

	/**
	 * This function is responsible for returning the value for given i18n key.
	 * 
	 * @param key
	 * @return value
	 */
	public static String getI18NMessage(AbstractRequestTracker requestTracker,
			String key) {
		if (StringUtils.isNotBlank(requestTracker.getLocale())) {
			String[] vals = requestTracker.getLocale().split(
					Punctuation.UNDERSCORE.toString());
			return ResourceBundle.getBundle(
					MESSAGE,
					vals.length > 1 ? new Locale(vals[0], vals[1])
							: new Locale("en", "US"), i18nLoader)
					.getString(key);
		} else {
			return ResourceBundle.getBundle(MESSAGE, new Locale("en", "US"),
					i18nLoader).getString(key);
		}
	}

}
