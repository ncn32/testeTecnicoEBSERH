package br.gov.ebserh.pacientes.commons.exception.HttpResp500CustomException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ErroInterno500CustomExceptionHandler implements ExceptionMapper<ErroInterno500CustomException> {
   
    @Override
    public Response toResponse(ErroInterno500CustomException exception) {
        return Response
                .status(Status.INTERNAL_SERVER_ERROR)
                .entity(exception.getDetalheErro())
                .build();
    }
}