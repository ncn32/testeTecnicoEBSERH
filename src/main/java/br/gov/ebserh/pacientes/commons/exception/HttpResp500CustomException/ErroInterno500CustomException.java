package br.gov.ebserh.pacientes.commons.exception.HttpResp500CustomException;

import br.gov.ebserh.pacientes.commons.exception.MensagemDetalhe;

public class ErroInterno500CustomException extends RuntimeException {

    private static final long serialVersionUID = 0L;
    private MensagemDetalhe detalheErro;

    public ErroInterno500CustomException(MensagemDetalhe detalheErro) {
        super(detalheErro.getMensagemMainframe());
        this.detalheErro = detalheErro;
    }

    public ErroInterno500CustomException(MensagemDetalhe detalheErro, Throwable cause) {
        super(detalheErro.getMensagemMainframe(), cause);
        this.detalheErro = detalheErro;
    }

    public MensagemDetalhe getDetalheErro() {
        return this.detalheErro;
    }

}
