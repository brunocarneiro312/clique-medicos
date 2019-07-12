package com.hepta.cliquemedicos.util;

public class FormatadorCPF {
	public static String retiraPontuacao(String cpf) {
		String cpfFormatado = cpf.replace(".", "").replace("-", "");
		return cpfFormatado;
	}
	
	public static String inserePontuacao(String cpf) {
		String cpfFormatado = cpf.substring(0,2) + "." + cpf.substring(3,5) + "." + cpf.substring(6,8)  
			+ "-" + cpf.substring(9,10);
		return cpfFormatado;
	}
}
