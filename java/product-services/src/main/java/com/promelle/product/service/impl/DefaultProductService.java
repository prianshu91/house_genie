package com.promelle.product.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.promelle.amazon.upload.S3Uploader;
import com.promelle.common.service.AbstractService;
import com.promelle.dto.AbstractAuditDTO;
import com.promelle.exception.AbstractException;
import com.promelle.paging.Paging;
import com.promelle.payment.dto.Payment;
import com.promelle.payment.stripe.dto.StripeInfo;
import com.promelle.product.constants.OrderStatus;
import com.promelle.product.constants.PaymentMode;
import com.promelle.product.constants.RedeemStatus;
import com.promelle.product.dto.CartItem;
import com.promelle.product.dto.ChequeAddress;
import com.promelle.product.dto.Document;
import com.promelle.product.dto.Earning;
import com.promelle.product.dto.Favorite;
import com.promelle.product.dto.Order;
import com.promelle.product.dto.OrderAddress;
import com.promelle.product.dto.OrderItem;
import com.promelle.product.dto.Product;
import com.promelle.product.filter.DocumentFilter;
import com.promelle.product.filter.EarningFilter;
import com.promelle.product.filter.FavoriteFilter;
import com.promelle.product.filter.OrderFilter;
import com.promelle.product.filter.OrderItemFilter;
import com.promelle.product.filter.PaymentsFilter;
import com.promelle.product.filter.ProductFilter;
import com.promelle.product.filter.ShipmentFilter;
import com.promelle.product.management.ProductManagement;
import com.promelle.product.service.ProductService;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.promelle.response.PagedList;
import com.promelle.sort.SortOrder;
import com.promelle.sort.SortRule;
import com.promelle.sort.SortRules;
import com.promelle.utils.FileUtils;
import com.promelle.utils.HashUtils;
import com.promelle.utils.ImageUtils;
import com.promelle.utils.JsonUtils;
import com.promelle.utils.UUIDUtils;

