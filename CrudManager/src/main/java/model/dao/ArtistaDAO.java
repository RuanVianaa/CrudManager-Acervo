package model.dao;

import java.util.List;

import model.ModelException;
import model.Artista;

public interface ArtistaDAO {
	boolean save(Artista artista) throws ModelException;

	boolean update(Artista artista) throws ModelException;

	boolean delete(Artista artista) throws ModelException;

	List<Artista> listAll() throws ModelException;

	Artista findById(int id) throws ModelException;

	List<Artista> listPage(int page, int pageSize) throws ModelException;

	int countAll() throws ModelException;
}
