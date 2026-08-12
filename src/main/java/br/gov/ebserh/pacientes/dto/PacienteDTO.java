package br.gov.ebserh.pacientes.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(name="Paciente", description="Dados do paciente")
public class PacienteDTO {
	
	@JsonProperty(index = 1)
	@Schema(description="", required = false)
	long id = 0;
	
	@JsonProperty(index = 2)
	@Schema(description="", required = false)
	String nome = "";
	
	@JsonProperty(index = 3)
	@Schema(description="", required = false)
	String cpf = "";
	
	@JsonProperty(index = 4)
	@Schema(description="", required = false)
	String cns = "";
	
	@JsonProperty(index = 5)
	@Schema(description="", required = false)
	String nascimento = "";
	
	@JsonProperty(index = 6)
	@Schema(description="", required = false)
	String telefone = "";

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCns() {
		return cns;
	}

	public void setCns(String cns) {
		this.cns = cns;
	}

	public String getNascimento() {
		return nascimento;
	}

	public void setNascimento(String nascimento) {
		this.nascimento = nascimento;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	
}
