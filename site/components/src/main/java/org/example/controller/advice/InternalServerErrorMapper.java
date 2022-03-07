package org.example.controller.advice;

import org.example.dto.ErrorResponse;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InternalServerErrorMapper implements ExceptionMapper<InternalServerErrorException> {
    @Override
    public Response toResponse(InternalServerErrorException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(e.getMessage(),
                        Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
    }
}
