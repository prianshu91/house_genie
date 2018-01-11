package com.promelle.ui.bootstrap;

import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import com.promelle.common.bootstrap.AbstractBootstrap;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.ui.config.UIBootstrapConfiguration;
import com.promelle.ui.dao.LoginSessionDao;
import com.promelle.ui.discovery.DiscoveryManager;
import com.promelle.ui.filter.AuthenticationFilter;
import com.promelle.ui.filter.AuthorizationFilter;
import com.promelle.ui.filter.FacadeFilter;
import com.promelle.ui.filter.RequestInitializingFilter;
import com.promelle.ui.service.TestService;

/**
 * Hello world!
 *
 */

/**
 * This class is intended for starting the application. It registers ui module.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class UIBootstrap extends AbstractBootstrap<UIBootstrapConfiguration> {

	public static final String ID = "ui";

	public static void main(String[] args) throws Exception {
		(new UIBootstrap()).run(args);
	}

	@Override
	public void initializeConfig(Bootstrap<UIBootstrapConfiguration> bootstrap) {
		bootstrap.addBundle(new AssetsBundle("/promelle", "/ui/promelle/",
				"index.html", "promelle"));
	}

	@Override
	public void runBootstrap(UIBootstrapConfiguration configuration,
			Environment environment) throws Exception {
		MongoManager mongoManager = new MongoManager(configuration.getMongo());
		mongoManager.start();

		LoginSessionDao loginSessionDao = new LoginSessionDao(mongoManager);
		DiscoveryManager discoveryManager = new DiscoveryManager(
				configuration.getRegistry());

		environment
				.servlets()
				.addFilter("RequestInitializingFilter",
						new RequestInitializingFilter())
				.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class),
						true, "/service/*");

		environment
				.servlets()
				.addFilter(
						"AuthorizationFilter",
						new AuthorizationFilter(loginSessionDao, configuration
								.getSessionTimeOutMillis()))
				.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class),
						true, "/service/*");

		environment
				.servlets()
				.addFilter(
						"AuthenticationFilter",
						new AuthenticationFilter(discoveryManager,
								loginSessionDao))
				.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class),
						true, "/service/user/login");

		environment
				.servlets()
				.addFilter("FacadeFilter", new FacadeFilter(discoveryManager))
				.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class),
						true, "/*");

		environment.jersey().register(new TestService());
	}

	@Override
	public String getId() {
		return ID;
	}

}
