package com.promelle.communication.dao.mapper;

import java.io.IOException;
import java.sql.ResultSet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.promelle.address.Address;
import com.promelle.communication.dto.Product;
import com.promelle.exception.TransformationException;
import com.promelle.filter.SearchFilter;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.dao.mapper.MongoMapper;
import com.promelle.mongo.utils.MongoUtils;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link Product}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ProductMapper implements MongoMapper<Product, SearchFilter> {
	private static final String TITLE = "title";
	public static final String OWNER_ID = "ownerId";
	public static final String OWNER_NAME = "ownerName";
	public static final String DRESS_LOCATION = "dressLocation";

	@Override
	public BasicDBObject convertToDao(Product dto) {
		BasicDBObject obj = new BasicDBObject();
		if (dto == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, dto.getId());
		MongoUtils.appendToObj(obj, TITLE, dto.getTitle());
		MongoUtils.appendToObj(obj, OWNER_ID, dto.getOwnerId());
		MongoUtils.appendToObj(obj, OWNER_NAME, dto.getOwnerName());
		try {
			if (dto.getDressLocation() != null) {
				MongoUtils.appendToObj(obj, DRESS_LOCATION, new BasicDBObject(
						dto.getDressLocation().toMap()));
			}
		} catch (TransformationException e) {
			e.printStackTrace();
		}
		return obj;
	}

	@Override
	public BasicDBObject convertToDao(SearchFilter filter) {
		BasicDBObject obj = new BasicDBObject();
		if (filter == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, filter.getId());
		return obj;
	}

	@Override
	public Product convertToDto(BasicDBObject obj) {
		Product dto = new Product();
		dto.setId(obj.getString(AbstractMongoDao.MONGO_ID));
		dto.setTitle(obj.getString(TITLE));
		dto.setOwnerId(obj.getString(OWNER_ID));
		dto.setOwnerName(obj.getString(OWNER_NAME));

		if (obj.containsField(DRESS_LOCATION)) {
			try {
				dto.setDressLocation(new ObjectMapper().readValue(
						obj.get(DRESS_LOCATION).toString(), Address.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dto;
	}

}
