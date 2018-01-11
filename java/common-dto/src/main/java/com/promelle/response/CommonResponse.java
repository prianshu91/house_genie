package com.promelle.response;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.promelle.dto.AbstractDTO;
import com.promelle.paging.Paging;

/**
 * This class is responsible for holding generic fields to be returned in
 * response.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class CommonResponse extends AbstractDTO {
	private static final long serialVersionUID = 1331711099811393200L;
	private String errorCode;
    private Object data;
    private Paging paging;
    private List<String> errors;
    private Map<String, Object> additionalData;

    public String getErrorCode() {
        return errorCode;
    }

    public CommonResponse setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public Object getData() {
        return data;
    }

    public CommonResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public Paging getPaging() {
        return paging;
    }

    public CommonResponse setPaging(Paging paging) {
        this.paging = paging;
        return this;
    }

    public List<String> getErrors() {
        return errors;
    }

    public CommonResponse setErrors(List<String> errors) {
        this.errors = errors;
        return this;
    }

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public CommonResponse setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
        return this;
    }

    public CommonResponse addAdditionalData(String key, Object value) {
        if (additionalData == null) {
            additionalData = new HashMap<String, Object>();
        }
        additionalData.put(key, value);
        return this;
    }

    public Object getAdditionalData(String key) {
        if (additionalData == null) {
            additionalData = new HashMap<String, Object>();
        }
        return additionalData.get(key);
    }

    public void removeAdditionalData(String key) {
        if (additionalData != null) {
            additionalData.remove(key);
        }
    }

    public void clearAdditionalData() {
        if (additionalData != null) {
            additionalData.clear();
        }
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        if (errorCode != null) {
            map.put("errorCode", errorCode);
        }
        if (data != null) {
            map.put("data", data);
        }
        if (paging != null) {
            map.put("paging", paging);
        }
        if (errors != null && !errors.isEmpty()) {
            map.put("errors", errors);
        }
        if (additionalData != null && additionalData.size() > 0) {
            map.putAll(additionalData);
        }
        return map;
    }

}
