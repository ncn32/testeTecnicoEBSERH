package br.gov.ebserh.pacientes.commons.exception.HttpResp409Custom;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class Erro409CustomExceptionHandler implements ExceptionMapper<Erro409CustomException> {

    @Override
    public Response toResponse(Erro409CustomException exception) {
        return Response
                .status(Status.CONFLICT)
                .entity(exception.getDetalheErro())
                .build();
    }
}