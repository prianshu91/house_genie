package com.promelle.common.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

import org.apache.commons.lang.StringUtils;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import com.promelle.common.dao.mapper.MariaMapper;
import com.promelle.constants.Punctuation;
import com.promelle.dto.AbstractAuditDTO;
import com.promelle.dto.AbstractDTO;
import com.promelle.exception.AbstractException;
import com.promelle.filter.SearchFilter;
import com.promelle.paging.Paging;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.PagedList;
import com.promelle.response.Pagination;
import com.promelle.sort.SortRule;
import com.promelle.sort.SortRules;
import com.promelle.utils.UUIDUtils;

/**
 * 
 * This interface is intended for providing generic dao layer.
 * 
 * @author Hemant Kumar
 * @version 1.0
 * 
 * @param <T>
 *            extends {@link AbstractDTO}
 * @param <E>
 *            extends {@link SearchFilter}
 * @param <Z>
 *            extends {@link MariaMapper}
 */
@UseStringTemplate3StatementLocator
public abstract class AbstractMariaDao<T extends AbstractDTO, E extends SearchFilter, Z extends MariaMapper<T, E>>
		implements CrudMariaDao<T> {

	private Z instance;

	public AbstractMariaDao(Class<Z> requestMapper) throws AbstractException {
		super();
		try {
			instance = requestMapper.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new AbstractException(e);
		}
	}

	public int save(AbstractRequestTracker requestTracker, T dto) {
		AbstractAuditDTO b;
		if (dto instanceof AbstractAuditDTO) {
			b = (AbstractAuditDTO) dto;
			if (StringUtils.isEmpty(b.getId())) {
				b.setId(UUIDUtils.getUUID());
			}
			if (b.getCreatedBy() == null) {
				b.setCreatedBy(requestTracker.getRequestBy());
			}
			if (b.getCreatedOn() == null) {
				b.setCreatedOn(requestTracker.getRequestTimestamp());
			}
			if (b.getModifiedBy() == null) {
				b.setModifiedBy(requestTracker.getRequestBy());
			}
			if (b.getModifiedOn() == null) {
				b.setModifiedOn(requestTracker.getRequestTimestamp());
			}
			if (b.getStatus() == null) {
				b.setStatus(1);
			}
		}
		Map<String, Object> colMap = instance.convertToDao(dto);
		StringJoiner params = new StringJoiner(Punctuation.COMMA.toString());
		StringJoiner values = new StringJoiner(Punctuation.COMMA.toString());
		for (Entry<String, Object> entry : colMap.entrySet()) {
			params.add(entry.getKey());
			values.add(resolve(entry.getValue()));
		}
		return save(instance.getTableName(), params.toString(),
				values.toString());
	}

	public T findById(AbstractRequestTracker requestTracker, String id) {
		return findById(instance.getTableName(), id);
	}

	public T findOne(AbstractRequestTracker requestTracker, E filter) {
		StringJoiner queryParams = join(AND, instance.convertToDao(filter));
		return findOne(instance.getTableName(),
				queryParams.length() == 0 ? Punctuation.SPACE.toString()
						: (WHERE + queryParams.toString()));
	}

	public PagedList<T> list(AbstractRequestTracker requestTracker, E filter,
			Paging paging, SortRules sortRules) {
		String queryParams = join(AND, instance.convertToDao(filter))
				.toString();
		if (StringUtils.isNotBlank(filter.getQuery())) {
			queryParams += StringUtils.isNotBlank(queryParams) ? (AND
					+ Punctuation.SPACE.toString() + filter.getQuery())
					: filter.getQuery();
		}
		List<String> orders = new LinkedList<>();
		if (sortRules != null && !sortRules.getRules().isEmpty()) {
			List<SortRule> rules = sortRules.getRules();
			Map<String, String> sortMap = instance.getSortMap();
			for (SortRule rule : rules) {
				if (sortMap.containsKey(rule.getFieldName())) {
					orders.add(sortMap.get(rule.getFieldName()) + " "
							+ rule.getValue().name());
				}
			}
		}
		List<T> list = list(
				instance.getTableName(),
				queryParams.length() == 0 ? Punctuation.SPACE.toString()
						: (WHERE + queryParams),
				paging != null && !paging.isNotReqd() ? String.format(
						" limit %s, %s", paging.getOffset(), paging.getLimit())
						: "", orders.isEmpty() ? "" : " order by "
						+ StringUtils.join(orders, ","));
		Pagination pagination = new Pagination();
		pagination.setCount(list.size());
		if (paging == null || paging.isNotReqd()) {
			pagination.setOffset(-1);
			pagination.setLimit(-1);
			pagination.setTotal(list.size());
		} else {
			pagination.setOffset(paging.getOffset());
			pagination.setLimit(paging.getLimit());
			pagination.setTotal(count(requestTracker, filter));
		}
		PagedList<T> pagedList = new PagedList<>();
		pagedList.setObjects(list);
		pagedList.setPagination(pagination);
		return pagedList;
	}

	public PagedList<T> list(AbstractRequestTracker requestTracker, E filter,
			Paging paging) {
		return list(requestTracker, filter, paging, null);
	}

	public PagedList<T> list(AbstractRequestTracker requestTracker, E filter) {
		return list(requestTracker, filter, null, null);
	}

	public int update(AbstractRequestTracker requestTracker, T dto, E filter) {
		if (dto.getId() != null && filter.getId() == null) {
			filter.setId(dto.getId());
		}
		StringJoiner setFields = join(Punctuation.COMMA.toString(),
				instance.convertToDao(dto));
		if (setFields.length() == 0) {
			return 0;
		}
		StringJoiner queryParams = join(AND, instance.convertToDao(filter));
		System.out.println("queryParams => " + queryParams);
		System.out.println("setFields => " + setFields);
		return update(instance.getTableName(), setFields.toString(),
				queryParams.length() == 0 ? Punctuation.SPACE.toString()
						: (WHERE + queryParams.toString()));
	}

	private String resolve(Object val) {
		if (val instanceof String) {
			return Punctuation.SINGLE_QUOTE.toString()
					+ ((String) val).replace(
							Punctuation.SINGLE_QUOTE.toString(),
							Punctuation.SINGLE_QUOTE.toString()
									+ Punctuation.SINGLE_QUOTE.toString())
					+ Punctuation.SINGLE_QUOTE.toString();
		} else if (val instanceof Integer) {
			return String.valueOf(val);
		} else if (val instanceof Double) {
			return String.valueOf(val);
		} else if (val instanceof Long) {
			return String.valueOf(val);
		} else if (val instanceof Boolean) {
			return String.valueOf(val);
		} else if (val instanceof List) {
			// TODO Add csv
			System.out.println("Field Type Not Supported : " + val);
		} else {
			System.out.println("Field Type Not Supported : " + val);
		}
		return null;
	}

	public StringJoiner join(String joinWord, Map<String, Object> fieldMap) {
		StringJoiner joiner = new StringJoiner(joinWord);
		for (Entry<String, Object> entry : fieldMap.entrySet()) {
			String value = resolve(entry.getValue());
			if (value != null) {
				if (value.contains(Punctuation.COMMA.toString())) {
					joiner.add(entry.getKey() + "=" + value);
				} else {
					joiner.add(entry.getKey() + "=" + value);
				}
			}
		}
		return joiner;
	}

	public int softDelete(AbstractRequestTracker requestTracker, String id) {
		return softDelete(instance.getTableName(), id);
	}

	public int count(AbstractRequestTracker requestTracker, E filter) {
		StringJoiner queryParams = join(AND, instance.convertToDao(filter));
		if (StringUtils.isNotBlank(filter.getQuery())) {
			queryParams.add(filter.getQuery());
		}
		return count(instance.getTableName(),
				queryParams.length() == 0 ? Punctuation.SPACE.toString()
						: (WHERE + queryParams.toString()));
	}

	public StringJoiner searchJoin(String joinWord, Map<String, Object> fieldMap) {
		StringJoiner joiner = new StringJoiner(joinWord);
		for (Entry<String, Object> entry : fieldMap.entrySet()) {
			String value = resolve(entry.getValue());
			if (value != null) {
				if (value.contains(Punctuation.COMMA.toString())) {
					joiner.add(entry.getKey() + LIKE + value);
				} else {
					joiner.add(entry.getKey() + LIKE + value);
				}
			}
		}
		return joiner;
	}

}
