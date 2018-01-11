package com.promelle.product.service.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;
import com.promelle.common.service.AbstractService;
import com.promelle.exception.AbstractException;
import com.promelle.paging.Paging;
import com.promelle.product.dto.Review;
import com.promelle.product.filter.ReviewFilter;
import com.promelle.product.management.ProductManagement;
import com.promelle.product.service.ReviewService;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.sort.SortRules;

/**
 * This class is intended for providing services related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@Path("/review")
@Produces(MediaType.APPLICATION_JSON)
public class DefaultReviewService extends AbstractService<Review, ReviewFilter>
		implements ReviewService {
	private ProductManagement productManagement;

	public DefaultReviewService(ProductManagement productManagement) {
		super(Review.class, ReviewFilter.class);
		this.productManagement = productManagement;
	}

	@Override
	@Timed(name = "Add review#timer")
	protected Response save(AbstractRequestTracker requestTracker, Review review)
			throws AbstractException {
		if (review.getTimestamp() == null) {
			review.setTimestamp(Calendar.getInstance().getTimeInMillis());
		}
		productManagement.addReview(requestTracker, review);
		return onSuccess(review);
	}

	@Override
	@Timed(name = "Update review#timer")
	protected Response update(AbstractRequestTracker requestTracker,
			Review review, ReviewFilter filter) throws AbstractException {
		if (productManagement.updateReview(requestTracker, review, filter)) {
			return onSuccess(review);
		}
		return onError(new AbstractException());
	}

	@Override
	@Timed(name = "Delete review#timer")
	public Response deleteById(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		productManagement.deleteReview(requestTracker, id);
		return onSuccess(id);
	}

	@Override
	public Response findById(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		throw new UnsupportedOperationException();
	}

	@Override
	@Timed(name = "List reviews#timer")
	protected Response list(AbstractRequestTracker requestTracker,
			ReviewFilter filter, Paging paging, SortRules sortRules)
			throws AbstractException {
		return onSuccess(productManagement.listReviews(requestTracker, filter,
				paging, sortRules));
	}

	@Override
	@Path("/isReviewPossible")
	@GET
	public Response isReviewPossible(@QueryParam("productId") String productId,
			@QueryParam("userId") String userId,
			@Context HttpServletRequest request) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("isReviewPossible", productManagement.isReviewPossible(
				new AbstractRequestTracker(request), productId, userId));
		return onSuccess(map);
	}

}
