/**
 * 
 */
package com.promelle.product.dao;

import com.promelle.exception.AbstractException;
import com.promelle.mongo.dao.AbstractMongoDao;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.product.dao.mapper.DocumentMapper;
import com.promelle.product.dto.Document;
import com.promelle.product.filter.DocumentFilter;

/**
 * @author Kanak Sony
 * @version 1.0
 *
 */
public class DocumentDao extends AbstractMongoDao<Document, DocumentFilter, DocumentMapper> {

	public DocumentDao(MongoManager mongoManager) throws AbstractException {
		super(mongoManager, "documents", DocumentMapper.class);
	}
}
