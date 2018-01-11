package com.promelle.product.handler;

import java.util.Arrays;
import java.util.List;

import com.promelle.common.config.AbstractConfiguration;
import com.promelle.exception.AbstractException;
import com.promelle.product.dto.ProductMessage;
import com.promelle.product.dto.Product;
import com.promelle.product.dto.Review;
import com.promelle.product.filter.ProductMessageFilter;
import com.promelle.product.filter.ProductFilter;
import com.promelle.product.filter.ReviewFilter;
import com.promelle.product.management.ProductManagement;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.topic.message.TopicMessage;

/**
 * This class is responsible for handling message.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class UserDeleteHandler extends ProductMessageHandler {

	@Override
	public List<String> getTypes() {
		return Arrays.asList(AbstractConfiguration.getProperty("user.deleted"));
	}

	@Override
	public void handle(TopicMessage message) throws AbstractException {
		System.out.println(message);
		AbstractRequestTracker requestTracker = message.getRequestTracker();
		String userId = message.getData();
		ProductFilter productFilter = new ProductFilter();
		productFilter.setOwnerId(userId);
		ProductManagement productManagement = getProductManagement();
		List<Product> products = productManagement.listProducts(requestTracker,
				productFilter, null, null).getObjects();
		for (Product product : products) {
			productManagement.deleteProduct(requestTracker, product.getId());
			// TODO add bulk later
			ReviewFilter reviewFilter = new ReviewFilter();
			reviewFilter.setProductId(product.getId());
			List<Review> reviews = productManagement.listReviews(
					requestTracker, reviewFilter, null, null).getObjects();
			for (Review review : reviews) {
				productManagement.deleteReview(requestTracker, review.getId());
			}
			ProductMessageFilter messageFilter = new ProductMessageFilter();
			messageFilter.setProductId(product.getId());
			List<ProductMessage> productMessages = productManagement.listMessages(
					requestTracker, messageFilter, null, null).getObjects();
			for (ProductMessage productMessage : productMessages) {
				productManagement.deleteMessage(requestTracker,
						productMessage.getId());
			}
		}
	}

}
