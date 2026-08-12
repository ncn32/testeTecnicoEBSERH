package br.gov.ebserh.pacientes.commons.exception.HttpResp413ErroRequisicaoTamanho;

public class ErroRequisicao413Exception extends RuntimeException {

    private static final long serialVersionUID = 794615122902064405L;

    public ErroRequisicao413Exception() {
        super();
    }

    public ErroRequisicao413Exception(String message) {
        super(message);
    }

    public ErroRequisicao413Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public ErroRequisicao413Exception(Throwable cause) {
        super(cause);
    }

}

