package br.gov.ebserh.pacientes.commons.exception.HttpResp413ErroRequisicaoTamanho;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.gov.ebserh.pacientes.commons.exception.EnumMensagensErro;

@Provider
public class ErroRequisicao413ExceptionHandler implements ExceptionMapper<ErroRequisicao413Exception> {
   
    @Override
    public Response toResponse(ErroRequisicao413Exception exception) {
        return Response
                .status(Status.REQUEST_ENTITY_TOO_LARGE)
                .entity(EnumMensagensErro.HTTP_RESP_413_ERRO_TAMANHO.getDetalheErro())
                .build();
    }
}