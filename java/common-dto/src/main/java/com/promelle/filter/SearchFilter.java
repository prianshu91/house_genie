package com.promelle.filter;

import com.promelle.object.AbstractObject;

/**
 * This class is the base class for all filters.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class SearchFilter extends AbstractObject {
	private static final long serialVersionUID = 1234901290L;
    private String id;
    private String query;

    public String getId() {
        return id;
    }

    public SearchFilter setId(String id) {
        this.id = id;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public SearchFilter setQuery(String query) {
        this.query = query;
        return this;
    }

}
