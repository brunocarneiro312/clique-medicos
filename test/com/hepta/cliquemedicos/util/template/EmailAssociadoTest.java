package com.hepta.cliquemedicos.util.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hepta.cliquemedicos.dto.CamposEmailDTO;

public class EmailAssociadoTest {

	/**
	 * Inicia cenário de testes (test fixture)
	 */
	@BeforeAll
	public static void beforeClass() {
			
	}
	
	/**
	 * Realiza verificações antes da execução de um método de teste
	 */
	@BeforeEach
	public void setUp() {
			
	}
	
	@Test
	public void givenCamposDTO_whenEmailAssociado_thenReturnEmailAssociadoTemplate() {
		
		// given
		CamposEmailDTO camposEmailDTO = new CamposEmailDTO();
		
		camposEmailDTO.setNomeBeneficiario    ("Bruno Carneiro");     
		camposEmailDTO.setPrestadorNome       ("Thiago Ferreira");        
		camposEmailDTO.setEnderecoAtendimento ("Asa Norte");  
		camposEmailDTO.setNomeCredenciado     ("Bruno Ferreira");
		camposEmailDTO.setEmailBeneficiario   ("bruno.carneiro@hepta.com.br");
		camposEmailDTO.setDataCompra          ("2019-05-24T07:00:00.435");
		camposEmailDTO.setInicio              ("2019-05-24T07:00:00.435");
		camposEmailDTO.setTelefoneBeneficiario("(61)985770401"); 
		
		// when
		String template = EmailAssociado.getTemplate(camposEmailDTO);
		
		// then
		assertNotNull(template);
		assertEquals(expectedTemplateResult(), template);
		
	}
	
