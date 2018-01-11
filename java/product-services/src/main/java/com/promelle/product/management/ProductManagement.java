package com.promelle.product.management;

import java.util.List;

import com.promelle.exception.AbstractException;
import com.promelle.paging.Paging;
import com.promelle.payment.dto.Payment;
import com.promelle.payment.stripe.dto.StripeInfo;
import com.promelle.product.dto.CartItem;
import com.promelle.product.dto.Document;
import com.promelle.product.dto.DressRentalItem;
import com.promelle.product.dto.Earning;
import com.promelle.product.dto.Favorite;
import com.promelle.product.dto.Invoice;
import com.promelle.product.dto.Order;
import com.promelle.product.dto.OrderAddress;
import com.promelle.product.dto.OrderItem;
import com.promelle.product.dto.PaymentCard;
import com.promelle.product.dto.Product;
import com.promelle.product.dto.ProductMessage;
import com.promelle.product.dto.Review;
import com.promelle.product.dto.Shipment;
import com.promelle.product.filter.DocumentFilter;
import com.promelle.product.filter.EarningFilter;
import com.promelle.product.filter.FavoriteFilter;
import com.promelle.product.filter.OrderFilter;
import com.promelle.product.filter.OrderItemFilter;
import com.promelle.product.filter.PaymentsFilter;
import com.promelle.product.filter.ProductFilter;
import com.promelle.product.filter.ProductMessageFilter;
import com.promelle.product.filter.ReviewFilter;
import com.promelle.product.filter.ShipmentFilter;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.PagedList;
import com.promelle.sort.SortRules;

