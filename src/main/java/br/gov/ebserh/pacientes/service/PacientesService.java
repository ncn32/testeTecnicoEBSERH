package br.gov.ebserh.pacientes.service;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Semaphore;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
//import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

import br.gov.ebserh.pacientes.dto.HistoricoDTO;
import br.gov.ebserh.pacientes.dto.PacienteDTO;


@ApplicationScoped
public class PacientesService {

	@Inject
	Logger log;

	static String strLog = "";

	ComunicacaoBD commBd = null;
	
	List<HistoricoDTO> lstHistorico = new ArrayList<HistoricoDTO>();
	final Semaphore semaphoreIdHistorico = new Semaphore(1);
	public long idHistoricoUltimo= 0L;
	
	List<PacienteDTO> lstParametro = new ArrayList<PacienteDTO>();
	long idParametroUltimo= 0L;
	
	public PacientesService() {
		Config config = ConfigProvider.getConfig();
		String strUrlBD = config.getOptionalValue("BD_URL", String.class).orElse("");
		String strUsuario = config.getOptionalValue("BD_USUARIO", String.class).orElse("");
		String strSenha = config.getOptionalValue("BD_SENHA", String.class).orElse("");
		int simultaneo = config.getOptionalValue("BD_SIMULTANEO", Integer.class).orElse(3);
		commBd = new ComunicacaoBD(strUrlBD, strUsuario, strSenha, simultaneo);
		// try {
		// 	lstParametro = carregarParametros();
		// 	idHistoricoUltimo = carregarTotalHistorico();
		// } catch (Exception ex){
		// }
	}
	
	// ---------------------------------------------------------------------
	//  CRUD - PacienteDTO  (tabela tbl0001_Paciente)
	// ---------------------------------------------------------------------

	private static final String TABELA = "tbl0001_Paciente";

	/** Escapa aspas simples para evitar quebra/injecao no SQL montado. */
	private String esc(String valor) {
		if (valor == null)
			return "";
		return valor.replace("'", "''");
	}

	/** Mapeia a linha atual do ResultSet para um PacienteDTO. */
	private PacienteDTO preencherPaciente(ResultSet rs) throws SQLException {
		PacienteDTO dto = new PacienteDTO();
		dto.setId(rs.getLong("id"));
		dto.setNome(rs.getString("nome"));
		dto.setCpf(rs.getString("cpf"));
		dto.setCns(rs.getString("cns"));
		dto.setNascimento(rs.getString("nascimento"));
		dto.setTelefone(rs.getString("telefone"));
		return dto;
	}

	// CREATE
	public boolean inserir(PacienteDTO paciente) {
		if (!commBd.conectar())
			return false;
		String sql = "INSERT INTO " + TABELA + " (nome, cpf, cns, nascimento, telefone) "
				+ "VALUES('" + esc(paciente.getNome()) + "', '" + esc(paciente.getCpf()) + "', '"
				+ esc(paciente.getCns()) + "', '" + esc(paciente.getNascimento()) + "', '"
				+ esc(paciente.getTelefone()) + "');";
		return commBd.execEscrita(sql) > 0;
	}

	// READ - todos
	public List<PacienteDTO> listar() {
		List<PacienteDTO> lista = new ArrayList<PacienteDTO>();
		if (!commBd.conectar())
			return lista;
		String sql = "SELECT id, nome, cpf, cns, nascimento, telefone FROM " + TABELA + " ORDER BY id;";
		ResultSet rs = commBd.execLeitura(sql);
		if (rs == null)
			return lista;
		try {
			while (rs.next()) {
				lista.add(preencherPaciente(rs));
			}
		} catch (SQLException e) {
			log.error("Erro ao listar pacientes: " + e.getMessage());
		}
		return lista;
	}

	// READ - por id
	public PacienteDTO buscarPorId(long id) {
		if (!commBd.conectar())
			return null;
		PacienteDTO dto = null;
		String sql = "SELECT id, nome, cpf, cns, nascimento, telefone FROM " + TABELA + " WHERE id = " + id + ";";
		ResultSet rs = commBd.execLeitura(sql);
		if (rs == null)
			return null;
		try {
			if (rs.next()) {
				dto = preencherPaciente(rs);
			}
		} catch (SQLException e) {
			log.error("Erro ao buscar paciente: " + e.getMessage());
		}
		return dto;
	}

