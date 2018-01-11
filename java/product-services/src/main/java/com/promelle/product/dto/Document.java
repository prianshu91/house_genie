package com.promelle.product.dto;

import com.promelle.dto.AbstractAuditDTO;

/**
 * @author Kanak Sony
 * @version 1.0
 *
 */
public class Document extends AbstractAuditDTO {
	private static final long serialVersionUID = 6008045533685561200L;
	private String name;
	private String url;
	private String content;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
