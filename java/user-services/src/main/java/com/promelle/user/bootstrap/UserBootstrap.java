package com.promelle.user.bootstrap;

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
import com.promelle.user.bundle.UserMigrationsBundle;
import com.promelle.user.config.UserBootstrapConfiguration;
import com.promelle.user.dao.AccountDao;
import com.promelle.user.dao.DeviceDao;
import com.promelle.user.dao.GroupDao;
import com.promelle.user.dao.GroupRoleDao;
import com.promelle.user.dao.PermissionDao;
import com.promelle.user.dao.RoleDao;
import com.promelle.user.dao.RolePermissionDao;
import com.promelle.user.dao.SchoolDao;
import com.promelle.user.dao.UserDao;
import com.promelle.user.dao.UserGroupDao;
import com.promelle.user.dao.UserPermissionDao;
import com.promelle.user.dao.UserRoleDao;
import com.promelle.user.handler.UserMessageHandler;
import com.promelle.user.listener.UserMessageListener;
import com.promelle.user.management.AccountManagement;
import com.promelle.user.management.GroupManagement;
import com.promelle.user.management.PermissionManagement;
import com.promelle.user.management.RoleManagement;
import com.promelle.user.management.UserManagement;
import com.promelle.user.management.impl.DefaultAccountManagement;
import com.promelle.user.management.impl.DefaultGroupManagement;
import com.promelle.user.management.impl.DefaultPermissionManagement;
import com.promelle.user.management.impl.DefaultRoleManagement;
import com.promelle.user.management.impl.DefaultUserManagement;
import com.promelle.user.service.impl.DefaultAccountService;
import com.promelle.user.service.impl.DefaultGroupService;
import com.promelle.user.service.impl.DefaultPermissionService;
import com.promelle.user.service.impl.DefaultRoleService;
import com.promelle.user.service.impl.DefaultUserService;

/**
 * This class is intended for starting the application. It registers service, ui
 * & db migration modules.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class UserBootstrap
		extends
		MessageEnabledBootstrap<UserBootstrapConfiguration, UserMessageHandler, UserMessageListener> {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserBootstrap.class.getSimpleName());
	public static final String ID = "user";

	private PermissionManagement permissionManagement;
	private RoleManagement roleManagement;
	private GroupManagement groupManagement;
	private UserManagement userManagement;
	private AccountManagement accountManagement;

	private UserMessageListener userMessageListener;

	public static void main(String[] args) throws AbstractException {
		try {
			(new UserBootstrap()).run(args);
		} catch (Exception e) {
			throw new AbstractException(e);
		}
	}

	@Override
	public void initializeConfig(Bootstrap<UserBootstrapConfiguration> bootstrap) {
		bootstrap.addBundle(new AssetsBundle("/ui", "/ui/user/manage/",
				"index.html", "manage"));
		bootstrap.addBundle(new UserMigrationsBundle());
	}

	@Override
	public void runBootstrap(UserBootstrapConfiguration configuration,
			Environment environment) throws AbstractException {
		LOGGER.info("runBootstrap() called");
		// Setup DAOs
		DBIFactory dbInstanceFactory = new DBIFactory();
		DBI dbi = dbInstanceFactory.build(environment,
				configuration.getDataSourceFactory(), "dataSourceFactory");

		PermissionDao permissionDao = dbi.onDemand(PermissionDao.class);
		RoleDao roleDao = dbi.onDemand(RoleDao.class);
		RolePermissionDao rolePermissionDao = dbi
				.onDemand(RolePermissionDao.class);
		GroupDao groupDao = dbi.onDemand(GroupDao.class);
		GroupRoleDao groupRoleDao = dbi.onDemand(GroupRoleDao.class);
		UserDao userDao = dbi.onDemand(UserDao.class);
		UserRoleDao userRoleDao = dbi.onDemand(UserRoleDao.class);
		UserPermissionDao userPermissionDao = dbi
				.onDemand(UserPermissionDao.class);
		UserGroupDao userGroupDao = dbi.onDemand(UserGroupDao.class);
		AccountDao accountDao = dbi.onDemand(AccountDao.class);
		DeviceDao deviceDao = dbi.onDemand(DeviceDao.class);
		SchoolDao schoolDao = dbi.onDemand(SchoolDao.class);

		permissionManagement = new DefaultPermissionManagement(permissionDao);
		roleManagement = new DefaultRoleManagement(roleDao, rolePermissionDao,
				permissionDao);
		groupManagement = new DefaultGroupManagement(roleDao, groupDao,
				groupRoleDao);
		userManagement = new DefaultUserManagement(permissionDao, roleDao,
				groupDao, rolePermissionDao, userDao, userRoleDao,
				userPermissionDao, userGroupDao, groupRoleDao, deviceDao,
				schoolDao, getMessageProducer());
		accountManagement = new DefaultAccountManagement(accountDao,
				getMessageProducer());

		environment.jersey().register(MultiPartFeature.class);

		// Register Services
		environment.jersey().register(
				new DefaultPermissionService(permissionManagement));
		environment.jersey().register(new DefaultRoleService(roleManagement));
		environment.jersey().register(
				new DefaultGroupService(groupManagement, getMessageProducer()));
		environment.jersey().register(
				new DefaultUserService(userManagement, new S3Uploader()));
		environment.jersey().register(
				new DefaultAccountService(accountManagement, roleManagement,
						userManagement));
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public UserMessageListener getMessageListener(
			KafkaConfiguration configuration) throws AbstractException {
		if (userMessageListener == null) {
			userMessageListener = new UserMessageListener(userManagement,
					roleManagement, getMessageProducer());
			userMessageListener.register(configuration.getHandlers());
		}
		return userMessageListener;
	}

}
