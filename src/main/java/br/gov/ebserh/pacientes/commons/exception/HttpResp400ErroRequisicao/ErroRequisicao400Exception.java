package br.gov.ebserh.pacientes.commons.exception.HttpResp400ErroRequisicao;

public class ErroRequisicao400Exception extends RuntimeException {

    private static final long serialVersionUID = 794615122902064405L;

    public ErroRequisicao400Exception() {
        super();
    }

    public ErroRequisicao400Exception(String message) {
        super(message);
    }

    public ErroRequisicao400Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public ErroRequisicao400Exception(Throwable cause) {
        super(cause);
    }

}

