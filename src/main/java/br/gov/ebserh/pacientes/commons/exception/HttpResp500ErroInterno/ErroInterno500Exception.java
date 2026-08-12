package br.gov.ebserh.pacientes.commons.exception.HttpResp500ErroInterno;

public class ErroInterno500Exception extends RuntimeException {

    private static final long serialVersionUID = 794615122902064405L;

    public ErroInterno500Exception() {
        super();
    }

    public ErroInterno500Exception(String message) {
        super(message);
    }

    public ErroInterno500Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public ErroInterno500Exception(Throwable cause) {
        super(cause);
    }


}
