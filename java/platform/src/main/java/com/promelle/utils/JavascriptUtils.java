package com.promelle.utils;

import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is intended for providing application wide common functions
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class JavascriptUtils {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JavascriptUtils.class.getName());

	public static Object evalutate(String expression,
			Map<String, Object> context) {
		Object result = null;
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
		for (String key : context.keySet()) {
			jsEngine.put(key, context.get(key));
		}
		try {
			result = jsEngine.eval(expression);
		} catch (ScriptException e) {
			LOGGER.error("Error in evalutate()", e);
		}
		return result;
	}

}