	// UPDATE
	public boolean atualizar(PacienteDTO paciente) {
		if (!commBd.conectar())
			return false;
		String sql = "UPDATE " + TABELA + " SET "
				+ "nome = '" + esc(paciente.getNome()) + "', "
				+ "cpf = '" + esc(paciente.getCpf()) + "', "
				+ "cns = '" + esc(paciente.getCns()) + "', "
				+ "nascimento = '" + esc(paciente.getNascimento()) + "', "
				+ "telefone = '" + esc(paciente.getTelefone()) + "' "
				+ "WHERE id = " + paciente.getId() + ";";
		return commBd.execEscrita(sql) > 0;
	}

	// DELETE
	public boolean excluir(long id) {
		if (!commBd.conectar())
			return false;
		String sql = "DELETE FROM " + TABELA + " WHERE id = " + id + ";";
		return commBd.execEscrita(sql) > 0;
	}

	// ---------------------------------------------------------------------
	//  HISTORICO - HistoricoDTO  (tabela tbl0002_historico)
	// ---------------------------------------------------------------------

	private static final String TABELA_HISTORICO = "tbl0002_historico";

	/** Mapeia a linha atual do ResultSet para um HistoricoDTO. */
	private HistoricoDTO preencherHistorico(ResultSet rs) throws SQLException {
		HistoricoDTO dto = new HistoricoDTO();
		dto.setId(rs.getLong("id"));
		dto.setUsuario(rs.getString("usuario"));
		dto.setOrigem(rs.getString("origem"));
		dto.setRequisicao(rs.getString("requisicao"));
		dto.setDhRequisicao(rs.getString("dhRequisicao"));
		dto.setDeRequisicao(rs.getString("deRequisicao"));
		dto.setResposta(rs.getString("resposta"));
		dto.setDhResposta(rs.getString("dhResposta"));
		dto.setDeResposta(rs.getString("deResposta"));
		return dto;
	}

	// CREATE
	public boolean inserirHistorico(HistoricoDTO historico) {
		if (!commBd.conectar())
			return false;
		String sql = "INSERT INTO " + TABELA_HISTORICO
				+ " (usuario, origem, requisicao, dhRequisicao, deRequisicao, resposta, dhResposta, deResposta) "
				+ "VALUES('" + esc(historico.getUsuario()) + "', '" + esc(historico.getOrigem()) + "', '"
				+ esc(historico.getRequisicao()) + "', '" + esc(historico.getDhRequisicao()) + "', '"
				+ esc(historico.getDeRequisicao()) + "', '" + esc(historico.getResposta()) + "', '"
				+ esc(historico.getDhResposta()) + "', '" + esc(historico.getDeResposta()) + "');";
		return commBd.execEscrita(sql) > 0;
	}

	// READ - todos
	public List<HistoricoDTO> listarHistorico() {
		List<HistoricoDTO> lista = new ArrayList<HistoricoDTO>();
		if (!commBd.conectar())
			return lista;
		String sql = "SELECT id, usuario, origem, requisicao, dhRequisicao, deRequisicao, resposta, dhResposta, deResposta "
				+ "FROM " + TABELA_HISTORICO + " ORDER BY id;";
		ResultSet rs = commBd.execLeitura(sql);
		if (rs == null)
			return lista;
		try {
			while (rs.next()) {
				lista.add(preencherHistorico(rs));
			}
		} catch (SQLException e) {
			log.error("Erro ao listar historico: " + e.getMessage());
		}
		return lista;
	}

	private String getResourceFileAsString(String fileName) {
	    try {
	    	return IOUtils.toString(this.getClass().getResourceAsStream("/"+fileName), "UTF-8");
	    }
	    catch(Exception ex) {
	    	return "ERRO: " + ex.toString();
	    }
	}

	
	
	
}
