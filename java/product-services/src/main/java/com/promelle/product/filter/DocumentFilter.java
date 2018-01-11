package com.promelle.product.filter;

import com.promelle.filter.SearchFilter;

/**
 * @author Kanak Sony
 * @version 1.0
 *
 */
public class DocumentFilter extends SearchFilter {
	private static final long serialVersionUID = 9044396999224212406L;
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
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}
}
