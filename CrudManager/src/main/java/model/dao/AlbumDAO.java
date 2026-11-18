package model.dao;

import java.util.List;

import model.ModelException;
import model.Album;

public interface AlbumDAO {
	boolean save(Album album) throws ModelException;

	boolean update(Album album) throws ModelException;

	boolean delete(Album album) throws ModelException;

	List<Album> listAll() throws ModelException;

	Album findById(int id) throws ModelException;
	
	List<Album> listPage(int page, int pageSize) throws ModelException;
	
	int countAll() throws ModelException;
}
