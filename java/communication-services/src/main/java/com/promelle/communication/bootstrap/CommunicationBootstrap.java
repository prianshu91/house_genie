package com.promelle.communication.bootstrap;

import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.skife.jdbi.v2.DBI;

import com.promelle.common.bootstrap.MessageEnabledBootstrap;
import com.promelle.common.config.KafkaConfiguration;
import com.promelle.communication.config.CommunicationBootstrapConfiguration;
import com.promelle.communication.dao.DeviceDao;
import com.promelle.communication.dao.MessageDao;
import com.promelle.communication.dao.ProductDao;
import com.promelle.communication.dao.UserDao;
import com.promelle.communication.handler.CommunicationMessageHandler;
import com.promelle.communication.listener.CommunicationMessageListener;
import com.promelle.communication.management.CommunicationManagement;
import com.promelle.communication.management.impl.DefaultCommunicationManagement;
import com.promelle.communication.sender.EmailSender;
import com.promelle.communication.sender.NotificationSender;
import com.promelle.communication.sender.SmsSender;
import com.promelle.communication.service.impl.DefaultEmailService;
import com.promelle.communication.service.impl.DefaultMessageService;
import com.promelle.communication.service.impl.DefaultNotificationService;
import com.promelle.exception.AbstractException;
import com.promelle.mongo.manager.MongoManager;

/**
 * This class is intended for starting the application. It registers service, ui
 * & db migration modules.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class CommunicationBootstrap
		extends
		MessageEnabledBootstrap<CommunicationBootstrapConfiguration, CommunicationMessageHandler, CommunicationMessageListener> {

	public static final String ID = "communication";
	private CommunicationMessageListener communicationMessageListener;
	private EmailSender emailSender;
	private SmsSender smsSender;
	private NotificationSender notificationSender;
	private CommunicationManagement communicationManagement;

	public static void main(String[] args) throws AbstractException {
		try {
			(new CommunicationBootstrap()).run(args);
		} catch (Exception e) {
			throw new AbstractException(e);
		}
	}

	@Override
	public void initializeConfig(
			Bootstrap<CommunicationBootstrapConfiguration> bootstrap) {
		bootstrap.addBundle(new AssetsBundle("/ui",
				"/ui/communication/manage/", "index.html", "manage"));
	}

	@Override
	public void runBootstrap(CommunicationBootstrapConfiguration configuration,
			Environment environment) throws AbstractException {
		MongoManager mongoManager = new MongoManager(configuration.getMongo());
		mongoManager.start();

		DBIFactory dbInstanceFactory = new DBIFactory();
		DBI dbi = dbInstanceFactory.build(environment,
				configuration.getDataSourceFactory(), "dataSourceFactory");

		communicationManagement = new DefaultCommunicationManagement(
				new MessageDao(mongoManager), dbi.onDemand(UserDao.class),
				new ProductDao(mongoManager), dbi.onDemand(DeviceDao.class),
				getMessageProducer());

		emailSender = new EmailSender(configuration.getSmtpCredentials());
		smsSender = new SmsSender(configuration.getSmsCredentials());
		notificationSender = new NotificationSender(
				configuration.getApnsCredentials());

		// Register Services
		environment.jersey().register(
				new DefaultMessageService(communicationManagement));
		environment.jersey().register(
				new DefaultEmailService(emailSender, communicationManagement));
		environment.jersey().register(
				new DefaultNotificationService(notificationSender,
						communicationManagement));
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public CommunicationMessageListener getMessageListener(
			KafkaConfiguration configuration) throws AbstractException {
		if (communicationMessageListener == null) {
			communicationMessageListener = new CommunicationMessageListener(
					getMessageProducer(), emailSender, smsSender,
					communicationManagement, notificationSender);
			communicationMessageListener.register(configuration.getHandlers());
		}
		return communicationMessageListener;
	}

}
