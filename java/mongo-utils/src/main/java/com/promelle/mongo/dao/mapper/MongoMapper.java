package com.promelle.mongo.dao.mapper;

import com.mongodb.BasicDBObject;
import com.promelle.dto.AbstractDTO;
import com.promelle.filter.SearchFilter;

/**
 * This class is responsible for mapping of database request.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 *
 * @param <T>
 *            extends {@link AbstractDTO}
 * @param <E>
 *            extends {@link SearchFilter}
 */
public interface MongoMapper<T extends AbstractDTO, E extends SearchFilter> {

    BasicDBObject convertToDao(T dto);

    BasicDBObject convertToDao(E filter);

    T convertToDto(BasicDBObject obj);

}
