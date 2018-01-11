package com.promelle.dto;

import com.promelle.object.AbstractObject;

/**
 * This class will act as a base class for all DTO classes
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class AbstractDTO extends AbstractObject {
	private static final long serialVersionUID = -6451901749776967187L;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
