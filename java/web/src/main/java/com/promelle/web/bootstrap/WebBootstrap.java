package com.promelle.web.bootstrap;

import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.promelle.common.bootstrap.AbstractBootstrap;
import com.promelle.web.config.WebBootstrapConfiguration;

public class WebBootstrap extends AbstractBootstrap<WebBootstrapConfiguration> {

	public static final String ID = "web";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WebBootstrap.class.getSimpleName());

	public static void main(String[] args) throws Exception {
		(new WebBootstrap()).run(args);
	}

	@Override
	public void initializeConfig(Bootstrap<WebBootstrapConfiguration> bootstrap) {
		bootstrap.addBundle(new AssetsBundle("/promelle", "/web/promelle/",
				"index.html", "promelle"));
	}

	@Override
	public void runBootstrap(WebBootstrapConfiguration configuration,
			Environment environment) throws Exception {
		LOGGER.info("runBootstrap() called");
	}

	@Override
	public String getId() {
		return ID;
	}

}
