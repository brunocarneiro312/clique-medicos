package com.hepta.cliquemedicos.util.template;

import com.hepta.cliquemedicos.dto.CamposEmailDTO;
import com.hepta.cliquemedicos.util.AmbienteUtil;

public class EmailVerificacao extends EmailTemplate {

	/**
	 * Obtém o template do email de verificação de registro
	 * 
	 * @param campos
	 * @param token
	 * @return
	 */
	public static String getTemplate(CamposEmailDTO campos, String token) {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<!doctype html>".trim());
		buffer.append("	<html>".trim());
		buffer.append("	<head>".trim());
		buffer.append("	 	<meta charset=\"utf-8\">".trim());
		buffer.append("	 	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">".trim());
		buffer.append("	 	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">".trim());
		buffer.append("	 	<title>Clique Médicos</title>".trim());
		buffer.append("	 	<style type=\"text/css\">".trim());
		buffer.append("	 	html,body{font-family:sans-serif;font-size:15px;mso-height-rule:exactly;line-height:20px;color:#555;margin:0!important;padding:0!important;height:100%!important;width:100%!important}*{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}.ExternalClass{width:100%}div[style*=\"margin: 16px 0\"]{margin:0!important}table,td{mso-table-lspace:0pt!important;mso-table-rspace:0pt!important}table{border-spacing:0!important;border-collapse:collapse!important;table-layout:fixed!important;margin:0 auto!important}table table table{table-layout:auto}img{-ms-interpolation-mode:bicubic}.yshortcuts a{border-bottom:none!important}a[x-apple-data-detectors]{color:inherit!important}.button-td,.button-a{transition:all 100ms ease-in}.button-td:hover,.button-a:hover{background:#555555!important;border-color:#555555!important}@media screen and (max-width:600px){.email-container{width:100%!important}.fluid,.fluid-centered{max-width:100%!important;height:auto!important;margin-left:auto!important;margin-right:auto!important}.fluid-centered{margin-left:auto!important;margin-right:auto!important}.stack-column,.stack-column-center{display:block!important;width:100%!important;max-width:100%!important;direction:ltr!important}.stack-column-center{text-align:center!important}.center-on-narrow{text-align:center!important;display:block!important;margin-left:auto!important;margin-right:auto!important;float:none!important}table.center-on-narrow{display:inline-block!important}}".trim());
		buffer.append("	 	</style>".trim());
		buffer.append("	</head>	".trim());
		buffer.append("	<body bgcolor=\"#E8F0F2\" width=\"100%\" style=\"margin: 0;\">	".trim());
		buffer.append("	 	<table bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\"".trim());
		buffer.append("	 		style=\"border-collapse:collapse;\">".trim());
		buffer.append("	 		<tr>".trim());
		buffer.append("	 			<td>".trim());
		buffer.append("	 				<br/>".trim());
		buffer.append("	 				<br/>".trim());
		buffer.append("	 				<center style=\"width: 100%;\">".trim());
		buffer.append("	 					<div".trim());
		buffer.append("	 						style=\"display:none;font-size:1px;line-height:1px;max-height:0px;max-width:0px;opacity:0;overflow:hidden;mso-hide:all;font-family: sans-serif;\">".trim());
		buffer.append("	 					</div>".trim());
		buffer.append("	 					<table cellspacing=\"5\" cellpadding=\"10\" border=\"0\" align=\"center\" bgcolor=\"#ffffff\" width=\"600\"".trim());
		buffer.append("	 						class=\"email-container\" style=\"border-radius: 5px\">".trim());
		buffer.append("	 						<tr>".trim());
		buffer.append("	 							<td>".trim());
		buffer.append("	 								<table align=\"right\" class=\"email-container\">".trim());
		buffer.append("	 									<tr>".trim());
		buffer.append("	 										<td style=\"padding: 20px 0; text-align: center\">".trim());
		buffer.append("	 											<img src=\"${caminhoImagem}logoCliqueMedicos_email.png\" width=\"351\" alt=\"Clique Médicos\"  border=\"0\" />".trim());
		buffer.append("	 										</td>".trim());
		buffer.append("	 									</tr>".trim());
		buffer.append("	 								</table>".trim());
		buffer.append("	 							</td>".trim());
		buffer.append("	 						</tr>".trim());
		buffer.append("	 						<tr >".trim());
		buffer.append("	 							<td >".trim());
		buffer.append("	 								<table align=\"center\" class=\"email-container\"  bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\">".trim());
		buffer.append("	 									<tr>".trim());
		buffer.append("	 										<td dir=\"ltr\" valign=\"top\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 25px; color: #006880; padding: 5px; text-align: center;\" class=\"center-on-narrow\">										".trim());
		buffer.append("	 											<h1>Clique no link a seguir para verificar sua conta:</h1> <p style=\"font-family: sans-serif; font-size: 18px; mso-height-rule: exactly; line-height: 22px; padding: 10px; text-align: center;\" >Confirme seu e-mail e marque sua consulta de forma rápida, segura e moderna</p>".trim());
		buffer.append("	 											".trim());
		buffer.append("	 											 <h2 style=\"text-align: center\"><a href=\"${emailVerificacao}\">${emailVerificacao}</a></h2>".trim());
		buffer.append("	 										</td>".trim());
		buffer.append("	 									</tr>".trim());
		buffer.append("	 								</table> ".trim());
		buffer.append("	 							</td>".trim());
		buffer.append("	 						</tr>".trim());
		buffer.append("	 						<tr>".trim());
		buffer.append("	 							<td dir=\"ltr\" align=\"left\" valign=\"top\" width=\"100%\" style=\"padding: 0px;\">".trim());
		buffer.append("	 								<hr>".trim());
		buffer.append("	 								<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" >".trim());
		buffer.append("	 									<tr>".trim());
		buffer.append("	 									  <td align=\"left\" valign=\"top\" width=\"232\"><img src=\"${caminhoImagem}background.png\"/></td>".trim());
		buffer.append("	 									  <td class=\"stack-column-center \" valign=\"top\" >".trim());
		buffer.append("	 										  <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">".trim());
		buffer.append("	 											<tr>".trim());
		buffer.append("	 											  <td dir=\"ltr\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 20px; color: #006880; padding: 10px; text-align: left;\">".trim());
		buffer.append("	 												  <strong style=\"color:#111111;\">Atenção</strong>".trim());
		buffer.append("	 													<p>Caso não tenha solicitado a alteração da senha, desconsidere esse e-mail.<br/>       Atenciosamente, Equipe Clique Médicos</p>".trim());
		buffer.append("	 													<br>".trim());
		buffer.append("	 													<br>".trim());
		buffer.append("	 												</td>".trim());
		buffer.append("	 											</tr>".trim());
		buffer.append("	 										  </table>".trim());
		buffer.append("	 										</td>".trim());
		buffer.append("	 									</tr>".trim());
		buffer.append("	 							  </table>".trim());
		buffer.append("	 							</td>".trim());
		buffer.append("	 						</tr>".trim());
		buffer.append("	 					</table>".trim());
		buffer.append("	 					<table align=\"center\" width=\"600\" class=\"email-container\">".trim());
		buffer.append("	 						<tr>".trim());
		buffer.append("	 							<td style=\"padding: 40px 10px;width: 100%;font-size: 12px; font-family: sans-serif; mso-height-rule: exactly; line-height:18px; text-align: center; color: #888888;\">".trim());
		buffer.append("	 								<webversion style=\"color:#cccccc; text-decoration:underline; font-weight: bold;\">View as a Web Page".trim());
		buffer.append("	 								</webversion><br><br>".trim());
		buffer.append("	 								Clique Médicos<br>".trim());
		buffer.append("	 								<span class=\"mobile-link--footer\">AMHP - Associação dos Médicos de Hospitais Privados do DF</span><br><br>".trim());
		buffer.append("	 								<unsubscribe style=\"color:#888888; text-decoration:underline;\"></unsubscribe>".trim());
		buffer.append("	 							</td>".trim());
		buffer.append("	 						</tr>".trim());
		buffer.append("	 					</table>".trim());
		buffer.append("	 				</center>".trim());
		buffer.append("	 			</td>".trim());
		buffer.append("	 		</tr>".trim());
		buffer.append("	 	</table>".trim());
		buffer.append("	</body>	".trim());
		buffer.append("	</html>	".trim());
		
		String template = buffer.toString();
		
		// Setando os parâmetros do template
		template = template.replaceAll("\\$\\{caminhoImagem\\}",        AmbienteUtil.CAMINHO_IMAGEM);
		template = template.replaceAll("\\$\\{emailVerificacao\\}",     AmbienteUtil.ENDERECO_EMAIL_VERIFICACAO + token);
		template = template.replaceAll("\\$\\{nomeBeneficiario\\}",     campos.getNomeBeneficiario());
		template = template.replaceAll("\\$\\{prestadorNome\\}",        campos.getPrestadorNome());
		template = template.replaceAll("\\$\\{enderecoAtendimento\\}",  campos.getEnderecoAtendimento());
		template = template.replaceAll("\\$\\{nomeCredenciado\\}",      campos.getNomeCredenciado());
		template = template.replaceAll("\\$\\{telefoneBeneficiario\\}", campos.getTelefoneBeneficiario());
		
		return template
				.replaceAll("^\\s+", "")
				.replaceAll("\\s+$", "");
	}
}
