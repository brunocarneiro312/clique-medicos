package com.hepta.cliquemedicos.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

/**
 * 
 * Classe utilitária contendo funções para manipulação de arquivos
 * 
 * @author bruno.carneiro
 *
 */
public class ArquivoUtil {

	/**
	 * 
	 * Gera HTML à partir do PDF
	 * 
	 * @param linkDoBoleto
	 * 		URL do boleto
	 * @return
	 * 		true caso consiga obter o HTML
	 * 	    false caso não consiga
	 */
	public static boolean requestBoleto(String linkDoBoleto) {

		try {

			Client client = ClientBuilder.newClient();

			Response response = client
					.target(linkDoBoleto)
					.request(MediaType.TEXT_HTML)
					.get();

			String responseString = response.readEntity(String.class);
			File boletoHTML = new File("WebContent/assets/boleto/html/boleto.html");

			if (boletoHTML.createNewFile()) {

				FileWriter writer = new FileWriter(boletoHTML);
				writer.write(responseString);
				writer.flush();
				writer.close();

				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 
	 * Converter o boleto HTML para documento PDf
	 * 
	 * @param filename
	 * 		Caminho do arquivo HTML que representa o boleto
	 * @return
	 * 		true caso consiga gerar o PDF à partir do HTML
	 * 		false caso não consiga
	 * @throws Exception
	 */
	public static boolean boletoHTML2PDF(String filename) throws Exception {

		try {

			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(
					document, 
					new FileOutputStream("WebContent/assets/boleto/html/boleto.pdf"));
			
			document.open();

			XMLWorkerHelper
				.getInstance()
				.parseXHtml(
						writer, 
						document,
						new FileInputStream("WebContent/assets/boleto/html/boleto.html"));
			
			document.close();

			return true;
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
