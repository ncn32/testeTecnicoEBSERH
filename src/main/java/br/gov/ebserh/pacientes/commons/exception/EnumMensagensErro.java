package br.gov.ebserh.pacientes.commons.exception;

public enum EnumMensagensErro {

    HTTP_RESP_400_ERRO_REQUISICAO(3, "Erro na Requisicao"),
    HTTP_RESP_413_ERRO_TAMANHO(4, "Requisição maior que o limite"),
    HTTP_RESP_500(99, "Erro Interno");

    public final int codigo;
    public final String descricao;

    EnumMensagensErro(int codigo, String descricao){
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public MensagemDetalhe getDetalheErro() {
        return new MensagemDetalhe(this);
    }

}
