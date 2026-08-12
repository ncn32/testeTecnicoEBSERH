package br.gov.ebserh.pacientes.commons.exception;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="Detalhe", description="Detalhes erro")
public class MensagemDetalhe implements Serializable{

    @JsonProperty(index = 1)
    @Schema(description="Indica o codigo do erro", required = false)
    private int codigo;

    @JsonProperty(index = 2)
    @Schema(description="Descricao do erro", required = false)
    private String descricao = "";

    public MensagemDetalhe(EnumMensagensErro enumMensagensErro) {
        this.codigo = enumMensagensErro.codigo;
        this.descricao = enumMensagensErro.descricao;
    }

    public MensagemDetalhe(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    @JsonIgnore
    public String getMensagemMainframe(){
        StringBuilder msgErro = new StringBuilder();
        msgErro.append("ERRO MAINFRAME");
        msgErro.append(" - RETURN CODE: ");
        msgErro.append(codigo);
        msgErro.append(" - MENSAGEM: ");
        msgErro.append(descricao);
        return msgErro.toString();
    }
    
}