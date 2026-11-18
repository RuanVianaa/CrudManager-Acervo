package controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Artista;
import model.ModelException;
import model.User;
import model.dao.ArtistaDAO;
import model.dao.DAOFactory;

@WebServlet(urlPatterns = {"/artistas", "/artista/form", "/artista/insert",
		"/artista/update", "/artista/delete"})
public class ArtistaController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final List<String> PAISES = Arrays.asList("Brasil", "Estados Unidos",
			"Reino Unido", "Canadá", "Barbados", "Jamaica", "Nigéria", "Argentina",
			"México", "Portugal", "Coreia do Sul", "Outro");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String action = RequestUtil.getAction(req);

		switch (action) {
		case "/artista/form": {
			CommonsController.listUsers(req); // preenche o <select> de curador
			req.setAttribute("paises", PAISES);
			req.setAttribute("action", "insert");
			ControllerUtil.forward(req, resp, "/form-artista.jsp");
			break;
		}
		case "/artista/update": {
			Artista artista = null;
			Integer id = RequestUtil.parseInt(req.getParameter("artistaId"));

			if (id != null) {
				ArtistaDAO dao = DAOFactory.createDAO(ArtistaDAO.class);
				try {
					artista = dao.findById(id);
				} catch (ModelException e) {
					e.printStackTrace();
				}
			}

			if (artista == null) {
				ControllerUtil.errorMessage(req, "Artista não encontrado.");
				ControllerUtil.redirect(resp, req.getContextPath() + "/artistas");
				break;
			}

			CommonsController.listUsers(req);
			req.setAttribute("paises", PAISES);
			req.setAttribute("action", "update");
			req.setAttribute("artista", artista);
			ControllerUtil.forward(req, resp, "/form-artista.jsp");
			break;
		}
		default:
			listArtistas(req);

			ControllerUtil.transferSessionMessagesToRequest(req);

			ControllerUtil.forward(req, resp, "/artistas.jsp");
		}
	}

	private void listArtistas(HttpServletRequest req) {
		ArtistaDAO dao = DAOFactory.createDAO(ArtistaDAO.class);

		List<Artista> artistas = null;
		try {
			int totalPages = PaginationUtil.totalPages(dao.countAll());
			int page = Math.min(PaginationUtil.getPage(req), totalPages);

			artistas = dao.listPage(page, PaginationUtil.PAGE_SIZE);
			PaginationUtil.setAttributes(req, page, totalPages);
		} catch (ModelException e) {
			e.printStackTrace();
		}

		if (artistas != null)
			req.setAttribute("artistas", artistas);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String action = RequestUtil.getAction(req);

		switch (action) {
		case "/artista/insert":
			insertArtista(req);
			break;
		case "/artista/update":
			updateArtista(req);
			break;
		case "/artista/delete":
			deleteArtista(req);
			break;
		default:
			System.out.println("URL inválida " + action);
		}

		ControllerUtil.redirect(resp, req.getContextPath() + "/artistas");
	}

	private void insertArtista(HttpServletRequest req) {
		Artista artista = lerFormulario(req, new Artista());
		if (artista == null)
			return; 

		ArtistaDAO dao = DAOFactory.createDAO(ArtistaDAO.class);

		try {
			if (dao.save(artista)) {
				ControllerUtil.sucessMessage(req, "Artista '" + artista.getNome()
						+ "' salvo com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Artista '" + artista.getNome()
						+ "' não pode ser salvo.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void updateArtista(HttpServletRequest req) {
		Integer id = RequestUtil.parseInt(req.getParameter("artistaId"));
		if (id == null) {
			ControllerUtil.errorMessage(req, "Artista inválido para atualização.");
			return;
		}

		Artista artista = lerFormulario(req, new Artista(id));
		if (artista == null)
			return;

		ArtistaDAO dao = DAOFactory.createDAO(ArtistaDAO.class);

		try {
			if (dao.update(artista)) {
				ControllerUtil.sucessMessage(req, "Artista '" + artista.getNome()
						+ "' atualizado com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Artista '" + artista.getNome()
						+ "' não pode ser atualizado.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void deleteArtista(HttpServletRequest req) {
		Integer id = RequestUtil.parseInt(req.getParameter("id"));
		if (id == null) {
			ControllerUtil.errorMessage(req, "Artista inválido para exclusão.");
			return;
		}

		ArtistaDAO dao = DAOFactory.createDAO(ArtistaDAO.class);

		try {
			Artista artista = dao.findById(id);

			if (artista == null) {
				ControllerUtil.errorMessage(req, "Artista não encontrado para exclusão.");
				return;
			}

			if (dao.delete(artista)) {
				ControllerUtil.sucessMessage(req, "Artista '" + artista.getNome()
						+ "' deletado com sucesso (os álbuns dele também foram removidos).");
			} else {
				ControllerUtil.errorMessage(req, "Artista '" + artista.getNome()
						+ "' não pode ser deletado.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private Artista lerFormulario(HttpServletRequest req, Artista artista) {
		String nome = req.getParameter("nome");
		String pais = req.getParameter("pais");
		String biografia = req.getParameter("biografia");
		Integer userId = RequestUtil.parseInt(req.getParameter("user"));
		java.util.Date nascimento = RequestUtil.parseDate(req.getParameter("dataNascimento"));

		if (RequestUtil.isBlank(nome)) {
			ControllerUtil.errorMessage(req, "Informe o nome do artista.");
			return null;
		}
		if (nascimento == null) {
			ControllerUtil.errorMessage(req, "Informe a data de nascimento.");
			return null;
		}
		if (RequestUtil.isBlank(pais)) {
			ControllerUtil.errorMessage(req, "Informe o país de origem.");
			return null;
		}
		if (userId == null) {
			ControllerUtil.errorMessage(req, "Informe o curador.");
			return null;
		}

		artista.setNome(nome.trim());
		artista.setDataNascimento(nascimento);
		artista.setPais(pais);
		artista.setAtivo(req.getParameter("ativo") != null);
		artista.setBiografia(biografia == null ? "" : biografia.trim());
		artista.setUser(new User(userId));

		return artista;
	}
}