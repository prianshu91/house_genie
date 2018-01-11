package com.promelle.product.management.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.promelle.common.config.AbstractConfiguration;
import com.promelle.exception.AbstractException;
import com.promelle.paging.Paging;
import com.promelle.product.dao.OrderMessageConversationDao;
import com.promelle.product.dao.OrderMessageDao;
import com.promelle.product.dto.OrderMessage;
import com.promelle.product.filter.OrderMessageFilter;
import com.promelle.product.management.OrderManagement;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.PagedList;
import com.promelle.sort.SortRules;
import com.promelle.topic.message.TopicMessage;
import com.promelle.topic.producer.MessageProducer;
import com.promelle.utils.UUIDUtils;

/**
 * This implementation is intended for providing interactions between
 * orderMessage classes related to manage.
 * 
 * @author Satish Sharma
 * @version 1.0
 */
public class DefaultOrderManagement implements OrderManagement {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DefaultOrderManagement.class.getSimpleName());

	private OrderMessageDao messageDao;
	private OrderMessageConversationDao messageConversationDao;
	private MessageProducer messageProducer;

	private static final String ORDER_MESSAGE_CREATED = AbstractConfiguration
			.getProperty("order.message.created");

	public DefaultOrderManagement(OrderMessageDao messageDao,
			OrderMessageConversationDao messageConversationDao,
			MessageProducer messageProducer) {
		super();
		this.messageDao = messageDao;
		this.messageConversationDao = messageConversationDao;
		this.messageProducer = messageProducer;
	}

	@Override
	public String sendMessage(AbstractRequestTracker requestTracker,
			OrderMessage message) throws AbstractException {
		if (StringUtils.isBlank(message.getId())) {
			message.setId(UUIDUtils.getUUID());
		}
		messageDao.save(requestTracker, message);
		TopicMessage msg = new TopicMessage(requestTracker,
				ORDER_MESSAGE_CREATED);
		msg.setData(message.toString());
		messageProducer.sendMessage(msg);
		LOGGER.info("Message Send ==> " + message.toString());
		return message.getId();
	}

	@Override
	public PagedList<OrderMessage> listMessages(
			AbstractRequestTracker requestTracker, OrderMessageFilter filter,
			Paging paging, SortRules sortRules) throws AbstractException {
		if (StringUtils.isNotBlank(filter.getReceiverId())
				&& StringUtils.isBlank(filter.getSenderId())) {
			LOGGER.info("Return list of User inbox messages");
			return messageDao.getUserInbox(filter.getReceiverId());
		}
		return messageDao.list(filter, paging, sortRules);
	}

	@Override
	public int deleteMessage(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {

		OrderMessage message = messageDao.findById(id);
		int count = messageDao.softDeleteMessage(requestTracker, id,
				message.getSenderId());
		count += messageDao.softDeleteMessage(requestTracker, id,
				message.getReceiverId());
		LOGGER.info("Message Deleted => " + count);
		return count > 0 ? 1 : 0;
	}

	@Override
	public int deleteMessage(AbstractRequestTracker requestTracker, String id,
			String userId) throws AbstractException {
		return messageDao.softDeleteMessage(requestTracker, id, userId);
	}

	@Override
	public String getOrderConversationId(AbstractRequestTracker requestTracker,
			String senderId, String receiverId, String orderId)
			throws AbstractException {
		return messageConversationDao.getConversationId(requestTracker,
				senderId, receiverId, orderId);
	}

	@Override
	public boolean readOrderConveration(AbstractRequestTracker requestTracker,
			String conversationId, String userId) throws AbstractException {
		OrderMessageFilter filter = new OrderMessageFilter();
		filter.setConversationId(conversationId);
		filter.setReceiverId(userId);
		OrderMessage message = new OrderMessage();
		message.setRead(true);
		return messageDao.update(requestTracker, filter, message) > 0;
	}

	@Override
	public int deleteOrderConveration(AbstractRequestTracker requestTracker,
			String conversationId, String userId) throws AbstractException {

		return messageDao.softDeleteConversation(requestTracker,
				conversationId, userId);
	}
}
