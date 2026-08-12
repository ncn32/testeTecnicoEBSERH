package br.gov.ebserh.pacientes.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ComunicacaoBD extends Thread {
	boolean fgConexaoOK = false;
	boolean fgTentandoConectar = false;
	String strUrlBD = "";
	String strUsuario = "";
	String strSenha = "";
	Connection conn = null;
	
	int TENTATIVAS_CONSECUTIVAS_VEZES = 3;
	int TENTATIVAS_CONSECUTIVAS_DELAY = 30;
	int TENTATIVAS_AGUARDAR_HORAS = 1;
	
	Semaphore semaphoreExec = null;
 	
	public ComunicacaoBD(String strUrlBD, String strUsuario, String strSenha, int simultaneo) {
		super();
		this.strUrlBD = strUrlBD;
		this.strUsuario = strUsuario;
		this.strSenha = strSenha;
		semaphoreExec = new Semaphore(simultaneo);
		semaphoreExec.release();
	}
	
	public boolean conectar() {
		if (conn==null) {
			try {
				conn = DriverManager.getConnection(strUrlBD, strUsuario, strSenha);
				fgConexaoOK = conn!=null;
				if (fgConexaoOK) fgTentandoConectar = false;
				System.out.println("CONECTADO COM SUCESSO!");
			} catch (SQLException e) {
				System.out.println("ERRO Conexão SQL: " + e.getMessage());
			}
	    }
		return fgConexaoOK;
	}
	
	public boolean desconectar() {
		if (!fgConexaoOK)
			return true;
		if (conn!=null) {
			try {
				conn.close();
				fgConexaoOK = false;
			} catch (SQLException e) {
			}
			conn = null;
	    }
		return true;
	}
	
	public ResultSet execLeitura(String strQuery) {
		ResultSet rs = null;
		
		if (!fgConexaoOK)
			return null;
		
		try {
			semaphoreExec.tryAcquire(60000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			return null;
		}
		
		try {
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(strQuery);
		} catch (SQLException e) {
			fgConexaoOK = false;
			fgTentandoConectar = false;
			System.out.println("ERRO: " + e.getMessage());
		}
		
		if (!fgConexaoOK && conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
			conn = null;
		}
		
		semaphoreExec.release();
		
		return rs;
	}
	
	public int execEscrita(String strSQL) {
		int ret = -1;
		
		if (!fgConexaoOK)
			return -1;
		
		try {
			semaphoreExec.tryAcquire(60000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			return -1;
		}
		
		try {
			Statement stmt = conn.createStatement();
			ret = stmt.executeUpdate(strSQL);
		} catch (SQLException e) {
			fgConexaoOK = false;
			fgTentandoConectar = false;
			System.out.println("ERRO: " + e.getMessage());
		}
		
		if (!fgConexaoOK && conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
			conn = null;
		}
		
		semaphoreExec.release();
		
		return ret;
	}
	
	public void run() {
		//
		fgConexaoOK = false;
		fgTentandoConectar = true;
		if (conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
		conn = null;
		while (conn==null && fgTentandoConectar) {
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				fgTentandoConectar = false;
				return;
			}
			conectar();
		}
		fgTentandoConectar = false;
	}
	
	
}