	/**
	 * Recupera o resultado do email desejado após utilizar o template.
	 * 
	 * @return
	 */
	private String expectedTemplateResult() {
		return "<!doctype html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><title>Clique Médicos</title><style type=\"text/css\">html,body{font-family:sans-serif;font-size:15px;mso-height-rule:exactly;line-height:20px;color:#555;margin:0!important;padding:0!important;height:100%!important;width:100%!important}*{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}.ExternalClass{width:100%}div[style*=\"margin: 16px 0\"]{margin:0!important}table,td{mso-table-lspace:0pt!important;mso-table-rspace:0pt!important}table{border-spacing:0!important;border-collapse:collapse!important;table-layout:fixed!important;margin:0 auto!important}table table table{table-layout:auto}img{-ms-interpolation-mode:bicubic}.yshortcuts a{border-bottom:none!important}a[x-apple-data-detectors]{color:inherit!important}.button-td,.button-a{transition:all 100ms ease-in}.button-td:hover,.button-a:hover{background:#555555!important;border-color:#555555!important}@media screen and (max-width:600px){.email-container{width:100%!important}.fluid,.fluid-centered{max-width:100%!important;height:auto!important;margin-left:auto!important;margin-right:auto!important}.fluid-centered{margin-left:auto!important;margin-right:auto!important}.stack-column,.stack-column-center{display:block!important;width:100%!important;max-width:100%!important;direction:ltr!important}.stack-column-center{text-align:center!important}.center-on-narrow{text-align:center!important;display:block!important;margin-left:auto!important;margin-right:auto!important;float:none!important}table.center-on-narrow{display:inline-block!important}}</style></head><body bgcolor=\"#E8F0F2\" width=\"100%\" style=\"margin: 0;\"><table bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\"style=\"border-collapse:collapse;\"><tr><td><br/><br/><center style=\"width: 100%;\"><divstyle=\"display:none;font-size:1px;line-height:1px;max-height:0px;max-width:0px;opacity:0;overflow:hidden;mso-hide:all;font-family: sans-serif;\"></div><table cellspacing=\"5\" cellpadding=\"10\" border=\"0\" align=\"center\" bgcolor=\"#ffffff\" width=\"600\"class=\"email-container\" style=\"border-radius: 5px\"><tr><td><table align=\"right\" class=\"email-container\"><tr><td style=\"padding: 20px 0; text-align: center\"><img src=\"http://www.hepta.com.br/Public/email-cliquemedicos/logoCliqueMedicos_email.png\" width=\"351\" alt=\"Clique Médicos\"  border=\"0\" /></td></tr></table></td></tr><tr ><td ><table align=\"center\" class=\"email-container\"  bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\"><tr><td dir=\"ltr\" valign=\"top\" style=\"text-align: center;color:#006880;\" class=\"center-on-narrow\"><h1>Aviso de Servico</h1> <p style=\"font-family: sans-serif; font-size: 18px; mso-height-rule: exactly; line-height: 22px; padding: 10px; text-align: center;\" >Prezado(a) Dr(a)Thiago Ferreira, foi realizado um agendamento por meio do sistema Clique Médicos, seguem os dados da consulta. </p><br/></td></tr></table></td></tr><tr><td style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 15px; color: #555555;\"><hr><table cellspacing=\"5\" cellpadding=\"5\" border=\"0\" align=\"left\"  bgcolor=\"#E8F0F2\" ><tr><td colspan=\"2\"><p><h2>Dados da Consulta</h2></p></td></tr><tr><td valign=\"top\" align=\"right\"><img src=\"http://www.hepta.com.br/Public/email-cliquemedicos/agenda-icon.png\" /></td><td valign=\"top\"><p><strong>24/05/19 - 04:00</strong></p></td></tr><tr ><td valign=\"top\" align=\"right\"><img src=\"http://www.hepta.com.br/Public/email-cliquemedicos/user-icon.png\" /></td><td width=\"500\" valign=\"top\" align=\"left\"><h3><strong>Beneficiário: Bruno Carneiro</tr><tr  valign=\"top\" align=\"right\"><td><img src=\"http://www.hepta.com.br/Public/email-cliquemedicos/contato-icon.png\"></td><td valign=\"top\" align=\"left\"><p><strong>Telefone: (61)985770401</strong></p><p><strong>Email: bruno.carneiro@hepta.com.br</strong></p></td></tr><tr ><td valign=\"top\" align=\"right\"><img src=\"http://www.hepta.com.br/Public/email-cliquemedicos/clinica-icon.png\" /></td><td valign=\"top\"><h3><strong> Bruno Ferreira </strong></h3><p><strong>Asa Norte </strong></p></td></tr></table></td></tr><tr><td dir=\"ltr\" align=\"left\" valign=\"top\" width=\"100%\" style=\"padding: 0px;\"><hr><table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" ><tr><td align=\"left\" valign=\"top\" width=\"150\"><img src=\"http://www.hepta.com.br/Public/email-cliquemedicos/background.png\"/></td><td class=\"stack-column-center \" valign=\"top\" ><table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td dir=\"ltr\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 20px; color: #006880; padding: 10px; text-align: left;\"><strong style=\"color:#111111;\">Lembretes</strong><p>Matenha sua agenda sempre atualizada e não esqueça de validar o Vale no dia da consulta</p><br><br></td></tr></table></td></tr></table></td></tr></table><table align=\"center\" width=\"600\" class=\"email-container\"><tr><tdstyle=\"padding: 40px 10px;width: 100%;font-size: 12px; font-family: sans-serif; mso-height-rule: exactly; line-height:18px; text-align: center; color: #888888;\"><webversion style=\"color:#cccccc; text-decoration:underline; font-weight: bold;\">View as a Web Page</webversion><br><br>Clique Médicos<br><span class=\"mobile-link--footer\">AMHP - Associação dos Médicos de Hospitais Privados do DF</span><br><br><unsubscribe style=\"color:#888888; text-decoration:underline;\"></unsubscribe></td></tr></table></center></td></tr></table></body></html>";
	}
}
