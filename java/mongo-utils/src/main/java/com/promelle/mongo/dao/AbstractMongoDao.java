package com.promelle.mongo.dao;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.promelle.dto.AbstractAuditDTO;
import com.promelle.dto.AbstractDTO;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.mongo.dao.mapper.MongoMapper;
import com.promelle.mongo.manager.MongoManager;
import com.promelle.mongo.utils.MongoUtils;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.PagedList;
import com.promelle.response.Pagination;
import com.promelle.sort.SortOrder;
import com.promelle.sort.SortRule;
import com.promelle.sort.SortRules;

/**
 * 
 * This interface is intended for providing generic dao layer.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 * 
 * @param <T>
 *            extends {@link AbstractDTO}
 * @param <E>
 *            extends {@link SearchFilter}
 */
public abstract class AbstractMongoDao<T extends AbstractDTO, E extends SearchFilter, Z extends MongoMapper<T, E>> {

	public static final String MONGO_ID = "_id";
	public static final String SET_OPERATOR = "$set";
	public static final String PUSH_OPERATOR = "$push";

	private DBCollection collection;
	private Z instance;

	public AbstractMongoDao(MongoManager mongoManager, String collectionName,
			Class<Z> mapper) throws AbstractException {
		super();
		try {
			instance = mapper.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new AbstractException(e);
		}
		collection = mongoManager.getClient()
				.getDB(mongoManager.getConfiguration().getDbName())
				.getCollection(collectionName);
	}

	public DBCollection getCollection() {
		return collection;
	}

	public Z getMapper() {
		return instance;
	}

	public void preProcessForAudit(AbstractRequestTracker requestTracker, T dto) {
		if (dto instanceof AbstractAuditDTO) {
			MongoUtils.preProcessForAudit(requestTracker,
					(AbstractAuditDTO) dto);
		}
	}

	public int save(AbstractRequestTracker requestTracker, T dto) {
		preProcessForAudit(requestTracker, dto);
		DBObject obj = instance.convertToDao(dto);
		return getCollection().update(
				new BasicDBObject(MONGO_ID, obj.get(MONGO_ID)),
				new BasicDBObject(SET_OPERATOR, instance.convertToDao(dto)),
				true, true).getN();
	}

	public void saveAll(AbstractRequestTracker requestTracker, List<T> dtos) {
		DBObject[] list = new DBObject[dtos.size()];
		for (int i = 0; i < dtos.size(); i++) {
			T dto = dtos.get(i);
			preProcessForAudit(requestTracker, dto);
			list[i] = instance.convertToDao(dto);
		}
		getCollection().insert(list);
	}

	public T findById(String id) {
		BasicDBObject obj = (BasicDBObject) getCollection().findOne(
				new BasicDBObject(MONGO_ID, id));
		return obj == null ? null : instance.convertToDto(obj);
	}

	public T findOne(E filter) {
		BasicDBObject obj = (BasicDBObject) getCollection().findOne(
				instance.convertToDao(filter));
		return obj == null ? null : instance.convertToDto(obj);
	}

	private BasicDBObject getSortObj(SortRules sortRules) {
		BasicDBObject sortObj = null;
		if (sortRules != null) {
			List<SortRule> sortRule = sortRules.getRules();
			if (CollectionUtils.isNotEmpty(sortRule)) {
				sortObj = new BasicDBObject();
				for (SortRule sort : sortRule) {
					sortObj.put(
							sort.getFieldName(),
							SortOrder.DESC.toString().equalsIgnoreCase(
									String.valueOf(sort.getValue())) ? -1 : 1);
				}
			}
		}
		if (sortObj == null) {
			sortObj = new BasicDBObject(MONGO_ID, 1);
		}
		return sortObj;
	}

	public PagedList<T> list(E filter, Paging paging, SortRules sortRules) {
		PagedList<T> pagedList = new PagedList<>();
		BasicDBObject sortObj = getSortObj(sortRules);
		List<T> results = new LinkedList<>();
		DBCursor cur = paging == null || paging.isNotReqd() ? getCollection()
				.find(instance.convertToDao(filter)).sort(sortObj)
				: getCollection().find(instance.convertToDao(filter))
						.sort(sortObj).skip(paging.getOffset())
						.limit(paging.getLimit());

		for (DBObject obj : cur) {
			results.add(instance.convertToDto((BasicDBObject) obj));
		}
		Pagination pagination = new Pagination();
		pagination.setOffset(paging != null ? paging.getOffset() : 0);
		pagination.setLimit(paging != null ? paging.getLimit() : 0);
		pagination.setTotal(cur.count());
		pagination.setCount(results.size());
		pagedList.setObjects(results);
		pagedList.setPagination(pagination);
		cur.close();
		return pagedList;
	}

	public int count(E filter) {
		return getCollection().find(instance.convertToDao(filter)).count();
	}

	public int update(AbstractRequestTracker requestTracker, E filter, T dto) {
		if (dto instanceof AbstractAuditDTO) {
			AbstractAuditDTO b = (AbstractAuditDTO) dto;
			if (b.getModifiedOn() == null) {
				b.setModifiedOn(new Date().getTime());
			}
			if (b.getModifiedBy() == null) {
				b.setModifiedBy(requestTracker.getRequestBy());
			}
		}
		if (dto.getId() != null && filter.getId() == null) {
			filter.setId(dto.getId());
		}
		return getCollection().update(instance.convertToDao(filter),
				new BasicDBObject(SET_OPERATOR, instance.convertToDao(dto)),
				false, true).getN();
	}

	public int softDelete(AbstractRequestTracker requestTracker, String id) {
		return getCollection().update(
				new BasicDBObject(MONGO_ID, id),
				new BasicDBObject(SET_OPERATOR, new BasicDBObject(
						AbstractAuditDTO.STATUS, 0))).getN();
	}

}
