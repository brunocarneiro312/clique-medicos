package com.hepta.cliquemedicos.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;

import com.hepta.cliquemedicos.dto.CamposEmailDTO;
import com.hepta.cliquemedicos.dto.enums.TipoEmailEnum;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class EmailUtil {


	private static final String EMAIL_REMETENTE = "desenvolvimento@hepta.com";
	private static final String NOME_REMETENTE = "CliqueMédicos";

	private static final String ASSUNTO_EMAIL_CLIENTE = "Nova Consulta foi agendada!";
	private static final String ASSUNTO_EMAIL_ASSOCIADO = "Nova Consulta foi agendada!";
	private static final String ASSUNTO_EMAIL_VERIFICACAO = "Verifique sua conta CliqueMédicos!";
	private static final String ASSUNTO_EMAIL_BOLETO = "Confira o boleto da sua compra no Cliquemédicos!";
	private static final String ASSUNTO_EMAIL_ALTERAR_SENHA = "Pedido de alteração de senha no CliqueMédicos";
	private static final String ASSUNTO_EMAIL_AGENDAMENTO_CANCELADO = "Sua consulta foi cancelada";
	private static final String ASSUNTO_EMAIL_AGENDAMENTO_PAGO = "Sua consulta foi aprovada!";
	private static final String ASSUNTO_EMAIL_ASSOCIADO_CANCELADO = "Consulta cancelada!";

	private static Properties setProperties() {
		Properties properties = System.getProperties();

		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.port", "25");
		properties.setProperty("mail.smtp.auth", "true");
		return properties;
	}

	private static Properties setPropertiesHepta() {
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", "smtp.hepta.com.br");
		properties.setProperty("mail.smtp.port", "25");
		properties.setProperty("mail.user", "happyta@hepta.com.br");
		properties.setProperty("mail.password", "Passw0rd31072017");
		return properties;
	}

	private static void sendEmail(Session session, Message msg) throws Exception {
		Transport transport = session.getTransport();
		try {
			System.out.println("Sending...");

			// Connect to Amazon SES using the SMTP username and password you specified above.
			transport.connect("email-smtp.us-east-1.amazonaws.com", "AKIARTCPQBGA4AUF3MXD",
					"BAVZkd2HgYOO08iwtBWCS2Z6j823CVst0omeIBvK4rjA");
			transport.sendMessage(msg, msg.getAllRecipients());
			System.out.println("Email sent!");
		} catch (Exception ex) {
			System.out.println("The email was not sent.");
			System.out.println("Error message: " + ex.getMessage());
		} finally {
			transport.close();
		}
	}

	private static String[] escolheConteudo(CamposEmailDTO campos, String token, TipoEmailEnum tipoEmail) {
		String[] resultado = { "", "", "" };
		switch (tipoEmail) {
			case AGENDAMENTO_CLIENTE:
				resultado[0] = ASSUNTO_EMAIL_CLIENTE;
				resultado[1] = emailPedido(campos);
				resultado[2] = campos.getEmailBeneficiario();
				break;
			case AGENDAMENTO_ASSOCIADO:
				resultado[0] = ASSUNTO_EMAIL_ASSOCIADO;
				resultado[1] = emailAssociado(campos);
				resultado[2] = campos.getPrestadorEmail();
				break;
			case BOLETO:
				resultado[0] = ASSUNTO_EMAIL_BOLETO;
				resultado[1] = emailBoleto(campos);
				resultado[2] = campos.getEmailBeneficiario();
				break;
			case VERIFICACAO:
				resultado[0] = ASSUNTO_EMAIL_VERIFICACAO;
				resultado[1] = emailVerificacao(campos, token);
				resultado[2] = campos.getEmailBeneficiario();
				break;
			case ALTERAR_SENHA:
				resultado[0] = ASSUNTO_EMAIL_ALTERAR_SENHA;
				resultado[1] = emailAlterarSenha(campos, token);
				resultado[2] = campos.getEmailBeneficiario();
				break;
			case AGENDAMENTO_CANCELADO:
				resultado[0] = ASSUNTO_EMAIL_AGENDAMENTO_CANCELADO;
				resultado[1] = emailCancelado(campos);
				resultado[2] = campos.getEmailBeneficiario();
				break;
			case AGENDAMENTO_PAGO:
				resultado[0] = ASSUNTO_EMAIL_AGENDAMENTO_PAGO;
				resultado[1] = emailPago(campos);
				resultado[2] = campos.getEmailBeneficiario();
				break;
			case AGENDAMENTO_ASSOCIADO_CANCELADO:
				resultado[0] = ASSUNTO_EMAIL_ASSOCIADO_CANCELADO;
				resultado[1] = emailAssociadoCancelado(campos);
				resultado[2] = campos.getPrestadorEmail();
				break;
			default:
				break;
		}
		return resultado;
	}

	public static void enviarEmailAmazon(CamposEmailDTO campos, String token, TipoEmailEnum tipoEmail)
			throws Exception {
		Properties properties = setProperties();
		Session session = Session.getDefaultInstance(properties);
		MimeMessage msg = new MimeMessage(session);

		String[] res = escolheConteudo(campos, token, tipoEmail);
		String subject = res[0];
		String content = res[1];
		String email = res[2];

		msg.setSubject(subject, "UTF-8");
		msg.setFrom(new InternetAddress(EMAIL_REMETENTE, NOME_REMETENTE));
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
		msg.setContent(content, "text/html; charset=UTF-8");

		sendEmail(session, msg);
	}

	public static void enviarEmail(CamposEmailDTO campos, String token, TipoEmailEnum tipoEmail) throws Exception {
		Properties properties = setPropertiesHepta();
		Session session = Session.getDefaultInstance(properties);
		MimeMessage message = new MimeMessage(session);

		String[] res = escolheConteudo(campos, token, tipoEmail);
		String subject = res[0];
		String content = res[1];
		String email = res[2];

		message.setFrom(new InternetAddress(EMAIL_REMETENTE));
		message.setSubject(subject, "UTF-8");
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
		message.setContent(content, "text/html; charset=UTF-8");
		Transport.send(message);
	}

	private static String emailPedido(CamposEmailDTO campos) {
		return  "	<!doctype html>	\n"
				+"	<html>	\n"
				+"	<head>	\n"
				+"	    <meta charset=\"utf-8\">	\n"
				+"	    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">	\n"
				+"	    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">	\n"
				+"	    <title>Clique Médicos</title>	\n"
				+"	    <style type=\"text/css\">	\n"
				+"	        html,body{font-family:sans-serif;font-size:15px;mso-height-rule:exactly;line-height:20px;color:#555;margin:0!important;padding:0!important;height:100%!important;width:100%!important}*{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}.ExternalClass{width:100%}div[style*=\"margin: 16px 0\"]{margin:0!important}table,td{mso-table-lspace:0pt!important;mso-table-rspace:0pt!important}table{border-spacing:0!important;border-collapse:collapse!important;table-layout:fixed!important;margin:0 auto!important}table table table{table-layout:auto}img{-ms-interpolation-mode:bicubic}.yshortcuts a{border-bottom:none!important}a[x-apple-data-detectors]{color:inherit!important}.button-td,.button-a{transition:all 100ms ease-in}.button-td:hover,.button-a:hover{background:#555555!important;border-color:#555555!important}@media screen and (max-width:600px){.email-container{width:100%!important}.fluid,.fluid-centered{max-width:100%!important;height:auto!important;margin-left:auto!important;margin-right:auto!important}.fluid-centered{margin-left:auto!important;margin-right:auto!important}.stack-column,.stack-column-center{display:block!important;width:100%!important;max-width:100%!important;direction:ltr!important}.stack-column-center{text-align:center!important}.center-on-narrow{text-align:center!important;display:block!important;margin-left:auto!important;margin-right:auto!important;float:none!important}table.center-on-narrow{display:inline-block!important}}	\n"
				+"	    </style>	\n"
				+"	</head>	\n"
				+"		\n"
				+"	<body bgcolor=\"#E8F0F2\" width=\"100%\" style=\"margin: 0;\">	\n"
				+"	    <table bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\" style=\"border-collapse:collapse;\">	\n"
				+"	        <tr>	\n"
				+"	            <td>	\n"
				+"	                <br/>	\n"
				+"	                <br/>	\n"
				+"	                <center style=\"width: 100%;\">	\n"
				+"	                    <div style=\"display:none;font-size:1px;line-height:1px;max-height:0px;max-width:0px;opacity:0;overflow:hidden;mso-hide:all;font-family: sans-serif;\">	\n"
				+"	                    </div>	\n"
				+"	                    <table cellspacing=\"5\" cellpadding=\"10\" border=\"0\" align=\"center\" bgcolor=\"#ffffff\" width=\"600\" class=\"email-container\" style=\"border-radius: 5px\">	\n"
				+"	                        <tr>	\n"
				+"	                            <td>	\n"
				+"	                                <table align=\"right\" class=\"email-container\">	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td style=\"padding: 20px 0; text-align: center\">	\n"
				+"	                                            <img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "logoCliqueMedicos_email.png\" width=\"351\" alt=\"Clique Médicos\" border=\"0\" />	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                </table>	\n"
				+"	                            </td>	\n"
				+"	                        </tr>	\n"
				+"	                        <tr>	\n"
				+"	                            <td>	\n"
				+"	                                <table align=\"center\" class=\"email-container\" bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\">	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td dir=\"ltr\" valign=\"top\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 18px; color: #555555; padding: 10px; text-align: center;\" class=\"center-on-narrow\">	\n"
				+"	                                            <h1><strong style=\"color:#006880;\">SOLICITAÇÃO DE CONSULTA</strong> </h1>	\n"
				+"	                                            <p style=\"font-family: sans-serif; font-size: 18px; mso-height-rule: exactly; line-height: 22px; padding: 10px; text-align: center;\" >Prezado(a) 	"+campos.getNomeBeneficiario()+", seu pedido foi registrado e estamos aguardando o pagamento para confirmar sua consulta. O pedido pode ser acompanhado pelo site em \"MEUS AGENDAMENTOS\".</p>	\n"
				+"												<p  style=\"text-align: center\"> "+ campos.getDataCompraEmail() + "<br/></p>\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                </table>	\n"
				+"	                            </td>	\n"
				+"	                        </tr>	\n"
				+"	                        <tr>	\n"
				+"	                            <td style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 15px; color: #555555;\">	\n"
				+"	                                <hr>	\n"
				+"	                                <table cellspacing=\"5\" cellpadding=\"5\" border=\"0\" align=\"left\" bgcolor=\"#E8F0F2\">	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td colspan=\"2\">	\n"
				+"	                                            <p><h2>Dados da Consulta</h2></p>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td width=\"100\" valign=\"top\" align=\"right\">	\n"
				+"	                                            <img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "user-icon.png\" />	\n"
				+"	                                        </td>	\n"
				+"	                                        <td width=\"500\" valign=\"top\" align=\"left\">	\n"
				+"	                                            <p><strong>Beneficiário</strong></p>	\n"
				+"	                                            <h3><strong>" + campos.getNomeBeneficiario()+"</strong></h3>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td width=\"100\" valign=\"top\" align=\"right\">	\n"
				+"	                                            <img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "associado-icon.png\" />	\n"
				+"	                                        </td>	\n"
				+"	                                        <td width=\"500\" valign=\"top\" align=\"left\">	\n"
				+"	                                            <p><strong>"+campos.getEspecialidadeDescricao()+"</strong></p>	\n"
				+"	                                            <h3><strong> Dr(a). "+campos.getPrestadorNome()+"</strong></h3>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td valign=\"top\" align=\"right\">	\n"
				+"	                                            <img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "clinica-icon.png\" />	\n"
				+"	                                        </td>	\n"
				+"	                                        <td valign=\"top\">	\n"
				+"	                                            <h3><strong>"+campos.getNomeCredenciado()+"</strong></h3>	\n"
				+"	                                            <p>"+campos.getEnderecoAtendimento()+"</p>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td valign=\"top\" align=\"right\">	\n"
				+"	                                            <img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "agenda-icon.png\" />	\n"
				+"	                                        </td>	\n"
				+"	                                        <td valign=\"top\">	\n"
				+"	                                            <p><strong>"+campos.getDataEmail()+"</strong></p>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td>	\n"
				+"	                                            <h3>Valor </h3>	\n"
				+"	                                        </td>	\n"
				+"	                                        <td style=\"text-align: right\" align=\"right\">	\n"
				+"	                                            <h3><strong>R$ "+campos.getValor()+"</strong></h3>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                </table>	\n"
				+"	                            </td>	\n"
				+"	                        </tr>	\n"
				+"	                        <tr>	\n"
				+"	                            <td dir=\"ltr\" align=\"left\" valign=\"top\" width=\"100%\" style=\"padding: 0px;\">	\n"
				+"	                                <hr>	\n"
				+"	                                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td class=\"stack-column-center\">	\n"
				+"	                                            <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">	\n"
				+"	                                                <tr>	\n"
				+"	                                                    <td dir=\"ltr\" valign=\"top\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 20px; color: #555555; padding: 10px; text-align: left;\">	\n"
				+"	                                                        <strong style=\"color:#111111;\">Lembretes</strong>	\n"
				+"	                                                        <p>O número do Vale será gerado após a confirmação do pagamento.</p>	\n"
				+"	                                                        <p>Não esqueça de trazer o seu número do Vale! Ele é essencial para a sua consulta.</p>	\n"
				+"	                                                        <p>Recomenda-se chegar, com pelo menos, 15 minutos de antecedência. </p>	\n"
				+"	                                                        <p>Lembre-se de trazer um documento oficial com foto, em bom estado de conservação. São aceitas carteira de identidade (RG), carteira nacional de habilitação (CNH), carteira de trabalho, carteira profissional (por exemplo, CREA, OAB etc.) ou passaporte.</p>	\n"
				+"	                                                        <p>Menores de 18 anos devem estar acompanhados de um responsável.</p><br><br>	\n"
				+"	                                                    </td>	\n"
				+"	                                                </tr>	\n"
				+"	                                            </table>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                </table>	\n"
				+"	                            </td>	\n"
				+"	                        </tr>	\n"
				+"	                    </table>	\n"
				+"	                    <table align=\"center\" width=\"600\" class=\"email-container\">	\n"
				+"	                        <tr>	\n"
				+"	                            <td style=\"padding: 40px 10px;width: 100%;font-size: 12px; font-family: sans-serif; mso-height-rule: exactly; line-height:18px; text-align: center; color: #888888;\">	\n"
				+"	                                <webversion style=\"color:#cccccc; text-decoration:underline; font-weight: bold;\">View as a Web Page	\n"
				+"	                                </webversion><br><br> Clique Médicos<br>	\n"
				+"	                                <span class=\"mobile-link--footer\">AMHP - Associação dos Médicos de Hospitais Privados do DF</span>	\n"
				+"	                                <br><br><unsubscribe style=\"color:#888888; text-decoration:underline;\"></unsubscribe>	\n"
				+"	                            </td>	\n"
				+"	                        </tr>	\n"
				+"	                    </table>	\n"
				+"	                </center>	\n"
				+"	            </td>	\n"
				+"	        </tr>	\n"
				+"	    </table>	\n"
				+"	</body>	\n"
				+"	</html>	\n";
	}

	private static String emailPago(CamposEmailDTO campos) {
		return  "	<!doctype html>	\n"
				+"	<html>	\n"
				+"	<head>	\n"
				+"	    <meta charset=\"utf-8\">	\n"
				+"	    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">	\n"
				+"	    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">	\n"
				+"	    <title>Clique Médicos</title>	\n"
				+"	    <style type=\"text/css\">	\n"
				+"	        html,body{font-family:sans-serif;font-size:15px;mso-height-rule:exactly;line-height:20px;color:#555;margin:0!important;padding:0!important;height:100%!important;width:100%!important}*{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}.ExternalClass{width:100%}div[style*=\"margin: 16px 0\"]{margin:0!important}table,td{mso-table-lspace:0pt!important;mso-table-rspace:0pt!important}table{border-spacing:0!important;border-collapse:collapse!important;table-layout:fixed!important;margin:0 auto!important}table table table{table-layout:auto}img{-ms-interpolation-mode:bicubic}.yshortcuts a{border-bottom:none!important}a[x-apple-data-detectors]{color:inherit!important}.button-td,.button-a{transition:all 100ms ease-in}.button-td:hover,.button-a:hover{background:#555555!important;border-color:#555555!important}@media screen and (max-width:600px){.email-container{width:100%!important}.fluid,.fluid-centered{max-width:100%!important;height:auto!important;margin-left:auto!important;margin-right:auto!important}.fluid-centered{margin-left:auto!important;margin-right:auto!important}.stack-column,.stack-column-center{display:block!important;width:100%!important;max-width:100%!important;direction:ltr!important}.stack-column-center{text-align:center!important}.center-on-narrow{text-align:center!important;display:block!important;margin-left:auto!important;margin-right:auto!important;float:none!important}table.center-on-narrow{display:inline-block!important}}	\n"
				+"	    </style>	\n"
				+"	</head>	\n"
				+"		\n"
				+"	<body bgcolor=\"#E8F0F2\" width=\"100%\" style=\"margin: 0;\">	\n"
				+"	    <table bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\" style=\"border-collapse:collapse;\">	\n"
				+"	        <tr>	\n"
				+"	            <td>	\n"
				+"	                <br/>	\n"
				+"	                <br/>	\n"
				+"	                <center style=\"width: 100%;\">	\n"
				+"	                    <div style=\"display:none;font-size:1px;line-height:1px;max-height:0px;max-width:0px;opacity:0;overflow:hidden;mso-hide:all;font-family: sans-serif;\">	\n"
				+"	                    </div>	\n"
				+"	                    <table cellspacing=\"5\" cellpadding=\"10\" border=\"0\" align=\"center\" bgcolor=\"#ffffff\" width=\"600\" class=\"email-container\" style=\"border-radius: 5px\">	\n"
				+"	                        <tr>	\n"
				+"	                            <td>	\n"
				+"	                                <table align=\"right\" class=\"email-container\">	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td style=\"padding: 20px 0; text-align: center\">	\n"
				+"	                                            <img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "logoCliqueMedicos_email.png\" width=\"351\" alt=\"Clique Médicos\" border=\"0\" />	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                </table>	\n"
				+"	                            </td>	\n"
				+"	                        </tr>	\n"
				+"	                        <tr>	\n"
				+"	                            <td>	\n"
				+"	                                <table align=\"center\" class=\"email-container\" bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\">	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td dir=\"ltr\" valign=\"top\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 18px; color: #555555; padding: 10px; text-align: center;\" class=\"center-on-narrow\">	\n"
				+"	                                            <h2><strong style=\"color:#006880;\">VALE COMPRA</strong> </h2>	\n"
				+"	                                            <h1>"+ campos.getNumeroVoucher()+"</h1>	\n"
				+"	                                            <p style=\"font-family: sans-serif; font-size: 18px; mso-height-rule: exactly; line-height: 22px; padding: 10px; text-align: center;\" >Prezado(a) "+ campos.getNomeBeneficiario() + ", seu pedido foi confirmado, apresente esse número de Vale no dia da consulta.<br/> Seu pedidos podem ser consultado pelo site em \"MEUS AGENDAMENTOS\".</p>	\n"
				+"												<p  style=\"text-align: center\"> "+ campos.getDataCompraEmail() + " <br/></p>\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                </table>	\n"
				+"	                            </td>	\n"
				+"	                        </tr>	\n"
				+"	                        <tr>	\n"
				+"	                            <td style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 15px; color: #555555;\">	\n"
				+"	                                <hr>	\n"
				+"	                                <table cellspacing=\"5\" cellpadding=\"5\" border=\"0\" align=\"left\" bgcolor=\"#E8F0F2\">	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td colspan=\"2\">	\n"
				+"	                                            <p><h2>Dados da Consulta</h2></p>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td width=\"100\" valign=\"top\" align=\"right\">	\n"
				+"	                                            <img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "user-icon.png\" />	\n"
				+"	                                        </td>	\n"
				+"	                                        <td width=\"500\" valign=\"top\" align=\"left\">	\n"
				+"	                                            <p><strong>Beneficiário</strong></p>	\n"
				+"	                                            <h3><strong>"+campos.getNomeBeneficiario()+"</strong></h3>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td width=\"100\" valign=\"top\" align=\"right\">	\n"
				+"	                                            <img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "associado-icon.png\" />	\n"
				+"	                                        </td>	\n"
				+"	                                        <td width=\"500\" valign=\"top\" align=\"left\">	\n"
				+"	                                            <p><strong>"+campos.getEspecialidadeDescricao()+"</strong></p>	\n"
				+"	                                            <h3><strong>Dr(a). "+campos.getPrestadorNome()+"</strong></h3>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td valign=\"top\" align=\"right\">	\n"
				+"	                                            <img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "clinica-icon.png\" />	\n"
				+"	                                        </td>	\n"
				+"	                                        <td valign=\"top\">	\n"
				+"	                                            <h3><strong>"+campos.getNomeCredenciado()+"</strong></h3>	\n"
				+"	                                            <p><strong>"+campos.getEnderecoAtendimento()+"</strong></p>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td valign=\"top\" align=\"right\">	\n"
				+"	                                            <img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "agenda-icon.png\" />	\n"
				+"	                                        </td>	\n"
				+"	                                        <td valign=\"top\">	\n"
				+"	                                            <p><strong>"+campos.getDataEmail()+"</strong></p>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td>	\n"
				+"	                                            <h3>Valor </h3>	\n"
				+"	                                        </td>	\n"
				+"	                                        <td style=\"text-align: right\" align=\"right\">	\n"
				+"	                                            <h3><strong>R$ "+campos.getValor()+"</strong></h3>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                </table>	\n"
				+"	                            </td>	\n"
				+"	                        </tr>	\n"
				+"	                        <tr>	\n"
				+"	                            <td dir=\"ltr\" align=\"left\" valign=\"top\" width=\"100%\" style=\"padding: 0px;\">	\n"
				+"	                                <hr>	\n"
				+"	                                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td class=\"stack-column-center\">	\n"
				+"	                                            <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">	\n"
				+"	                                                <tr>	\n"
				+"	                                                    <td dir=\"ltr\" valign=\"top\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 20px; color: #555555; padding: 10px; text-align: left;\">	\n"
				+"	                                                        <strong style=\"color:#111111;\">Lembretes</strong>	\n"
				+"	                                                        <p>Não esqueça de trazer o seu número do Vale! Ele é essencial para a sua consulta.</p>	\n"
				+"	                                                        <p>Recomenda-se chegar, com pelo menos, 15 minutos de antecedência. </p>	\n"
				+"	                                                        <p>Lembre-se de trazer um documento oficial com foto, em bom estado de conservação. São aceitas carteira de identidade (RG), carteira nacional de habilitação (CNH), carteira de trabalho, carteira profissional (por exemplo, CREA, OAB etc.) ou passaporte.</p>	\n"
				+"	                                                        <p>Menores de 18 anos devem estar acompanhados de um responsável.</p><br><br>	\n"
				+"	                                                    </td>	\n"
				+"	                                                </tr>	\n"
				+"	                                            </table>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                </table>	\n"
				+"	                            </td>	\n"
				+"	                        </tr>	\n"
				+"	                    </table>	\n"
				+"	                    <table align=\"center\" width=\"600\" class=\"email-container\">	\n"
				+"	                        <tr>	\n"
				+"	                            <td style=\"padding: 40px 10px;width: 100%;font-size: 12px; font-family: sans-serif; mso-height-rule: exactly; line-height:18px; text-align: center; color: #888888;\">	\n"
				+"	                                <webversion style=\"color:#cccccc; text-decoration:underline; font-weight: bold;\">View as a Web Page	\n"
				+"	                                </webversion><br><br> Clique Médicos<br>	\n"
				+"	                                <span class=\"mobile-link--footer\">AMHP - Associação dos Médicos de Hospitais Privados do DF</span>	\n"
				+"	                                <br><br><unsubscribe style=\"color:#888888; text-decoration:underline;\"></unsubscribe>	\n"
				+"	                            </td>	\n"
				+"	                        </tr>	\n"
				+"	                    </table>	\n"
				+"	                </center>	\n"
				+"	            </td>	\n"
				+"	        </tr>	\n"
				+"	    </table>	\n"
				+"	</body>	\n"
				+"	</html>	\n";
	}

	private static String emailCancelado(CamposEmailDTO campos) {
		return  "	<!doctype html>	\n"
				+"	<html>	\n"
				+"	<head>	\n"
				+"	    <meta charset=\"utf-8\">	\n"
				+"	    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">	\n"
				+"	    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">	\n"
				+"	    <title>Clique Médicos</title>	\n"
				+"	    <style type=\"text/css\">	\n"
				+"	        html,body{font-family:sans-serif;font-size:15px;mso-height-rule:exactly;line-height:20px;color:#555;margin:0!important;padding:0!important;height:100%!important;width:100%!important}*{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}.ExternalClass{width:100%}div[style*=\"margin: 16px 0\"]{margin:0!important}table,td{mso-table-lspace:0pt!important;mso-table-rspace:0pt!important}table{border-spacing:0!important;border-collapse:collapse!important;table-layout:fixed!important;margin:0 auto!important}table table table{table-layout:auto}img{-ms-interpolation-mode:bicubic}.yshortcuts a{border-bottom:none!important}a[x-apple-data-detectors]{color:inherit!important}.button-td,.button-a{transition:all 100ms ease-in}.button-td:hover,.button-a:hover{background:#555555!important;border-color:#555555!important}@media screen and (max-width:600px){.email-container{width:100%!important}.fluid,.fluid-centered{max-width:100%!important;height:auto!important;margin-left:auto!important;margin-right:auto!important}.fluid-centered{margin-left:auto!important;margin-right:auto!important}.stack-column,.stack-column-center{display:block!important;width:100%!important;max-width:100%!important;direction:ltr!important}.stack-column-center{text-align:center!important}.center-on-narrow{text-align:center!important;display:block!important;margin-left:auto!important;margin-right:auto!important;float:none!important}table.center-on-narrow{display:inline-block!important}}	\n"
				+"	    </style>	\n"
				+"	</head>	\n"
				+"		\n"
				+"	<body bgcolor=\"#E8F0F2\" width=\"100%\" style=\"margin: 0;\">	\n"
				+"	    <table bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\" style=\"border-collapse:collapse;\">	\n"
				+"	        <tr>	\n"
				+"	            <td>	\n"
				+"	                <br/>	\n"
				+"	                <br/>	\n"
				+"	                <center style=\"width: 100%;\">	\n"
				+"	                    <div style=\"display:none;font-size:1px;line-height:1px;max-height:0px;max-width:0px;opacity:0;overflow:hidden;mso-hide:all;font-family: sans-serif;\">	\n"
				+"	                    </div>	\n"
				+"	                    <table cellspacing=\"5\" cellpadding=\"10\" border=\"0\" align=\"center\" bgcolor=\"#ffffff\" width=\"600\" class=\"email-container\" style=\"border-radius: 5px\">	\n"
				+"	                        <tr>	\n"
				+"	                            <td>	\n"
				+"	                                <table align=\"right\" class=\"email-container\">	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td style=\"padding: 20px 0; text-align: center\">	\n"
				+"	                                            <img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "logoCliqueMedicos_email.png\" width=\"351\" alt=\"Clique Médicos\" border=\"0\" />	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                </table>	\n"
				+"	                            </td>	\n"
				+"	                        </tr>	\n"
				+"	                        <tr>	\n"
				+"	                            <td>	\n"
				+"	                                <table align=\"center\" class=\"email-container\" bgcolor=\"#dc3545\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\">	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td dir=\"ltr\" valign=\"top\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 18px; color: #FFFFFF; padding: 10px; text-align: center;\" class=\"center-on-narrow\">	\n"
				+"	                                            <h2><strong style=\"color:#FFFFFF;\">CONSULTA CANCELADA</strong> </h2>	\n"
				+"	                                            <h1>Cancelamento de Consulta</h1>	\n"
				+"	                                            <p style=\"font-family: sans-serif; font-size: 18px; mso-height-rule: exactly; line-height: 22px; padding: 10px; text-align: center;\" >	\n"
				+"												Prezado(a) "+ campos.getNomeBeneficiario() + ", sua consulta foi cancelada. Não foi possível confirmar o pagamento. Tente novamente.<br/> Os dados e os estados de seus pedidos podem ser consultado pelo site em \"MEUS AGENDAMENTOS\".</p>	\n"
				+"												<p  style=\"text-align: center\"> "+ campos.getDataCompraEmail() + "</p> <br/><br/>\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                </table>	\n"
				+"	                            </td>	\n"
				+"	                        </tr>	\n"
				+"	                        <tr>	\n"
				+"	                            <td style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 15px; color: #555555;\">	\n"
				+"	                                <hr>	\n"
				+"	                                <table cellspacing=\"5\" cellpadding=\"5\" border=\"0\" align=\"left\" bgcolor=\"#E8F0F2\">	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td colspan=\"2\">	\n"
				+"	                                            <p><h2>Dados da Consulta</h2></p>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td width=\"100\" valign=\"top\" align=\"right\">	\n"
				+"	                                            <img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "user-icon.png\" />	\n"
				+"	                                        </td>	\n"
				+"	                                        <td width=\"500\" valign=\"top\" align=\"left\">	\n"
				+"	                                            <p><strong>Beneficiário</strong></p>	\n"
				+"	                                            <h3><strong>"+campos.getNomeBeneficiario()+"</strong></h3>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td width=\"100\" valign=\"top\" align=\"right\">	\n"
				+"	                                            <img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "associado-icon.png\" />	\n"
				+"	                                        </td>	\n"
				+"	                                        <td width=\"500\" valign=\"top\" align=\"left\">	\n"
				+"	                                            <p><strong>"+campos.getEspecialidadeDescricao()+"</strong></p>	\n"
				+"	                                            <h3><strong>Dr(a). "+campos.getPrestadorNome()+"</strong></h3>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td valign=\"top\" align=\"right\">	\n"
				+"	                                            <img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "clinica-icon.png\" />	\n"
				+"	                                        </td>	\n"
				+"	                                        <td valign=\"top\">	\n"
				+"	                                            <h3><strong>"+campos.getNomeCredenciado()+"</strong></h3>	\n"
				+"	                                            <p><strong>"+campos.getEnderecoAtendimento()+"</strong></p>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td valign=\"top\" align=\"right\">	\n"
				+"	                                            <img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "agenda-icon.png\" />	\n"
				+"	                                        </td>	\n"
				+"	                                        <td valign=\"top\">	\n"
				+"	                                            <p><strong>"+campos.getDataEmail()+"</strong></p>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td>	\n"
				+"	                                            <h3>Valor </h3>	\n"
				+"	                                        </td>	\n"
				+"	                                        <td style=\"text-align: right\" align=\"right\">	\n"
				+"	                                            <h3><strong>R$ "+campos.getValor()+"</strong></h3>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                </table>	\n"
				+"	                            </td>	\n"
				+"	                        </tr>	\n"
				+"	                        <tr>	\n"
				+"	                            <td dir=\"ltr\" align=\"left\" valign=\"top\" width=\"100%\" style=\"padding: 0px;\">	\n"
				+"	                                <hr>	\n"
				+"	                                <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">	\n"
				+"	                                    <tr>	\n"
				+"	                                        <td class=\"stack-column-center\">	\n"
				+"	                                            <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">	\n"
				+"	                                                <tr>	\n"
				+"	                                                    <td dir=\"ltr\" valign=\"top\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 20px; color: #555555; padding: 10px; text-align: left;\">	\n"
				+"	                                                        <strong style=\"color:#111111;\">Lembretes</strong>	\n"
				+"	                                                        <p>Não esqueça de trazer o seu número do Vale! Ele é essencial para a sua consulta.</p>	\n"
				+"	                                                        <p>Recomenda-se chegar, com pelo menos, 15 minutos de antecedência. </p>	\n"
				+"	                                                        <p>Lembre-se de trazer um documento oficial com foto, em bom estado de conservação. São aceitas carteira de identidade (RG), carteira nacional de habilitação (CNH), carteira de trabalho, carteira profissional (por exemplo, CREA, OAB etc.) ou passaporte.</p>	\n"
				+"	                                                        <p>Menores de 18 anos devem estar acompanhados de um responsável.</p><br><br>	\n"
				+"	                                                    </td>	\n"
				+"	                                                </tr>	\n"
				+"	                                            </table>	\n"
				+"	                                        </td>	\n"
				+"	                                    </tr>	\n"
				+"	                                </table>	\n"
				+"	                            </td>	\n"
				+"	                        </tr>	\n"
				+"	                    </table>	\n"
				+"	                    <table align=\"center\" width=\"600\" class=\"email-container\">	\n"
				+"	                        <tr>	\n"
				+"	                            <td style=\"padding: 40px 10px;width: 100%;font-size: 12px; font-family: sans-serif; mso-height-rule: exactly; line-height:18px; text-align: center; color: #888888;\">	\n"
				+"	                                <webversion style=\"color:#cccccc; text-decoration:underline; font-weight: bold;\">View as a Web Page	\n"
				+"	                                </webversion><br><br> Clique Médicos<br>	\n"
				+"	                                <span class=\"mobile-link--footer\">AMHP - Associação dos Médicos de Hospitais Privados do DF</span>	\n"
				+"	                                <br><br><unsubscribe style=\"color:#888888; text-decoration:underline;\"></unsubscribe>	\n"
				+"	                            </td>	\n"
				+"	                        </tr>	\n"
				+"	                    </table>	\n"
				+"	                </center>	\n"
				+"	            </td>	\n"
				+"	        </tr>	\n"
				+"	    </table>	\n"
				+"	</body>	\n"
				+"	</html>	\n";
	}

	private static String emailAssociado(CamposEmailDTO campos) {
		return   "	<!doctype html>	\n"
				+"	<html>	\n"
				+"	<head>	\n"
				+"	 	<meta charset=\"utf-8\"> 	\n"
				+"	 	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"> 	\n"
				+"	 	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> 	\n"
				+"	 	<title>Clique Médicos</title> 	\n"
				+"	 	<style type=\"text/css\"> 	\n"
				+"	 	html,body{font-family:sans-serif;font-size:15px;mso-height-rule:exactly;line-height:20px;color:#555;margin:0!important;padding:0!important;height:100%!important;width:100%!important}*{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}.ExternalClass{width:100%}div[style*=\"margin: 16px 0\"]{margin:0!important}table,td{mso-table-lspace:0pt!important;mso-table-rspace:0pt!important}table{border-spacing:0!important;border-collapse:collapse!important;table-layout:fixed!important;margin:0 auto!important}table table table{table-layout:auto}img{-ms-interpolation-mode:bicubic}.yshortcuts a{border-bottom:none!important}a[x-apple-data-detectors]{color:inherit!important}.button-td,.button-a{transition:all 100ms ease-in}.button-td:hover,.button-a:hover{background:#555555!important;border-color:#555555!important}@media screen and (max-width:600px){.email-container{width:100%!important}.fluid,.fluid-centered{max-width:100%!important;height:auto!important;margin-left:auto!important;margin-right:auto!important}.fluid-centered{margin-left:auto!important;margin-right:auto!important}.stack-column,.stack-column-center{display:block!important;width:100%!important;max-width:100%!important;direction:ltr!important}.stack-column-center{text-align:center!important}.center-on-narrow{text-align:center!important;display:block!important;margin-left:auto!important;margin-right:auto!important;float:none!important}table.center-on-narrow{display:inline-block!important}} 	\n"
				+"	 	</style> 	\n"
				+"	</head>	\n"
				+"	<body bgcolor=\"#E8F0F2\" width=\"100%\" style=\"margin: 0;\">	\n"
				+"	 	<table bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\"style=\"border-collapse:collapse;\"> 	\n"
				+"	 		<tr> 	\n"
				+"	 			<td><br/><br/> 	\n"
				+"	 				<center style=\"width: 100%;\"> 	\n"
				+"	 					<div 	\n"
				+"	 						style=\"display:none;font-size:1px;line-height:1px;max-height:0px;max-width:0px;opacity:0;overflow:hidden;mso-hide:all;font-family: sans-serif;\"> 	\n"
				+"	 					</div> 	\n"
				+"	 					<table cellspacing=\"5\" cellpadding=\"10\" border=\"0\" align=\"center\" bgcolor=\"#ffffff\" width=\"600\" 	\n"
				+"	 						class=\"email-container\" style=\"border-radius: 5px\"> 	\n"
				+"	 						<tr> 	\n"
				+"	 							<td> 	\n"
				+"	 								<table align=\"right\" class=\"email-container\"> 	\n"
				+"	 									<tr> 	\n"
				+"	 										<td style=\"padding: 20px 0; text-align: center\"> 	\n"
				+"	 											<img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "logoCliqueMedicos_email.png\" width=\"351\" alt=\"Clique Médicos\"  border=\"0\" /> 	\n"
				+"	 										</td> 	\n"
				+"	 									</tr> 	\n"
				+"	 								</table> 	\n"
				+"	 							</td> 	\n"
				+"	 						</tr> 	\n"
				+"	 						<tr > 	\n"
				+"	 							<td > 	\n"
				+"	 								<table align=\"center\" class=\"email-container\"  bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\"> 	\n"
				+"	 									<tr> 	\n"
				+"	 										<td dir=\"ltr\" valign=\"top\" style=\"text-align: center;color:#006880;\" class=\"center-on-narrow\">										 	\n"
				+"	 											<h1>Aviso de Servico</h1> <p style=\"font-family: sans-serif; font-size: 18px; mso-height-rule: exactly; line-height: 22px; padding: 10px; text-align: center;\" >Prezado(a) Dr(a) \n"
				+ 													campos.getPrestadorNome() + ", foi realizado um agendamento por meio do sistema Clique Médicos, seguem os dados da consulta. </p><br/> 	\n"
				+"	 										</td> 	\n"
				+"	 									</tr> 	\n"
				+"	 								</table>                        	\n"
				+"	 							</td> 	\n"
				+"	 						</tr> 	\n"
				+"	 						<tr> 	\n"
				+"	 							<td style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 15px; color: #555555;\"> 	\n"
				+"	 								<hr> 	\n"
				+"	 								<table cellspacing=\"5\" cellpadding=\"5\" border=\"0\" align=\"left\"  bgcolor=\"#E8F0F2\" > 	\n"
				+"	 									<tr> 	\n"
				+"	 										<td colspan=\"2\"> 	\n"
				+"	 											<p><h2>Dados da Consulta</h2></p> 	\n"
				+"	 										</td> 	\n"
				+"	 									</tr>						 	\n"
				+"	 									<tr> 	\n"
				+"	 										<td valign=\"top\" align=\"right\"> 	\n"
				+"	 											<img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "agenda-icon.png\" /> 	\n"
				+"	 										</td> 	\n"
				+"	 										<td valign=\"top\"> 	\n"
				+"	 											<p><strong>"+ campos.getDataEmail()+"</strong></p> 	\n"
				+"	 										</td> 	\n"
				+"	 									</tr> 	\n"
				+"	 									<tr > 	\n"
				+"	 										<td valign=\"top\" align=\"right\"> 	\n"
				+"	 											<img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "user-icon.png\" /> 	\n"
				+"	 										</td> 	\n"
				+"	 										<td width=\"500\" valign=\"top\" align=\"left\"> 	\n"
				+"	 											<h3><strong>Beneficiário: "+campos.getNomeBeneficiario()+"\n"
				+"	 									</tr> 	\n"
				+"	 									<tr  valign=\"top\" align=\"right\"> 	\n"
				+"	 										<td> 	\n"
				+"	 											<img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "contato-icon.png\"> 	\n"
				+"	 										</td> 	\n"
				+"	 										<td valign=\"top\" align=\"left\"> 	\n"
				+"	 											<p><strong>Telefone: "+campos.getTelefoneBeneficiario()+"</strong></p> 	\n"
				+"	 											<p><strong>Email: "+ campos.getEmailBeneficiario()+"</strong></p> 	\n"
				+"	 										</td> 	\n"
				+"	 									</tr> 	\n"
				+"	 									<tr > 	\n"
				+"	 										<td valign=\"top\" align=\"right\"> 	\n"
				+"	 											<img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "clinica-icon.png\" />		 	\n"
				+"	 										</td> 	\n"
				+"	 										<td valign=\"top\"> 	\n"
				+"	 											<h3><strong> "+campos.getNomeCredenciado()+" </strong></h3> 	\n"
				+"	 											<p><strong>"+ campos.getEnderecoAtendimento()+" </strong></p> 	\n"
				+"	 										</td> 	\n"
				+"	 									</tr> 	\n"
				+"	 								</table> 	\n"
				+"	 							</td> 	\n"
				+"	 						</tr> 	\n"
				+"	 						<tr> 	\n"
				+"	 							<td dir=\"ltr\" align=\"left\" valign=\"top\" width=\"100%\" style=\"padding: 0px;\"> 	\n"
				+"	 								<hr> 	\n"
				+"	 								<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" > 	\n"
				+"	 									<tr> 	\n"
				+"	 									  <td align=\"left\" valign=\"top\" width=\"150\"><img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "background.png\"/></td> 	\n"
				+"	 									  <td class=\"stack-column-center \" valign=\"top\" > 	\n"
				+"	 										  <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> 	\n"
				+"	 											<tr> 	\n"
				+"	 											  <td dir=\"ltr\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 20px; color: #006880; padding: 10px; text-align: left;\"> 	\n"
				+"	 													<strong style=\"color:#111111;\">Lembretes</strong> 	\n"
				+"	 													<p>Matenha sua agenda sempre atualizada e não esqueça de validar o Vale no dia da consulta</p> 	\n"
				+"	 													<br> 	\n"
				+"	 													<br> 	\n"
				+"	 												</td> 	\n"
				+"	 											</tr> 	\n"
				+"	 										  </table> 	\n"
				+"	 										</td> 	\n"
				+"	 									</tr> 	\n"
				+"	 							  </table> 	\n"
				+"	 							</td> 	\n"
				+"	 						</tr> 	\n"
				+"	 					</table> 	\n"
				+"	 					<table align=\"center\" width=\"600\" class=\"email-container\"> 	\n"
				+"	 						<tr> 	\n"
				+"	 							<td 	\n"
				+"	 								style=\"padding: 40px 10px;width: 100%;font-size: 12px; font-family: sans-serif; mso-height-rule: exactly; line-height:18px; text-align: center; color: #888888;\"> 	\n"
				+"	 								<webversion style=\"color:#cccccc; text-decoration:underline; font-weight: bold;\">View as a Web Page 	\n"
				+"	 								</webversion> 	\n"
				+"	 								<br> 	\n"
				+"	 								<br> 	\n"
				+"	 								Clique Médicos<br> 	\n"
				+"	 								<span class=\"mobile-link--footer\">AMHP - Associação dos Médicos de Hospitais Privados do DF</span> 	\n"
				+"	 								<br> 	\n"
				+"	 								<br> 	\n"
				+"	 								<unsubscribe style=\"color:#888888; text-decoration:underline;\"></unsubscribe> 	\n"
				+"	 							</td> 	\n"
				+"	 						</tr> 	\n"
				+"	 					</table> 	\n"
				+"	 				</center> 	\n"
				+"	 			</td> 	\n"
				+"	 		</tr> 	\n"
				+"	 	</table> 	\n"
				+"	</body>	\n"
				+"	</html>	\n";

	}

	private static String emailAssociadoCancelado(CamposEmailDTO campos) {
		return   "	<!doctype html>	\n"
				+"	<html>	\n"
				+"	<head>	\n"
				+"	 	<meta charset=\"utf-8\"> 	\n"
				+"	 	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"> 	\n"
				+"	 	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> 	\n"
				+"	 	<title>Clique Médicos</title> 	\n"
				+"	 	<style type=\"text/css\"> 	\n"
				+"	 	html,body{font-family:sans-serif;font-size:15px;mso-height-rule:exactly;line-height:20px;color:#555;margin:0!important;padding:0!important;height:100%!important;width:100%!important}*{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}.ExternalClass{width:100%}div[style*=\"margin: 16px 0\"]{margin:0!important}table,td{mso-table-lspace:0pt!important;mso-table-rspace:0pt!important}table{border-spacing:0!important;border-collapse:collapse!important;table-layout:fixed!important;margin:0 auto!important}table table table{table-layout:auto}img{-ms-interpolation-mode:bicubic}.yshortcuts a{border-bottom:none!important}a[x-apple-data-detectors]{color:inherit!important}.button-td,.button-a{transition:all 100ms ease-in}.button-td:hover,.button-a:hover{background:#555555!important;border-color:#555555!important}@media screen and (max-width:600px){.email-container{width:100%!important}.fluid,.fluid-centered{max-width:100%!important;height:auto!important;margin-left:auto!important;margin-right:auto!important}.fluid-centered{margin-left:auto!important;margin-right:auto!important}.stack-column,.stack-column-center{display:block!important;width:100%!important;max-width:100%!important;direction:ltr!important}.stack-column-center{text-align:center!important}.center-on-narrow{text-align:center!important;display:block!important;margin-left:auto!important;margin-right:auto!important;float:none!important}table.center-on-narrow{display:inline-block!important}} 	\n"
				+"	 	</style> 	\n"
				+"	</head>	\n"
				+"	<body bgcolor=\"#E8F0F2\" width=\"100%\" style=\"margin: 0;\">	\n"
				+"	 	<table bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\"style=\"border-collapse:collapse;\"> 	\n"
				+"	 		<tr> 	\n"
				+"	 			<td><br/><br/> 	\n"
				+"	 				<center style=\"width: 100%;\"> 	\n"
				+"	 					<div 	\n"
				+"	 						style=\"display:none;font-size:1px;line-height:1px;max-height:0px;max-width:0px;opacity:0;overflow:hidden;mso-hide:all;font-family: sans-serif;\"> 	\n"
				+"	 					</div> 	\n"
				+"	 					<table cellspacing=\"5\" cellpadding=\"10\" border=\"0\" align=\"center\" bgcolor=\"#ffffff\" width=\"600\" 	\n"
				+"	 						class=\"email-container\" style=\"border-radius: 5px\"> 	\n"
				+"	 						<tr> 	\n"
				+"	 							<td> 	\n"
				+"	 								<table align=\"right\" class=\"email-container\"> 	\n"
				+"	 									<tr> 	\n"
				+"	 										<td style=\"padding: 20px 0; text-align: center\"> 	\n"
				+"	 											<img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "logoCliqueMedicos_email.png\" width=\"351\" alt=\"Clique Médicos\"  border=\"0\" /> 	\n"
				+"	 										</td> 	\n"
				+"	 									</tr> 	\n"
				+"	 								</table> 	\n"
				+"	 							</td> 	\n"
				+"	 						</tr> 	\n"
				+"	 						<tr > 	\n"
				+"	 							<td > 	\n"
				+"	 								<table align=\"center\" class=\"email-container\"  bgcolor=\"#dc3545\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\"> 	\n"
				+"	 									<tr> 	\n"
				+"	 										<td dir=\"ltr\" valign=\"top\" style=\"text-align: center;color:#FFFFFF;\" class=\"center-on-narrow\">										 	\n"
				+"	 											<h1>Aviso de Cancelamento</h1> <p style=\"font-family: sans-serif; font-size: 18px; mso-height-rule: exactly; line-height: 22px; padding: 10px; text-align: center;\" >Prezado Dr(a) \n"
				+ 													campos.getPrestadorNome() + ", o agendamento foi cancelado. Seguem os dados da consulta. </p><br/><br/> 	\n"
				+"	 										</td> 	\n"
				+"	 									</tr> 	\n"
				+"	 								</table> \n"
				+"	 							</td> 	\n"
				+"	 						</tr> 	\n"
				+"	 						<tr> 	\n"
				+"	 							<td style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 15px; color: #555555;\"> 	\n"
				+"	 								<hr> 	\n"
				+"	 								<table cellspacing=\"5\" cellpadding=\"5\" border=\"0\" align=\"left\"  bgcolor=\"#E8F0F2\" > 	\n"
				+"	 									<tr> 	\n"
				+"	 										<td colspan=\"2\"> 	\n"
				+"	 											<p><h2>Dados da Consulta Cancelada</h2></p> 	\n"
				+"	 										</td> 	\n"
				+"	 									</tr>						 	\n"
				+"	 									<tr> 	\n"
				+"	 										<td valign=\"top\" align=\"right\"> 	\n"
				+"	 											<img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "agenda-icon.png\" /> 	\n"
				+"	 										</td> 	\n"
				+"	 										<td valign=\"top\"> 	\n"
				+"	 											<p><strong>"+ campos.getDataEmail()+"</strong></p> 	\n"
				+"	 										</td> 	\n"
				+"	 									</tr> 	\n"
				+"	 									<tr > 	\n"
				+"	 										<td valign=\"top\" align=\"right\"> 	\n"
				+"	 											<img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "user-icon.png\" /> 	\n"
				+"	 										</td> 	\n"
				+"	 										<td width=\"500\" valign=\"top\" align=\"left\"> 	\n"
				+"	 											<h3><strong>Beneficiário: "+campos.getNomeBeneficiario()+"\n"
				+"	 									</tr> 	\n"
				+"	 									<tr  valign=\"top\" align=\"right\"> 	\n"
				+"	 										<td> 	\n"
				+"	 											<img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "contato-icon.png\"> 	\n"
				+"	 										</td> 	\n"
				+"	 										<td valign=\"top\" align=\"left\"> 	\n"
				+"	 											<p><strong>Telefone: "+campos.getTelefoneBeneficiario()+"</strong></p> 	\n"
				+"	 											<p><strong>Email: "+ campos.getEmailBeneficiario()+"</strong></p> 	\n"
				+"	 										</td> 	\n"
				+"	 									</tr> 	\n"
				+"	 									<tr > 	\n"
				+"	 										<td valign=\"top\" align=\"right\"> 	\n"
				+"	 											<img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "clinica-icon.png\" />		 	\n"
				+"	 										</td> 	\n"
				+"	 										<td valign=\"top\"> 	\n"
				+"	 											<h3><strong> "+campos.getNomeCredenciado()+" </strong></h3> 	\n"
				+"	 											<p><strong>"+ campos.getEnderecoAtendimento()+" </strong></p> 	\n"
				+"	 										</td> 	\n"
				+"	 									</tr> 	\n"
				+"	 								</table> 	\n"
				+"	 							</td> 	\n"
				+"	 						</tr> 	\n"
				+"	 						<tr> 	\n"
				+"	 							<td dir=\"ltr\" align=\"left\" valign=\"top\" width=\"100%\" style=\"padding: 0px;\"> 	\n"
				+"	 								<hr> 	\n"
				+"	 								<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" > 	\n"
				+"	 									<tr> 	\n"
				+"	 									  <td align=\"left\" valign=\"top\" width=\"150\"><img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "background.png\"/></td> 	\n"
				+"	 									  <td class=\"stack-column-center \" valign=\"top\" > 	\n"
				+"	 										  <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> 	\n"
				+"	 											<tr> 	\n"
				+"	 											  <td dir=\"ltr\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 20px; color: #006880; padding: 10px; text-align: left;\"> 	\n"
				+"	 													<strong style=\"color:#111111;\">Lembretes</strong> 	\n"
				+"	 													<p></p> 	\n"
				+"	 													<br> 	\n"
				+"	 													<br> 	\n"
				+"	 												</td> 	\n"
				+"	 											</tr> 	\n"
				+"	 										  </table> 	\n"
				+"	 										</td> 	\n"
				+"	 									</tr> 	\n"
				+"	 							  </table> 	\n"
				+"	 							</td> 	\n"
				+"	 						</tr> 	\n"
				+"	 					</table> 	\n"
				+"	 					<table align=\"center\" width=\"600\" class=\"email-container\"> 	\n"
				+"	 						<tr> 	\n"
				+"	 							<td 	\n"
				+"	 								style=\"padding: 40px 10px;width: 100%;font-size: 12px; font-family: sans-serif; mso-height-rule: exactly; line-height:18px; text-align: center; color: #888888;\"> 	\n"
				+"	 								<webversion style=\"color:#cccccc; text-decoration:underline; font-weight: bold;\">View as a Web Page 	\n"
				+"	 								</webversion> 	\n"
				+"	 								<br> 	\n"
				+"	 								<br> 	\n"
				+"	 								Clique Médicos<br> 	\n"
				+"	 								<span class=\"mobile-link--footer\">AMHP - Associação dos Médicos de Hospitais Privados do DF</span> 	\n"
				+"	 								<br> 	\n"
				+"	 								<br> 	\n"
				+"	 								<unsubscribe style=\"color:#888888; text-decoration:underline;\"></unsubscribe> 	\n"
				+"	 							</td> 	\n"
				+"	 						</tr> 	\n"
				+"	 					</table> 	\n"
				+"	 				</center> 	\n"
				+"	 			</td> 	\n"
				+"	 		</tr> 	\n"
				+"	 	</table> 	\n"
				+"	</body>	\n"
				+"	</html>	\n";

	}

	private static String emailVerificacao(CamposEmailDTO campos, String token) {
		return   "<!doctype html>	\n"
				+"	<html>	\n"
				+"	<head>	\n"
				+"	 	<meta charset=\"utf-8\">\n"
				+"	 	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
				+"	 	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
				+"	 	<title>Clique Médicos</title>\n"
				+"	 	<style type=\"text/css\">\n"
				+"	 	html,body{font-family:sans-serif;font-size:15px;mso-height-rule:exactly;line-height:20px;color:#555;margin:0!important;padding:0!important;height:100%!important;width:100%!important}*{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}.ExternalClass{width:100%}div[style*=\"margin: 16px 0\"]{margin:0!important}table,td{mso-table-lspace:0pt!important;mso-table-rspace:0pt!important}table{border-spacing:0!important;border-collapse:collapse!important;table-layout:fixed!important;margin:0 auto!important}table table table{table-layout:auto}img{-ms-interpolation-mode:bicubic}.yshortcuts a{border-bottom:none!important}a[x-apple-data-detectors]{color:inherit!important}.button-td,.button-a{transition:all 100ms ease-in}.button-td:hover,.button-a:hover{background:#555555!important;border-color:#555555!important}@media screen and (max-width:600px){.email-container{width:100%!important}.fluid,.fluid-centered{max-width:100%!important;height:auto!important;margin-left:auto!important;margin-right:auto!important}.fluid-centered{margin-left:auto!important;margin-right:auto!important}.stack-column,.stack-column-center{display:block!important;width:100%!important;max-width:100%!important;direction:ltr!important}.stack-column-center{text-align:center!important}.center-on-narrow{text-align:center!important;display:block!important;margin-left:auto!important;margin-right:auto!important;float:none!important}table.center-on-narrow{display:inline-block!important}}\n"
				+"	 	</style>\n"
				+"	</head>	\n"
				+"	<body bgcolor=\"#E8F0F2\" width=\"100%\" style=\"margin: 0;\">	\n"
				+"	 	<table bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\"\n"
				+"	 		style=\"border-collapse:collapse;\">\n"
				+"	 		<tr>\n"
				+"	 			<td>\n"
				+"	 				<br/>\n"
				+"	 				<br/>\n"
				+"	 				<center style=\"width: 100%;\">\n"
				+"	 					<div\n"
				+"	 						style=\"display:none;font-size:1px;line-height:1px;max-height:0px;max-width:0px;opacity:0;overflow:hidden;mso-hide:all;font-family: sans-serif;\">\n"
				+"	 					</div>\n"
				+"	 					<table cellspacing=\"5\" cellpadding=\"10\" border=\"0\" align=\"center\" bgcolor=\"#ffffff\" width=\"600\"\n"
				+"	 						class=\"email-container\" style=\"border-radius: 5px\">\n"
				+"	 						<tr>\n"
				+"	 							<td>\n"
				+"	 								<table align=\"right\" class=\"email-container\">\n"
				+"	 									<tr>\n"
				+"	 										<td style=\"padding: 20px 0; text-align: center\">\n"
				+"	 											<img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "logoCliqueMedicos_email.png\" width=\"351\" alt=\"Clique Médicos\"  border=\"0\" />\n"
				+"	 										</td>\n"
				+"	 									</tr>\n"
				+"	 								</table>\n"
				+"	 							</td>\n"
				+"	 						</tr>\n"
				+"	 						<tr >\n"
				+"	 							<td >\n"
				+"	 								<table align=\"center\" class=\"email-container\"  bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\">\n"
				+"	 									<tr>\n"
				+"	 										<td dir=\"ltr\" valign=\"top\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 25px; color: #006880; padding: 5px; text-align: center;\" class=\"center-on-narrow\">										\n"
				+"	 											<h1>Clique no link a seguir para verificar sua conta:</h1> <p style=\"font-family: sans-serif; font-size: 18px; mso-height-rule: exactly; line-height: 22px; padding: 10px; text-align: center;\" >Confirme seu e-mail e marque sua consulta de forma rápida, segura e moderna</p>\n"
				+"	 											\n"
				+"	 											 <h2 style=\"text-align: center\"><a href=\""+AmbienteUtil.ENDERECO_EMAIL_VERIFICACAO + token + "\">" + AmbienteUtil.ENDERECO_EMAIL_VERIFICACAO + token + "</a></h2>\n"
				+"	 										</td>\n"
				+"	 									</tr>\n"
				+"	 								</table> \n"
				+"	 							</td>\n"
				+"	 						</tr>\n"
				+"	 						<tr>\n"
				+"	 							<td dir=\"ltr\" align=\"left\" valign=\"top\" width=\"100%\" style=\"padding: 0px;\">\n"
				+"	 								<hr>\n"
				+"	 								<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" >\n"
				+"	 									<tr>\n"
				+"	 									  <td align=\"left\" valign=\"top\" width=\"232\"><img src=\"" + AmbienteUtil.CAMINHO_IMAGEM + "background.png\"/></td>\n"
				+"	 									  <td class=\"stack-column-center \" valign=\"top\" >\n"
				+"	 										  <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n"
				+"	 											<tr>\n"
				+"	 											  <td dir=\"ltr\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 20px; color: #006880; padding: 10px; text-align: left;\">\n"
				+"	 												  <strong style=\"color:#111111;\">Atenção</strong>\n"
				+"	 													<p>Caso não tenha solicitado a alteração da senha, desconsidere esse e-mail.<br/>       Atenciosamente, Equipe Clique Médicos</p>\n"
				+"	 													<br>\n"
				+"	 													<br>\n"
				+"	 												</td>\n"
				+"	 											</tr>\n"
				+"	 										  </table>\n"
				+"	 										</td>\n"
				+"	 									</tr>\n"
				+"	 							  </table>\n"
				+"	 							</td>\n"
				+"	 						</tr>\n"
				+"	 					</table>\n"
				+"	 					<table align=\"center\" width=\"600\" class=\"email-container\">\n"
				+"	 						<tr>\n"
				+"	 							<td style=\"padding: 40px 10px;width: 100%;font-size: 12px; font-family: sans-serif; mso-height-rule: exactly; line-height:18px; text-align: center; color: #888888;\">\n"
				+"	 								<webversion style=\"color:#cccccc; text-decoration:underline; font-weight: bold;\">View as a Web Page\n"
				+"	 								</webversion><br><br>\n"
				+"	 								Clique Médicos<br>\n"
				+"	 								<span class=\"mobile-link--footer\">AMHP - Associação dos Médicos de Hospitais Privados do DF</span><br><br>\n"
				+"	 								<unsubscribe style=\"color:#888888; text-decoration:underline;\"></unsubscribe>\n"
				+"	 							</td>\n"
				+"	 						</tr>\n"
				+"	 					</table>\n"
				+"	 				</center>\n"
				+"	 			</td>\n"
				+"	 		</tr>\n"
				+"	 	</table>\n"
				+"	</body>	\n"
				+"	</html>	\n";
	}

	private static String emailAlterarSenha(CamposEmailDTO campos, String token) {

		final String LINK = AmbienteUtil.ENDERECO_EMAIL_ALTERACAO_SENHA + token;

		return 	 "	<!doctype html>	\n"
				+"	<html>	\n"
				+"	<head>	\n"
				+"		<meta charset=\"utf-8\"> 	\n"
				+"		<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"> 	\n"
				+"		<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> 	\n"
				+"		<title>Clique Médicos</title> 	\n"
				+"		<style type=\"text/css\"> 	\n"
				+"		html,body{font-family:sans-serif;font-size:15px;mso-height-rule:exactly;line-height:20px;color:#555;margin:0!important;padding:0!important;height:100%!important;width:100%!important}*{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}.ExternalClass{width:100%}div[style*=\"margin: 16px 0\"]{margin:0!important}table,td{mso-table-lspace:0pt!important;mso-table-rspace:0pt!important}table{border-spacing:0!important;border-collapse:collapse!important;table-layout:fixed!important;margin:0 auto!important}table table table{table-layout:auto}img{-ms-interpolation-mode:bicubic}.yshortcuts a{border-bottom:none!important}a[x-apple-data-detectors]{color:inherit!important}.button-td,.button-a{transition:all 100ms ease-in}.button-td:hover,.button-a:hover{background:#555555!important;border-color:#555555!important}@media screen and (max-width:600px){.email-container{width:100%!important}.fluid,.fluid-centered{max-width:100%!important;height:auto!important;margin-left:auto!important;margin-right:auto!important}.fluid-centered{margin-left:auto!important;margin-right:auto!important}.stack-column,.stack-column-center{display:block!important;width:100%!important;max-width:100%!important;direction:ltr!important}.stack-column-center{text-align:center!important}.center-on-narrow{text-align:center!important;display:block!important;margin-left:auto!important;margin-right:auto!important;float:none!important}table.center-on-narrow{display:inline-block!important}} 	\n"
				+"		</style> 	\n"
				+"	</head>	\n"
				+"	<body bgcolor=\"#E8F0F2\" width=\"100%\" style=\"margin: 0;\">	\n"
				+"		<table bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\" 	\n"
				+"			style=\"border-collapse:collapse;\"> 	\n"
				+"			<tr> 	\n"
				+"				<td> 	\n"
				+"					<br/> 	\n"
				+"					<br/> 	\n"
				+"					<center style=\"width: 100%;\"> 	\n"
				+"						<div 	\n"
				+"							style=\"display:none;font-size:1px;line-height:1px;max-height:0px;max-width:0px;opacity:0;overflow:hidden;mso-hide:all;font-family: sans-serif;\"> 	\n"
				+"						</div> 	\n"
				+"						<table cellspacing=\"5\" cellpadding=\"10\" border=\"0\" align=\"center\" bgcolor=\"#ffffff\" width=\"600\" 	\n"
				+"							class=\"email-container\" style=\"border-radius: 5px\"> 	\n"
				+"							<tr> 	\n"
				+"								<td> 	\n"
				+"									<table align=\"right\" class=\"email-container\"> 	\n"
				+"										<tr> 	\n"
				+"											<td style=\"padding: 20px 0; text-align: center\"> 	\n"
				+"												<img src=\"http://www.hepta.com.br/Public/email-cliquemedicos/logoCliqueMedicos_email.png\" width=\"351\" alt=\"Clique Médicos\"  border=\"0\" /> 	\n"
				+"											</td> 	\n"
				+"										</tr> 	\n"
				+"									</table> 	\n"
				+"								</td> 	\n"
				+"							</tr> 	\n"
				+"							<tr > 	\n"
				+"								<td > 	\n"
				+"									<table align=\"center\" class=\"email-container\"  bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\"> 	\n"
				+"										<tr> 	\n"
				+"											<td dir=\"ltr\" valign=\"top\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 25px; color: #006880; padding: 5px; text-align: center;\" class=\"center-on-narrow\">										 	\n"
				+"												<h1>Alterar Senha</h1> <p style=\"font-family: sans-serif; font-size: 18px; mso-height-rule: exactly; line-height: 22px; padding: 10px; text-align: center;\" >Recebemos uma solicitação para redefinir sua senha. Para recuperar a sua senha no Clique Médicos, use o link de verificação abaixo</p> 	\n"
				+"												 	\n"
				+"												 <h2 style=\"text-align: center\"><a style=\"color: #006880\" href=\"" + LINK + "\">" + LINK + "</a></h2> 	\n"
				+"											</td> 	\n"
				+"										</tr> 	\n"
				+"									</table>                        	\n"
				+"								</td> 	\n"
				+"							</tr> 	\n"
				+"							<tr> 	\n"
				+"								<td dir=\"ltr\" align=\"left\" valign=\"top\" width=\"100%\" style=\"padding: 0px;\"> 	\n"
				+"									<hr> 	\n"
				+"									<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" > 	\n"
				+"										<tr> 	\n"
				+"										  <td align=\"left\" valign=\"top\" width=\"232\"><img src=\""+AmbienteUtil.CAMINHO_IMAGEM+"background.png\"/></td> 	\n"
				+"										  <td class=\"stack-column-center \" valign=\"top\" > 	\n"
				+"											  <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> 	\n"
				+"												<tr> 	\n"
				+"												  <td dir=\"ltr\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 20px; color: #006880; padding: 10px; text-align: left;\"> 	\n"
				+"													  <strong style=\"color:#111111;\">Atenção</strong> 	\n"
				+"														<p>Caso não tenha solicitado a alteração da senha, desconsidere esse e-mail.       Atenciosamente, Equipe Clique Médicos</p> 	\n"
				+"														<br> 	\n"
				+"														<br> 	\n"
				+"													</td> 	\n"
				+"												</tr> 	\n"
				+"											  </table> 	\n"
				+"											</td> 	\n"
				+"										</tr> 	\n"
				+"								  </table> 	\n"
				+"								</td> 	\n"
				+"							</tr> 	\n"
				+"						</table> 	\n"
				+"						<table align=\"center\" width=\"600\" class=\"email-container\"> 	\n"
				+"							<tr> 	\n"
				+"								<td style=\"padding: 40px 10px;width: 100%;font-size: 12px; font-family: sans-serif; mso-height-rule: exactly; line-height:18px; text-align: center; color: #888888;\"> 	\n"
				+"									<webversion style=\"color:#cccccc; text-decoration:underline; font-weight: bold;\">View as a Web Page 	\n"
				+"									</webversion><br><br> 	\n"
				+"									Clique Médicos<br> 	\n"
				+"									<span class=\"mobile-link--footer\">AMHP - Associação dos Médicos de Hospitais Privados do DF</span><br><br> 	\n"
				+"									<unsubscribe style=\"color:#888888; text-decoration:underline;\"></unsubscribe> 	\n"
				+"								</td> 	\n"
				+"							</tr> 	\n"
				+"						</table> 	\n"
				+"					</center> 	\n"
				+"				</td> 	\n"
				+"			</tr> 	\n"
				+"		</table> 	\n"
				+"	</body>	\n"
				+"	</html>	\n";
	}
	//TODO alterar a variavel dependendo de como sera o email do boleto
	private static String emailBoleto(CamposEmailDTO campos) {
		return "<!doctype html>	\r\n" + 
				"<html>	\r\n" + 
				"<head>	\r\n" + 
				"<meta charset=\"utf-8\">	\r\n" + 
				"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">	\r\n" + 
				"<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">	\r\n" + 
				"<title>Clique Médicos</title>	\r\n" + 
				"<style type=\"text/css\">	\r\n" + 
				"html,body{font-family:sans-serif;font-size:15px;mso-height-rule:exactly;line-height:20px;color:#555;margin:0!important;padding:0!important;height:100%!important;width:100%!important}*{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}.ExternalClass{width:100%}div[style*=\"margin: 16px 0\"]{margin:0!important}table,td{mso-table-lspace:0pt!important;mso-table-rspace:0pt!important}table{border-spacing:0!important;border-collapse:collapse!important;table-layout:fixed!important;margin:0 auto!important}table table table{table-layout:auto}img{-ms-interpolation-mode:bicubic}.yshortcuts a{border-bottom:none!important}a[x-apple-data-detectors]{color:inherit!important}.button-td,.button-a{transition:all 100ms ease-in}.button-td:hover,.button-a:hover{background:#555555!important;border-color:#555555!important}@media screen and (max-width:600px){.email-container{width:100%!important}.fluid,.fluid-centered{max-width:100%!important;height:auto!important;margin-left:auto!important;margin-right:auto!important}.fluid-centered{margin-left:auto!important;margin-right:auto!important}.stack-column,.stack-column-center{display:block!important;width:100%!important;max-width:100%!important;direction:ltr!important}.stack-column-center{text-align:center!important}.center-on-narrow{text-align:center!important;display:block!important;margin-left:auto!important;margin-right:auto!important;float:none!important}table.center-on-narrow{display:inline-block!important}}	\r\n" + 
				"</style>	\r\n" + 
				"</head>	\r\n" + 
				"\r\n" + 
				"<body bgcolor=\"#E8F0F2\" width=\"100%\" style=\"margin: 0;\">	\r\n" + 
				"<table bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\" style=\"border-collapse:collapse;\">	\r\n" + 
				"<tr>	\r\n" + 
				"<td>	\r\n" + 
				"<br/>	\r\n" + 
				"<br/>	\r\n" + 
				"<center style=\"width: 100%;\">	\r\n" + 
				"<div style=\"display:none;font-size:1px;line-height:1px;max-height:0px;max-width:0px;opacity:0;overflow:hidden;mso-hide:all;font-family: sans-serif;\">	\r\n" + 
				"</div>	\r\n" + 
				"<table cellspacing=\"5\" cellpadding=\"10\" border=\"0\" align=\"center\" bgcolor=\"#ffffff\" width=\"600\" class=\"email-container\" style=\"border-radius: 5px\">	\r\n" + 
				"<tr>	\r\n" + 
				"<td>	\r\n" + 
				"<table align=\"right\" class=\"email-container\">	\r\n" + 
				"<tr>	\r\n" + 
				"<td style=\"padding: 20px 0; text-align: center\">	\r\n" + 
				"<img src=\"https://www.hepta.com.br/Public/email-cliquemedicos/logoCliqueMedicos_email.png\" width=\"351\" alt=\"Clique Médicos\" border=\"0\" />	\r\n" + 
				"</td>	\r\n" + 
				"</tr>	\r\n" + 
				"</table>	\r\n" + 
				"</td>	\r\n" + 
				"</tr>	\r\n" + 
				"<tr>	\r\n" + 
				"<td>	\r\n" + 
				"<table align=\"center\" class=\"email-container\" bgcolor=\"#E8F0F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" height=\"100%\" width=\"100%\">	\r\n" + 
				"<tr>	\r\n" + 
				"<td dir=\"ltr\" valign=\"top\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 18px; color: #555555; padding: 10px; text-align: center;\" class=\"center-on-narrow\">	\r\n" + 
				"<h1><strong style=\"color:#006880;\">SOLICITAÇÃO DE CONSULTA</strong> </h1>	\r\n" + 
				"<p style=\"font-family: sans-serif; font-size: 18px; mso-height-rule: exactly; line-height: 22px; padding: 10px; text-align: center;\" >Prezado(a) 	"+ campos.getNomeBeneficiario() +", seu pedido foi registrado e estamos aguardando o pagamento para confirmar sua consulta. O pedido pode ser acompanhado pelo site em \"MEUS AGENDAMENTOS\".</p>	\r\n" + 
				"<h3  style=\"text-align: center\"> NÚMERO DO BOLETO</h3>\r\n" + 
				"<h3  style=\"text-align: center\">" + campos.getParametrosAdicionais().get("numeroDoBoleto") + "</h3>\r\n" + 
				"<h3  style=\"text-align: center\"> <a href=\"" + campos.getParametrosAdicionais().get("linkDoBoleto") + "\" target=\"_blank\">Visualizar Boleto</a></h3>\r\n" + 
				"</td>	\r\n" + 
				"</tr>	\r\n" + 
				"</table>	\r\n" + 
				"</td>	\r\n" + 
				"</tr>	\r\n" + 
				"<tr>	\r\n" + 
				"<td style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 15px; color: #555555;\">	\r\n" + 
				"<hr>	\r\n" + 
				"<table cellspacing=\"5\" cellpadding=\"5\" border=\"0\" align=\"left\" bgcolor=\"#E8F0F2\">	\r\n" + 
				"<tr>	\r\n" + 
				"<td colspan=\"2\">	\r\n" + 
				"<p><h2>Dados da Consulta</h2></p>	\r\n" + 
				"</td>	\r\n" + 
				"</tr>	\r\n" + 
				"<tr>	\r\n" + 
				"<td width=\"100\" valign=\"top\" align=\"right\">	\r\n" + 
				"<img src=\"https://www.hepta.com.br/Public/email-cliquemedicos/user-icon.png\" />	\r\n" + 
				"</td>	\r\n" + 
				"<td width=\"500\" valign=\"top\" align=\"left\">	\r\n" + 
				"<p><strong>Beneficiário</strong></p>	\r\n" + 
				"<h3><strong>" + campos.getNomeBeneficiario() + "</strong></h3>	\r\n" + 
				"</td>	\r\n" + 
				"</tr>	\r\n" + 
				"<tr>	\r\n" + 
				"<td width=\"100\" valign=\"top\" align=\"right\">	\r\n" + 
				"<img src=\"https://www.hepta.com.br/Public/email-cliquemedicos/associado-icon.png\" />	\r\n" + 
				"</td>	\r\n" + 
				"<td width=\"500\" valign=\"top\" align=\"left\">	\r\n" + 
				"<p><strong>" + campos.getEspecialidadeDescricao() + "</strong></p>	\r\n" + 
				"<h3><strong>" + campos.getPrestadorNome() + "</strong></h3>	\r\n" + 
				"</td>	\r\n" + 
				"</tr>	\r\n" + 
				"<tr>	\r\n" + 
				"<td valign=\"top\" align=\"right\">	\r\n" + 
				"<img src=\"https://www.hepta.com.br/Public/email-cliquemedicos/clinica-icon.png\" />	\r\n" + 
				"</td>	\r\n" + 
				"<td valign=\"top\">	\r\n" + 
				"<h3><strong>" + campos.getNomeCredenciado() + "</strong></h3>	\r\n" + 
				"<p>" + campos.getEnderecoAtendimento() + "</p>	\r\n" + 
				"</td>	\r\n" + 
				"</tr>	\r\n" + 
				"<tr>	\r\n" + 
				"<td valign=\"top\" align=\"right\">	\r\n" + 
				"<img src=\"https://www.hepta.com.br/Public/email-cliquemedicos/agenda-icon.png\" />	\r\n" + 
				"</td>	\r\n" + 
				"<td valign=\"top\">	\r\n" + 
				"<p><strong>" + campos.getDataEmail() + "</strong></p>	\r\n" + 
				"</td>	\r\n" + 
				"</tr>	\r\n" + 
				"<tr>	\r\n" + 
				"<td>	\r\n" + 
				"<h3>Valor </h3>	\r\n" + 
				"</td>	\r\n" + 
				"<td style=\"text-align: right\" align=\"right\">	\r\n" + 
				"<h3><strong> R$" + campos.getValor() + "</strong></h3>	\r\n" + 
				"</td>	\r\n" + 
				"</tr>	\r\n" + 
				"</table>	\r\n" + 
				"</td>	\r\n" + 
				"</tr>	\r\n" + 
				"<tr>	\r\n" + 
				"<td dir=\"ltr\" align=\"left\" valign=\"top\" width=\"100%\" style=\"padding: 0px;\">	\r\n" + 
				"<hr>	\r\n" + 
				"<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">	\r\n" + 
				"<tr>	\r\n" + 
				"<td class=\"stack-column-center\">	\r\n" + 
				"<table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">	\r\n" + 
				"<tr>	\r\n" + 
				"<td dir=\"ltr\" valign=\"top\" style=\"font-family: sans-serif; font-size: 15px; mso-height-rule: exactly; line-height: 20px; color: #555555; padding: 10px; text-align: left;\">	\r\n" + 
				"<strong style=\"color:#111111;\">Lembretes</strong>	\r\n" + 
				"<p>O número do Vale será gerado após a confirmação do pagamento.</p>	\r\n" + 
				"<p>Não esqueça de trazer o seu número do Vale! Ele é essencial para a sua consulta.</p>	\r\n" + 
				"<p>Recomenda-se chegar, com pelo menos, 15 minutos de antecedência. </p>	\r\n" + 
				"<p>Lembre-se de trazer um documento oficial com foto, em bom estado de conservação. São aceitas carteira de identidade (RG), carteira nacional de habilitação (CNH), carteira de trabalho, carteira profissional (por exemplo, CREA, OAB etc.) ou passaporte.</p>	\r\n" + 
				"<p>Menores de 18 anos devem estar acompanhados de um responsável.</p><br><br>	\r\n" + 
				"</td>	\r\n" + 
				"</tr>	\r\n" + 
				"</table>	\r\n" + 
				"</td>	\r\n" + 
				"</tr>	\r\n" + 
				"</table>	\r\n" + 
				"</td>	\r\n" + 
				"</tr>	\r\n" + 
				"</table>	\r\n" + 
				"<table align=\"center\" width=\"600\" class=\"email-container\">	\r\n" + 
				"<tr>	\r\n" + 
				"<td style=\"padding: 40px 10px;width: 100%;font-size: 12px; font-family: sans-serif; mso-height-rule: exactly; line-height:18px; text-align: center; color: #888888;\">	\r\n" + 
				"<webversion style=\"color:#cccccc; text-decoration:underline; font-weight: bold;\">View as a Web Page	\r\n" + 
				"</webversion><br><br> Clique Médicos<br>	\r\n" + 
				"<span class=\"mobile-link--footer\">AMHP - Associação dos Médicos de Hospitais Privados do DF</span>	\r\n" + 
				"<br><br><unsubscribe style=\"color:#888888; text-decoration:underline;\"></unsubscribe>	\r\n" + 
				"</td>	\r\n" + 
				"</tr>	\r\n" + 
				"</table>	\r\n" + 
				"</center>	\r\n" + 
				"</td>	\r\n" + 
				"</tr>	\r\n" + 
				"</table>	\r\n" + 
				"</body>	\r\n" + 
				"</html>";
	}

}
