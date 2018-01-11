package com.promelle.product.dao.mapper;

import static com.promelle.dto.AbstractAuditDTO.CREATED_BY;
import static com.promelle.dto.AbstractAuditDTO.CREATED_ON;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_BY;
import static com.promelle.dto.AbstractAuditDTO.MODIFIED_ON;
import static com.promelle.dto.AbstractAuditDTO.STATUS;

import com.mongodb.BasicDBObject;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.dao.mapper.MongoMapper;
import com.promelle.mongo.utils.MongoUtils;
import com.promelle.product.dto.Document;
import com.promelle.product.filter.DocumentFilter;

public class DocumentMapper implements MongoMapper<Document, DocumentFilter> {
	public static final String URL = "url";
	public static final String NAME = "name";
	public static final String CONTENT = "content";
	
	@Override
	public BasicDBObject convertToDao(Document dto) {
		BasicDBObject obj = new BasicDBObject();
		if (dto == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, dto.getId());
		MongoUtils.appendToObj(obj, NAME, dto.getName());
		MongoUtils.appendToObj(obj, URL, dto.getUrl());
		if(dto.getContent()!=null) {
			MongoUtils.appendToObj(obj, CONTENT, dto.getContent());
		}
		MongoUtils.appendToObj(obj, STATUS, dto.getStatus());
		MongoUtils.appendToObj(obj, CREATED_ON, dto.getCreatedOn());
		MongoUtils.appendToObj(obj, MODIFIED_ON, dto.getModifiedOn());
		MongoUtils.appendToObj(obj, CREATED_BY, dto.getCreatedBy());
		MongoUtils.appendToObj(obj, MODIFIED_BY, dto.getModifiedBy());
		return obj;
	}

	@Override
	public BasicDBObject convertToDao(DocumentFilter filter) {
		BasicDBObject obj = new BasicDBObject();
		if (filter == null) {
			return obj;
		}
		MongoUtils.appendToObj(obj, AbstractMongoDao.MONGO_ID, filter.getId());
		MongoUtils.appendToObj(obj, URL, filter.getUrl());
		MongoUtils.appendToObj(obj, NAME, filter.getName());
		return obj;
	}

	@Override
	public Document convertToDto(BasicDBObject obj) {
		Document dto = new Document();
		dto.setId(obj.getString(AbstractMongoDao.MONGO_ID));
		dto.setUrl(obj.getString(URL));
		dto.setName(obj.getString(NAME));
		
		if (obj.containsField(CREATED_ON)) {
			dto.setCreatedOn(obj.getLong(CREATED_ON));
		}
		if (obj.containsField(MODIFIED_ON)) { 
			dto.setModifiedOn(obj.getLong(MODIFIED_ON));
		}
		if (obj.containsField(STATUS)) { 
			dto.setStatus(obj.getInt(STATUS));
		}
		if(obj.containsField(CONTENT)) {
			dto.setContent(obj.getString(CONTENT));
		}
		dto.setCreatedBy(obj.getString(CREATED_BY));
		dto.setModifiedBy(obj.getString(MODIFIED_BY));		
		return dto;
	}

}
