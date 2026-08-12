//*****************************************************************************
//*                    EBSERH-PACIENTES CONTROLLER
//*                           GEQTI-2023
//*
//******************************************************************************

package br.gov.ebserh.pacientes.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.ebserh.pacientes.dto.HistoricoDTO;
import br.gov.ebserh.pacientes.dto.PacienteDTO;
import br.gov.ebserh.pacientes.service.PacientesService;



@Path("/")

//@SecuritySchemes( value = {
//	@SecurityScheme(
//			securitySchemeName = "apiKey", 
//			type = SecuritySchemeType.APIKEY, 
//			in = SecuritySchemeIn.HEADER, 
//			apiKeyName = "X-API-KEY"				
//			),
//	@SecurityScheme(
//			securitySchemeName = "access_token_sso", 
//			type = SecuritySchemeType.HTTP, 
//			scheme = "bearer", 
//			bearerFormat = "jwt"
//			)		
//	}
//)

@Tag(name = "EBSERH Pacientes")
@ApplicationScoped
//@Authenticated
public class PacientesController {

	@Inject
	Logger log;
	
	@Inject
	PacientesService pacientesService;

	public static final java.lang.String APPLICATION_XML_VALUE = "application/xml";
	public static final java.lang.String APPLICATION_JSON_VALUE = "application/json";
	public static final java.lang.String ALL_VALUE = "*/*";
	
