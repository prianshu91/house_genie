package com.promelle.common.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import com.promelle.exception.AbstractException;
import com.promelle.object.AbstractObject;
import com.promelle.response.CommonResponse;
import com.promelle.response.PagedList;
import com.promelle.response.Pagination;

/**
 * 
 * This class is intended for providing base services.
 * 
 * @author Hemant Kumar
 * @version 1.0
 * 
 */
public abstract class BaseService {

	public Response onSuccess(Object data, Pagination pagination) {
		return Response.ok(
				new CommonResponse().setData(data).setPaging(pagination)
						.toString()).build();
	}

	public Response onSuccess(Object data) {
		if (data instanceof PagedList) {
			@SuppressWarnings("rawtypes")
			PagedList pagedList = (PagedList) data;
			return Response.ok(
					new CommonResponse().setData(pagedList.getObjects())
							.setPaging(pagedList.getPagination()).toString())
					.build();
		}
		return Response.ok(new CommonResponse().setData(data).toString())
				.build();
	}

	public Response onError(String errorCode, List<String> errors) {
		return Response.ok(
				new CommonResponse().setErrorCode(errorCode).setErrors(errors)
						.toString()).build();
	}

	public Response onError(String errorCode) {
		return Response.ok(
				new CommonResponse().setErrorCode(errorCode).toString())
				.build();
	}

	public Response onError(AbstractException e) {
		return Response.ok(
				new CommonResponse()
						.setErrorCode(
								e.getCode() != null ? e.getCode()
										: "Something went wrong!")
						.setErrors(
								e.getCustomMessage() == null ? null : Arrays
										.asList(e.getCustomMessage()))
						.toString()).build();
	}

	public <T extends AbstractObject> T load(HttpServletRequest request,
			Class<T> clazz) {
		T instance = null;
		try {
			instance = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		Enumeration<String> headers = request.getHeaderNames();
		while (headers.hasMoreElements()) {
			try {
				String header = headers.nextElement();
				if ("baseurl".equalsIgnoreCase(header)) {
					header = "baseUrl";
				}
				setField(clazz.getDeclaredField(header), instance,
						request.getHeader(header));
			} catch (Exception e) {
			}
		}
		for (Object param : request.getParameterMap().keySet()) {
			try {
				setField(clazz.getDeclaredField(String.valueOf(param)),
						instance, request.getParameter(String.valueOf(param)));
			} catch (Exception e) {
			}
		}
		try {
			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					request.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			String data = buffer.toString();
			for (String paramStr : data.split("&")) {
				String paramName = paramStr.substring(0, paramStr.indexOf("="));
				String paramValue = paramStr
						.substring(paramStr.indexOf("=") + 1);
				try {
					setField(clazz.getDeclaredField(paramName), instance,
							paramValue);
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
		}
		return instance;
	}

	@SuppressWarnings("rawtypes")
	private <T extends AbstractObject> void setField(Field field, T obj,
			String value) {
		try {
			field.setAccessible(true);
			Class clazz = field.getType();
			if (Boolean.class == clazz) {
				field.set(obj, Boolean.valueOf(value));
			} else if (Integer.class == clazz) {
				field.set(obj, Integer.valueOf(value));
			} else if (Long.class == clazz) {
				field.set(obj, Long.valueOf(value));
			} else {
				field.set(obj, value);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
		}
	}

}