/**
 * This interface is intended for providing interactions between dao classes
 * related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public interface ProductManagement {

	String addProduct(AbstractRequestTracker requestTracker, Product product)
			throws AbstractException;

	boolean updateProduct(AbstractRequestTracker requestTracker,
			Product product, ProductFilter filter) throws AbstractException;

	Product activateProduct(AbstractRequestTracker requestTracker,
			String productId) throws AbstractException;

	PagedList<Product> listProducts(AbstractRequestTracker requestTracker,
			ProductFilter filter, Paging paging, SortRules sortRules)
			throws AbstractException;

	int deleteProduct(AbstractRequestTracker requestTracker, String id)
			throws AbstractException;

	Product findProductById(AbstractRequestTracker requestTracker, String id)
			throws AbstractException;

	boolean updatePic(AbstractRequestTracker requestTracker, String id,
			String tag, String url) throws AbstractException;

	String addProductToCart(AbstractRequestTracker requestTracker,
			CartItem cartItem) throws AbstractException;

	List<CartItem> getUserCart(AbstractRequestTracker requestTracker,
			String userId) throws AbstractException;

	String removeProductFromCart(AbstractRequestTracker requestTracker,
			String cartItemId) throws AbstractException;

	Order placeOrder(AbstractRequestTracker requestTracker, Order order)
			throws AbstractException;

	Invoice updateOrderStatus(AbstractRequestTracker requestTracker,
			String orderId, String status, OrderAddress orderAddress,
			Payment payment, StripeInfo stripeInfo) throws AbstractException;

	Order getOrder(AbstractRequestTracker requestTracker, String orderId,
			Boolean includeItems, Boolean includeShipments,
			Boolean includeEarnings) throws AbstractException;

	void populateOrder(AbstractRequestTracker requestTracker, Order order,
			Boolean includeItems, Boolean includeShipments,
			Boolean includeEarnings) throws AbstractException;

	PagedList<Order> getOrderHistory(AbstractRequestTracker requestTracker,
			OrderFilter orderFilter, Paging paging) throws AbstractException;

	PagedList<OrderItem> getUserRentedProducts(
			AbstractRequestTracker requestTracker, String userId, Paging paging)
			throws AbstractException;

	PagedList<Invoice> getUserInvoices(AbstractRequestTracker requestTracker,
			String userId, Paging paging) throws AbstractException;

	String addReview(AbstractRequestTracker requestTracker, Review review)
			throws AbstractException;

	boolean updateReview(AbstractRequestTracker requestTracker, Review review,
			ReviewFilter filter) throws AbstractException;

	PagedList<Review> listReviews(AbstractRequestTracker requestTracker,
			ReviewFilter filter, Paging paging, SortRules sortRules)
			throws AbstractException;

	int deleteReview(AbstractRequestTracker requestTracker, String id)
			throws AbstractException;

	String sendMessage(AbstractRequestTracker requestTracker,
			ProductMessage message) throws AbstractException;

	PagedList<ProductMessage> listMessages(
			AbstractRequestTracker requestTracker, ProductMessageFilter filter,
			Paging paging, SortRules sortRules) throws AbstractException;

	int deleteMessage(AbstractRequestTracker requestTracker, String id,
			String userId) throws AbstractException;

	int deleteMessage(AbstractRequestTracker requestTracker, String id)
			throws AbstractException;

	void calculateProductRating(AbstractRequestTracker requestTracker,
			String productId) throws AbstractException;

	boolean isReviewPossible(AbstractRequestTracker requestTracker,
			String productId, String userId);

	PagedList<Shipment> getShipmentHistory(
			AbstractRequestTracker requestTracker,
			ShipmentFilter shipmentFilter, Paging paging)
			throws AbstractException;

	PagedList<OrderItem> listItems(AbstractRequestTracker requestTracker,
			OrderItemFilter itemFilter, Paging paging, SortRules sortRules)
			throws AbstractException;

	List<OrderItem> getShipmentItems(AbstractRequestTracker requestTracker,
			String shipmentId) throws AbstractException;

	String updateShipmentStatus(AbstractRequestTracker requestTracker,
			String shipmentId, String status) throws AbstractException;

	PagedList<Earning> getEarnings(AbstractRequestTracker requestTracker,
			EarningFilter earningFilter, Paging paging)
			throws AbstractException;

	String updateEarningStatus(AbstractRequestTracker requestTracker,
			Earning earning) throws AbstractException;

	String getConversationId(AbstractRequestTracker requestTracker,
			String senderId, String receiverId, String productId)
			throws AbstractException;

	boolean readConveration(AbstractRequestTracker requestTracker,
			String conversationId, String userId) throws AbstractException;

	int deleteConveration(AbstractRequestTracker requestTracker,
			String conversationId, String userId) throws AbstractException;

	int updateFavoriteStatus(AbstractRequestTracker abstractRequestTracker,
			FavoriteFilter filter, Favorite favourite);

	PagedList<Product> listFavorites(AbstractRequestTracker requestTracker,
			FavoriteFilter filter, ProductFilter productFilter, Paging paging,
			SortRules sortRules) throws AbstractException;

	PagedList<PaymentCard> listSavedPaymentCard(
			AbstractRequestTracker requestTracker, PaymentsFilter filter,
			Paging paging) throws AbstractException;

	PaymentCard addPaymentCard(AbstractRequestTracker requestTracker,
			StripeInfo stripeInfo, String userId) throws AbstractException;

	PagedList<DressRentalItem> listDressRentals(
			AbstractRequestTracker requestTracker, ShipmentFilter filter,
			Paging paging, SortRules sortRules) throws AbstractException;

	int removePaymentCard(AbstractRequestTracker requestTracker, String cardId)
			throws AbstractException;

	Product deactivateProduct(AbstractRequestTracker requestTracker,
			String productId) throws AbstractException;

	int saveDocument(AbstractRequestTracker requestTracker, Document document)
			throws AbstractException;

	Document findDocumentByName(AbstractRequestTracker abstractRequestTracker,
			DocumentFilter filter);

	Document updateDocument(AbstractRequestTracker abstractRequestTracker,
			DocumentFilter filter, String content);

	void updateProductRentalPeriod(AbstractRequestTracker requestTracker,
			String shipmentId, String cancelledBY) throws AbstractException;
	
	String getAPIVersion(AbstractRequestTracker requestTracker)
			throws AbstractException;
	
	int removeProduct(AbstractRequestTracker requestTracker, String id)
			throws AbstractException;

}
