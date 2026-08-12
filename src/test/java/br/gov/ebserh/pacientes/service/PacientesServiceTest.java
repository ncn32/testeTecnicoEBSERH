package br.gov.ebserh.pacientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import br.gov.ebserh.pacientes.dto.HistoricoDTO;
import br.gov.ebserh.pacientes.dto.PacienteDTO;
import io.quarkus.arc.ClientProxy;
import io.quarkus.test.junit.QuarkusTest;

/**
 * Testes do PacientesService com o helper ComunicacaoBD "mockado".
 *
 * Usa @QuarkusTest porque o construtor do servico le a configuracao via
 * MicroProfile Config (ConfigProvider), que so esta disponivel dentro do
 * contexto Quarkus. O mock e injetado no campo 'commBd' da instancia real
 * (obtida com ClientProxy.unwrap) via reflection.
 */
@QuarkusTest
public class PacientesServiceTest {

	@Inject
	PacientesService service;

	private ComunicacaoBD commBd;

	@BeforeEach
	void setup() throws Exception {
		PacientesService real = (PacientesService) ((ClientProxy) service).arc_contextualInstance();
		commBd = mock(ComunicacaoBD.class);
		Field f = PacientesService.class.getDeclaredField("commBd");
		f.setAccessible(true);
		f.set(real, commBd);
	}

	private PacienteDTO novoPaciente() {
		PacienteDTO p = new PacienteDTO();
		p.setNome("Jose Souza");
		p.setCpf("12345678909");
		p.setCns("700000000000001");
		p.setNascimento("1980-01-01");
		p.setTelefone("+550012345678");
		return p;
	}

	// ---------------- CREATE ----------------

	@Test
	void inserir_sucesso_montaSqlEsalva() {
		when(commBd.conectar()).thenReturn(true);
		when(commBd.execEscrita(anyString())).thenReturn(1);

		assertTrue(service.inserir(novoPaciente()));

		ArgumentCaptor<String> sql = ArgumentCaptor.forClass(String.class);
		verify(commBd).execEscrita(sql.capture());
		assertTrue(sql.getValue().startsWith("INSERT INTO tbl0001_Paciente"));
		assertTrue(sql.getValue().contains("'Jose Souza'"));
		assertTrue(sql.getValue().contains("'12345678909'"));
	}

	@Test
	void inserir_semConexao_retornaFalseNaoEscreve() {
		when(commBd.conectar()).thenReturn(false);
		assertFalse(service.inserir(novoPaciente()));
		verify(commBd, never()).execEscrita(anyString());
	}

	@Test
	void inserir_escritaZero_retornaFalse() {
		when(commBd.conectar()).thenReturn(true);
		when(commBd.execEscrita(anyString())).thenReturn(0);
		assertFalse(service.inserir(novoPaciente()));
	}

	@Test
	void inserir_escapaAspasSimples() {
		when(commBd.conectar()).thenReturn(true);
		when(commBd.execEscrita(anyString())).thenReturn(1);

		PacienteDTO p = novoPaciente();
		p.setNome("O'Brien");
		service.inserir(p);

		ArgumentCaptor<String> sql = ArgumentCaptor.forClass(String.class);
		verify(commBd).execEscrita(sql.capture());
		assertTrue(sql.getValue().contains("'O''Brien'"));
	}

	// ---------------- READ (lista) ----------------

	@Test
	void listar_semConexao_retornaListaVazia() {
		when(commBd.conectar()).thenReturn(false);
		List<PacienteDTO> lst = service.listar();
		assertNotNull(lst);
		assertTrue(lst.isEmpty());
	}

	@Test
	void listar_comResultados_mapeia() throws Exception {
		when(commBd.conectar()).thenReturn(true);
		ResultSet rs = mock(ResultSet.class);
		when(rs.next()).thenReturn(true, true, false);
		when(rs.getLong("id")).thenReturn(1L, 2L);
		when(rs.getString("nome")).thenReturn("A", "B");
		when(rs.getString("cpf")).thenReturn("c1", "c2");
		when(rs.getString("cns")).thenReturn("n1", "n2");
		when(rs.getString("nascimento")).thenReturn("d1", "d2");
		when(rs.getString("telefone")).thenReturn("t1", "t2");
		when(commBd.execLeitura(anyString())).thenReturn(rs);

		List<PacienteDTO> lst = service.listar();
		assertEquals(2, lst.size());
		assertEquals("A", lst.get(0).getNome());
		assertEquals(2L, lst.get(1).getId());
	}

	@Test
	void listar_resultSetNull_retornaVazia() {
		when(commBd.conectar()).thenReturn(true);
		when(commBd.execLeitura(anyString())).thenReturn(null);
		assertTrue(service.listar().isEmpty());
	}

	// ---------------- READ (por id) ----------------

