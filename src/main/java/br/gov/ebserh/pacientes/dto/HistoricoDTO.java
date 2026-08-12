package br.gov.ebserh.pacientes.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(name="Historico", description="Historico da trilha de auditoria")
public class HistoricoDTO {
	// ATENÇÃO: variáveis do JSON primeira letra sempre em minúsculo,
	// pois a lib não interpreta direito.
	
	@JsonProperty(index = 1)
	@Schema(description="", required = false)
	long id = 0;
	
	@JsonProperty(index = 2)
	@Schema(description="", required = false)
	String usuario = "";
	
	@JsonProperty(index = 3)
	@Schema(description="", required = false)
	String origem = "";
	
	@JsonProperty(index = 4)
	@Schema(description="", required = false)
	String requisicao = "";
	
	@JsonProperty(index = 5)
	@Schema(description="", required = false)
	String dhRequisicao = LocalDateTime.now().toString();
	
	@JsonProperty(index = 6)
	@Schema(description="", required = false)
	String deRequisicao = "";
	
	@JsonProperty(index = 7)
	@Schema(description="", required = false)
	String resposta = "";
	
	@JsonProperty(index = 8)
	@Schema(description="", required = false)
	String dhResposta = LocalDateTime.now().toString();
	
	@JsonProperty(index = 9)
	@Schema(description="", required = false)
	String deResposta = "";

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getRequisicao() {
		return requisicao;
	}

	public void setRequisicao(String requisicao) {
		this.requisicao = requisicao;
	}

	public String getDhRequisicao() {
		return dhRequisicao;
	}

	public void setDhRequisicao(String dhRequisicao) {
		this.dhRequisicao = dhRequisicao;
	}

	public String getResposta() {
		return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

	public String getDhResposta() {
		return dhResposta;
	}

	public void setDhResposta(String dhResposta) {
		this.dhResposta = dhResposta;
	}

	public String getDeRequisicao() {
		return deRequisicao;
	}

	public void setDeRequisicao(String deRequisicao) {
		this.deRequisicao = deRequisicao;
	}

	public String getDeResposta() {
		return deResposta;
	}

	public void setDeResposta(String deResposta) {
		this.deResposta = deResposta;
	}

	
}
