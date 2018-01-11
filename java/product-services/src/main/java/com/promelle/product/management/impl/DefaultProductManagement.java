package com.promelle.product.management.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.common.config.AbstractConfiguration;
import com.promelle.dto.AbstractAuditDTO;
import com.promelle.exception.AbstractException;
import com.promelle.paging.Paging;
import com.promelle.payment.dto.Payment;
import com.promelle.payment.dto.PaymentCustomer;
import com.promelle.payment.stripe.dto.StripeInfo;
import com.promelle.payment.stripe.handler.StripePaymentHandler;
import com.promelle.payment.utils.PaymentUtils;
import com.promelle.product.constants.OrderStatus;
import com.promelle.product.constants.PaymentMode;
import com.promelle.product.constants.ProductListAs;
import com.promelle.product.constants.RedeemStatus;
import com.promelle.product.constants.ShipmentStatus;
import com.promelle.product.dao.CartDao;
import com.promelle.product.dao.ChargeHistoryDao;
import com.promelle.product.dao.DocumentDao;
import com.promelle.product.dao.EarningDao;
import com.promelle.product.dao.FavoriteDao;
import com.promelle.product.dao.InvoiceDao;
import com.promelle.product.dao.OrderAddressDao;
import com.promelle.product.dao.OrderDao;
import com.promelle.product.dao.OrderItemDao;
import com.promelle.product.dao.PaymentsDao;
import com.promelle.product.dao.ProductDao;
import com.promelle.product.dao.ProductMessageConversationDao;
import com.promelle.product.dao.ProductMessageDao;
import com.promelle.product.dao.ReviewDao;
import com.promelle.product.dao.ShipmentDao;
import com.promelle.product.dao.UserDao;
import com.promelle.product.dao.mapper.ProductMapper;
import com.promelle.product.dto.CartItem;
import com.promelle.product.dto.ChargeHistory;
import com.promelle.product.dto.Document;
import com.promelle.product.dto.DressRentalItem;
import com.promelle.product.dto.Earning;
import com.promelle.product.dto.Favorite;
import com.promelle.product.dto.Invoice;
import com.promelle.product.dto.Order;
import com.promelle.product.dto.OrderAddress;
import com.promelle.product.dto.OrderItem;
import com.promelle.product.dto.PaymentCard;
import com.promelle.product.dto.Payments;
import com.promelle.product.dto.Product;
import com.promelle.product.dto.ProductMessage;
import com.promelle.product.dto.Review;
import com.promelle.product.dto.ReviewStats;
import com.promelle.product.dto.Services;
import com.promelle.product.dto.Shipment;
import com.promelle.product.dto.User;
import com.promelle.product.filter.CartItemFilter;
import com.promelle.product.filter.ChargeHistoryFilter;
import com.promelle.product.filter.DocumentFilter;
import com.promelle.product.filter.EarningFilter;
import com.promelle.product.filter.FavoriteFilter;
import com.promelle.product.filter.InvoiceFilter;
import com.promelle.product.filter.OrderFilter;
import com.promelle.product.filter.OrderItemFilter;
import com.promelle.product.filter.PaymentsFilter;
import com.promelle.product.filter.ProductFilter;
import com.promelle.product.filter.ProductMessageFilter;
import com.promelle.product.filter.ReviewFilter;
import com.promelle.product.filter.ShipmentFilter;
import com.promelle.product.management.ProductManagement;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.PagedList;
import com.promelle.response.Pagination;
import com.promelle.sort.SortOrder;
import com.promelle.sort.SortRule;
import com.promelle.sort.SortRules;
import com.promelle.topic.message.TopicMessage;
import com.promelle.topic.producer.MessageProducer;
import com.promelle.utils.JsonUtils;
import com.promelle.utils.UUIDUtils;
import com.stripe.model.Charge;