/**
 * This class is intended for providing services related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
public class DefaultProductService extends
		AbstractService<Product, ProductFilter> implements ProductService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DefaultProductService.class.getName());
	private static final String PRODUCT_FOLDER = "product";
	private static final String CHEQUE_FOLDER = "cheque";
	private static final String SETTINGS_FOLDER = "settings";
	private static final String DOCUMENT_EXTENSION = ".html";
	private static final String THUMB_EXTENSION = ".jpg";
	private static final int THUMB_HEIGHT;
	private static final int THUMB_WIDTH = THUMB_HEIGHT = 400;
	private ProductManagement productManagement;
	private S3Uploader s3Uploader;

	public DefaultProductService(ProductManagement productManagement,
			S3Uploader s3Uploader) {
		super(Product.class, ProductFilter.class);
		this.productManagement = productManagement;
		this.s3Uploader = s3Uploader;
	}

	@Override
	@Timed(name = "Add product#timer")
	protected Response save(AbstractRequestTracker requestTracker,
			Product product) throws AbstractException {
		try {
			productManagement.addProduct(requestTracker, product);
			return onSuccess(product);
		}catch(AbstractException e) {
			return onError(e);
		}
	}

	@Override
	@Timed(name = "Update product#timer")
	protected Response update(AbstractRequestTracker requestTracker,
			Product product, ProductFilter filter) throws AbstractException {
		// Always send for review after change.
		product.setStatus(2);
		if (productManagement.updateProduct(requestTracker, product, filter)) {
			return onSuccess(product);
		}
		return onError(new AbstractException());
	}

	@Override
	@Timed(name = "Delete product#timer")
	public Response deleteById(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		productManagement.removeProduct(requestTracker, id);
		return onSuccess(id);
	}

	@Override
	public Response findById(AbstractRequestTracker requestTracker, String id)
			throws AbstractException {
		return onSuccess(productManagement.findProductById(requestTracker, id));
	}

	@Override
	@Timed(name = "List products#timer")
	protected Response list(AbstractRequestTracker requestTracker,
			ProductFilter filter, Paging paging, SortRules sortRules)
			throws AbstractException {
		return onSuccess(productManagement.listProducts(requestTracker, filter,
				paging, sortRules));
	}

	@Override
	@Path("/{id}/activate")
	@PUT
	@Timed(name = "Enable user#timer")
	public Response enableProduct(@PathParam("id") String id,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(productManagement.activateProduct(
				new AbstractRequestTracker(request), id));
	}

	@Override
	@Path("/uploadProductImage")
	@POST
	@Timed(name = "Upload picture#timer")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadProductImage(
			@FormDataParam("file") InputStream inputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("id") String id, @FormDataParam("tag") String tag,
			@Context HttpServletRequest request) throws AbstractException {
		AbstractRequestTracker requestTracker = new AbstractRequestTracker(
				request);
		// Get the file extension
		String fileToUpload = fileDetail.getFileName();
		String extension = fileToUpload.substring(
				fileToUpload.lastIndexOf("."), fileToUpload.length());
		String fileName;
		try {
			File tempFile = FileUtils.writeToTempFile(inputStream, extension);
			s3Uploader.upload(ImageUtils.scaleImage(tempFile.getAbsolutePath(),
					THUMB_WIDTH, THUMB_HEIGHT), PRODUCT_FOLDER, id + "_" + tag
					+ "-thumb", extension, true);
			fileName = s3Uploader.upload(tempFile, PRODUCT_FOLDER, id + "_"
					+ tag, extension, true);
		} catch (IOException e) {
			throw new AbstractException(e);
		}
		productManagement.updatePic(requestTracker, id, tag, fileName);
		return Response.ok().entity(fileName).build();
	}

	@Override
	@Path("/uploadChequeImage")
	@POST
	@Timed(name = "Upload Cheque Image#timer")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadChequeImage(
			@FormDataParam("file") InputStream inputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@Context HttpServletRequest request) throws AbstractException {
		// Get the file extension
		String fileToUpload = fileDetail.getFileName();
		String extension = fileToUpload.substring(
				fileToUpload.lastIndexOf("."), fileToUpload.length());
		String fileName;
		try {
			fileName = s3Uploader.upload(inputStream, CHEQUE_FOLDER,
					UUIDUtils.getUUID(), extension, true);
		} catch (IOException e) {
			throw new AbstractException(e);
		}
		return Response.ok().entity(fileName).build();
	}

	@Path("/occasions")
	@GET
	@Override
	public Response listOccasions() throws AbstractException {
		return onSuccess(Arrays.asList(
				"Casual","Dance","Party","Prom", 
				"Homecoming","Graduation","Sweet Sixteen","Business"));
	}
	
	@Path("/categories")
	@GET
	@Override
	public Response listCategories() throws AbstractException {
		InputStream iStream = getClass().getResourceAsStream("/filter.json");
		if(null!=iStream) {			
			BufferedReader streamReader = null;
			try {
				streamReader = new BufferedReader(new InputStreamReader(iStream, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} 
			StringBuilder responseStrBuilder = new StringBuilder();

			String inputStr;
			if(streamReader!=null) {
				try {
					while ((inputStr = streamReader.readLine()) != null) {
					    responseStrBuilder.append(inputStr);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				return onSuccess(new JSONObject(responseStrBuilder.toString()));
			} catch (JSONException e) {
				e.printStackTrace();
			}			
		}		
		return onError("Filter JSON not found!");
	}
	
	@Path("/categories2")
	@GET
	@Override
	public Response listCategories2() throws AbstractException {
		InputStream iStream = getClass().getResourceAsStream("/categories.json");
		if(null!=iStream) {			
			BufferedReader streamReader = null;
			try {
				streamReader = new BufferedReader(new InputStreamReader(iStream, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} 
			StringBuilder responseStrBuilder = new StringBuilder();

			String inputStr;
			if(streamReader!=null) {
				try {
					while ((inputStr = streamReader.readLine()) != null) {
					    responseStrBuilder.append(inputStr);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				return onSuccess(new JSONObject(responseStrBuilder.toString()));
			} catch (JSONException e) {
				e.printStackTrace();
			}			
		}		
		return onError("Filter 2 JSON not found!");
	}
	
	@Path("/colors")
	@GET
	@Override
	public Response listColors() throws AbstractException {
		InputStream iStream = getClass().getResourceAsStream("/colors.json");
		if(null!=iStream) {			
			BufferedReader streamReader = null;
			try {
				streamReader = new BufferedReader(new InputStreamReader(iStream, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} 
			StringBuilder responseStrBuilder = new StringBuilder();

			String inputStr;
			if(streamReader!=null) {
				try {
					while ((inputStr = streamReader.readLine()) != null) {
					    responseStrBuilder.append(inputStr);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				return onSuccess(new JSONObject(responseStrBuilder.toString()));
			} catch (JSONException e) {
				e.printStackTrace();
			}			
		}		
		return onError("Colors JSON not found!");
	}

	@Path("/sizes")
	@GET
	@Override
	public Response listSizes() throws AbstractException {
		Map<String, List<String>> map = new HashMap<String, List<String>>();

		map.put("Women's Regular", Arrays.asList("00", "0", "2", "4", "6", "8",
				"10", "12", "14", "16", "18", "20", "XXS", "XS", "S", "M", "L",
				"XL", "XXL"));

		map.put("Juniors", Arrays.asList("00", "0", "1", "3", "5", "7", "9",
				"11", "13", "15", "17", "19", "XXS", "XS", "S", "M", "L", "XL",
				"XXL"));

		return onSuccess(map);
	}

	@Path("/careInstructions")
	@GET
	@Override
	public Response listCareInstructions() throws AbstractException {
		return onSuccess(Arrays.asList("Machine Wash", "Hand Wash",
				"Spot Clean", "Dry Clean"));
	}

	@Override
	@Path("/cart")
	@POST
	public Response addProductToCart(String data,
			@Context HttpServletRequest request) throws AbstractException {
		try {
			CartItem cartItem = new ObjectMapper().readValue(data,
					CartItem.class);
			cartItem.setId(HashUtils.getMD5Hash(cartItem.getUserId()
					+ cartItem.getProductId()));
			productManagement.addProductToCart(new AbstractRequestTracker(
					request), cartItem);
			return onSuccess(cartItem);
		} catch (IOException e) {
			throw new AbstractException(e);
		}
	}

	@Override
	@Path("/cart/user/{userId}")
	@GET
	public Response getUserCart(@PathParam("userId") String userId,
			@Context HttpServletRequest request) throws AbstractException {
		AbstractRequestTracker requestTracker = new AbstractRequestTracker(
				request);
		List<CartItem> items = productManagement.getUserCart(requestTracker,
				userId);
		for (CartItem item : items) {
			item.setProduct(productManagement.findProductById(requestTracker,
					item.getProductId()));
		}
		return onSuccess(items);
	}

	@Override
	@Path("/cart/{id}")
	@DELETE
	public Response removeProductFromCart(@PathParam("id") String id,
			@Context HttpServletRequest request) throws AbstractException {
		productManagement.removeProductFromCart(new AbstractRequestTracker(
				request), id);
		return onSuccess(id);
	}

	@Override
	@Path("/order")
	@POST
	public Response placeOrder(String data, @Context HttpServletRequest request)
			throws AbstractException {
		try {
			Order order = new ObjectMapper().readValue(data, Order.class);
			order.setId(UUIDUtils.getUUID());
			return onSuccess(productManagement.placeOrder(
					new AbstractRequestTracker(request), order));
		} catch (IOException e) {
			throw new AbstractException("oops")
					.setCustomMessage("Something went wrong!");
		}
	}

	@Override
	@Path("/order/{orderId}/status/{status}")
	@PUT
	public Response updateOrderStatus(@PathParam("orderId") String orderId,
			@PathParam("status") String status, String data,
			@Context HttpServletRequest request) throws AbstractException {
		OrderAddress orderAddress = null;
		Payment payment = null;
		StripeInfo stripeInfo = null;
		if (OrderStatus.ORDER_PLACED.name().equalsIgnoreCase(status)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode node = mapper.readTree(data);
				orderAddress = mapper.convertValue(node.get("address"),
						OrderAddress.class);
				payment = mapper.convertValue(node.get("payment"),
						Payment.class);
				stripeInfo = mapper.convertValue(node.get("stripe"),
						StripeInfo.class);
			} catch (Exception e) {
				e.printStackTrace();
				return onError(new AbstractException("bad.request")
						.setCustomMessage("Invalid order information!"));
			}
		}

		try {
			return onSuccess(productManagement.updateOrderStatus(
					new AbstractRequestTracker(request), orderId, status,
					orderAddress, payment, stripeInfo));
		} catch (AbstractException e) {
			return onError(e);
		}
	}

	@Override
	@Path("/order/{orderId}")
	@GET
	public Response getOrderById(@PathParam("orderId") String orderId,
			@QueryParam("includeItems") String includeItems,
			@QueryParam("includeShipments") String includeShipments,
			@QueryParam("includeEarnings") String includeEarnings,
			@Context HttpServletRequest request) throws AbstractException {
		if (StringUtils.isBlank(orderId)) {
			return onError(new AbstractException("missing.field")
					.setCustomMessage("orderId is missing"));
		}
		return onSuccess(productManagement.getOrder(new AbstractRequestTracker(
				request), orderId,
				Boolean.TRUE.toString().equalsIgnoreCase(includeItems),
				Boolean.TRUE.toString().equalsIgnoreCase(includeShipments),
				Boolean.TRUE.toString().equalsIgnoreCase(includeEarnings)));
	}

	@Override
	@Path("/order/history")
	@GET
	public Response getOrderHistory(@Context HttpServletRequest request)
			throws AbstractException {
		OrderFilter filter = load(request, OrderFilter.class);
		if (StringUtils.isBlank(filter.getUserId())) {
			return onError(new AbstractException("missing.field")
					.setCustomMessage("userId is missing"));
		}
		return onSuccess(productManagement.getOrderHistory(
				new AbstractRequestTracker(request), filter,
				load(request, Paging.class)));
	}

	@Override
	@Path("/user/{userId}/rentedProducts")
	@GET
	public Response getUserRentedProducts(@PathParam("userId") String userId,
			@Context HttpServletRequest request) throws AbstractException {
		AbstractRequestTracker requestTracker = new AbstractRequestTracker(
				request);
		PagedList<OrderItem> pagedList = productManagement
				.getUserRentedProducts(requestTracker, userId,
						load(request, Paging.class));
		List<OrderItem> orders = pagedList.getObjects();
		for (OrderItem order : orders) {
			order.setProduct(productManagement.findProductById(requestTracker,
					order.getProductId()));
		}
		return onSuccess(pagedList);
	}

	@Override
	@Path("/user/{userId}/invoices")
	@GET
	public Response getUserInvoices(@PathParam("userId") String userId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(productManagement.getUserInvoices(
				new AbstractRequestTracker(request), userId,
				load(request, Paging.class)));
	}

	@Override
	@Path("/shipment/history")
	@GET
	public Response getShipmentHistory(@Context HttpServletRequest request)
			throws AbstractException {
		ShipmentFilter filter = load(request, ShipmentFilter.class);
		if (StringUtils.isBlank(filter.getOwnerId())) {
			return onError(new AbstractException("missing.field")
					.setCustomMessage("ownerId is missing"));
		}
		return onSuccess(productManagement.getShipmentHistory(
				new AbstractRequestTracker(request), filter,
				load(request, Paging.class)));
	}

	@Override
	@Path("/item/history")
	@GET
	public Response getItemHistory(@Context HttpServletRequest request)
			throws AbstractException {
		OrderItemFilter filter = load(request, OrderItemFilter.class);
		if (StringUtils.isBlank(filter.getProductId())) {
			return onError(new AbstractException("missing.field")
					.setCustomMessage("productId is missing"));
		}
		SortRules sortRules = new SortRules();
		sortRules
				.add(new SortRule(AbstractAuditDTO.CREATED_ON, SortOrder.DESC));
		return onSuccess(productManagement.listItems(
				new AbstractRequestTracker(request), filter,
				load(request, Paging.class), sortRules));
	}

	@Override
	@Path("/shipment/{shipmentId}/items")
	@GET
	public Response getShipmentItems(
			@PathParam("shipmentId") String shipmentId,
			@Context HttpServletRequest request) throws AbstractException {
		if (StringUtils.isBlank(shipmentId)) {
			return onError(new AbstractException("missing.field")
					.setCustomMessage("shipmentId is missing"));
		}
		return onSuccess(productManagement.getShipmentItems(
				new AbstractRequestTracker(request), shipmentId));
	}

	@Override
	@Path("/earnings")
	@GET
	public Response getEarnings(@Context HttpServletRequest request)
			throws AbstractException {
		return onSuccess(productManagement
				.getEarnings(new AbstractRequestTracker(request),
						load(request, EarningFilter.class),
						load(request, Paging.class)));
	}

	@Override
	@Path("/shipment/{shipmentId}/status/{status}")
	@PUT
	public Response updateShipmentStatus(
			@PathParam("shipmentId") String shipmentId,
			@PathParam("status") String status,
			@Context HttpServletRequest request) throws AbstractException {
		System.out.println("shipmentId => " + shipmentId);
		System.out.println("status => " + status);
		return onSuccess(productManagement.updateShipmentStatus(
				new AbstractRequestTracker(request), shipmentId, status));
	}

	@Override
	@Path("/earning/{earningId}/redeemStatus/{redeemStatus}")
	@PUT
	public Response updateEarningStatus(
			@PathParam("earningId") String earningId,
			@PathParam("redeemStatus") String redeemStatus, String data,
			@Context HttpServletRequest request) throws AbstractException {
		try {
			Earning earning = new Earning();
			earning.setId(earningId);
			earning.setRedeemStatus(redeemStatus);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(data);
			if (RedeemStatus.AWAITING.name().equalsIgnoreCase(redeemStatus)) {
				earning.setRedeemOn(Calendar.getInstance().getTimeInMillis());
				if (!node.has("chequeRecipient")) {
					return onError(new AbstractException("missing.field")
							.setCustomMessage("chequeRecipient is missing"));
				}
				earning.setChequeRecipient(JsonUtils.getStringValue(node,
						"chequeRecipient", null));

				if (!node.has("chequeAddress")) {
					return onError(new AbstractException("missing.field")
							.setCustomMessage("chequeAddress is missing"));
				}
				earning.setChequeAddress(mapper.convertValue(
						node.get("chequeAddress"), ChequeAddress.class));
			} else if (RedeemStatus.REDEEMED.name().equalsIgnoreCase(
					redeemStatus)) {

				earning.setRedeemedOn(Calendar.getInstance().getTimeInMillis());
				earning.setChequeNo(JsonUtils.getStringValue(node, "chequeNo",
						null));
				earning.setChequeImage(JsonUtils.getStringValue(node,
						"chequeImage", null));
				earning.setPaymentMode(PaymentMode.CHEQUE.name());
				earning.setChequeDate(JsonUtils.getLongValue(node,
						"chequeDate", null));
				earning.setNotes(JsonUtils.getStringValue(node, "notes", null));

				if (StringUtils.isEmpty(earning.getChequeNo())) {
					return onError(new AbstractException("missing.field")
							.setCustomMessage("chequeNo is missing"));
				}

				// if (StringUtils.isEmpty(earning.getChequeImage())) {
				// return onError(new AbstractException("missing.field")
				// .setCustomMessage("chequeImage is missing"));
				// }

				if (StringUtils
						.isEmpty(String.valueOf(earning.getChequeDate()))) {
					return onError(new AbstractException("missing.field")
							.setCustomMessage("chequeDate is missing"));
				}

			}
			return onSuccess(productManagement.updateEarningStatus(
					new AbstractRequestTracker(request), earning));
		} catch (Exception e) {
			e.printStackTrace();
			throw new AbstractException("oops", e)
					.setCustomMessage("Something went wrong!");
		}
	}

	@Override
	@Path("/{productId}/user/{userId}/favourite/{favorite}")
	@POST
	public Response updateFavoriteStatus(
			@PathParam("productId") String productId,
			@PathParam("userId") String userId,
			@PathParam("favorite") String favorite,
			@Context HttpServletRequest request) throws AbstractException {

		Favorite favourite = new Favorite();
		favourite.setProductId(productId);
		favourite.setUserId(userId);
		favourite.setFavourite(Boolean.TRUE.toString().equalsIgnoreCase(
				favorite));

		FavoriteFilter filter = new FavoriteFilter();
		filter.setProductId(favourite.getProductId());
		filter.setUserId(favourite.getUserId());

		return onSuccess(productManagement.updateFavoriteStatus(
				new AbstractRequestTracker(request), filter, favourite));
	}

	@Override
	@Path("/user/{userId}/favorites")
	@GET
	@Timed(name = "Get user favorites#timer")
	public Response getUserFavorites(@PathParam("userId") String userId,
			@QueryParam("status") String status,
			@Context HttpServletRequest request) throws AbstractException {

		FavoriteFilter filter = new FavoriteFilter();
		filter.setUserId(userId);

		ProductFilter productFilter = new ProductFilter();
		if (null != status) {
			productFilter.setStatus(Integer.parseInt(status));
		}

		return onSuccess(productManagement.listFavorites(
				new AbstractRequestTracker(request), filter, productFilter,
				new Paging(-1, -1), load(request, SortRules.class)));

	}

	@Override
	@Path("/{userId}/paymentCards")
	@GET
	@Timed(name = "List saved cards#timer")
	public Response listSavedCards(@PathParam("userId") String userId,
			@Context HttpServletRequest request) throws AbstractException {

		PaymentsFilter filter = new PaymentsFilter();
		filter.setUserId(userId);
		filter.setStatus(1);
		try {
			return onSuccess(productManagement.listSavedPaymentCard(
					new AbstractRequestTracker(request), filter, new Paging(-1,
							-1)));
		} catch (AbstractException e) {
			return onError(e);
		}
	}

	@Override
	@Path("/addPaymentCard")
	@POST
	public Response addPaymentCard(String data,
			@Context HttpServletRequest request) throws AbstractException {
		try {
			System.out.println("data ==> " + data);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(data);
			StripeInfo info = new StripeInfo();
			String userId = null;

			if (node.get("stripe") != null)
				info = mapper
						.convertValue(node.get("stripe"), StripeInfo.class);
			if (node.get("userId") != null)
				userId = node.get("userId").asText();

			return onSuccess(productManagement.addPaymentCard(
					new AbstractRequestTracker(request), info, userId));

		} catch (IOException e) {
			e.printStackTrace();
			return onError(new AbstractException("oops")
					.setCustomMessage("Something went wrong!"));
		} catch (AbstractException e) {
			return onError(e);
		}
	}

	@Override
	@Path("/rentedDresses")
	@GET
	public Response getRentalDressesListing(
			@Context HttpServletRequest request,
			@QueryParam("shipmentStatus") String shipmentStatus)
			throws AbstractException {

		ShipmentFilter shipmentFilter = new ShipmentFilter();
		if (StringUtils.isNotBlank(shipmentStatus))
			shipmentFilter.setShipmentStatus(shipmentStatus);

		return onSuccess(productManagement.listDressRentals(
				new AbstractRequestTracker(request), shipmentFilter,
				load(request, Paging.class), load(request, SortRules.class)));
	}

	@Override
	@Path("/{cardId}/removePaymentCard")
	@DELETE
	@Timed(name = "Delete product#timer")
	public Response removePaymentCard(@PathParam("cardId") String cardId,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(productManagement.removePaymentCard(
				new AbstractRequestTracker(request), cardId));
	}

	@Override
	@Path("/{id}/reject")
	@PUT
	@Timed(name = "Enable user#timer")
	public Response rejectProduct(@PathParam("id") String id,
			@Context HttpServletRequest request) throws AbstractException {
		return onSuccess(productManagement.deactivateProduct(
				new AbstractRequestTracker(request), id));
	}

	@Override
	@Path("/heightFit")
	@GET
	public Response listHeightFit() throws AbstractException {
		return onSuccess(Arrays.asList("4'11\"-5'2\"", "5'3\"-5'6\"",
				"5'7\"-5'10\"", "5'11\"-6'1\""));
	}

	@Override
	@Path("/bustFit")
	@GET
	public Response listBustFit() throws AbstractException {
		return onSuccess(Arrays.asList("22", "24", "26", "28", "30", "32",
				"34", "36", "38", "40", "42", "44"));
	}

	@Override
	@Path("/uploadFAQTnC")
	@POST
	@Timed(name = "Upload FAQ#timer")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFaqTnC(
			@FormDataParam("file") InputStream inputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@Context HttpServletRequest request) throws AbstractException {
		// Get the file extension
		String fileToUpload = fileDetail.getFileName();
		String fileName = fileToUpload.substring(0,
				fileToUpload.lastIndexOf("."));
		String extension = fileToUpload.substring(
				fileToUpload.lastIndexOf("."), fileToUpload.length());
		try {
			fileName = s3Uploader.upload(inputStream, SETTINGS_FOLDER,
					fileName, extension, true);
		} catch (IOException e) {
			throw new AbstractException(e);
		}
		return Response.ok().entity(fileName).build();
	}

	@Override
	@Path("/saveDocument")
	@POST
	public Response saveDocument(String data,
			@Context HttpServletRequest request) throws AbstractException {
		LOGGER.info("--------------saveDocument-----------------" +data);

		Document document = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			document = mapper.readValue(data, Document.class);
			LOGGER.info("Doc getName = ", document.getName());
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (document == null)
			return onError(new AbstractException("save.document.failed")
					.setCustomMessage("Save document operation aborted! Please try again!"));
		
		String url;
		try {
			LOGGER.info("Document => ", document);
			url = s3Uploader.createFile(SETTINGS_FOLDER, document.getName(),
					DOCUMENT_EXTENSION, document.getContent());
			LOGGER.info("Doc url aws = ", url);
		} catch (IOException e) {
			throw new AbstractException(e);
		} catch (AbstractException e) {
			return onError(e);
		}

		if (StringUtils.isBlank(url))
			return onError(new AbstractException("save.document.failed")
					.setCustomMessage("Save document operation aborted! Please try again!"));

		AbstractRequestTracker tracker = new AbstractRequestTracker(request);
		DocumentFilter filter = new DocumentFilter();
		filter.setName(document.getName());
		Document document2 = productManagement.findDocumentByName(tracker,
				filter);
		if (null == document2) {
			document2 = new Document();
			document2.setContent(document.getContent());
			document2.setName(document.getName());
			document2.setUrl(url);
			return onSuccess(productManagement.saveDocument(tracker, document2));
		} else {
			return onSuccess(productManagement.updateDocument(tracker, filter,
					document.getContent()));
		}
	}

	@Override
	@Path("/getDocumentContent")
	@GET
	public Response getDocumentContent(@Context HttpServletRequest request,
			@QueryParam("fileName") String fileName) throws AbstractException {

		DocumentFilter filter = new DocumentFilter();
		filter.setName(fileName);
		return onSuccess(productManagement.findDocumentByName(
				new AbstractRequestTracker(request), filter));
	}

	@Override
	@Path("/getDocumentUrl")
	@GET
	public Response getDocumentUrl(@Context HttpServletRequest request,
			@QueryParam("fileName") String fileName) throws AbstractException {
		DocumentFilter filter = new DocumentFilter();
		filter.setName(fileName);
		return onSuccess(productManagement.findDocumentByName(
				new AbstractRequestTracker(request), filter));
	}

	@Override
	@Path("/resizeThumbs")
	@GET
	public Response resizeThumbs(@Context HttpServletRequest request)
			throws AbstractException {
		PagedList<Product> products = productManagement.listProducts(
				new AbstractRequestTracker(request), new ProductFilter(),
				new Paging(-1, -1), null);
		// fetch all products from db
		List<Product> productList = products.getObjects();
		String fileName = null;
		LOGGER.info("Total Products count = " + productList.size());
		for (int count = 0; count < productList.size(); count++) {
			Product product = productList.get(count);
			Map<String, String> imagesMap = product.getImages();
			// iterate over images array for each product
			for (Map.Entry<String, String> pair : imagesMap.entrySet()) {
				String productImageUrl = pair.getValue();
				LOGGER.info("productImageUrl = " + productImageUrl);
				// find filename from url
				String fileId = productImageUrl.substring(
						productImageUrl.lastIndexOf(File.separator) + 1,
						productImageUrl.lastIndexOf('.')) + THUMB_EXTENSION;
				LOGGER.info("fileId = " + fileId + "\n [ + " + count + "]");
				S3Object s3Object = null;
				// download object from S3
				try {
					s3Object = s3Uploader
							.downloadObject(PRODUCT_FOLDER, fileId);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					// create blank temp file on file system
					File tempFile = File.createTempFile(UUIDUtils.getUUID(),
							THUMB_EXTENSION);
					LOGGER.info("tempFile = " + tempFile.getAbsolutePath());
					if (null != tempFile && null != s3Object) {
						OutputStream out = new FileOutputStream(tempFile);
						S3ObjectInputStream objectContent = s3Object
								.getObjectContent();
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						// Copy file content to temp blank file
						IOUtils.copy(objectContent, byteArrayOutputStream);
						byteArrayOutputStream.writeTo(out);
					}
					// Copy file content to temp blank file
					fileName = s3Uploader.upload(ImageUtils.scaleImage(
							tempFile.getAbsolutePath(), THUMB_WIDTH,
							THUMB_HEIGHT), PRODUCT_FOLDER, fileId + "-thumb",
							".jpg", true);
					LOGGER.info("fileName = " + fileName);
				} catch (AbstractException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Response.ok().entity(fileName).build();
	}

	@Override
	@Path("/getAPIVersion")
	@GET
	public Response getAPIVersion(@Context HttpServletRequest request)
			throws AbstractException {
		return onSuccess(productManagement
				.getAPIVersion(new AbstractRequestTracker(request)));
	}

	@Override
	@Path("/{productId}/deleteProduct")
	@DELETE
	@Timed(name = "Delete product#timer")
	public Response deleteProductById(@PathParam("productId") String id,
			@Context HttpServletRequest request) throws AbstractException {
		productManagement
				.deleteProduct(new AbstractRequestTracker(request), id);
		return onSuccess(id);
	}
}
