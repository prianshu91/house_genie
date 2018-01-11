package com.promelle.common.exception.mapper;

import java.util.Arrays;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.promelle.exception.AbstractException;
import com.promelle.response.CommonResponse;
import com.promelle.utils.UUIDUtils;

/**
 * This class is intended for mapping exception to service response.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
@Provider
public class AbstractExceptionMapper implements ExceptionMapper<AbstractException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExceptionMapper.class.getName());

    @Override
    public Response toResponse(AbstractException e) {
        String errorId = UUIDUtils.getUUID();
        LOGGER.error("Exception in service for error id :" + errorId, e);
        return Response.ok(
                new CommonResponse().setErrorCode(new Integer(500).toString()).setErrors(Arrays.asList(e.getMessage()))
                        .addAdditionalData("errorId", errorId)).build();
    }

}