/**
 * This interface is intended for providing interactions between dao classes
 * related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class DefaultProductManagement implements ProductManagement {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DefaultProductManagement.class.getSimpleName());
	private ProductDao productDao;
	private CartDao cartDao;
	private OrderItemDao orderItemDao;
	private OrderDao orderDao;
	private InvoiceDao invoiceDao;
	private ReviewDao reviewDao;
	private EarningDao earningDao;
	private ProductMessageDao messageDao;
	private OrderAddressDao orderAddressDao;
	private ShipmentDao shipmentDao;
	private ChargeHistoryDao chargeHistoryDao;
	private MessageProducer messageProducer;
	private StripePaymentHandler paymentHandler;
	private ProductMessageConversationDao messageConversationDao;
	private FavoriteDao favoriteDao;
	private PaymentsDao paymentsDao;
	private UserDao userDao;
	private DocumentDao documentDao;
	private Services services;

	private static final String PRODUCT_CREATED = AbstractConfiguration
			.getProperty("product.created");
	private static final String PRODUCT_UPDATED = AbstractConfiguration
			.getProperty("product.updated");
	private static final String PRODUCT_DELETED = AbstractConfiguration
			.getProperty("product.deleted");
	private static final String PRODUCT_ACTIVATED = AbstractConfiguration
			.getProperty("product.activated");

	private static final String REVIEW_CREATED = AbstractConfiguration
			.getProperty("review.created");
	private static final String REVIEW_UPDATED = AbstractConfiguration
			.getProperty("review.updated");
	private static final String REVIEW_DELETED = AbstractConfiguration
			.getProperty("review.deleted");

	private static final String PRODUCT_MESSAGE_CREATED = AbstractConfiguration
			.getProperty("product.message.created");
	private static final String PRODUCT_MESSAGE_DELETED = AbstractConfiguration
			.getProperty("product.message.deleted");

	private static final String ORDER_PLACED = AbstractConfiguration
			.getProperty("order.placed");

	private static final String EARNING_AWAITING = AbstractConfiguration
			.getProperty("earning.awaiting");
	private static final String EARNING_REDEEMED = AbstractConfiguration
			.getProperty("earning.redeemed");
	private static final String EARNING_CANCELLED = AbstractConfiguration
			.getProperty("earning.cancelled");

	private static final String SHIPMENT_DELIVERED = AbstractConfiguration
			.getProperty("shipment.delivered");
	private static final String SHIPMENT_COMPLETED = AbstractConfiguration
			.getProperty("shipment.completed");

	private static final String STRIPE_API_KEY = "sk_test_loU2vv3etEbg8Uixko45p2Hj";
//	private static final String STRIPE_API_KEY = "sk_live_3ibicaPCIaYlwAZA0p00nSFc";

	public DefaultProductManagement(ProductDao productDao, CartDao cartDao,
			OrderItemDao orderItemDao, OrderDao orderDao,
			InvoiceDao invoiceDao, ReviewDao reviewDao,
			ProductMessageDao messageDao, OrderAddressDao orderAddressDao,
			ShipmentDao shipmentDao, EarningDao earningDao,
			ChargeHistoryDao chargeHistoryDao,
			ProductMessageConversationDao messageConversationDao,
			StripePaymentHandler paymentHandler,
			MessageProducer messageProducer, FavoriteDao favouriteDao,
			PaymentsDao paymentsDao, UserDao userDao, DocumentDao documentDao,
			Services services) {
		super();
		this.productDao = productDao;
		this.cartDao = cartDao;
		this.orderItemDao = orderItemDao;
		this.orderDao = orderDao;
		this.invoiceDao = invoiceDao;
		this.reviewDao = reviewDao;
		this.messageDao = messageDao;
		this.orderAddressDao = orderAddressDao;
		this.shipmentDao = shipmentDao;
		this.earningDao = earningDao;
		this.chargeHistoryDao = chargeHistoryDao;
		this.messageConversationDao = messageConversationDao;
		this.paymentHandler = paymentHandler;
		this.messageProducer = messageProducer;
		this.favoriteDao = favouriteDao;
		this.paymentsDao = paymentsDao;
		this.userDao = userDao;
		this.documentDao = documentDao;
		this.services = services;
	}

	@Override
	public String addProduct(AbstractRequestTracker requestTracker,
			Product product) throws AbstractException {
		if (StringUtils.isBlank(product.getId())) {
			product.setId(UUIDUtils.getUUID());
		}
		product.setStatus(2);

		if (StringUtils.isBlank(product.getListAs())) {
			product.setListAs(ProductListAs.UNUSED.name());
		}
		productDao.save(requestTracker, product);
		TopicMessage msg = new TopicMessage(requestTracker, PRODUCT_CREATED);
		msg.setData(product.toString());
		messageProducer.sendMessage(msg);
		return product.getId();
	}

	@Override
	public boolean updateProduct(AbstractRequestTracker requestTracker,
			Product product, ProductFilter filter) throws AbstractException {
		if (productDao.update(requestTracker, filter, product) > 0) {
			if (StringUtils.isNotBlank(product.getTitle())) {
				List<Product> dbProducts = productDao.list(filter, null, null)
						.getObjects();
				for (Product dbProduct : dbProducts) {
					messageDao.updateProductTitle(requestTracker,
							dbProduct.getId(), dbProduct.getTitle());
				}
			}
			Map<String, Object> map = new HashMap<>();
			map.put(TopicMessage.F_DATA, product);
			map.put(TopicMessage.F_FILTER, filter);

			TopicMessage msg = new TopicMessage(requestTracker, PRODUCT_UPDATED);
			// msg.setData(JsonUtils.getJsonString(map));
			msg.setData(product.toString());
			messageProducer.sendMessage(msg);
			return true;
		}
		return false;
	}

	@Override
	public Product activateProduct(AbstractRequestTracker requestTracker,
			String productId) throws AbstractException {
		ProductFilter filter = new ProductFilter();
		filter.setId(productId);
		filter.setStatus(2);
		Product product = new Product();
		product.setStatus(1);
		if (productDao.update(requestTracker, filter, product) > 0) {
			Product dbProduct = productDao.findById(productId);
			TopicMessage msg = new TopicMessage(requestTracker,
					PRODUCT_ACTIVATED);
			msg.setData(dbProduct.toString());
			messageProducer.sendMessage(msg);
			return dbProduct;
		}
		return null;
	}

	@Override
	public PagedList<Product> listProducts(
			AbstractRequestTracker requestTracker, ProductFilter filter,
			Paging paging, SortRules sortRules) throws AbstractException {

		FavoriteFilter favFilter = new FavoriteFilter();
		favFilter.setUserId(filter.getUserId());

		// user's favorite products list
		List<Favorite> favourites = favoriteDao.list(favFilter,
				new Paging(-1, -1), sortRules).getObjects();

		// list of all products
		PagedList<Product> productsList = productDao.list(filter, paging,
				sortRules);
		List<Product> products = productsList.getObjects();

		// PEA-591 related changes
		if (filter.getIsLended() != null && filter.getIsLended()) {

			List<Product> lendedProducts = new ArrayList<Product>();

			for (Product product : products) {
				OrderItemFilter itemFilter = new OrderItemFilter();
				itemFilter.setProductId(product.getId());
				itemFilter.setIsShipmentExists(true);
				PagedList<OrderItem> orderItemList = orderItemDao.list(
						itemFilter, paging, sortRules);
				if (orderItemList.getObjects() != null
						&& orderItemList.getObjects().size() > 0) {
					lendedProducts.add(product);
				}
			}

			PagedList<Product> newPagedList = new PagedList<Product>();
			newPagedList.setObjects(lendedProducts);
			Pagination pagination = productsList.getPagination();
			pagination.setCount(lendedProducts.size());
			newPagedList.setPagination(pagination);

			productsList = newPagedList;
			products = productsList.getObjects();
		}
		for (Product product : products) {
			for (Favorite favorite : favourites) {
				if (favorite.getProductId().equalsIgnoreCase(product.getId())) {
					product.setFavorite(true);
				}
			}
			product.setOwnerDetails(userDao.findById(requestTracker,
					product.getOwnerId()));
			product.setInsuranceAmount(PaymentUtils
					.getInsuranceAmountFromPrice(product.getRentalPrice()));
		}
		System.out.println("Product List # Returning = " +productsList.getObjects().size());
		return productsList;
	}

	@Override
	public int deleteProduct(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		if (productDao.softDelete(requestTracker, id) > 0) {
			Product dbProduct = productDao.findById(id);

			TopicMessage msg = new TopicMessage(requestTracker, PRODUCT_DELETED);
			msg.setData(dbProduct.toString());
			messageProducer.sendMessage(msg);
			return 1;
		}
		return 0;
	}

	public Product findProductById(AbstractRequestTracker requestTracker,
			String id) throws AbstractException {
		Product product = productDao.findById(id);
		if (null != product) {
			product.setInsuranceAmount(PaymentUtils
					.getInsuranceAmountFromPrice(product.getRentalPrice()));
		}
		return product;
	}

	@Override
	public boolean updatePic(AbstractRequestTracker requestTracker, String id,
			String tag, String url) throws AbstractException {
		productDao.updateImage(id, tag, url);
		return true;
	}

	@Override
	public String addProductToCart(AbstractRequestTracker requestTracker,
			CartItem cartItem) throws AbstractException {
		cartDao.save(requestTracker, cartItem);
		return cartItem.getId();
	}

	@Override
	public List<CartItem> getUserCart(AbstractRequestTracker requestTracker,
			String userId) throws AbstractException {
		CartItemFilter filter = new CartItemFilter();
		filter.setUserId(userId);
		return cartDao.list(filter, null, null).getObjects();
	}

	@Override
	public String removeProductFromCart(AbstractRequestTracker requestTracker,
			String cartItemId) throws AbstractException {
		cartDao.deleteById(cartItemId);
		return cartItemId;
	}

	@Override
	public Order placeOrder(AbstractRequestTracker requestTracker, Order order)
			throws AbstractException {
		if (order.getOrderStatus() == null) {
			order.setOrderStatus(OrderStatus.ORDER_PENDING.name());
			order.setCreatedOn(Calendar.getInstance().getTimeInMillis());
			Map<String, Long> track = new HashMap<String, Long>();
			track.put(OrderStatus.ORDER_PENDING.name(), order.getCreatedOn());
			order.setTrack(track);
		}
		orderDao.save(requestTracker, order);
		List<OrderItem> items = order.getItems();
		for (OrderItem item : items) {
			item.setId(UUIDUtils.getUUID());
			item.setOrderId(order.getId());
			item.setUserId(order.getUserId());
			item.setUserName(order.getUserName());
			orderItemDao.save(requestTracker, item);
		}
		CartItemFilter cartItemFilter = new CartItemFilter();
		cartItemFilter.setUserId(order.getUserId());
		cartDao.delete(cartItemFilter);
		return order;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Invoice updateOrderStatus(AbstractRequestTracker requestTracker,
			String orderId, String status, OrderAddress orderAddress,
			Payment payment, StripeInfo stripeInfo) throws AbstractException {
		Order dbOrder = orderDao.findById(orderId);
		if (dbOrder == null) {
			throw new AbstractException("missing.order")
					.setCustomMessage("Order doesn't exists!");
		}
		if (!OrderStatus.isValidStatus(dbOrder.getOrderStatus(), status)) {
			throw new AbstractException("invalid.status")
					.setCustomMessage("Invalid status!");
		}

		if (StringUtils.isBlank(stripeInfo.getToken())
				&& StringUtils.isBlank(stripeInfo.getCardId())) {
			throw new AbstractException("invalid.payment")
					.setCustomMessage("Invalid payment info!");
		}

		Invoice invoice = null;
		OrderFilter orderFilter = new OrderFilter();
		orderFilter.setId(orderId);
		if (OrderStatus.ORDER_PLACED.name().equalsIgnoreCase(status)) {
			stripeInfo.setApiKey(STRIPE_API_KEY);

			// Add order_id as meta-data with charge history
			// Code changes for future payment implementation
			Map<String, Object> metaData = new HashMap<String, Object>();
			metaData.put("orderId", orderId);
			payment.setMetaData(metaData);

			String stripeCustomerId = null;
			if (StringUtils.isNotBlank(stripeInfo.getCardId())) {
				stripeCustomerId = paymentsDao.findById(requestTracker,
						stripeInfo.getCardId()).getStripeCustomerId();
			} else {
				stripeCustomerId = paymentHandler.createStripeCustomer(payment,
						stripeInfo).getCustomerId();
				Payments payments = new Payments();
				payments.setId(UUIDUtils.getUUID());
				payments.setUserId(dbOrder.getUserId());
				payments.setStripeCustomerId(stripeCustomerId);
				paymentsDao.save(requestTracker, payments);
			}

			Charge charge;
			if (StringUtils.isNotBlank(stripeCustomerId)) {
				stripeInfo.setCustomerId(stripeCustomerId);
				charge = paymentHandler.handlePaymentUsingCustomer(payment,
						stripeInfo);
			} else {
				if (StringUtils.isBlank(stripeInfo.getToken()))
					throw new AbstractException("card.error")
							.setCustomMessage("Invalid card details!");

				charge = paymentHandler.handlePayment(payment, stripeInfo);
			}

			ChargeHistory chargeHistory = new ChargeHistory();
			chargeHistory.setId(UUIDUtils.getUUID());
			chargeHistory.setOrderId(orderId);
			String chargeStr = charge.toString().substring(
					charge.toString().indexOf("{"));
			try {
				chargeHistory.setCharge(new ObjectMapper().readValue(chargeStr,
						Map.class));
			} catch (Exception e) {
				LOGGER.error("Error on stripe", e);
				chargeHistory.setChargeStr(chargeStr);
			}
			chargeHistoryDao.save(requestTracker, chargeHistory);
			Order order = new Order();
			order.setOrderStatus(charge.getFailureCode() != null ? OrderStatus.TRANSACTION_FAILED
					.name() : OrderStatus.TRANSACTION_SUCCESSFUL.name());
			Map<String, Long> track = new HashMap<String, Long>();
			track.put(order.getOrderStatus(), Calendar.getInstance()
					.getTimeInMillis());
			order.setTrack(track);
			orderDao.update(requestTracker, orderFilter, order);

			if (order.getOrderStatus().equals(
					OrderStatus.TRANSACTION_FAILED.name()))
				return invoice;

			invoice = new Invoice();
			invoice.setId(UUIDUtils.getUUID());
			invoice.setTransactionId(charge.getId());
			invoice.setUserId(dbOrder.getUserId());
			invoice.setOrderId(orderId);
			invoice.setPaymentMode(PaymentMode.STRIPE.name());
			invoice.setAmount(Double.valueOf(charge.getAmount()));
			invoice.setPaymentDate(Calendar.getInstance().getTimeInMillis());
			invoiceDao.save(requestTracker, invoice);
			if (StringUtils.isNotBlank(orderAddress.getId())) {
				orderAddress.setId(UUIDUtils.getUUID());
			}
			orderAddress.setOrderId(orderId);
			orderAddress.setUserId(dbOrder.getUserId());
			orderAddressDao.save(requestTracker, orderAddress);
			Map<String, List<OrderItem>> ownerItemMap = new HashMap<String, List<OrderItem>>();
			Map<String, String> ownerMap = new HashMap<String, String>();
			List<OrderItem> orderItems = orderItemDao.findByOrderId(
					requestTracker, orderId);
			for (OrderItem orderItem : orderItems) {
				Product product = productDao.findById(orderItem.getProductId());
				orderItem.setProduct(product);
				List<OrderItem> ownerItems = ownerItemMap.containsKey(product
						.getOwnerId()) ? ownerItemMap.get(product.getOwnerId())
						: new ArrayList<OrderItem>();
				ownerItems.add(orderItem);
				ownerItemMap.put(product.getOwnerId(), ownerItems);
				ownerMap.put(product.getOwnerId(), product.getOwnerName());
				productDao.addRentalPeriod(orderItem.getProductId(),
						orderItem.getStartDate(), orderItem.getEndDate());
			}
			List<Shipment> shipments = new ArrayList<Shipment>();
			for (Entry<String, List<OrderItem>> entry : ownerItemMap.entrySet()) {
				String shipmentId = UUIDUtils.getUUID();
				Shipment shipment = new Shipment();
				shipment.setId(shipmentId);
				shipment.setUserId(dbOrder.getUserId());
				shipment.setUserName(dbOrder.getUserName());
				shipment.setOrderId(dbOrder.getId());
				shipment.setOwnerId(entry.getKey());
				shipment.setOwnerName(ownerMap.get(entry.getKey()));
				shipment.setShipmentStatus(ShipmentStatus.SHIPMENT_PENDING
						.name());
				Map<String, Long> shipmentTrack = new HashMap<String, Long>();
				shipmentTrack.put(ShipmentStatus.SHIPMENT_PENDING.name(),
						Calendar.getInstance().getTimeInMillis());
				shipment.setTrack(shipmentTrack);
				shipment.setCreatedOn(Calendar.getInstance().getTimeInMillis());
				shipment.setUser(userDao.findById(requestTracker,
						dbOrder.getUserId()));
				shipment.setOwner(userDao.findById(requestTracker,
						entry.getKey()));
				Product shipmentProduct = null;
				List<OrderItem> items = entry.getValue();
				for (OrderItem orderItem : items) {
					orderItemDao.updateShipment(requestTracker,
							orderItem.getId(), shipmentId);
					shipmentProduct = productDao.findById(orderItem
							.getProductId());
				}

				shipmentProduct.setRentedOn(new ArrayList<Map<String, Long>>());
				shipmentProduct.setNotAvailableOn(new ArrayList<Long>());

				shipment.setProduct(shipmentProduct);
				if (payment.getDeliveryCharge() != null)
					shipment.setDeliveryCharge(payment.getDeliveryCharge());

				if (payment.getInsuranceAmount() != null)
					shipment.setInsuranceAmount(payment.getInsuranceAmount());

				shipmentDao.save(requestTracker, shipment);
				shipment.setItems(items);
				shipments.add(shipment);
			}
			track.clear();
			track.put(OrderStatus.ORDER_PLACED.name(), Calendar.getInstance()
					.getTimeInMillis());
			order.setTrack(track);
			order.setOrderStatus(OrderStatus.ORDER_PLACED.name());
			orderDao.update(requestTracker, orderFilter, order);
			// Set transactionId to Order #305
			order.setTransactionId(invoice.getTransactionId());

			TopicMessage msg = new TopicMessage(requestTracker, ORDER_PLACED);
			order.setUserName(dbOrder.getUserName());
			order.setShipments(shipments);
			msg.setData(order.toString());
			messageProducer.sendMessage(msg);
		} else if (OrderStatus.ORDER_CANCELLED.name().equalsIgnoreCase(status)) {
			Order order = new Order();
			Map<String, Long> track = new HashMap<String, Long>();
			track.put(OrderStatus.ORDER_CANCELLED.name(), Calendar
					.getInstance().getTimeInMillis());
			order.setTrack(track);
			order.setOrderStatus(OrderStatus.ORDER_CANCELLED.name());
			orderDao.update(requestTracker, orderFilter, order);

			// PEA-536 unblockRentalPeriod for cancelled order
			List<OrderItem> itemsList = orderItemDao.findByOrderId(
					requestTracker, orderFilter.getId());
			for (OrderItem item : itemsList) {
				List<Map<String, Long>> rentedOn = productDao.findById(
						item.getProductId()).getRentedOn();
				for (Map<String, Long> map : rentedOn) {
					if (map.get(ProductMapper.START).longValue() == item
							.getStartDate()
							|| map.get(ProductMapper.END).longValue() == item
									.getEndDate()) {
						rentedOn.remove(map);
						break;
					}
				}
				productDao.updateRentalPeriod(item.getProductId(), rentedOn);
			}

		}
		return invoice;
	}

	@Override
	public Order getOrder(AbstractRequestTracker requestTracker,
			String orderId, Boolean includeItems, Boolean includeShipments,
			Boolean includeEarnings) throws AbstractException {
		Order order = orderDao.findById(orderId);
		populateOrder(requestTracker, order, includeItems, includeShipments,
				includeEarnings);

		// Add last 4 digits of payment card
		ChargeHistoryFilter filter = new ChargeHistoryFilter();
		filter.setOrderId(orderId);
		ChargeHistory chargeHistory = chargeHistoryDao.findOne(filter);

		if (chargeHistory != null) {
			@SuppressWarnings("unchecked")
			Map<String, Object> sourceMap = (Map<String, Object>) chargeHistory
					.getCharge().get("source");
			order.setCcLast4((String) sourceMap.get("last4"));
			order.setCardBranding((String) sourceMap.get("brand"));
		}

		List<Shipment> shipmentsList = order.getShipments();
		for (Shipment shipment : shipmentsList) {

			if (shipment.getUser() == null)
				shipment.setUser(userDao.findById(requestTracker,
						shipment.getUserId()));

			if (shipment.getOwner() == null)
				shipment.setOwner(userDao.findById(requestTracker,
						shipment.getOwnerId()));
		}

		return order;
	}

	@Override
	public void populateOrder(AbstractRequestTracker requestTracker,
			Order order, Boolean includeItems, Boolean includeShipments,
			Boolean includeEarnings) throws AbstractException {
		if (includeItems != null && includeItems) {
			List<OrderItem> items = orderItemDao.findByOrderId(requestTracker,
					order.getId());
			for (OrderItem item : items) {
				item.setProduct(productDao.findById(item.getProductId()));
			}
			order.setItems(items);
		}
		if (includeShipments != null && includeShipments) {
			OrderAddress address = orderAddressDao.findByOrderId(
					requestTracker, order.getId());
			order.setShippingAddress(address);
			List<OrderItem> items = orderItemDao.findByOrderId(requestTracker,
					order.getId());
			Map<String, List<OrderItem>> shipmentMap = new HashMap<String, List<OrderItem>>();
			for (OrderItem item : items) {
				item.setProduct(productDao.findById(item.getProductId()));
				String shipmentId = item.getShipmentId();
				List<OrderItem> shipmentItems = shipmentMap
						.containsKey(shipmentId) ? shipmentMap.get(shipmentId)
						: new ArrayList<OrderItem>();
				shipmentItems.add(item);
				shipmentMap.put(shipmentId, shipmentItems);
			}
			List<Shipment> shipments = shipmentDao.findByOrderId(
					requestTracker, order.getId());

			Boolean isDelivery = null;
			for (Shipment shipment : shipments) {
				List<OrderItem> itemsList = shipmentMap.get(shipment.getId());
				shipment.setItems(itemsList);

				if (shipment.getInsuranceAmount() == 0.00) {
					for (OrderItem item : itemsList) {
						shipment.setInsuranceAmount(PaymentUtils
								.getInsuranceAmountFromPrice(item.getPrice()));
					}
				}
				Earning earning = null;
				if (includeEarnings != null && includeEarnings) {
					earning = earningDao.findByShipmentId(requestTracker,
							shipment.getId());
				}

				if (earning != null) {
					shipment.setEarning(earning);
				} else {

					// TODO: Should change logic according to order history data
					String lenderSchoolId;
					String renterSchoolId;
					if (shipment.getUser() != null
							&& shipment.getOwner() != null) {

						renterSchoolId = shipment.getUser().getSchoolId();
						lenderSchoolId = shipment.getOwner().getSchoolId();
					} else {
						lenderSchoolId = userDao.findById(requestTracker,
								shipment.getOwnerId()).getSchoolId();
						renterSchoolId = userDao.findById(requestTracker,
								shipment.getUserId()).getSchoolId();
					}
					isDelivery = (lenderSchoolId != null && lenderSchoolId
							.equals(renterSchoolId));
				}
			}
			order.setShipments(shipments);
			order.setInPersonDelivery(isDelivery);
		}
	}

	@Override
	public PagedList<Order> getOrderHistory(
			AbstractRequestTracker requestTracker, OrderFilter orderFilter,
			Paging paging) throws AbstractException {
		orderFilter.setExcludePending(true);
		SortRules sortRules = new SortRules();
		sortRules
				.add(new SortRule(AbstractAuditDTO.CREATED_ON, SortOrder.DESC));
		PagedList<Order> pagedList = orderDao.list(orderFilter, paging,
				sortRules);
		List<Order> orders = pagedList.getObjects();
		for (Order order : orders) {
			populateOrder(requestTracker, order, orderFilter.getIncludeItems(),
					orderFilter.getIncludeShipments(),
					orderFilter.getIncludeEarnings());
		}
		return pagedList;
	}

	@Override
	public PagedList<OrderItem> getUserRentedProducts(
			AbstractRequestTracker requestTracker, String userId, Paging paging)
			throws AbstractException {
		OrderItemFilter filter = new OrderItemFilter();
		filter.setUserId(userId);
		return orderItemDao.list(filter, paging, null);
	}

	@Override
	public PagedList<Invoice> getUserInvoices(
			AbstractRequestTracker requestTracker, String userId, Paging paging)
			throws AbstractException {
		InvoiceFilter filter = new InvoiceFilter();
		filter.setUserId(userId);
		return invoiceDao.list(filter, paging, null);
	}

	@Override
	public String addReview(AbstractRequestTracker requestTracker, Review review)
			throws AbstractException {
		if (StringUtils.isBlank(review.getId())) {
			review.setId(UUIDUtils.getUUID());
		}
		ReviewFilter reviewFilter = new ReviewFilter();
		reviewFilter.setProductId(review.getProductId());
		reviewFilter.setUserId(review.getUserId());
		Review dbReview = reviewDao.findOne(reviewFilter);
		if (dbReview != null) {
			throw new AbstractException("review.already.exists")
					.setCustomMessage("A review from you already exists!");
		}
		OrderItemFilter orderItemFilter = new OrderItemFilter();
		orderItemFilter.setProductId(review.getProductId());
		orderItemFilter.setUserId(review.getUserId());
		OrderItem orderItem = orderItemDao.findOne(orderItemFilter);
		if (orderItem == null) {
			throw new AbstractException("order.not.placed")
					.setCustomMessage("Please place your order in order to review.");
		}
		reviewDao.save(requestTracker, review);
		TopicMessage msg = new TopicMessage(requestTracker, REVIEW_CREATED);
		msg.setData(review.toString());
		messageProducer.sendMessage(msg);
		calculateProductRating(requestTracker, review.getProductId());
		return review.getId();
	}

	@Override
	public boolean updateReview(AbstractRequestTracker requestTracker,
			Review review, ReviewFilter filter) throws AbstractException {
		if (reviewDao.update(requestTracker, filter, review) > 0) {
			Map<String, Object> map = new HashMap<>();
			map.put(TopicMessage.F_DATA, review);
			map.put(TopicMessage.F_FILTER, filter);

			TopicMessage msg = new TopicMessage(requestTracker, REVIEW_UPDATED);
			msg.setData(JsonUtils.getJsonString(map));
			messageProducer.sendMessage(msg);
			return true;
		}
		return false;
	}

	@Override
	public PagedList<Review> listReviews(AbstractRequestTracker requestTracker,
			ReviewFilter filter, Paging paging, SortRules sortRules)
			throws AbstractException {
		return reviewDao.list(filter, paging, sortRules);
	}

	@Override
	public int deleteReview(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		if (reviewDao.softDelete(requestTracker, id) > 0) {
			TopicMessage msg = new TopicMessage(requestTracker, REVIEW_DELETED);
			msg.setData(id);
			messageProducer.sendMessage(msg);
			return 1;
		}
		return 0;
	}

	@Override
	public String sendMessage(AbstractRequestTracker requestTracker,
			ProductMessage message) throws AbstractException {
		if (StringUtils.isBlank(message.getId())) {
			message.setId(UUIDUtils.getUUID());
		}
		messageDao.save(requestTracker, message);
		TopicMessage msg = new TopicMessage(requestTracker,
				PRODUCT_MESSAGE_CREATED);
		msg.setData(message.toString());
		messageProducer.sendMessage(msg);
		return message.getId();
	}

	@Override
	public PagedList<ProductMessage> listMessages(
			AbstractRequestTracker requestTracker, ProductMessageFilter filter,
			Paging paging, SortRules sortRules) throws AbstractException {
		if (StringUtils.isNotBlank(filter.getReceiverId())
				&& StringUtils.isBlank(filter.getSenderId())) {
			return messageDao.getUserInbox(filter.getReceiverId());
		}
		return messageDao.list(filter, paging, sortRules);
	}

	@Override
	public int deleteMessage(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		ProductMessage message = messageDao.findById(id);
		int count = messageDao.softDeleteMessage(requestTracker, id,
				message.getSenderId());
		count += messageDao.softDeleteMessage(requestTracker, id,
				message.getReceiverId());
		if (count > 0) {
			ProductMessageFilter filter = new ProductMessageFilter();
			filter.setId(id);
			Map<String, Object> map = new HashMap<>();
			map.put(TopicMessage.F_FILTER, filter);
			TopicMessage msg = new TopicMessage(requestTracker,
					PRODUCT_MESSAGE_DELETED);
			msg.setData(JsonUtils.getJsonString(map));
			messageProducer.sendMessage(msg);
			return 1;
		}
		return 0;
	}

	@Override
	public int deleteMessage(AbstractRequestTracker requestTracker, String id,
			String userId) throws AbstractException {
		if (messageDao.softDeleteMessage(requestTracker, id, userId) > 0) {
			ProductMessageFilter filter = new ProductMessageFilter();
			filter.setId(id);
			Map<String, Object> map = new HashMap<>();
			map.put(TopicMessage.F_FILTER, filter);
			TopicMessage msg = new TopicMessage(requestTracker,
					PRODUCT_MESSAGE_DELETED);
			msg.setData(JsonUtils.getJsonString(map));
			messageProducer.sendMessage(msg);
			return 1;
		}
		return 0;
	}

	@Override
	public void calculateProductRating(AbstractRequestTracker requestTracker,
			String productId) throws AbstractException {
		ReviewStats reviewStats = reviewDao.calculateAverageRating(productId);
		ProductFilter filter = new ProductFilter();
		filter.setId(productId);
		Product product = new Product();
		product.setRating(reviewStats.getAverageRating());
		product.setReviewCount(reviewStats.getReviewCount());
		productDao.update(requestTracker, filter, product);

		Map<String, Object> map = new HashMap<>();
		map.put(TopicMessage.F_DATA, product);
		map.put(TopicMessage.F_FILTER, filter);

		TopicMessage msg = new TopicMessage(requestTracker, PRODUCT_UPDATED);
		msg.setData(JsonUtils.getJsonString(map));
		messageProducer.sendMessage(msg);
	}

	@Override
	public boolean isReviewPossible(AbstractRequestTracker requestTracker,
			String productId, String userId) {
		ReviewFilter reviewFilter = new ReviewFilter();
		reviewFilter.setProductId(productId);
		reviewFilter.setUserId(userId);
		Review dbReview = reviewDao.findOne(reviewFilter);
		if (dbReview != null) {
			return false;
		}
		OrderItemFilter orderItemFilter = new OrderItemFilter();
		orderItemFilter.setProductId(productId);
		orderItemFilter.setUserId(userId);
		return orderItemDao.findOne(orderItemFilter) != null;
	}

	@Override
	public PagedList<Shipment> getShipmentHistory(
			AbstractRequestTracker requestTracker,
			ShipmentFilter shipmentFilter, Paging paging)
			throws AbstractException {
		SortRules sortRules = new SortRules();
		sortRules
				.add(new SortRule(AbstractAuditDTO.CREATED_ON, SortOrder.DESC));

		PagedList<Shipment> shipmentPagedList = populateShipment(
				requestTracker,
				shipmentDao.list(shipmentFilter, paging, sortRules));
		return shipmentPagedList;
	}

	private PagedList<Shipment> populateShipment(
			AbstractRequestTracker requestTracker,
			PagedList<Shipment> shipmentList) throws AbstractException {
		List<Shipment> shipments = shipmentList.getObjects();
		for (Shipment shipment : shipments) {
			List<OrderItem> items = orderItemDao.findByOrderId(requestTracker,
					shipment.getOrderId());
			for (OrderItem item : items) {
				Product product = productDao.findById(item.getProductId());
				item.setProduct(product);
			}
			shipment.setItems(items);
		}
		shipmentList.setObjects(shipments);
		return shipmentList;
	}

	@Override
	public PagedList<OrderItem> listItems(
			AbstractRequestTracker requestTracker,
			OrderItemFilter orderItemFilter, Paging paging, SortRules sortRules)
			throws AbstractException {
		PagedList<OrderItem> pagedList = orderItemDao.list(orderItemFilter,
				paging, sortRules);
		if (orderItemFilter.getIncludeShipmentStatus() != null
				&& orderItemFilter.getIncludeShipmentStatus()) {
			List<OrderItem> items = pagedList.getObjects();
			for (OrderItem item : items) {
				item.setShipmentStatus(shipmentDao.getShipmentStatus(
						requestTracker, item.getShipmentId()));
			}
		}
		return pagedList;
	}

	@Override
	public List<OrderItem> getShipmentItems(
			AbstractRequestTracker requestTracker, String shipmentId)
			throws AbstractException {
		List<OrderItem> items = orderItemDao.findByShipmentId(requestTracker,
				shipmentId);
		Shipment shipment = shipmentDao.findById(shipmentId);

		for (OrderItem item : items) {
			item.setProduct(productDao.findById(item.getProductId()));
			item.setShipmentStatus(shipment.getShipmentStatus());
		}
		return items;
	}

	@Override
	public String updateShipmentStatus(AbstractRequestTracker requestTracker,
			String shipmentId, String status) throws AbstractException {
		Shipment dbShipment = shipmentDao.findById(shipmentId);
		if (dbShipment == null) {
			throw new AbstractException("missing.order")
					.setCustomMessage("Shipment doesn't exists!");
		}
		if (!ShipmentStatus.isValidStatus(dbShipment.getShipmentStatus(),
				status)) {
			throw new AbstractException("invalid.status")
					.setCustomMessage("Invalid status!");
		}
		ShipmentFilter filter = new ShipmentFilter();
		filter.setId(shipmentId);
		Shipment shipment = new Shipment();
		Map<String, Long> track = new HashMap<String, Long>();
		track.put(status, Calendar.getInstance().getTimeInMillis());
		shipment.setTrack(track);
		shipment.setShipmentStatus(status);
		String messageType = null;

		if (ShipmentStatus.SHIPMENT_COMPLETED.name().equalsIgnoreCase(status)) {
			Earning earning = new Earning();
			earning.setUserId(dbShipment.getUserId());
			earning.setUserName(dbShipment.getUserName());
			earning.setShipmentId(dbShipment.getId());
			earning.setOrderId(dbShipment.getOrderId());
			earning.setOwnerId(dbShipment.getOwnerId());
			earning.setOwnerName(dbShipment.getOwnerName());
			List<OrderItem> orderItems = orderItemDao.findByShipmentId(
					requestTracker, shipmentId);
			double price = 0;
			for (OrderItem orderItem : orderItems) {
				price += orderItem.getPrice();
			}

			earning.setRentalPrice(price);

			// TODO: Should change logic according to order history data
			double deliveryCharge = 0.00;

			if (shipment.getUser() != null && shipment.getOwner() != null) {
				deliveryCharge = dbShipment.getDeliveryCharge();
			} else {
				String lenderSchoolId = userDao.findById(requestTracker,
						dbShipment.getOwnerId()).getSchoolId();
				String renterSchoolId = userDao.findById(requestTracker,
						dbShipment.getUserId()).getSchoolId();
				if (lenderSchoolId != null
						&& !lenderSchoolId.equals(renterSchoolId)) {
					deliveryCharge = 5.00;
				}
			}

			earning.setDeliveryCharge(deliveryCharge);
			double percentage = 75.0;
			double redeemPrice = (percentage * price) / 100;
			double redeemableAmount = redeemPrice - deliveryCharge;
			earning.setAmount(redeemPrice);
			earning.setRedeemableAmount(redeemableAmount);
			earning.setPercentage(percentage);
			earning.setRedeemStatus(RedeemStatus.REDEEM.name());
			earning.setCreatedOn(Calendar.getInstance().getTimeInMillis());
			earningDao.save(requestTracker, earning);
			messageType = SHIPMENT_COMPLETED;
		} else if (ShipmentStatus.SHIPMENT_DELIVERED.name().equalsIgnoreCase(
				status)) {
			messageType = SHIPMENT_DELIVERED;
		} else if (ShipmentStatus.SHIPMENT_CANCELLED.name().equalsIgnoreCase(
				status)
				|| ShipmentStatus.SHIPMENT_CANCELLED_BY_LENDER.name()
						.equalsIgnoreCase(status)) {
			updateProductRentalPeriod(requestTracker, shipmentId, "LENDER");
		} else if (ShipmentStatus.SHIPMENT_CANCELLED_BY_RENTER.name()
				.equalsIgnoreCase(status)) {
			updateProductRentalPeriod(requestTracker, shipmentId, "RENTER");
		}

		if (StringUtils.isNotBlank(messageType)) {
			TopicMessage msg = new TopicMessage(requestTracker, messageType);
			msg.setData(dbShipment.toString());
			messageProducer.sendMessage(msg);
		}

		shipmentDao.update(requestTracker, filter, shipment);
		return shipmentId;
	}

	@Override
	public PagedList<Earning> getEarnings(
			AbstractRequestTracker requestTracker, EarningFilter earningFilter,
			Paging paging) throws AbstractException {
		SortRules sortRules = new SortRules();
		sortRules
				.add(new SortRule(AbstractAuditDTO.CREATED_ON, SortOrder.DESC));
		PagedList<Earning> pagedList = earningDao.list(earningFilter, paging,
				sortRules);
		for (Earning earning : pagedList.getObjects()) {
			Shipment shipment = shipmentDao.findById(earning.getShipmentId());
			if (shipment != null)
				earning.setOrderDate(shipment.getCreatedOn());
		}

		return pagedList;
	}

	@Override
	public String updateEarningStatus(AbstractRequestTracker requestTracker,
			Earning earning) throws AbstractException {
		Earning dbEarning = earningDao.findById(earning.getId());
		if (dbEarning == null) {
			throw new AbstractException("missing.order")
					.setCustomMessage("Earning doesn't exists!");
		}
		EarningFilter filter = new EarningFilter();
		filter.setId(earning.getId());
		if (earningDao.update(requestTracker, filter, earning) > 0) {
			String messageType = null;
			if (RedeemStatus.AWAITING.name().equalsIgnoreCase(
					earning.getRedeemStatus())) {
				messageType = EARNING_AWAITING;
			} else if (RedeemStatus.REDEEMED.name().equalsIgnoreCase(
					earning.getRedeemStatus())) {
				messageType = EARNING_REDEEMED;
			} else if (RedeemStatus.CANCELLED.name().equalsIgnoreCase(
					earning.getRedeemStatus())) {
				messageType = EARNING_CANCELLED;
			}

			Earning updatedEarning = earningDao.findById(earning.getId());

			TopicMessage msg = new TopicMessage(requestTracker, messageType);
			msg.setData(updatedEarning.toString());
			messageProducer.sendMessage(msg);
		}
		return earning.getId();
	}

	@Override
	public String getConversationId(AbstractRequestTracker requestTracker,
			String senderId, String receiverId, String productId)
			throws AbstractException {
		return messageConversationDao.getConversationId(requestTracker,
				senderId, receiverId, productId);
	}

	@Override
	public boolean readConveration(AbstractRequestTracker requestTracker,
			String conversationId, String userId) throws AbstractException {
		ProductMessageFilter filter = new ProductMessageFilter();
		filter.setConversationId(conversationId);
		filter.setReceiverId(userId);
		ProductMessage message = new ProductMessage();
		message.setRead(true);
		return messageDao.update(requestTracker, filter, message) > 0;
	}

	@Override
	public int deleteConveration(AbstractRequestTracker requestTracker,
			String conversationId, String userId) throws AbstractException {
		if (messageDao.softDeleteConversation(requestTracker, conversationId,
				userId) > 0) {
			Map<String, Object> map = new HashMap<>();
			ProductMessageFilter filter = new ProductMessageFilter();
			filter.setConversationId(conversationId);
			filter.setReceiverId(userId);
			map.put(TopicMessage.F_FILTER, filter);
			TopicMessage msg = new TopicMessage(requestTracker,
					PRODUCT_MESSAGE_DELETED);
			msg.setData(JsonUtils.getJsonString(map));
			messageProducer.sendMessage(msg);
			return 1;
		}
		return 0;
	}

	@Override
	public int updateFavoriteStatus(AbstractRequestTracker requestTracker,
			FavoriteFilter filter, Favorite favourite) {

		Favorite favoriteDB = favoriteDao.findOne(filter);
		if (favoriteDB == null) {
			if (StringUtils.isBlank(favourite.getId())) {
				favourite.setId(UUIDUtils.getUUID());
			}
			favourite.setStatus(1);
			return favoriteDao.save(requestTracker, favourite);
		} else {
			return favoriteDao.deleteById(favoriteDB.getId());
		}
	}

	@Override
	public PagedList<Product> listFavorites(
			AbstractRequestTracker requestTracker, FavoriteFilter filter,
			ProductFilter productFilter, Paging paging, SortRules sortRules)
			throws AbstractException {

		PagedList<Favorite> favoritePagedList = favoriteDao.list(filter,
				paging, sortRules);
		// Pagination favPagination = favoritePagedList.getPagination();
		List<Product> listProducts = new ArrayList<Product>();

		for (Favorite favoriteItem : favoritePagedList.getObjects()) {
			productFilter.setId(favoriteItem.getProductId());
			System.out.println("productFilter = " + productFilter.toString());
			Product product = productDao.findOne(productFilter);
			if (null != product)
				listProducts.add(product);
		}
		PagedList<Product> pagedListProducts = new PagedList<Product>();
		Pagination pagination = new Pagination();
		pagination.setCount(listProducts.size());
		pagination.setTotal(listProducts.size());
		pagedListProducts.setPagination(pagination);
		pagedListProducts.setObjects(listProducts);
		return pagedListProducts;
	}

	@Override
	public PagedList<PaymentCard> listSavedPaymentCard(
			AbstractRequestTracker requestTracker, PaymentsFilter filter,
			Paging paging) throws AbstractException {

		PagedList<Payments> paymentsList = paymentsDao.list(requestTracker,
				filter, paging);

		List<PaymentCard> cardsList = new ArrayList<PaymentCard>();
		StripeInfo info = new StripeInfo();
		info.setApiKey(STRIPE_API_KEY);

		for (Payments payments : paymentsList.getObjects()) {
			info.setCustomerId(payments.getStripeCustomerId());
			cardsList.add(new PaymentCard(paymentHandler
					.collectPaymentCard(info), payments.getId()));
		}

		PagedList<PaymentCard> pagedList = new PagedList<PaymentCard>();
		pagedList.setObjects(cardsList);
		pagedList.setPagination(paymentsList.getPagination());

		return pagedList;
	}

	@Override
	public PaymentCard addPaymentCard(AbstractRequestTracker requestTracker,
			StripeInfo stripeInfo, String userId) throws AbstractException {

		if (StringUtils.isBlank(stripeInfo.getToken())
				|| StringUtils.isBlank(userId))
			throw new AbstractException("oops")
					.setCustomMessage("Something went wrong!");

		stripeInfo.setApiKey(STRIPE_API_KEY);

		Payment payment = new Payment();
		payment.setDescription("Add new card");

		PaymentCustomer paymentCustomer = paymentHandler.createStripeCustomer(
				payment, stripeInfo);

		// save details into database for future response
		Payments payments = new Payments();
		payments.setId(UUIDUtils.getUUID());
		payments.setUserId(userId);
		payments.setStripeCustomerId(paymentCustomer.getCustomerId());
		paymentsDao.save(requestTracker, payments);

		PaymentsFilter filter = new PaymentsFilter();
		filter.setUserId(userId);
		filter.setStripeCustomerId(paymentCustomer.getCustomerId());

		LOGGER.info("addPaymentCard filter : " + filter);

		return new PaymentCard(paymentCustomer.getCard(), paymentsDao.findOne(
				requestTracker, filter).getId());
	}

	@Override
	public PagedList<DressRentalItem> listDressRentals(
			AbstractRequestTracker requestTracker,
			ShipmentFilter shipmentFilter, Paging paging, SortRules sortRules)
			throws AbstractException {

		List<OrderItem> items = new ArrayList<OrderItem>();

		PagedList<Shipment> shipmentPagedList = shipmentDao.list(
				shipmentFilter, new Paging(-1, -1), sortRules);

		if (null != shipmentPagedList) {
			for (Shipment shipment : shipmentPagedList.getObjects()) {
				OrderItemFilter orderItemFilter = new OrderItemFilter();
				orderItemFilter.setShipmentId(shipment.getId());
				OrderItem orderItem = orderItemDao.findOne(orderItemFilter);
				if (null != orderItem)
					items.add(orderItem);
			}
		}
		if (items.size() > 0) {
			List<DressRentalItem> listRentedDresses = new ArrayList<DressRentalItem>();

			for (OrderItem item : items) {
				Product product = findProductById(requestTracker,
						item.getProductId());
				if (null != product) {
					// set product object in OrderItem
					item.setProduct(product);
					DressRentalItem rentalItem = new DressRentalItem();
					// set OrderItem in rental object
					rentalItem.setOrderItem(item);
					String lenderId = item.getProduct().getOwnerId();
					User user = userDao.findById(requestTracker, lenderId);
					if (null != user) {
						// set Lender in rental object
						rentalItem.setLender(user);
					}
					user = userDao.findById(requestTracker, item.getUserId());
					if (null != user) {
						// set Renter in rental object
						rentalItem.setRenter(user);
					}
					listRentedDresses.add(rentalItem);
				} else {
					System.out
							.println("oops, this product not found in database!!");
				}
			}

			Collections.sort(listRentedDresses, new DressRentalItem());
			Collections.reverse(listRentedDresses);

			// Pagination after collection sorting
			int startIndex = paging.getOffset() == null ? 10 : ((paging
					.getOffset() < 0 || paging.getOffset() > listRentedDresses
					.size()) ? listRentedDresses.size() : paging.getOffset());

			int endIndex = paging.getLimit() == null ? 10
					: ((paging.getLimit() <= 0 || (paging.getLimit() + paging
							.getOffset()) > listRentedDresses.size()) ? listRentedDresses
							.size() : (paging.getLimit() + paging.getOffset()));
			List<DressRentalItem> list = listRentedDresses.subList(startIndex,
					endIndex);
			listRentedDresses = list;

			PagedList<DressRentalItem> pagedListRentedDresses = new PagedList<DressRentalItem>();
			pagedListRentedDresses.setObjects(listRentedDresses);
			Pagination pagination = shipmentPagedList.getPagination();
			pagination.setCount(listRentedDresses.size());
			pagination.setOffset(paging.getOffset());
			pagination.setLimit(paging.getLimit());
			pagedListRentedDresses.setPagination(pagination);
			return pagedListRentedDresses;
		}
		return new PagedList<DressRentalItem>();
	}

	@Override
	public int removePaymentCard(AbstractRequestTracker requestTracker,
			String id) throws AbstractException {

		Payments payments = new Payments();
		payments.setStatus(0);
		payments.setStripeCustomerId("");
		payments.setModifiedOn(requestTracker.getRequestTimestamp());

		PaymentsFilter filter = new PaymentsFilter();
		filter.setId(id);
		filter.setStatus(1);
		return paymentsDao.update(requestTracker, payments, filter);
	}

	@Override
	public Product deactivateProduct(AbstractRequestTracker requestTracker,
			String productId) throws AbstractException {

		ProductFilter filter = new ProductFilter();
		filter.setId(productId);
		filter.setStatus(2);

		Product product = new Product();
		product.setStatus(3);
		if (productDao.update(requestTracker, filter, product) > 0) {
			Product dbProduct = productDao.findById(productId);
			return dbProduct;
		}
		return null;
	}

	@Override
	public Document findDocumentByName(
			AbstractRequestTracker abstractRequestTracker, DocumentFilter filter) {
		return documentDao.findOne(filter);
	}

	@Override
	public int saveDocument(AbstractRequestTracker requestTracker,
			Document document) throws AbstractException {
		return documentDao.save(requestTracker, document);
	}

	@Override
	public Document updateDocument(
			AbstractRequestTracker abstractRequestTracker,
			DocumentFilter filter, String content) {
		Document document = documentDao.findOne(filter);
		document.setContent(content);
		documentDao.update(abstractRequestTracker, filter, document);
		return document;
	}

	@Override
	public void updateProductRentalPeriod(
			AbstractRequestTracker requestTracker, String shipmentId,
			String cancelledBY) throws AbstractException {

		// PEA-602 unblockRentalPeriod for cancelled order by RENTER/LENDER
		List<OrderItem> itemsList = orderItemDao.findByShipmentId(
				requestTracker, shipmentId);
		for (OrderItem item : itemsList) {
			List<Map<String, Long>> rentedOn = productDao.findById(
					item.getProductId()).getRentedOn();
			long oneDayMilis = (long) (24 * 60 * 60 * 1000);
			int rentalPeriod = (int) ((item.getEndDate() - item.getStartDate()) / oneDayMilis) + 1;

			for (Map<String, Long> map : rentedOn) {
				if (map.get(ProductMapper.START).longValue() == item
						.getStartDate()
						|| map.get(ProductMapper.END).longValue() == item
								.getEndDate()) {
					if (cancelledBY.equalsIgnoreCase("LENDER")) {
						map.replace(ProductMapper.START, 0L);
						map.replace(ProductMapper.END, 0L);
						// rentedOn.remove(map);
					} else if (cancelledBY.equalsIgnoreCase("RENTER")) {
						Shipment shipment = shipmentDao.findById(shipmentId);
						if (ShipmentStatus.SHIPMENT_DELIVERED.name()
								.equalsIgnoreCase(shipment.getShipmentStatus())) {
							if (rentalPeriod == 8) {
								map.replace(
										ProductMapper.END,
										(map.get(ProductMapper.START)
												.longValue() + (2 * oneDayMilis)));
							}
						} else {
							map.replace(ProductMapper.END,
									map.get(ProductMapper.START).longValue());
						}
					}
					break;
				}
			}
			productDao.updateRentalPeriod(item.getProductId(), rentedOn);
		}
	}

	@Override
	public String getAPIVersion(AbstractRequestTracker requestTracker)
			throws AbstractException {
		return services.getVersion();
	}

	@Override
	public int removeProduct(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		return (productDao.softDelete(requestTracker, id) > 0 ? 1 : 0);
	}
}
