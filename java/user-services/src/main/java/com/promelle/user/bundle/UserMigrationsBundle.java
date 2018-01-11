package com.promelle.user.bundle;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;

import com.promelle.user.config.UserBootstrapConfiguration;

/**
 * This interface is intended for providing interactions between dao classes
 * related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class UserMigrationsBundle extends
		MigrationsBundle<UserBootstrapConfiguration> {

	@Override
	public DataSourceFactory getDataSourceFactory(
			UserBootstrapConfiguration configuration) {
		return configuration.getDataSourceFactory();
	}

}
