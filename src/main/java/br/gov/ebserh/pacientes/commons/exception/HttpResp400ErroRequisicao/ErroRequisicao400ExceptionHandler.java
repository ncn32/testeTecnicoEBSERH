package br.gov.ebserh.pacientes.commons.exception.HttpResp400ErroRequisicao;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.gov.ebserh.pacientes.commons.exception.EnumMensagensErro;

@Provider
public class ErroRequisicao400ExceptionHandler implements ExceptionMapper<ErroRequisicao400Exception> {
   
    @Override
    public Response toResponse(ErroRequisicao400Exception exception) {
        return Response
                .status(Status.BAD_REQUEST)
                .entity(EnumMensagensErro.HTTP_RESP_400_ERRO_REQUISICAO.getDetalheErro())
                .build();
    }
}