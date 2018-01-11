package com.promelle.product.bootstrap;

import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.promelle.amazon.upload.S3Uploader;
import com.promelle.common.bootstrap.MessageEnabledBootstrap;
import com.promelle.common.config.KafkaConfiguration;
import com.promelle.exception.AbstractException;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.payment.stripe.handler.StripePaymentHandler;
import com.promelle.product.config.ProductBootstrapConfiguration;
import com.promelle.product.dao.CartDao;
import com.promelle.product.dao.ChargeHistoryDao;
import com.promelle.product.dao.DocumentDao;
import com.promelle.product.dao.EarningDao;
import com.promelle.product.dao.FavoriteDao;
import com.promelle.product.dao.InvoiceDao;
import com.promelle.product.dao.OrderAddressDao;
import com.promelle.product.dao.OrderDao;
import com.promelle.product.dao.OrderItemDao;
import com.promelle.product.dao.OrderMessageConversationDao;
import com.promelle.product.dao.OrderMessageDao;
import com.promelle.product.dao.PaymentsDao;
import com.promelle.product.dao.ProductDao;
import com.promelle.product.dao.ProductMessageConversationDao;
import com.promelle.product.dao.ProductMessageDao;
import com.promelle.product.dao.ReviewDao;
import com.promelle.product.dao.ShipmentDao;
import com.promelle.product.dao.UserDao;
import com.promelle.product.dto.Services;
import com.promelle.product.handler.ProductMessageHandler;
import com.promelle.product.listener.ProductMessageListener;
import com.promelle.product.management.OrderManagement;
import com.promelle.product.management.ProductManagement;
import com.promelle.product.management.impl.DefaultOrderManagement;
import com.promelle.product.management.impl.DefaultProductManagement;
import com.promelle.product.service.impl.DefaultMessageService;
import com.promelle.product.service.impl.DefaultOrderMessageService;
import com.promelle.product.service.impl.DefaultProductService;
import com.promelle.product.service.impl.DefaultReviewService;

/**
 * This class is intended for starting the application. It registers service, ui
 * & db migration modules.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ProductBootstrap
		extends
		MessageEnabledBootstrap<ProductBootstrapConfiguration, ProductMessageHandler, ProductMessageListener> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProductBootstrap.class.getSimpleName());
	public static final String ID = "product";
	private ProductManagement productManagement;
	private ProductMessageListener productMessageListener;
	private OrderManagement orderManagement;

	public static void main(String[] args) throws AbstractException {
		try {
			(new ProductBootstrap()).run(args);
		} catch (Exception e) {
			throw new AbstractException(e);
		}
	}

	@Override
	public void initializeConfig(
			Bootstrap<ProductBootstrapConfiguration> bootstrap) {
		bootstrap.addBundle(new AssetsBundle("/ui", "/ui/product/manage/",
				"index.html", "manage"));
	}

	@Override
	public void runBootstrap(ProductBootstrapConfiguration configuration,
			Environment environment) throws AbstractException {
		LOGGER.info("runBootstrap() called");

		// Setup DAOs
		MongoManager mongoManager = new MongoManager(configuration.getMongo());
		mongoManager.start();

		DBIFactory dbInstanceFactory = new DBIFactory();
		DBI dbi = dbInstanceFactory.build(environment,
				configuration.getDataSourceFactory(), "dataSourceFactory");
		Services services = configuration.getServices();

		productManagement = new DefaultProductManagement(new ProductDao(
				mongoManager), new CartDao(mongoManager), new OrderItemDao(
				mongoManager), new OrderDao(mongoManager), new InvoiceDao(
				mongoManager), new ReviewDao(mongoManager),
				new ProductMessageDao(mongoManager), new OrderAddressDao(
						mongoManager), new ShipmentDao(mongoManager),
				new EarningDao(mongoManager),
				new ChargeHistoryDao(mongoManager),
				new ProductMessageConversationDao(mongoManager),
				new StripePaymentHandler(), getMessageProducer(),
				new FavoriteDao(mongoManager), dbi.onDemand(PaymentsDao.class),
				dbi.onDemand(UserDao.class),
				new DocumentDao(mongoManager),
				services);

		orderManagement = new DefaultOrderManagement(new OrderMessageDao(
				mongoManager), new OrderMessageConversationDao(mongoManager),
				getMessageProducer());

		environment.jersey().register(MultiPartFeature.class);

		// Register Services
		environment.jersey().register(
				new DefaultMessageService(productManagement));
		environment.jersey().register(
				new DefaultProductService(productManagement, new S3Uploader()));
		environment.jersey().register(
				new DefaultReviewService(productManagement));
		environment.jersey().register(
				new DefaultOrderMessageService(orderManagement));
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public ProductMessageListener getMessageListener(
			KafkaConfiguration configuration) throws AbstractException {
		if (productMessageListener == null) {
			productMessageListener = new ProductMessageListener(
					productManagement, getMessageProducer());
			productMessageListener.register(configuration.getHandlers());
		}
		return productMessageListener;
	}

}
