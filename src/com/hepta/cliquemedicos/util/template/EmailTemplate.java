package com.hepta.cliquemedicos.util.template;

public abstract class EmailTemplate {

	/**
	 * 
	 * Escapa as barras do campo de data para substituir corretamente as datas no template
	 * 
	 * @param data
	 * 		ex: 29/07/1987
	 * 
	 * @return
	 * 		ex: 29\\/07\\/1987
	 */
	public static String escapeData(String data) {
		
		// A data deve conter 10 caracteres no total (com as barras)
		boolean isLengthOk = data.length() == 10;
		
		// separando a data em dia, mes e ano
		String[] dataArray = data.split("/");
		String dia = dataArray[0];
		String mes = dataArray[1];
		String ano = dataArray[2];
		
		// Verificando se o dia, mes e ano são numéricos
		try {
			Integer.parseInt(dia);
			Integer.parseInt(mes);
			Integer.parseInt(ano);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Data inválida: " + data);
		}
		
		// Adicionando barras escapadas à string data final
		String dataEscapada = dia + "\\/" + mes + "\\/" + ano;
		
		// Retorna a data escapada
		return dataEscapada;
	}
	
}
