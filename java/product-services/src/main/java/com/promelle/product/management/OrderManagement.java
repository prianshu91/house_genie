package com.promelle.product.management;

import com.promelle.exception.AbstractException;
import com.promelle.paging.Paging;
import com.promelle.product.dto.OrderMessage;
import com.promelle.product.filter.OrderMessageFilter;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.PagedList;
import com.promelle.sort.SortRules;

/**
 * This interface is intended for providing interactions between dao classes
 * related to manage.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
public interface OrderManagement {

	String sendMessage(AbstractRequestTracker requestTracker,
			OrderMessage message) throws AbstractException;

	PagedList<OrderMessage> listMessages(AbstractRequestTracker requestTracker,
			OrderMessageFilter filter, Paging paging, SortRules sortRules)
			throws AbstractException;

	int deleteMessage(AbstractRequestTracker requestTracker, String id,
			String userId) throws AbstractException;

	int deleteMessage(AbstractRequestTracker requestTracker, String id)
			throws AbstractException;

	String getOrderConversationId(AbstractRequestTracker requestTracker,
			String senderId, String receiverId, String orderId)
			throws AbstractException;

	boolean readOrderConveration(AbstractRequestTracker requestTracker,
			String conversationId, String userId) throws AbstractException;

	int deleteOrderConveration(AbstractRequestTracker requestTracker,
			String conversationId, String userId) throws AbstractException;

}
