package com.promelle.topic.message;

import com.promelle.object.AbstractObject;
import com.promelle.request.tracker.AbstractRequestTracker;

/**
 * This class is responsible for holding message.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class TopicMessage extends AbstractObject {
	private static final long serialVersionUID = 1458087067327178361L;

	private AbstractRequestTracker requestTracker;
	private String type;
	private String data;

	public static final String F_DATA = "data";
	public static final String F_FILTER = "filter";

	public TopicMessage() {
		super();
	}

	public TopicMessage(AbstractRequestTracker requestTracker, String type) {
		this.requestTracker = requestTracker;
		this.type = type;
	}

	public AbstractRequestTracker getRequestTracker() {
		return requestTracker;
	}

	public void setRequestTracker(AbstractRequestTracker requestTracker) {
		this.requestTracker = requestTracker;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getPortalName() {
		return requestTracker != null ? requestTracker.getPortalName() : null;
	}

}
