package com.promelle.product.service;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.promelle.exception.AbstractException;

/**
 * This interface is intended for providing services related to manage.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public interface ProductService {

	Response uploadProductImage(InputStream uploadedInputStream,
			FormDataContentDisposition fileDetail, String id, String tag,
			HttpServletRequest request) throws AbstractException;

	Response uploadChequeImage(InputStream uploadedInputStream,
			FormDataContentDisposition fileDetail, HttpServletRequest request)
			throws AbstractException;

	Response enableProduct(String id, HttpServletRequest request)
			throws AbstractException;

	Response listOccasions() throws AbstractException;

	Response listCategories() throws AbstractException;
	
	Response listCategories2() throws AbstractException;

	Response listColors() throws AbstractException;

	Response listSizes() throws AbstractException;

	Response listCareInstructions() throws AbstractException;

	Response addProductToCart(String data, HttpServletRequest request)
			throws AbstractException;

	Response getUserCart(String userId, HttpServletRequest request)
			throws AbstractException;

	Response removeProductFromCart(String id, HttpServletRequest request)
			throws AbstractException;

	Response placeOrder(String data, HttpServletRequest request)
			throws AbstractException;

	Response getOrderById(String orderId, String includeItems,
			String includeShipments, String includeEarnings,
			HttpServletRequest request) throws AbstractException;

	Response getOrderHistory(HttpServletRequest request)
			throws AbstractException;

	Response updateOrderStatus(String orderId, String status, String data,
			HttpServletRequest request) throws AbstractException;

	Response getUserRentedProducts(String userId, HttpServletRequest request)
			throws AbstractException;

	Response getUserInvoices(String userId, HttpServletRequest request)
			throws AbstractException;

	Response getShipmentHistory(HttpServletRequest request)
			throws AbstractException;

	Response getItemHistory(HttpServletRequest request)
			throws AbstractException;

	Response getShipmentItems(String shipmentId, HttpServletRequest request)
			throws AbstractException;

	Response getEarnings(HttpServletRequest request) throws AbstractException;

	Response updateShipmentStatus(String shipmentId, String status,
			HttpServletRequest request) throws AbstractException;

	Response updateEarningStatus(String earningId, String status, String data,
			HttpServletRequest request) throws AbstractException;

	Response updateFavoriteStatus(String productId, String favourite,
			String userId, HttpServletRequest request) throws AbstractException;

	Response getUserFavorites(String ownerId, String status,
			HttpServletRequest request) throws AbstractException;

	Response listSavedCards(String ownerId, HttpServletRequest request)
			throws AbstractException;

	Response addPaymentCard(String data, HttpServletRequest request)
			throws AbstractException;

	Response getRentalDressesListing(HttpServletRequest request,
			String shipmentStatus) throws AbstractException;

	Response removePaymentCard(String id, HttpServletRequest request)
			throws AbstractException;

	Response rejectProduct(String id, HttpServletRequest request)
			throws AbstractException;

	Response listBustFit() throws AbstractException;

	Response listHeightFit() throws AbstractException;

	Response uploadFaqTnC(InputStream inputStream,
			FormDataContentDisposition fileDetail, HttpServletRequest request)
			throws AbstractException;

	Response getDocumentContent(HttpServletRequest request, String fileName)
			throws AbstractException;

	Response getDocumentUrl(HttpServletRequest request, String fileName)
			throws AbstractException;

	Response resizeThumbs(HttpServletRequest request) throws AbstractException;

	Response saveDocument(String data, HttpServletRequest request)
			throws AbstractException;

	Response getAPIVersion(HttpServletRequest request) throws AbstractException;

	Response deleteProductById(String id, HttpServletRequest request)
			throws AbstractException;
}
