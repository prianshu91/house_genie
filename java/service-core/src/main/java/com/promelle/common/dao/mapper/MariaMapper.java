package com.promelle.common.dao.mapper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.promelle.dto.AbstractDTO;
import com.promelle.filter.SearchFilter;
import com.promelle.utils.MapUtils;

/**
 * This class is responsible for mapping of database request.
 * 
 * @author Hemant Kumar
 * @version 1.0
 *
 * @param <T>
 *            extends {@link AbstractDTO}
 * @param <E>
 *            extends {@link SearchFilter}
 */
public interface MariaMapper<T extends AbstractDTO, E extends SearchFilter> extends ResultSetMapper<T> {

	Map<String, Object> convertToDao(T dto);

    String getTableName();

    default Map<String, Object> convertToDao(E filter) {
        Map<String, Object> map = new HashMap<>();
        if (filter == null) {
            return map;
        }
        MapUtils.put(map, "id", filter.getId());
        return map;
    }

    default Map<String, String> getSortMap() {
		return new LinkedHashMap<>();
	}

}
