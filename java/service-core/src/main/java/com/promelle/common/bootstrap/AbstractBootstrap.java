package com.promelle.common.bootstrap;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.codahale.metrics.servlets.MetricsServlet;
import com.promelle.common.config.AbstractBootstrapConfiguration;

/**
 * This class is intended for starting the application. It registers service, ui
 * & db migration modules.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class AbstractBootstrap<T extends AbstractBootstrapConfiguration>
		extends Application<T> {

	public abstract void initializeConfig(Bootstrap<T> bootstrap);

	public abstract void runBootstrap(T configuration, Environment environment)
			throws Exception;

	public abstract String getId();

	@Override
	public void initialize(Bootstrap<T> bootstrap) {
		// bootstrap.addBundle(new EurekaClientBundle());
		initializeConfig(bootstrap);
	}

	@Override
	public void run(T configuration, Environment environment) throws Exception {
		environment
				.servlets()
				.addServlet("metrics-servlet",
						new MetricsServlet(environment.metrics()))
				.addMapping("/metrics");
		runBootstrap(configuration, environment);
		// add custom exception mapper
		// environment
		// .jersey()
		// .getResourceConfig()
		// .getSingletons()
		// .removeIf(
		// singleton -> singleton instanceof
		// ConstraintViolationExceptionMapper);
		// environment.jersey().register(new ExceptionMapper());
	}

}
