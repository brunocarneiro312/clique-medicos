package com.hepta.cliquemedicos.util.template;

import com.hepta.cliquemedicos.dto.CamposEmailDTO;
import com.hepta.cliquemedicos.util.AmbienteUtil;

public class EmailBoleto extends EmailTemplate {

	/**
	 * 
	 * Obtém o template do email de boleto
	 * 
	 * @param campos
	 * @return
	 */
	public static String getTemplate(CamposEmailDTO campos) { 
		
		StringBuffer buffer = new StringBuffer();

		buffer.append("	<!doctype html>	\n");
		buffer.append("	<html>	\n");
		buffer.append("	<head>	\n");
		buffer.append("	    <meta charset=\"utf-8\">	\n");
		buffer.append("	    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">	\n");
		buffer.append("	    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">	\n");
		buffer.append("	    <title>Clique Médicos</title>	\n");
		buffer.append("	    <style type=\"text/css\">	\n");
		buffer.append("	        html,body{font-family:sans-serif;font-size:15px;mso-height-rule:exactly;line-height:20px;color:#555;margin:0!important;padding:0!important;height:100%!important;width:100%!important}*{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}.ExternalClass{width:100%}div[style*=\"margin: 16px 0\"]{margin:0!important}table,td{mso-table-lspace:0pt!important;mso-table-rspace:0pt!important}table{border-spacing:0!important;border-collapse:collapse!important;table-layout:fixed!important;margin:0 auto!important}table table table{table-layout:auto}img{-ms-interpolation-mode:bicubic}.yshortcuts a{border-bottom:none!important}a[x-apple-data-detectors]{color:inherit!important}.button-td,.button-a{transition:all 100ms ease-in}.button-td:hover,.button-a:hover{background:#555555!important;border-color:#555555!important}@media screen and (max-width:600px){.email-container{width:100%!important}.fluid,.fluid-centered{max-width:100%!important;height:auto!important;margin-left:auto!important;margin-right:auto!important}.fluid-centered{margin-left:auto!important;margin-right:auto!important}.stack-column,.stack-column-center{display:block!important;width:100%!important;max-width:100%!important;direction:ltr!important}.stack-column-center{text-align:center!important}.center-on-narrow{text-align:center!important;display:block!important;margin-left:auto!important;margin-right:auto!important;float:none!important}table.center-on-narrow{display:inline-block!important}}	\n");
		buffer.append("	    </style>	\n");
		buffer.append("	</head>	\n");
		buffer.append("		\n");
		buffer.append("	<body bgcolor=\"#E8F0F2\" width=\"100%\" style=\"margin: 0;\">	\n");
		buffer.append("	    <table bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\" style=\"border-collapse:collapse;\">	\n");
		buffer.append("	        <tr>	\n");
		buffer.append("	            <td>	\n");
		buffer.append("	                <br/>	\n");
		buffer.append("	                <br/>	\n");
		buffer.append("	                <center style=\"width: 100%;\">	\n");
		buffer.append("	                    <div style=\"display:none;font-size:1px;line-height:1px;max-height:0px;max-width:0px;opacity:0;overflow:hidden;mso-hide:all;font-family: sans-serif;\">	\n");
		buffer.append("	                    </div>	\n");
		buffer.append("	                    <table cellspacing=\"5\" cellpadding=\"10\" border=\"0\" align=\"center\" bgcolor=\"#ffffff\" width=\"600\" class=\"email-container\" style=\"border-radius: 5px\">	\n");
		buffer.append("	                        <tr>	\n");
		buffer.append("	                            <td>	\n");
		buffer.append("	                                <table align=\"right\" class=\"email-container\">	\n");
		buffer.append("	                                    <tr>	\n");
		buffer.append("	                                        <td style=\"padding: 20px 0; text-align: center\">	\n");
		buffer.append("	                                            <img src=\"${caminhoImagem}logoCliqueMedicos_email.png\" width=\"351\" alt=\"Clique Médicos\" border=\"0\" />	\n");
		buffer.append("	                                        </td>	\n");
		buffer.append("	                                    </tr>	\n");
		buffer.append("	                                </table>	\n");
		buffer.append("	                            </td>	\n");
		buffer.append("	                        </tr>	\n");
		buffer.append("	                        <tr>	\n");
		buffer.append("	                            <td>	\n");
		buffer.append("	                                <table align=\"center\" class=\"email-container\" bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\">	\n");
		buffer.append("	                                    <tr>	\n");
		buffer.append("	                                        <td dir=\"ltr\" valign=\"top\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 18px; color: #555555; padding: 10px; text-align: center;\" class=\"center-on-narrow\">	\n");
		buffer.append("	                                            <h1><strong style=\"color:#006880;\">SOLICITAÇÃO DE CONSULTA</strong> </h1>	\n");
		buffer.append("	                                            <p style=\"font-family: sans-serif; font-size: 18px; mso-height-rule: exactly; line-height: 22px; padding: 10px; text-align: center;\" >Prezado(a) 	${nomeBeneficiario}, seu pedido foi registrado e estamos aguardando o pagamento para confirmar sua consulta. O pedido pode ser acompanhado pelo site em \"MEUS AGENDAMENTOS\".</p>	\n");
		buffer.append("												<h3  style=\"text-align: center\"> NÚMERO DO BOLETO</h3>\n");
		buffer.append("												<h3  style=\"text-align: center\"> ${numeroDoBoleto}</h3>\n");
		buffer.append("												<h3  style=\"text-align: center\"> <a href=\"${linkDoBoleto}\" target=\"_blank\">Visualizar Boleto</a></h3>\n");
		buffer.append("	                                        </td>	\n");
		buffer.append("	                                    </tr>	\n");
		buffer.append("	                                </table>	\n");
		buffer.append("	                            </td>	\n");
		buffer.append("	                        </tr>	\n");
		buffer.append("	                        <tr>	\n");
		buffer.append("	                            <td style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 15px; color: #555555;\">	\n");
		buffer.append("	                                <hr>	\n");
		buffer.append("	                                <table cellspacing=\"5\" cellpadding=\"5\" border=\"0\" align=\"left\" bgcolor=\"#E8F0F2\">	\n");
		buffer.append("	                                    <tr>	\n");
		buffer.append("	                                        <td colspan=\"2\">	\n");
		buffer.append("	                                            <p><h2>Dados da Consulta</h2></p>	\n");
		buffer.append("	                                        </td>	\n");
		buffer.append("	                                    </tr>	\n");
		buffer.append("	                                    <tr>	\n");
		buffer.append("	                                        <td width=\"100\" valign=\"top\" align=\"right\">	\n");
		buffer.append("	                                            <img src=\"${caminhoImagem}user-icon.png\" />	\n");
		buffer.append("	                                        </td>	\n");
		buffer.append("	                                        <td width=\"500\" valign=\"top\" align=\"left\">	\n");
		buffer.append("	                                            <p><strong>Beneficiário</strong></p>	\n");
		buffer.append("	                                            <h3><strong>${nomeBeneficiario}</strong></h3>	\n");
		buffer.append("	                                        </td>	\n");
		buffer.append("	                                    </tr>	\n");
		buffer.append("	                                    <tr>	\n");
		buffer.append("	                                        <td width=\"100\" valign=\"top\" align=\"right\">	\n");
		buffer.append("	                                            <img src=\"${caminhoImagem}associado-icon.png\" />	\n");
		buffer.append("	                                        </td>	\n");
		buffer.append("	                                        <td width=\"500\" valign=\"top\" align=\"left\">	\n");
		buffer.append("	                                            <p><strong>${especialidadeDescricao}</strong></p>	\n");
		buffer.append("	                                            <h3><strong> Dr(a). ${prestadorNome}</strong></h3>	\n");
		buffer.append("	                                        </td>	\n");
		buffer.append("	                                    </tr>	\n");
		buffer.append("	                                    <tr>	\n");
		buffer.append("	                                        <td valign=\"top\" align=\"right\">	\n");
		buffer.append("	                                            <img src=\"${caminhoImagem}clinica-icon.png\" />	\n");
		buffer.append("	                                        </td>	\n");
		buffer.append("	                                        <td valign=\"top\">	\n");
		buffer.append("	                                            <h3><strong>${nomeCredenciado}</strong></h3>	\n");
		buffer.append("	                                            <p>${enderecoAtendimento}</p>	\n");
		buffer.append("	                                        </td>	\n");
		buffer.append("	                                    </tr>	\n");
		buffer.append("	                                    <tr>	\n");
		buffer.append("	                                        <td valign=\"top\" align=\"right\">	\n");
		buffer.append("	                                            <img src=\"${caminhoImagem}agenda-icon.png\" />	\n");
		buffer.append("	                                        </td>	\n");
		buffer.append("	                                        <td valign=\"top\">	\n");
		buffer.append("	                                            <p><strong>${dataEmail}</strong></p>	\n");
		buffer.append("	                                        </td>	\n");
		buffer.append("	                                    </tr>	\n");
		buffer.append("	                                    <tr>	\n");
		buffer.append("	                                        <td>	\n");
		buffer.append("	                                            <h3>Valor </h3>	\n");
		buffer.append("	                                        </td>	\n");
		buffer.append("	                                        <td style=\"text-align: right\" align=\"right\">	\n");
		buffer.append("	                                            <h3><strong>R$ ${valor}</strong></h3>	\n");
		buffer.append("	                                        </td>	\n");
		buffer.append("	                                    </tr>	\n");
		buffer.append("	                                </table>	\n");
		buffer.append("	                            </td>	\n");
		buffer.append("	                        </tr>	\n");
		buffer.append("	                        <tr>	\n");
		buffer.append("	                            <td dir=\"ltr\" align=\"left\" valign=\"top\" width=\"100%\" style=\"padding: 0px;\">	\n");
		buffer.append("	                                <hr>	\n");
		buffer.append("	                                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">	\n");
		buffer.append("	                                    <tr>	\n");
		buffer.append("	                                        <td class=\"stack-column-center\">	\n");
		buffer.append("	                                            <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">	\n");
		buffer.append("	                                                <tr>	\n");
		buffer.append("	                                                    <td dir=\"ltr\" valign=\"top\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 20px; color: #555555; padding: 10px; text-align: left;\">	\n");
		buffer.append("	                                                        <strong style=\"color:#111111;\">Lembretes</strong>	\n");
		buffer.append("	                                                        <p>O número do Vale será gerado após a confirmação do pagamento.</p>	\n");
		buffer.append("	                                                        <p>Não esqueça de trazer o seu número do Vale! Ele é essencial para a sua consulta.</p>	\n");
		buffer.append("	                                                        <p>Recomenda-se chegar, com pelo menos, 15 minutos de antecedência. </p>	\n");
		buffer.append("	                                                        <p>Lembre-se de trazer um documento oficial com foto, em bom estado de conservação. São aceitas carteira de identidade (RG), carteira nacional de habilitação (CNH), carteira de trabalho, carteira profissional (por exemplo, CREA, OAB etc.) ou passaporte.</p>	\n");
		buffer.append("	                                                        <p>Menores de 18 anos devem estar acompanhados de um responsável.</p><br><br>	\n");
		buffer.append("	                                                    </td>	\n");
		buffer.append("	                                                </tr>	\n");
		buffer.append("	                                            </table>	\n");
		buffer.append("	                                        </td>	\n");
		buffer.append("	                                    </tr>	\n");
		buffer.append("	                                </table>	\n");
		buffer.append("	                            </td>	\n");
		buffer.append("	                        </tr>	\n");
		buffer.append("	                    </table>	\n");
		buffer.append("	                    <table align=\"center\" width=\"600\" class=\"email-container\">	\n");
		buffer.append("	                        <tr>	\n");
		buffer.append("	                            <td style=\"padding: 40px 10px;width: 100%;font-size: 12px; font-family: sans-serif; mso-height-rule: exactly; line-height:18px; text-align: center; color: #888888;\">	\n");
		buffer.append("	                                <webversion style=\"color:#cccccc; text-decoration:underline; font-weight: bold;\">View as a Web Page	\n");
		buffer.append("	                                </webversion><br><br> Clique Médicos<br>	\n");
		buffer.append("	                                <span class=\"mobile-link--footer\">AMHP - Associação dos Médicos de Hospitais Privados do DF</span>	\n");
		buffer.append("	                                <br><br><unsubscribe style=\"color:#888888; text-decoration:underline;\"></unsubscribe>	\n");
		buffer.append("	                            </td>	\n");
		buffer.append("	                        </tr>	\n");
		buffer.append("	                    </table>	\n");
		buffer.append("	                </center>	\n");
		buffer.append("	            </td>	\n");
		buffer.append("	        </tr>	\n");
		buffer.append("	    </table>	\n");
		buffer.append("	</body>	\n");
		buffer.append("	</html>	\n");
		
		String template = buffer.toString();

		// Setando os parâmetros do template
		template = template.replaceAll("\\$\\{caminhoImagem\\}",          AmbienteUtil.CAMINHO_IMAGEM);
		template = template.replaceAll("\\$\\{nomeBeneficiario\\}",       campos.getNomeBeneficiario());
		template = template.replaceAll("\\$\\{especialidadeDescricao\\}", campos.getEspecialidadeDescricao());
		template = template.replaceAll("\\$\\{prestadorNome\\}",          campos.getPrestadorNome());
		template = template.replaceAll("\\$\\{enderecoAtendimento\\}",    campos.getEnderecoAtendimento());
		template = template.replaceAll("\\$\\{nomeCredenciado\\}",        campos.getNomeCredenciado());
		template = template.replaceAll("\\$\\{dataEmail\\}",              campos.getDataEmail());
		template = template.replaceAll("\\$\\{valor\\}",                  campos.getValor());
		template = template.replaceAll("\\$\\{linkDoBoleto}",             campos.getParametrosAdicionais().get("linkDoBoleto").toString());
		template = template.replaceAll("\\$\\{numeroDoBoleto}",           campos.getParametrosAdicionais().get("numeroDoBoleto").toString());

		return template
				.replaceAll("^\\s+", "")
				.replaceAll("\\s+$", "");
	}
}
