package com.promelle.communication.management.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.promelle.common.config.AbstractConfiguration;
import com.promelle.communication.dao.DeviceDao;
import com.promelle.communication.dao.MessageDao;
import com.promelle.communication.dao.ProductDao;
import com.promelle.communication.dao.UserDao;
import com.promelle.communication.dao.mapper.UserMapper;
import com.promelle.communication.dto.BroadcastEmail;
import com.promelle.communication.dto.Device;
import com.promelle.communication.dto.Message;
import com.promelle.communication.dto.Product;
import com.promelle.communication.dto.User;
import com.promelle.communication.filter.DeviceFilter;
import com.promelle.communication.filter.MessageFilter;
import com.promelle.communication.management.CommunicationManagement;
import com.promelle.exception.AbstractException;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.PagedList;
import com.promelle.sort.SortRules;
import com.promelle.topic.message.TopicMessage;
import com.promelle.topic.producer.MessageProducer;
import com.promelle.utils.JsonUtils;
import com.promelle.utils.UUIDUtils;

/**
 * This interface is intended for providing interactions between dao classes
 * related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class DefaultCommunicationManagement implements CommunicationManagement {
	private MessageDao messageDao;
	private UserDao userDao;
	private ProductDao productDao;
	private DeviceDao deviceDao;
	private MessageProducer messageProducer;

	private static final String MESSAGE_CREATED = AbstractConfiguration
			.getProperty("message.created");
	private static final String MESSAGE_DELETED = AbstractConfiguration
			.getProperty("message.deleted");
	private static final String BROADCAST_EMAIL = AbstractConfiguration
			.getProperty("broadcast.email");

	public DefaultCommunicationManagement(MessageDao messageDao,
			UserDao userDao, ProductDao productDao, DeviceDao deviceDao,
			MessageProducer messageProducer) {
		super();
		this.messageDao = messageDao;
		this.userDao = userDao;
		this.productDao = productDao;
		this.deviceDao = deviceDao;
		this.messageProducer = messageProducer;
	}

	@Override
	public String sendMessage(AbstractRequestTracker requestTracker,
			Message message) throws AbstractException {
		if (StringUtils.isBlank(message.getId())) {
			message.setId(UUIDUtils.getUUID());
		}
		messageDao.save(requestTracker, message);
		TopicMessage msg = new TopicMessage(requestTracker, MESSAGE_CREATED);
		msg.setData(message.toString());
		messageProducer.sendMessage(msg);
		return message.getId();
	}

	@Override
	public PagedList<Message> listMessages(
			AbstractRequestTracker requestTracker, MessageFilter filter,
			Paging paging, SortRules sortRules) throws AbstractException {
		if (StringUtils.isNotBlank(filter.getReceiverId())
				&& StringUtils.isBlank(filter.getSenderId())) {
			return messageDao.getUserInbox(filter.getReceiverId());
		}
		return messageDao.list(filter, paging, sortRules);
	}

	@Override
	public int deleteMessage(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		if (messageDao.softDelete(requestTracker, id) > 0) {
			Map<String, Object> map = new HashMap<>();
			MessageFilter filter = new MessageFilter();
			filter.setId(id);
			map.put(TopicMessage.F_FILTER, filter);
			TopicMessage msg = new TopicMessage(requestTracker, MESSAGE_DELETED);
			msg.setData(JsonUtils.getJsonString(map));
			messageProducer.sendMessage(msg);
			return 1;
		}
		return 0;
	}

	@Override
	public Map<String, String> getUsers() {
		return messageDao.getUsers();
	}

	@Override
	public User findUserById(AbstractRequestTracker requestTracker,
			String userId) {
		return userDao.findById(requestTracker, userId);
	}

	@Override
	public Product findProductById(AbstractRequestTracker requestTracker,
			String productId) {
		return productDao.findById(productId);
	}

	@Override
	public PagedList<Device> listDevices(AbstractRequestTracker requestTracker,
			DeviceFilter filter) throws AbstractException {
		return deviceDao.list(requestTracker, filter);
	}

	@Override
	public List<String> getDevices(AbstractRequestTracker requestTracker,
			DeviceFilter filter) throws AbstractException {
		return deviceDao.getDevices(requestTracker, filter);
	}

	@Override
	public String broadcastEmail(String from, String bcc, String subject,
			String body, AbstractRequestTracker requestTracker)
			throws AbstractException {

		if (StringUtils.isBlank(bcc))
			throw new AbstractException("invalid.receipent.list")
					.setCustomMessage("Invalid BCC receipent list!");

		if (StringUtils.isBlank(subject))
			throw new AbstractException("subject.required")
					.setCustomMessage("Subject Required!");

		String[] bccEmailList = bcc.split(",");
		BroadcastEmail message = new BroadcastEmail();
		message.setFrom(from);
		message.setSubject(subject);
		message.setBody(body);

		for (String bccMail : bccEmailList) {
			User user = userDao.findOne(new UserMapper().getTableName(),
					"WHERE email=\"" + bccMail + "\"");

			if (user != null) {
				message.setTo(bccMail);
				message.setUserName(user.getUsername());

				// send broadcast email to all listed users
				TopicMessage msg = new TopicMessage(requestTracker,
						BROADCAST_EMAIL);
				msg.setData(message.toString());
				messageProducer.sendMessage(msg);
			}
		}

		return "Message sent successfully!";
	}
}
