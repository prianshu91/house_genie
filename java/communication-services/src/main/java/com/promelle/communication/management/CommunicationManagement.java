package com.promelle.communication.management;

import java.util.List;
import java.util.Map;

import com.promelle.communication.dto.Device;
import com.promelle.communication.dto.Message;
import com.promelle.communication.dto.Product;
import com.promelle.communication.dto.User;
import com.promelle.communication.filter.DeviceFilter;
import com.promelle.communication.filter.MessageFilter;
import com.promelle.exception.AbstractException;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.PagedList;
import com.promelle.sort.SortRules;

/**
 * This interface is intended for providing interactions between dao classes
 * related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public interface CommunicationManagement {

	String sendMessage(AbstractRequestTracker requestTracker, Message message)
			throws AbstractException;

	PagedList<Message> listMessages(AbstractRequestTracker requestTracker,
			MessageFilter filter, Paging paging, SortRules sortRules)
			throws AbstractException;

	int deleteMessage(AbstractRequestTracker requestTracker, String id)
			throws AbstractException;

	User findUserById(AbstractRequestTracker requestTracker, String userId);

	Map<String, String> getUsers();

	Product findProductById(AbstractRequestTracker requestTracker,
			String productId);

	PagedList<Device> listDevices(AbstractRequestTracker requestTracker,
			DeviceFilter filter) throws AbstractException;

	List<String> getDevices(AbstractRequestTracker requestTracker,
			DeviceFilter filter) throws AbstractException;

	String broadcastEmail(String from, String bcc, String subject, String body,
			AbstractRequestTracker requestTracker) throws AbstractException;

}
