package com.promelle.mongo.manager;

import io.dropwizard.lifecycle.Managed;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.promelle.exception.AbstractException;
import com.promelle.mongo.config.MongoConfiguration;
import com.promelle.utils.ListUtils;

/**
 * This class is responsible for managing mongo.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class MongoManager implements Managed {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MongoManager.class);
	private MongoClient client = null;
	private MongoConfiguration configuration = null;

	public MongoManager(MongoConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void start() throws AbstractException {
		try {
			List<ServerAddress> serverList = new ArrayList<ServerAddress>();
			List<String> list = ListUtils.splitStringIntoTrimmedList(
					configuration.getServerList(), ",");
			for (String address : list) {
				String[] addrArray = address.split(":");
				String host = addrArray[0].trim();
				Integer port = 27017;
				if (addrArray.length > 1) {
					try {
						port = Integer.parseInt(addrArray[1].trim());
					} catch (Exception e) {
						LOGGER.warn(String.format(
								"Invalid port no for %s. Switching to default",
								address), e);
					}
				}
				serverList.add(new ServerAddress(host, port));
			}
			client = new MongoClient(serverList);
			client.setReadPreference(ReadPreference.secondaryPreferred());
		} catch (UnknownHostException e) {
			throw new AbstractException(e);
		}
	}

	@Override
	public void stop() throws AbstractException {
		client.close();
	}

	public MongoClient getClient() {
		return client;
	}

	public MongoConfiguration getConfiguration() {
		return configuration;
	}

}
