package br.gov.ebserh.pacientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.sql.ResultSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Teste de integracao "leve" do helper ComunicacaoBD contra um arquivo
 * SQLite temporario. Exercita o caminho real de JDBC (incluindo o registro
 * explicito do driver via Class.forName), que foi onde o bug de runtime ocorreu.
 */
public class ComunicacaoBDTest {

	private static String url;
	private static File dbFile;

	private ComunicacaoBD bd;

	@BeforeAll
	static void criarBanco() throws Exception {
		dbFile = File.createTempFile("pacientes-test", ".sqlite");
		dbFile.deleteOnExit();
		url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
	}

	@BeforeEach
	void setup() {
		bd = new ComunicacaoBD(url, "", "", 1);
		assertTrue(bd.conectar(), "deveria conectar ao SQLite");
		bd.execEscrita("CREATE TABLE IF NOT EXISTS tbl0001_Paciente "
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, cpf TEXT, cns TEXT, nascimento TEXT, telefone TEXT);");
		bd.execEscrita("DELETE FROM tbl0001_Paciente;");
	}

	@AfterEach
	void tearDown() {
		bd.desconectar();
	}

	@Test
	void conectar_ehIdempotente() {
		assertTrue(bd.conectar());
	}

	@Test
	void escrita_e_leitura() throws Exception {
		int n = bd.execEscrita("INSERT INTO tbl0001_Paciente (nome, cpf) VALUES('Teste','111');");
		assertEquals(1, n);

		ResultSet rs = bd.execLeitura("SELECT nome, cpf FROM tbl0001_Paciente;");
		assertNotNull(rs);
		assertTrue(rs.next());
		assertEquals("Teste", rs.getString("nome"));
		assertEquals("111", rs.getString("cpf"));
	}

	@Test
	void execLeitura_semConexao_retornaNull() {
		ComunicacaoBD semConn = new ComunicacaoBD(url, "", "", 1);
		// nao chama conectar() -> fgConexaoOK == false
		assertNull(semConn.execLeitura("SELECT 1;"));
	}

	@Test
	void execEscrita_semConexao_retornaMenosUm() {
		ComunicacaoBD semConn = new ComunicacaoBD(url, "", "", 1);
		assertEquals(-1, semConn.execEscrita("INSERT INTO tbl0001_Paciente (nome) VALUES('x');"));
	}

	@Test
	void desconectar_fechaConexao() {
		assertTrue(bd.desconectar());
		// apos desconectar, leitura retorna null pois fgConexaoOK == false
		assertNull(bd.execLeitura("SELECT 1;"));
	}
}
