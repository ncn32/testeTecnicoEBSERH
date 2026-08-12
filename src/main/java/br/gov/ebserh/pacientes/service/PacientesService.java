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
	
	List<HistoricoDTO> lstHistorico = new ArrayList<HistoricoDTO>();
	final Semaphore semaphoreIdHistorico = new Semaphore(1);
	public long idHistoricoUltimo= 0L;
	
	List<PacienteDTO> lstParametro = new ArrayList<PacienteDTO>();
	long idParametroUltimo= 0L;
	
	public PacientesService() {
		// try {
		// 	lstParametro = carregarParametros();
		// 	idHistoricoUltimo = carregarTotalHistorico();
		// } catch (Exception ex){
		// }
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
