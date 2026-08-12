package br.gov.ebserh.pacientes.service;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

public class CustomRestHeaderFilter implements ClientRequestFilter {
	   
	  private final String name;
	  private final String value;
	 
	  public CustomRestHeaderFilter(String name, String value) {
	    this.name = name;
	    this.value = value;
	  }
	 
	  @Override
	  public void filter(ClientRequestContext requestContext) throws IOException {
	    requestContext.getHeaders().add(name, value);  
	  }
}