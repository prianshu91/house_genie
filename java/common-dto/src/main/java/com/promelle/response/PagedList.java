package com.promelle.response;

import java.util.List;

import com.promelle.dto.AbstractDTO;

/**
 * This class is intended for holding paged list.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class PagedList<T> extends AbstractDTO {
	private static final long serialVersionUID = 12394789L;

	private List<T> objects;
	private Pagination pagination;

	public List<T> getObjects() {
		return objects;
	}

	public void setObjects(List<T> objects) {
		this.objects = objects;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

}