	public static String getClientIpAddr(HttpServletRequest request) {  
	    String ip = request.getHeader("X-Forwarded-For");  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("Proxy-Client-IP");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("WL-Proxy-Client-IP");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("HTTP_X_FORWARDED");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("HTTP_CLIENT_IP");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("HTTP_FORWARDED_FOR");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("HTTP_FORWARDED");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("HTTP_VIA");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("REMOTE_ADDR");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getRemoteAddr();  
	    }  
	    return ip;  
	}	
	
	@GET
	@Path("/api/v1/keepalive")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.APPLICATION_JSON)
	@Operation(summary = "KeepAlive da aplicação", description = "")
	public Response teste() {
		return Response.ok("TESTE-OK").build();
	}
	
	// ---------------------------------- CRUD PACIENTES ------------------------------------------

	@GET
	@Path("/api/v1/pacientes")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.APPLICATION_JSON)
	@Operation(summary = "Lista todos os pacientes", description = "")
	public Response listarPacientes() {
		log.info("### [INICIO] EBSERH PACIENTES listarPacientes() ###");
		List<PacienteDTO> lstPacientes = pacientesService.listar();
		log.info("### [FIM   ] EBSERH PACIENTES listarPacientes() ###");
		return Response.status(200).entity(lstPacientes).build();
	}

	@GET
	@Path("/api/v1/pacientes/{id}")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.APPLICATION_JSON)
	@Operation(summary = "Busca um paciente pelo id", description = "")
	public Response buscarPaciente(@PathParam("id") long id) {
		log.info("### [INICIO] EBSERH PACIENTES buscarPaciente(" + id + ") ###");
		PacienteDTO paciente = pacientesService.buscarPorId(id);
		log.info("### [FIM   ] EBSERH PACIENTES buscarPaciente() ###");
		if (paciente == null)
			return Response.status(Status.NOT_FOUND).entity("PACIENTE NAO ENCONTRADO").build();
		return Response.status(200).entity(paciente).build();
	}

	@POST
	@Path("/api/v1/pacientes")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.APPLICATION_JSON)
	@Operation(summary = "Cadastra um novo paciente", description = "")
	public Response inserirPaciente(PacienteDTO paciente) {
		log.info("### [INICIO] EBSERH PACIENTES inserirPaciente() ###");
		boolean ret = pacientesService.inserir(paciente);
		log.info("### [FIM   ] EBSERH PACIENTES inserirPaciente() ###");
		if (ret)
			return Response.status(Status.CREATED).entity("SUCESSO").build();
		return Response.status(Status.BAD_REQUEST).entity("ERRO").build();
	}

	@PUT
	@Path("/api/v1/pacientes/{id}")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.APPLICATION_JSON)
	@Operation(summary = "Atualiza um paciente existente", description = "")
	public Response atualizarPaciente(@PathParam("id") long id, PacienteDTO paciente) {
		log.info("### [INICIO] EBSERH PACIENTES atualizarPaciente(" + id + ") ###");
		paciente.setId(id);
		boolean ret = pacientesService.atualizar(paciente);
		log.info("### [FIM   ] EBSERH PACIENTES atualizarPaciente() ###");
		if (ret)
			return Response.status(200).entity("SUCESSO").build();
		return Response.status(Status.BAD_REQUEST).entity("ERRO").build();
	}

	@DELETE
	@Path("/api/v1/pacientes/{id}")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.APPLICATION_JSON)
	@Operation(summary = "Exclui um paciente pelo id", description = "")
	public Response excluirPaciente(@PathParam("id") long id) {
		log.info("### [INICIO] EBSERH PACIENTES excluirPaciente(" + id + ") ###");
		boolean ret = pacientesService.excluir(id);
		log.info("### [FIM   ] EBSERH PACIENTES excluirPaciente() ###");
		if (ret)
			return Response.status(200).entity("SUCESSO").build();
		return Response.status(Status.BAD_REQUEST).entity("ERRO").build();
	}

	// ---------------------------------- HISTORICO ------------------------------------------

	@GET
	@Path("/api/v1/historico")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.APPLICATION_JSON)
	@Operation(summary = "Lista todo o historico", description = "")
	public Response listarHistorico() {
		log.info("### [INICIO] EBSERH PACIENTES listarHistorico() ###");
		List<HistoricoDTO> lstHistorico = pacientesService.listarHistorico();
		log.info("### [FIM   ] EBSERH PACIENTES listarHistorico() ###");
		return Response.status(200).entity(lstHistorico).build();
	}

	@POST
	@Path("/api/v1/historico")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.APPLICATION_JSON)
	@Operation(summary = "Registra um novo historico", description = "")
	public Response inserirHistorico(HistoricoDTO historico) {
		log.info("### [INICIO] EBSERH PACIENTES inserirHistorico() ###");
		boolean ret = pacientesService.inserirHistorico(historico);
		log.info("### [FIM   ] EBSERH PACIENTES inserirHistorico() ###");
		if (ret)
			return Response.status(Status.CREATED).entity("SUCESSO").build();
		return Response.status(Status.BAD_REQUEST).entity("ERRO").build();
	}

	// ---------------------------------- FRONTEND-WEB ------------------------------------------
	
	// O REGEX abaixo aproveita todo o subpath de WEB que é passado.
	@GET
	@Path("/web/{subPath:.*}")
	public Response web(@Context UriInfo uriInfo, @PathParam("subPath") String subPath) {
		//System.out.println("subPath: " + subPath);
		//return Response.ok("subPath: " + subPath + "\n" + "UriInfo: " + uriInfo.getPath()).build();
		byte[] bytes = null;
		String mediaType = MediaType.TEXT_HTML;
		String strResourcePath = "";
		
		// Aqui aponta para o documento default caso não especifique o arquivo.
		if (subPath.equalsIgnoreCase("console"))
			strResourcePath = "/webapp/console.html";
		else
			strResourcePath = "/webapp/"+subPath;
		
		// Scripts
		if (subPath.toLowerCase().indexOf(".css")>0)
			mediaType = "text/css";
		if (subPath.toLowerCase().indexOf(".js")>0)
			mediaType = "text/javascript";
		// Images
		if (subPath.toLowerCase().indexOf(".gif")>0)
			mediaType = "image/gif";
		if (subPath.toLowerCase().indexOf(".png")>0)
			mediaType = "image/png";
		if (subPath.toLowerCase().indexOf(".svg")>0)
			mediaType = "image/svg+xml";
		if (subPath.toLowerCase().indexOf(".jpg")>0)
			mediaType = "image/jpg";
		if (subPath.toLowerCase().indexOf(".bmp")>0)
			mediaType = "image/bmp";
		// Fonts
		if (subPath.toLowerCase().indexOf(".ttf")>0)
			mediaType = "application/x-font-ttf";
		if (subPath.toLowerCase().indexOf(".otf")>0)
			mediaType = "application/x-font-opentype";
		if (subPath.toLowerCase().indexOf(".eot")>0)
			mediaType = "application/vnd.ms-fontobject";
		if (subPath.toLowerCase().indexOf(".eot")>0)
			mediaType = "application/vnd.ms-fontobject";
		if (subPath.toLowerCase().indexOf(".sfnt")>0)
			mediaType = "application/font-sfnt";
		if (subPath.toLowerCase().indexOf(".woff2")>0)
			mediaType = "application/font-woff2";
		
		System.out.println("[RECURSO]: " + strResourcePath + "  (mediaType:" + mediaType + ")");
		
		try {
			//bytes = IOUtils.resourceToByteArray(strResourcePath, );
			bytes = IOUtils.toByteArray(this.getClass().getResourceAsStream(strResourcePath));
		} catch (Exception e) {
			System.out.println("[ERRO]: " + e.getMessage());
		}
		
		return Response.status(Status.OK).type(mediaType).entity(bytes).build();
	}
	
	

}


