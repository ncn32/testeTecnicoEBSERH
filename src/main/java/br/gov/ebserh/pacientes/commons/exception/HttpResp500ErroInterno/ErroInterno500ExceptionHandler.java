package br.gov.ebserh.pacientes.commons.exception.HttpResp500ErroInterno;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.gov.ebserh.pacientes.commons.exception.EnumMensagensErro;

@Provider
public class ErroInterno500ExceptionHandler implements ExceptionMapper<ErroInterno500Exception> {
   
    @Override
    public Response toResponse(ErroInterno500Exception exception) {
        return Response
                .status(Status.INTERNAL_SERVER_ERROR)
                .entity(EnumMensagensErro.HTTP_RESP_500.getDetalheErro())
                .build();
    }
}