package br.gov.ebserh.pacientes.service;

import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class Resources {

	@Produces
	public Logger produceLog(InjectionPoint injectionPoint) {
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}

	@Produces
	public ResourceBundle producerMessageBundle() {
		return ResourceBundle.getBundle("Message");
	}

}
