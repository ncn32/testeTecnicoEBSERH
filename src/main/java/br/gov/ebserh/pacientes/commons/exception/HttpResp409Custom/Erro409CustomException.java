package br.gov.ebserh.pacientes.commons.exception.HttpResp409Custom;

import br.gov.ebserh.pacientes.commons.exception.MensagemDetalhe;

public class Erro409CustomException extends RuntimeException {

    private static final long serialVersionUID = 794615122902064405L;
    private MensagemDetalhe detalheErro;

    public Erro409CustomException(MensagemDetalhe detalheErro) {
        super(detalheErro.getMensagemMainframe());
        this.detalheErro = detalheErro;
    }

    public Erro409CustomException(MensagemDetalhe detalheErro, Throwable cause) {
        super(detalheErro.getMensagemMainframe(), cause);
        this.detalheErro = detalheErro;
    }

    public MensagemDetalhe getDetalheErro() {
        return this.detalheErro;
    }

}
