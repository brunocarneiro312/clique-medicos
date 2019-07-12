package com.hepta.cliquemedicos.util.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hepta.cliquemedicos.dto.CamposEmailDTO;

import java.util.HashMap;
import java.util.Map;

public class EmailBoletoTest {

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
	public void givenCamposDTO_whenEmailBoleto_thenReturnEmailBoletoTemplate() {

		// given
		Map<String, Object> parametrosAdicionais = new HashMap<String, Object>();
		parametrosAdicionais.put("numeroDoBoleto", "75691500430100003290200044250017179410000015000");
		parametrosAdicionais.put("linkDoBoleto",   "https://develop.cliquemedicos.com.br/75691500430100003290200044250017179410000015000.html");

		CamposEmailDTO camposEmailDTO = new CamposEmailDTO();
		camposEmailDTO.setNomeBeneficiario      ("Bruno Carneiro");
		camposEmailDTO.setPrestadorNome         ("Thiago Ferreira");
		camposEmailDTO.setEnderecoAtendimento   ("Asa Norte");
		camposEmailDTO.setNomeCredenciado       ("Bruno Ferreira");
		camposEmailDTO.setDataCompra            ("2019-05-24T07:00:00.435");
		camposEmailDTO.setInicio                ("2019-05-24T07:00:00.435");
		camposEmailDTO.setTelefoneBeneficiario  ("(61)985770401");
		camposEmailDTO.setEspecialidadeDescricao("Fonoaudiologia");
		camposEmailDTO.setValor                 ("150,00");
		camposEmailDTO.setParametrosAdicionais  (parametrosAdicionais);
		
		// when
		String template = EmailBoleto.getTemplate(camposEmailDTO);

		System.out.println(template);

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
		return "<!doctype html>\t\n" +
				"\t<html>\t\n" +
				"\t<head>\t\n" +
				"\t    <meta charset=\"utf-8\">\t\n" +
				"\t    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\t\n" +
				"\t    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\t\n" +
				"\t    <title>Clique Médicos</title>\t\n" +
				"\t    <style type=\"text/css\">\t\n" +
				"\t        html,body{font-family:sans-serif;font-size:15px;mso-height-rule:exactly;line-height:20px;color:#555;margin:0!important;padding:0!important;height:100%!important;width:100%!important}*{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}.ExternalClass{width:100%}div[style*=\"margin: 16px 0\"]{margin:0!important}table,td{mso-table-lspace:0pt!important;mso-table-rspace:0pt!important}table{border-spacing:0!important;border-collapse:collapse!important;table-layout:fixed!important;margin:0 auto!important}table table table{table-layout:auto}img{-ms-interpolation-mode:bicubic}.yshortcuts a{border-bottom:none!important}a[x-apple-data-detectors]{color:inherit!important}.button-td,.button-a{transition:all 100ms ease-in}.button-td:hover,.button-a:hover{background:#555555!important;border-color:#555555!important}@media screen and (max-width:600px){.email-container{width:100%!important}.fluid,.fluid-centered{max-width:100%!important;height:auto!important;margin-left:auto!important;margin-right:auto!important}.fluid-centered{margin-left:auto!important;margin-right:auto!important}.stack-column,.stack-column-center{display:block!important;width:100%!important;max-width:100%!important;direction:ltr!important}.stack-column-center{text-align:center!important}.center-on-narrow{text-align:center!important;display:block!important;margin-left:auto!important;margin-right:auto!important;float:none!important}table.center-on-narrow{display:inline-block!important}}\t\n" +
				"\t    </style>\t\n" +
				"\t</head>\t\n" +
				"\t\t\n" +
				"\t<body bgcolor=\"#E8F0F2\" width=\"100%\" style=\"margin: 0;\">\t\n" +
				"\t    <table bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\" style=\"border-collapse:collapse;\">\t\n" +
				"\t        <tr>\t\n" +
				"\t            <td>\t\n" +
				"\t                <br/>\t\n" +
				"\t                <br/>\t\n" +
				"\t                <center style=\"width: 100%;\">\t\n" +
				"\t                    <div style=\"display:none;font-size:1px;line-height:1px;max-height:0px;max-width:0px;opacity:0;overflow:hidden;mso-hide:all;font-family: sans-serif;\">\t\n" +
				"\t                    </div>\t\n" +
				"\t                    <table cellspacing=\"5\" cellpadding=\"10\" border=\"0\" align=\"center\" bgcolor=\"#ffffff\" width=\"600\" class=\"email-container\" style=\"border-radius: 5px\">\t\n" +
				"\t                        <tr>\t\n" +
				"\t                            <td>\t\n" +
				"\t                                <table align=\"right\" class=\"email-container\">\t\n" +
				"\t                                    <tr>\t\n" +
				"\t                                        <td style=\"padding: 20px 0; text-align: center\">\t\n" +
				"\t                                            <img src=\"http://www.hepta.com.br/Public/email-cliquemedicos/logoCliqueMedicos_email.png\" width=\"351\" alt=\"Clique Médicos\" border=\"0\" />\t\n" +
				"\t                                        </td>\t\n" +
				"\t                                    </tr>\t\n" +
				"\t                                </table>\t\n" +
				"\t                            </td>\t\n" +
				"\t                        </tr>\t\n" +
				"\t                        <tr>\t\n" +
				"\t                            <td>\t\n" +
				"\t                                <table align=\"center\" class=\"email-container\" bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\">\t\n" +
				"\t                                    <tr>\t\n" +
				"\t                                        <td dir=\"ltr\" valign=\"top\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 18px; color: #555555; padding: 10px; text-align: center;\" class=\"center-on-narrow\">\t\n" +
				"\t                                            <h1><strong style=\"color:#006880;\">SOLICITAÇÃO DE CONSULTA</strong> </h1>\t\n" +
				"\t                                            <p style=\"font-family: sans-serif; font-size: 18px; mso-height-rule: exactly; line-height: 22px; padding: 10px; text-align: center;\" >Prezado(a) \tBruno Carneiro, seu pedido foi registrado e estamos aguardando o pagamento para confirmar sua consulta. O pedido pode ser acompanhado pelo site em \"MEUS AGENDAMENTOS\".</p>\t\n" +
				"\t\t\t\t\t\t\t\t\t\t\t\t<h3  style=\"text-align: center\"> NÚMERO DO BOLETO</h3>\n" +
				"\t\t\t\t\t\t\t\t\t\t\t\t<h3  style=\"text-align: center\"> 75691500430100003290200044250017179410000015000</h3>\n" +
				"\t\t\t\t\t\t\t\t\t\t\t\t<h3  style=\"text-align: center\"> <a href=\"https://develop.cliquemedicos.com.br/75691500430100003290200044250017179410000015000.html\" target=\"_blank\">Visualizar Boleto</a></h3>\n" +
				"\t                                        </td>\t\n" +
				"\t                                    </tr>\t\n" +
				"\t                                </table>\t\n" +
				"\t                            </td>\t\n" +
				"\t                        </tr>\t\n" +
				"\t                        <tr>\t\n" +
				"\t                            <td style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 15px; color: #555555;\">\t\n" +
				"\t                                <hr>\t\n" +
				"\t                                <table cellspacing=\"5\" cellpadding=\"5\" border=\"0\" align=\"left\" bgcolor=\"#E8F0F2\">\t\n" +
				"\t                                    <tr>\t\n" +
				"\t                                        <td colspan=\"2\">\t\n" +
				"\t                                            <p><h2>Dados da Consulta</h2></p>\t\n" +
				"\t                                        </td>\t\n" +
				"\t                                    </tr>\t\n" +
				"\t                                    <tr>\t\n" +
				"\t                                        <td width=\"100\" valign=\"top\" align=\"right\">\t\n" +
				"\t                                            <img src=\"http://www.hepta.com.br/Public/email-cliquemedicos/user-icon.png\" />\t\n" +
				"\t                                        </td>\t\n" +
				"\t                                        <td width=\"500\" valign=\"top\" align=\"left\">\t\n" +
				"\t                                            <p><strong>Beneficiário</strong></p>\t\n" +
				"\t                                            <h3><strong>Bruno Carneiro</strong></h3>\t\n" +
				"\t                                        </td>\t\n" +
				"\t                                    </tr>\t\n" +
				"\t                                    <tr>\t\n" +
				"\t                                        <td width=\"100\" valign=\"top\" align=\"right\">\t\n" +
				"\t                                            <img src=\"http://www.hepta.com.br/Public/email-cliquemedicos/associado-icon.png\" />\t\n" +
				"\t                                        </td>\t\n" +
				"\t                                        <td width=\"500\" valign=\"top\" align=\"left\">\t\n" +
				"\t                                            <p><strong>Fonoaudiologia</strong></p>\t\n" +
				"\t                                            <h3><strong> Dr(a). Thiago Ferreira</strong></h3>\t\n" +
				"\t                                        </td>\t\n" +
				"\t                                    </tr>\t\n" +
				"\t                                    <tr>\t\n" +
				"\t                                        <td valign=\"top\" align=\"right\">\t\n" +
				"\t                                            <img src=\"http://www.hepta.com.br/Public/email-cliquemedicos/clinica-icon.png\" />\t\n" +
				"\t                                        </td>\t\n" +
				"\t                                        <td valign=\"top\">\t\n" +
				"\t                                            <h3><strong>Bruno Ferreira</strong></h3>\t\n" +
				"\t                                            <p>Asa Norte</p>\t\n" +
				"\t                                        </td>\t\n" +
				"\t                                    </tr>\t\n" +
				"\t                                    <tr>\t\n" +
				"\t                                        <td valign=\"top\" align=\"right\">\t\n" +
				"\t                                            <img src=\"http://www.hepta.com.br/Public/email-cliquemedicos/agenda-icon.png\" />\t\n" +
				"\t                                        </td>\t\n" +
				"\t                                        <td valign=\"top\">\t\n" +
				"\t                                            <p><strong>24/05/19 - 04:00</strong></p>\t\n" +
				"\t                                        </td>\t\n" +
				"\t                                    </tr>\t\n" +
				"\t                                    <tr>\t\n" +
				"\t                                        <td>\t\n" +
				"\t                                            <h3>Valor </h3>\t\n" +
				"\t                                        </td>\t\n" +
				"\t                                        <td style=\"text-align: right\" align=\"right\">\t\n" +
				"\t                                            <h3><strong>R$ 150,00</strong></h3>\t\n" +
				"\t                                        </td>\t\n" +
				"\t                                    </tr>\t\n" +
				"\t                                </table>\t\n" +
				"\t                            </td>\t\n" +
				"\t                        </tr>\t\n" +
				"\t                        <tr>\t\n" +
				"\t                            <td dir=\"ltr\" align=\"left\" valign=\"top\" width=\"100%\" style=\"padding: 0px;\">\t\n" +
				"\t                                <hr>\t\n" +
				"\t                                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\t\n" +
				"\t                                    <tr>\t\n" +
				"\t                                        <td class=\"stack-column-center\">\t\n" +
				"\t                                            <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\t\n" +
				"\t                                                <tr>\t\n" +
				"\t                                                    <td dir=\"ltr\" valign=\"top\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 20px; color: #555555; padding: 10px; text-align: left;\">\t\n" +
				"\t                                                        <strong style=\"color:#111111;\">Lembretes</strong>\t\n" +
				"\t                                                        <p>O número do Vale será gerado após a confirmação do pagamento.</p>\t\n" +
				"\t                                                        <p>Não esqueça de trazer o seu número do Vale! Ele é essencial para a sua consulta.</p>\t\n" +
				"\t                                                        <p>Recomenda-se chegar, com pelo menos, 15 minutos de antecedência. </p>\t\n" +
				"\t                                                        <p>Lembre-se de trazer um documento oficial com foto, em bom estado de conservação. São aceitas carteira de identidade (RG), carteira nacional de habilitação (CNH), carteira de trabalho, carteira profissional (por exemplo, CREA, OAB etc.) ou passaporte.</p>\t\n" +
				"\t                                                        <p>Menores de 18 anos devem estar acompanhados de um responsável.</p><br><br>\t\n" +
				"\t                                                    </td>\t\n" +
				"\t                                                </tr>\t\n" +
				"\t                                            </table>\t\n" +
				"\t                                        </td>\t\n" +
				"\t                                    </tr>\t\n" +
				"\t                                </table>\t\n" +
				"\t                            </td>\t\n" +
				"\t                        </tr>\t\n" +
				"\t                    </table>\t\n" +
				"\t                    <table align=\"center\" width=\"600\" class=\"email-container\">\t\n" +
				"\t                        <tr>\t\n" +
				"\t                            <td style=\"padding: 40px 10px;width: 100%;font-size: 12px; font-family: sans-serif; mso-height-rule: exactly; line-height:18px; text-align: center; color: #888888;\">\t\n" +
				"\t                                <webversion style=\"color:#cccccc; text-decoration:underline; font-weight: bold;\">View as a Web Page\t\n" +
				"\t                                </webversion><br><br> Clique Médicos<br>\t\n" +
				"\t                                <span class=\"mobile-link--footer\">AMHP - Associação dos Médicos de Hospitais Privados do DF</span>\t\n" +
				"\t                                <br><br><unsubscribe style=\"color:#888888; text-decoration:underline;\"></unsubscribe>\t\n" +
				"\t                            </td>\t\n" +
				"\t                        </tr>\t\n" +
				"\t                    </table>\t\n" +
				"\t                </center>\t\n" +
				"\t            </td>\t\n" +
				"\t        </tr>\t\n" +
				"\t    </table>\t\n" +
				"\t</body>\t\n" +
				"\t</html>";
	}
}
