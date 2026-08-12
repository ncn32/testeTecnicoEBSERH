package br.gov.ebserh.pacientes.commons.util;

import org.jboss.logging.Logger;

import br.gov.ebserh.pacientes.commons.exception.MensagemDetalhe;
import br.gov.ebserh.pacientes.commons.exception.HttpResp409Custom.Erro409CustomException;
import br.gov.ebserh.pacientes.commons.exception.HttpResp500CustomException.ErroInterno500CustomException;

public class CodigoRetornoMainframeHandler {


    public static void avaliarCodigoRetorno(MensagemDetalhe detalheErro, Logger log) {

        switch (detalheErro.getCodigo()) {
            case 0:
                break;
            case 99:
                log.error("### " + detalheErro.getMensagemMainframe());
                throw new ErroInterno500CustomException(detalheErro);
            default:
                log.error("### " + detalheErro.getMensagemMainframe());
                throw new Erro409CustomException(detalheErro);
        }

    }
}
