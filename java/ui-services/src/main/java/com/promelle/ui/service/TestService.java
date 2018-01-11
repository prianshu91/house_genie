package com.promelle.ui.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This class is intended for providing sample service.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
@Path("/name")
@Produces(MediaType.APPLICATION_JSON)
public class TestService {

	@GET
	public Response getName() {
		return Response.ok("PROMELLE").build();
	}

}