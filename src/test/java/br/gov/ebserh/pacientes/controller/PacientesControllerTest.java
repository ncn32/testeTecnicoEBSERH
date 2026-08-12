package br.gov.ebserh.pacientes.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import br.gov.ebserh.pacientes.dto.HistoricoDTO;
import br.gov.ebserh.pacientes.dto.PacienteDTO;
import br.gov.ebserh.pacientes.service.PacientesService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;

/**
 * Testes dos endpoints REST com o PacientesService "mockado" via @InjectMock.
 * Nao acessa banco: valida roteamento, binding JSON e codigos de status.
 */
@QuarkusTest
public class PacientesControllerTest {

	@InjectMock
	PacientesService service;

	private PacienteDTO paciente(long id, String nome) {
		PacienteDTO p = new PacienteDTO();
		p.setId(id);
		p.setNome(nome);
		p.setCpf("12345678909");
		p.setCns("700000000000001");
		p.setNascimento("1980-01-01");
		p.setTelefone("+550012345678");
		return p;
	}

	// ---------------- PACIENTES ----------------

	@Test
	void listarPacientes_retorna200EJson() {
		when(service.listar()).thenReturn(Arrays.asList(paciente(1, "Jose"), paciente(2, "Maria")));
		given().accept(ContentType.JSON)
				.when().get("/api/v1/pacientes")
				.then().statusCode(200)
				.body("size()", is(2))
				.body("[0].nome", equalTo("Jose"));
	}

	@Test
	void buscarPaciente_encontrado200() {
		when(service.buscarPorId(1L)).thenReturn(paciente(1, "Jose"));
		given().accept(ContentType.JSON)
				.when().get("/api/v1/pacientes/1")
				.then().statusCode(200)
				.body("nome", equalTo("Jose"));
	}

	@Test
	void buscarPaciente_naoEncontrado404() {
		when(service.buscarPorId(99L)).thenReturn(null);
		given().accept(ContentType.JSON)
				.when().get("/api/v1/pacientes/99")
				.then().statusCode(404);
	}

	@Test
	void inserirPaciente_sucesso201() {
		when(service.inserir(any(PacienteDTO.class))).thenReturn(true);
		given().contentType(ContentType.JSON)
				.body("{\"nome\":\"Jose\",\"cpf\":\"12345678909\"}")
				.when().post("/api/v1/pacientes")
				.then().statusCode(201);
	}

	@Test
	void inserirPaciente_falha400() {
		when(service.inserir(any(PacienteDTO.class))).thenReturn(false);
		given().contentType(ContentType.JSON)
				.body("{\"nome\":\"Jose\"}")
				.when().post("/api/v1/pacientes")
				.then().statusCode(400);
	}

	@Test
	void atualizarPaciente_sucesso200() {
		when(service.atualizar(any(PacienteDTO.class))).thenReturn(true);
		given().contentType(ContentType.JSON)
				.body("{\"nome\":\"Jose\"}")
				.when().put("/api/v1/pacientes/1")
				.then().statusCode(200);
	}

	@Test
	void atualizarPaciente_falha400() {
		when(service.atualizar(any(PacienteDTO.class))).thenReturn(false);
		given().contentType(ContentType.JSON)
				.body("{\"nome\":\"Jose\"}")
				.when().put("/api/v1/pacientes/1")
				.then().statusCode(400);
	}

	@Test
	void excluirPaciente_sucesso200() {
		when(service.excluir(1L)).thenReturn(true);
		given().accept(ContentType.JSON)
				.when().delete("/api/v1/pacientes/1")
				.then().statusCode(200);
	}

	@Test
	void excluirPaciente_falha400() {
		when(service.excluir(1L)).thenReturn(false);
		given().accept(ContentType.JSON)
				.when().delete("/api/v1/pacientes/1")
				.then().statusCode(400);
	}

	// ---------------- HISTORICO ----------------

	@Test
	void listarHistorico_retorna200() {
		HistoricoDTO h = new HistoricoDTO();
		h.setUsuario("jose@email.com.br");
		when(service.listarHistorico()).thenReturn(Arrays.asList(h));
		given().accept(ContentType.JSON)
				.when().get("/api/v1/historico")
				.then().statusCode(200)
				.body("size()", is(1))
				.body("[0].usuario", equalTo("jose@email.com.br"));
	}

	@Test
	void inserirHistorico_sucesso201() {
		when(service.inserirHistorico(any(HistoricoDTO.class))).thenReturn(true);
		given().contentType(ContentType.JSON)
				.body("{\"usuario\":\"jose@email.com.br\",\"origem\":\"127.0.0.1\"}")
				.when().post("/api/v1/historico")
				.then().statusCode(201);
	}

	@Test
	void inserirHistorico_falha400() {
		when(service.inserirHistorico(any(HistoricoDTO.class))).thenReturn(false);
		given().contentType(ContentType.JSON)
				.body("{\"usuario\":\"x\"}")
				.when().post("/api/v1/historico")
				.then().statusCode(400);
	}

	// ---------------- KEEPALIVE ----------------

	@Test
	void keepAlive_ok() {
		given().accept(ContentType.JSON)
				.when().get("/api/v1/keepalive")
				.then().statusCode(200);
	}
}
