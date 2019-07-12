package com.hepta.cliquemedicos.service;

import java.net.URI;

import javax.persistence.NoResultException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.hepta.cliquemedicos.dto.UsuarioDTO;
import com.hepta.cliquemedicos.entity.Usuario;
import com.hepta.cliquemedicos.persistence.UsuarioDAO;

@Path("auth")
public class RESTAuthService {

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	protected void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/login")
	public Response login(final UsuarioDTO dto) {
		HttpSession session;

//		eraseCookie(request, response);
//		if (request.getSession() != null) {
//			request.getSession(false).invalidate();
//		}

		session = request.getSession();

		if ((dto.getStr_cpf() == null && dto.getStr_email() == null)
		|| (dto.getStr_cpf().isEmpty() && dto.getStr_email().isEmpty())
		|| dto.getStr_senha() == null
		|| dto.getStr_senha().isEmpty()) {
			return Response.status(Status.PRECONDITION_FAILED).entity("Dados do usuário são inválidos").build();
		}

		UsuarioDAO dao = new UsuarioDAO();
		try {
			Usuario usuarioAutenticado = dao.autenticar(dto);

			if (usuarioAutenticado != null) {
				//limpando todos os dados do usuário (exceto id e login) antes de botar na sessão, só pra ter certeza
				usuarioAutenticado.limparDados();
				session.setAttribute("user", usuarioAutenticado);

				return Response.ok().build();
			} else {
				return Response.status(Status.PRECONDITION_FAILED).entity("Dados do usuário são inválidos").build();
			}
		} catch (NoResultException e) {
			e.printStackTrace();
			return Response.status(Status.PRECONDITION_FAILED).entity("Dados do usuário são inválidos").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.PRECONDITION_FAILED).entity("Dados do usuário são inválidos").build();
		}
	}

	@GET
	@Path("/private/logout")
	public Response logout() {
		try {
			request.getSession().removeAttribute("user");
			eraseCookie(request, response);
			request.getSession().invalidate();
			URI uri = new URI(request.getContextPath());
			String url = uri.getPath();
			return Response.ok().entity(url).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	private void eraseCookie(HttpServletRequest req, HttpServletResponse resp) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies) {
				cookie.setValue("");
				cookie.setPath("/");
				cookie.setMaxAge(0);
				resp.addCookie(cookie);
			}
	}

}
