package com.promelle.product.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

/**
 * This interface is intended for providing services related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public interface ReviewService {

	Response isReviewPossible(String productId, String userId,
			HttpServletRequest request);

}