	@Test
	void buscarPorId_encontrado() throws Exception {
		when(commBd.conectar()).thenReturn(true);
		ResultSet rs = mock(ResultSet.class);
		when(rs.next()).thenReturn(true);
		when(rs.getLong("id")).thenReturn(5L);
		when(rs.getString("nome")).thenReturn("Maria");
		when(rs.getString("cpf")).thenReturn("111");
		when(rs.getString("cns")).thenReturn("222");
		when(rs.getString("nascimento")).thenReturn("1990-02-02");
		when(rs.getString("telefone")).thenReturn("999");
		when(commBd.execLeitura(anyString())).thenReturn(rs);

		PacienteDTO p = service.buscarPorId(5);
		assertNotNull(p);
		assertEquals("Maria", p.getNome());

		ArgumentCaptor<String> sql = ArgumentCaptor.forClass(String.class);
		verify(commBd).execLeitura(sql.capture());
		assertTrue(sql.getValue().contains("WHERE id = 5"));
	}

	@Test
	void buscarPorId_naoEncontrado_retornaNull() throws Exception {
		when(commBd.conectar()).thenReturn(true);
		ResultSet rs = mock(ResultSet.class);
		when(rs.next()).thenReturn(false);
		when(commBd.execLeitura(anyString())).thenReturn(rs);
		assertNull(service.buscarPorId(9));
	}

	@Test
	void buscarPorId_semConexao_retornaNull() {
		when(commBd.conectar()).thenReturn(false);
		assertNull(service.buscarPorId(9));
	}

	// ---------------- UPDATE ----------------

	@Test
	void atualizar_sucesso() {
		when(commBd.conectar()).thenReturn(true);
		when(commBd.execEscrita(anyString())).thenReturn(1);

		PacienteDTO p = novoPaciente();
		p.setId(7);
		assertTrue(service.atualizar(p));

		ArgumentCaptor<String> sql = ArgumentCaptor.forClass(String.class);
		verify(commBd).execEscrita(sql.capture());
		assertTrue(sql.getValue().startsWith("UPDATE tbl0001_Paciente"));
		assertTrue(sql.getValue().contains("WHERE id = 7"));
	}

	@Test
	void atualizar_semConexao_false() {
		when(commBd.conectar()).thenReturn(false);
		assertFalse(service.atualizar(novoPaciente()));
		verify(commBd, never()).execEscrita(anyString());
	}

	// ---------------- DELETE ----------------

	@Test
	void excluir_sucesso() {
		when(commBd.conectar()).thenReturn(true);
		when(commBd.execEscrita(anyString())).thenReturn(1);
		assertTrue(service.excluir(3));

		ArgumentCaptor<String> sql = ArgumentCaptor.forClass(String.class);
		verify(commBd).execEscrita(sql.capture());
		assertEquals("DELETE FROM tbl0001_Paciente WHERE id = 3;", sql.getValue());
	}

	@Test
	void excluir_semConexao_false() {
		when(commBd.conectar()).thenReturn(false);
		assertFalse(service.excluir(3));
	}

	// ---------------- HISTORICO ----------------

	@Test
	void inserirHistorico_sucesso_montaSql() {
		when(commBd.conectar()).thenReturn(true);
		when(commBd.execEscrita(anyString())).thenReturn(1);

		HistoricoDTO h = new HistoricoDTO();
		h.setUsuario("jose@email.com.br");
		h.setOrigem("127.0.0.1");
		h.setRequisicao("INCLUSAO");
		h.setDhRequisicao("2026-08-12 13:30:00");
		h.setDeRequisicao("abcdefghij");
		h.setResposta("200-OK");
		h.setDhResposta("2026-08-12 13:30:02");
		h.setDeResposta("");

		assertTrue(service.inserirHistorico(h));

		ArgumentCaptor<String> sql = ArgumentCaptor.forClass(String.class);
		verify(commBd).execEscrita(sql.capture());
		assertTrue(sql.getValue().startsWith("INSERT INTO tbl0002_historico"));
		assertTrue(sql.getValue().contains("'jose@email.com.br'"));
		assertTrue(sql.getValue().contains("'INCLUSAO'"));
	}

	@Test
	void inserirHistorico_semConexao_false() {
		when(commBd.conectar()).thenReturn(false);
		assertFalse(service.inserirHistorico(new HistoricoDTO()));
		verify(commBd, never()).execEscrita(anyString());
	}

	@Test
	void listarHistorico_comResultados() throws Exception {
		when(commBd.conectar()).thenReturn(true);
		ResultSet rs = mock(ResultSet.class);
		when(rs.next()).thenReturn(true, false);
		when(rs.getLong("id")).thenReturn(1L);
		when(rs.getString("usuario")).thenReturn("u");
		when(rs.getString("origem")).thenReturn("o");
		when(rs.getString("requisicao")).thenReturn("r");
		when(rs.getString("dhRequisicao")).thenReturn("dhr");
		when(rs.getString("deRequisicao")).thenReturn("der");
		when(rs.getString("resposta")).thenReturn("resp");
		when(rs.getString("dhResposta")).thenReturn("dhresp");
		when(rs.getString("deResposta")).thenReturn("deresp");
		when(commBd.execLeitura(anyString())).thenReturn(rs);

		List<HistoricoDTO> lst = service.listarHistorico();
		assertEquals(1, lst.size());
		assertEquals("u", lst.get(0).getUsuario());
	}

	@Test
	void listarHistorico_semConexao_vazia() {
		when(commBd.conectar()).thenReturn(false);
		assertTrue(service.listarHistorico().isEmpty());
	}
}
