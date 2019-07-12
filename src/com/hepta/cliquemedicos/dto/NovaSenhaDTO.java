package com.hepta.cliquemedicos.dto;

/**
 * 
 * DTO que armazena dados do Request para definição de nova senha
 * 
 * @author bruno.carneiro
 *
 */
public class NovaSenhaDTO {

	private String novaSenha;
	private String token;
	
	public String getNovaSenha() {
		return novaSenha;
	}
	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
}
