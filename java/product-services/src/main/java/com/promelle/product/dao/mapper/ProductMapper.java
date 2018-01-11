package com.promelle.product.dao.mapper;

import static com.promelle.dto.AbstractAuditDTO.CREATED_BY;
import static com.promelle.dto.AbstractAuditDTO.CREATED_ON;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_BY;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_ON;
import static com.promelle.dto.AbstractAuditDTO.STATUS;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.promelle.address.Address;
import com.promelle.constants.Punctuation;
import com.promelle.exception.TransformationException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.dao.mapper.MongoMapper;
import com.promelle.mongo.utils.MongoUtils;
import com.promelle.product.dto.Product;
import com.promelle.product.filter.ProductFilter;

/**
 * This class is intended for mapping a row of {@link ResultSet} to
 * {@link Product}.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ProductMapper implements MongoMapper<Product, ProductFilter> {
	private static final String TITLE = "title";
	private static final String DESCRIPTION = "description";
	private static final String OCCASION = "occasion";
	private static final String CATEGORY = "category";
	private static final String LENGTH = "length";
	private static final String TYPE = "type";
	private static final String SIZE = "size";
	private static final String DRESS_LENGTH = "dressLength";
	private static final String COLOR = "color";
	private static final String BUST_FIT = "bustFit";
	private static final String HEIGHT_FIT = "heightFit";
	private static final String CARE_INSTRUCTION = "careInstruction";
	private static final String BRAND = "brand";
	private static final String LIST_AS = "listAs";
	public static final String RENTAL_PERIOD = "rentalPeriod";
	public static final String RENTAL_PRICE = "rentalPrice";
	public static final String ORIGINAL_PRICE = "originalPrice";
	public static final String IMAGES = "images";
	public static final String OWNER_ID = "ownerId";
	public static final String OWNER_NAME = "ownerName";
	public static final String START = "start";
	public static final String END = "end";
	public static final String RENTED_ON = "rentedOn";
	public static final String RATING = "rating";
	public static final String REVIEW_COUNT = "reviewCount";
	public static final String NOT_AVAILABLE_ON = "notAvailableOn";
	public static final String AVAIL_FOR_OTHER_PERIOD = "availForOtherPeriod";
	public static final String DRESS_LOCATION = "dressLocation";
	private static final String WAIST = "waist";
	private static final String SLEEVE = "sleeve";

	@Override
	public BasicDBObject convertToDao(Product dto) {
		BasicDBObject obj = new BasicDBObject();
		if (dto == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, dto.getId());
		MongoUtils.appendToObj(obj, TITLE, dto.getTitle());
		MongoUtils.appendToObj(obj, DESCRIPTION, dto.getDescription());
		MongoUtils.appendToObj(obj, OCCASION, dto.getOccasion());
		MongoUtils.appendToObj(obj, CATEGORY, dto.getCategory());
		MongoUtils.appendToObj(obj, LENGTH, dto.getLength());
		MongoUtils.appendToObj(obj, TYPE, dto.getType());
		MongoUtils.appendToObj(obj, SIZE, dto.getSize());
		MongoUtils.appendToObj(obj, DRESS_LENGTH, dto.getDressLength());
		MongoUtils.appendToObj(obj, COLOR, dto.getColor());
		MongoUtils.appendToObj(obj, BUST_FIT, dto.getBustFit());
		MongoUtils.appendToObj(obj, HEIGHT_FIT, dto.getHeightFit());
		MongoUtils.appendToObj(obj, CARE_INSTRUCTION, dto.getCareInstruction());
		MongoUtils.appendToObj(obj, STATUS, dto.getStatus());
		MongoUtils.appendToObj(obj, BRAND, dto.getBrand());
		MongoUtils.appendToObj(obj, LIST_AS, dto.getListAs());
		MongoUtils.appendToObj(obj, CREATED_ON, dto.getCreatedOn());
		MongoUtils.appendToObj(obj, MODIFIED_ON, dto.getModifiedOn());
		MongoUtils.appendToObj(obj, CREATED_BY, dto.getCreatedBy());
		MongoUtils.appendToObj(obj, MODIFIED_BY, dto.getModifiedBy());
		MongoUtils.appendToObj(obj, RENTAL_PERIOD, dto.getRentalPeriod());
		MongoUtils.appendToObj(obj, RENTAL_PRICE, dto.getRentalPrice());
		MongoUtils.appendToObj(obj, ORIGINAL_PRICE, dto.getOriginalPrice());
		MongoUtils.appendToObj(obj, IMAGES, dto.getImages());
		MongoUtils.appendToObj(obj, OWNER_ID, dto.getOwnerId());
		MongoUtils.appendToObj(obj, OWNER_NAME, dto.getOwnerName());
		MongoUtils.appendToObj(obj, RATING, dto.getRating());
		MongoUtils.appendToObj(obj, REVIEW_COUNT, dto.getReviewCount());
		MongoUtils.appendToObj(obj, NOT_AVAILABLE_ON, dto.getNotAvailableOn());
		MongoUtils.appendToObj(obj, AVAIL_FOR_OTHER_PERIOD,
				dto.getAvailForOtherPeriod());

		try {
			if (dto.getDressLocation() != null) {
				MongoUtils.appendToObj(obj, DRESS_LOCATION, new BasicDBObject(
						dto.getDressLocation().toMap()));
			}
		} catch (TransformationException e) {
			e.printStackTrace();
		}

		MongoUtils.appendToObj(obj, WAIST, dto.getWaist());
		MongoUtils.appendToObj(obj, SLEEVE, dto.getSleeve());
		return obj;

	}

	@Override
	public BasicDBObject convertToDao(ProductFilter filter) {
		BasicDBObject obj = new BasicDBObject();
		if (filter == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, filter.getId());
		MongoUtils.appendToObjMulti(obj, CATEGORY, filter.getCategory());
		MongoUtils.appendToObjMulti(obj, OCCASION, filter.getOccasion());
		MongoUtils.appendToObjMulti(obj, LENGTH, filter.getLength());
		MongoUtils.appendToObjMulti(obj, TYPE, filter.getType());
		MongoUtils.appendToObjMulti(obj, SIZE, filter.getSize());
		MongoUtils.appendToObjMulti(obj, COLOR, filter.getColor());
		MongoUtils.appendToObjMulti(obj, BUST_FIT, filter.getBustFit());
		MongoUtils.appendToObjMulti(obj, HEIGHT_FIT, filter.getHeightFit());

		if (filter.getIsLended() == null || !filter.getIsLended())
			obj.put(STATUS, filter.getStatus() == null ? new BasicDBObject(
					"$in", Arrays.asList(1, 2, 3)) : filter.getStatus());

		MongoUtils.appendToObj(obj, OWNER_ID, filter.getOwnerId());

		// PEA-591 related changes
		if (StringUtils.isNotBlank(filter.getOwnerId())
				&& filter.getIsLended() != null && filter.getIsLended()) {
			// long now = Calendar.getInstance().getTimeInMillis();
			// obj.put(RENTED_ON + Punctuation.DOT.toString() + END,
			// new BasicDBObject("$gt", now));
			obj.put(RENTED_ON + Punctuation.DOT.toString() + END,
					new BasicDBObject("$exists", true));
		}
		DBObject priceObj = new BasicDBObject();
		if (filter.getRentalPriceStart() != null) {
			priceObj.put("$gte", filter.getRentalPriceStart());
		}
		if (filter.getRentalPriceEnd() != null) {
			priceObj.put("$lte", filter.getRentalPriceEnd());
		}
		if (!priceObj.keySet().isEmpty()) {
			obj.put(RENTAL_PRICE, priceObj);
		}
		if (StringUtils.isNotBlank(filter.getText())) {
			BasicDBList list = new BasicDBList();
			BasicDBObject textFilter = new BasicDBObject("$regex",
					filter.getText()).append("$options", "i");
			list.add(new BasicDBObject(TITLE, textFilter));
			list.add(new BasicDBObject(DESCRIPTION, textFilter));
			list.add(new BasicDBObject(OWNER_NAME, textFilter));
			obj.put("$or", list);
		}

		MongoUtils.appendToObjMulti(obj, WAIST, filter.getWaist());
		MongoUtils.appendToObjMulti(obj, SLEEVE, filter.getSleeve());

		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Product convertToDto(BasicDBObject obj) {
		Product dto = new Product();
		dto.setId(obj.getString(AbstractMongoDao.MONGO_ID));
		dto.setTitle(obj.getString(TITLE));
		dto.setDescription(obj.getString(DESCRIPTION));
		dto.setOccasion((List<String>) obj.get(OCCASION));
		dto.setCategory((List<String>) obj.get(CATEGORY));

		// Changes done as per new requirements
		dto.setLength((List<String>) obj.get(LENGTH));
//		dto.setType((List<String>) obj.get(TYPE));
		if (obj.get(TYPE) instanceof List) {
			dto.setType((List<String>) obj.get(TYPE));
		} else {
			List<String> list = new ArrayList<String>();
			list.add(obj.getString(TYPE));
			dto.setType(list);
		}
		dto.setSize((List<String>) obj.get(SIZE));

		dto.setDressLength(obj.getString(DRESS_LENGTH));
		dto.setColor((List<String>) obj.get(COLOR));
		dto.setBustFit((List<String>) obj.get(BUST_FIT));
		dto.setHeightFit((List<String>) obj.get(HEIGHT_FIT));
		dto.setCareInstruction((List<String>) obj.get(CARE_INSTRUCTION));
		dto.setBrand(obj.getString(BRAND));
		dto.setListAs(obj.getString(LIST_AS));
		if (obj.containsField(STATUS)) {
			dto.setStatus(obj.getInt(STATUS));
		}
		if (obj.containsField(RENTAL_PERIOD)) {
			dto.setRentalPeriod(obj.getInt(RENTAL_PERIOD));
		}
		if (obj.containsField(RENTAL_PRICE)) {
			dto.setRentalPrice(obj.getDouble(RENTAL_PRICE));
		}
		if (obj.containsField(ORIGINAL_PRICE)) {
			dto.setOriginalPrice(obj.getDouble(ORIGINAL_PRICE));
		}
		if (obj.containsField(CREATED_ON)) {
			dto.setCreatedOn(obj.getLong(CREATED_ON));
		}
		if (obj.containsField(MODIFIED_ON)) {
			dto.setModifiedOn(obj.getLong(MODIFIED_ON));
		}
		if (obj.containsField(REVIEW_COUNT)) {
			dto.setReviewCount(obj.getInt(REVIEW_COUNT));
		}
		if (obj.containsField(RATING)) {
			dto.setRating(obj.getDouble(RATING));
		}
		if (obj.containsField(RENTED_ON)) {
			dto.setRentedOn((List<Map<String, Long>>) obj.get(RENTED_ON));
		}
		dto.setCreatedBy(obj.getString(CREATED_BY));
		dto.setModifiedBy(obj.getString(MODIFIED_BY));
		dto.setImages((Map<String, String>) obj.get(IMAGES));
		dto.setOwnerId(obj.getString(OWNER_ID));
		dto.setOwnerName(obj.getString(OWNER_NAME));
		if (obj.containsField(NOT_AVAILABLE_ON)) {
			dto.setNotAvailableOn((List<Long>) obj.get(NOT_AVAILABLE_ON));
		}
		if (obj.containsField(AVAIL_FOR_OTHER_PERIOD)) {
			dto.setAvailForOtherPeriod(obj.getBoolean(AVAIL_FOR_OTHER_PERIOD));
		}

		if (obj.containsField(DRESS_LOCATION)) {
			try {
				dto.setDressLocation(new ObjectMapper().readValue(
						obj.get(DRESS_LOCATION).toString(), Address.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (obj.containsField(WAIST)) {
			dto.setWaist((List<String>) obj.get(WAIST));
		}
		if (obj.containsField(SLEEVE)) {
			dto.setSleeve((List<String>) obj.get(SLEEVE));
		}

		return dto;
	}
}
