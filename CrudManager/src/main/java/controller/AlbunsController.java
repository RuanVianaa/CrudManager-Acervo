package controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Album;
import model.Artista;
import model.ModelException;
import model.dao.AlbumDAO;
import model.dao.ArtistaDAO;
import model.dao.DAOFactory;

@WebServlet(urlPatterns = {"/albuns", "/album/form", "/album/insert",
		"/album/update", "/album/delete"})
public class AlbunsController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final List<String> FORMATOS = Arrays.asList("CD", "Vinil", "Digital");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String action = RequestUtil.getAction(req);

		switch (action) {
		case "/album/form": {
			prepararFormulario(req);
			req.setAttribute("action", "insert");
			ControllerUtil.forward(req, resp, "/form-album.jsp");
			break;
		}
		case "/album/update": {
			Album album = null;
			Integer id = RequestUtil.parseInt(req.getParameter("albumId"));

			if (id != null) {
				AlbumDAO dao = DAOFactory.createDAO(AlbumDAO.class);
				try {
					album = dao.findById(id);
				} catch (ModelException e) {
					e.printStackTrace();
				}
			}

			if (album == null) {
				ControllerUtil.errorMessage(req, "Álbum não encontrado.");
				ControllerUtil.redirect(resp, req.getContextPath() + "/albuns");
				break;
			}

			prepararFormulario(req);
			req.setAttribute("action", "update");
			req.setAttribute("album", album);
			ControllerUtil.forward(req, resp, "/form-album.jsp");
			break;
		}
		default:
			listAlbuns(req);

			ControllerUtil.transferSessionMessagesToRequest(req);

			ControllerUtil.forward(req, resp, "/albuns.jsp");
		}
	}

	private void prepararFormulario(HttpServletRequest req) {
		ArtistaDAO dao = DAOFactory.createDAO(ArtistaDAO.class);

		try {
			req.setAttribute("artistas", dao.listAll());
		} catch (ModelException e) {
			e.printStackTrace();
		}

		req.setAttribute("formatos", FORMATOS);
	}

	private void listAlbuns(HttpServletRequest req) {
		AlbumDAO dao = DAOFactory.createDAO(AlbumDAO.class);

		List<Album> albuns = null;
		try {
			int totalPages = PaginationUtil.totalPages(dao.countAll());
			int page = Math.min(PaginationUtil.getPage(req), totalPages);

			albuns = dao.listPage(page, PaginationUtil.PAGE_SIZE);
			PaginationUtil.setAttributes(req, page, totalPages);
		} catch (ModelException e) {
			e.printStackTrace();
		}

		if (albuns != null)
			req.setAttribute("albuns", albuns);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String action = RequestUtil.getAction(req);

		switch (action) {
		case "/album/insert":
			insertAlbum(req);
			break;
		case "/album/update":
			updateAlbum(req);
			break;
		case "/album/delete":
			deleteAlbum(req);
			break;
		default:
			System.out.println("URL inválida " + action);
		}

		ControllerUtil.redirect(resp, req.getContextPath() + "/albuns");
	}

	private void insertAlbum(HttpServletRequest req) {
		Album album = lerFormulario(req, new Album());
		if (album == null)
			return;

		AlbumDAO dao = DAOFactory.createDAO(AlbumDAO.class);

		try {
			if (dao.save(album)) {
				ControllerUtil.sucessMessage(req, "Álbum '" + album.getTitulo()
						+ "' salvo com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Álbum '" + album.getTitulo()
						+ "' não pode ser salvo.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void updateAlbum(HttpServletRequest req) {
		Integer id = RequestUtil.parseInt(req.getParameter("albumId"));
		if (id == null) {
			ControllerUtil.errorMessage(req, "Álbum inválido para atualização.");
			return;
		}

		Album album = lerFormulario(req, new Album(id));
		if (album == null)
			return;

		AlbumDAO dao = DAOFactory.createDAO(AlbumDAO.class);

		try {
			if (dao.update(album)) {
				ControllerUtil.sucessMessage(req, "Álbum '" + album.getTitulo()
						+ "' atualizado com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Álbum '" + album.getTitulo()
						+ "' não pode ser atualizado.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void deleteAlbum(HttpServletRequest req) {
		Integer id = RequestUtil.parseInt(req.getParameter("id"));
		if (id == null) {
			ControllerUtil.errorMessage(req, "Álbum inválido para exclusão.");
			return;
		}

		AlbumDAO dao = DAOFactory.createDAO(AlbumDAO.class);

		try {
			Album album = dao.findById(id);

			if (album == null) {
				ControllerUtil.errorMessage(req, "Álbum não encontrado para exclusão.");
				return;
			}

			if (dao.delete(album)) {
				ControllerUtil.sucessMessage(req, "Álbum '" + album.getTitulo()
						+ "' deletado com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Álbum '" + album.getTitulo()
						+ "' não pode ser deletado.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private Album lerFormulario(HttpServletRequest req, Album album) {
		String titulo = req.getParameter("titulo");
		String formato = req.getParameter("formato");
		String descricao = req.getParameter("descricao");
		Integer faixas = RequestUtil.parseInt(req.getParameter("faixas"));
		Integer artistaId = RequestUtil.parseInt(req.getParameter("artista"));
		Date lancamento = RequestUtil.parseDate(req.getParameter("dataLancamento"));

		if (RequestUtil.isBlank(titulo)) {
			ControllerUtil.errorMessage(req, "Informe o título do álbum.");
			return null;
		}
		if (lancamento == null) {
			ControllerUtil.errorMessage(req, "Informe a data de lançamento.");
			return null;
		}
		if (faixas == null || faixas < 1) {
			ControllerUtil.errorMessage(req, "Informe um número de faixas maior que zero.");
			return null;
		}
		if (formato == null || !FORMATOS.contains(formato)) {
			ControllerUtil.errorMessage(req, "Escolha um formato válido (CD, Vinil ou Digital).");
			return null;
		}
		if (artistaId == null) {
			ControllerUtil.errorMessage(req, "Informe o artista.");
			return null;
		}

		album.setTitulo(titulo.trim());
		album.setDataLancamento(lancamento);
		album.setFaixas(faixas);
		album.setFormato(formato);
		album.setExplicito(req.getParameter("explicito") != null);
		album.setDescricao(descricao == null ? "" : descricao.trim());
		album.setArtista(new Artista(artistaId));

		return album;
	}
}