package model.dao;

import java.util.ArrayList;
import java.util.List;
import model.Album;
import model.Artista;
import model.ModelException;

public class MySQLAlbumDAO implements AlbumDAO {

	private static final String SELECT_JOIN = " SELECT al.id AS album_id, al.titulo, al.data_lancamento, "
			+ "        al.numero_faixas, al.formato, al.explicito, al.descricao, "
			+ "        al.artista_id, ar.nome AS artista_nome " + " FROM albuns al "
			+ " INNER JOIN artistas ar ON al.artista_id = ar.id " + " ORDER BY al.titulo, al.id ";

	@Override
	public boolean save(Album album) throws ModelException {
		validar(album);

		DBHandler db = new DBHandler();
		String sqlInsert = "INSERT INTO albuns VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?);";
		db.prepareStatement(sqlInsert);

		db.setString(1, album.getTitulo());
		db.setDate(2, album.getDataLancamento());
		db.setInt(3, album.getFaixas());
		db.setString(4, album.getFormato());
		db.setInt(5, album.isExplicito() ? 1 : 0);
		db.setString(6, album.getDescricao());
		db.setInt(7, album.getArtista().getId());

		return db.executeUpdate() > 0;
	}

	@Override
	public boolean update(Album album) throws ModelException {
		validar(album);

		DBHandler db = new DBHandler();
		String sqlUpdate = " UPDATE albuns " + " SET titulo = ?, data_lancamento = ?, numero_faixas = ?, "
				+ "     formato = ?, explicito = ?, descricao = ?, artista_id = ? " + " WHERE id = ?;";
		db.prepareStatement(sqlUpdate);

		db.setString(1, album.getTitulo());
		db.setDate(2, album.getDataLancamento());
		db.setInt(3, album.getFaixas());
		db.setString(4, album.getFormato());
		db.setInt(5, album.isExplicito() ? 1 : 0);
		db.setString(6, album.getDescricao());
		db.setInt(7, album.getArtista().getId());
		db.setInt(8, album.getId());

		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Album album) throws ModelException {
		DBHandler db = new DBHandler();
		db.prepareStatement(" DELETE FROM albuns WHERE id = ?;");
		db.setInt(1, album.getId());

		return db.executeUpdate() > 0;
	}

	@Override
	public List<Album> listAll() throws ModelException {
		DBHandler db = new DBHandler();
		List<Album> albuns = new ArrayList<Album>();

		db.createStatement();
		db.executeQuery(SELECT_JOIN + ";");

		while (db.next()) {
			albuns.add(montarAlbum(db));
		}

		db.close();
		return albuns;
	}

	@Override
	public List<Album> listPage(int page, int pageSize) throws ModelException {
		DBHandler db = new DBHandler();
		List<Album> albuns = new ArrayList<Album>();

		db.prepareStatement(SELECT_JOIN + " LIMIT ? OFFSET ?;");
		db.setInt(1, pageSize);
		db.setInt(2, (page - 1) * pageSize);
		db.executeQuery();

		while (db.next()) {
			albuns.add(montarAlbum(db));
		}

		db.close();
		return albuns;
	}

	@Override
	public int countAll() throws ModelException {
		DBHandler db = new DBHandler();

		db.createStatement();
		db.executeQuery("SELECT COUNT(*) AS total FROM albuns;");

		int total = 0;
		if (db.next()) {
			total = db.getInt("total");
		}

		db.close();
		return total;
	}

	@Override
	public Album findById(int id) throws ModelException {
		DBHandler db = new DBHandler();

		db.prepareStatement("SELECT * FROM albuns WHERE id = ?;");
		db.setInt(1, id);
		db.executeQuery();

		Album a = null;
		if (db.next()) {
			a = new Album(id);
			a.setTitulo(db.getString("titulo"));
			a.setDataLancamento(db.getDate("data_lancamento"));
			a.setFaixas(db.getInt("numero_faixas"));
			a.setFormato(db.getString("formato"));
			a.setExplicito(db.getInt("explicito") == 1);
			a.setDescricao(db.getString("descricao"));

			ArtistaDAO artistaDAO = DAOFactory.createDAO(ArtistaDAO.class);
			a.setArtista(artistaDAO.findById(db.getInt("artista_id")));
		}

		db.close();
		return a;
	}

	private Album montarAlbum(DBHandler db) throws ModelException {
		Artista artista = new Artista(db.getInt("artista_id"));
		artista.setNome(db.getString("artista_nome"));

		Album a = new Album(db.getInt("album_id"));
		a.setTitulo(db.getString("titulo"));
		a.setDataLancamento(db.getDate("data_lancamento"));
		a.setFaixas(db.getInt("numero_faixas"));
		a.setFormato(db.getString("formato"));
		a.setExplicito(db.getInt("explicito") == 1);
		a.setDescricao(db.getString("descricao"));
		a.setArtista(artista);

		return a;
	}

	private void validar(Album album) throws ModelException {
		if (album.getDataLancamento() == null)
			throw new ModelException("A data de lançamento é obrigatória.");
		if (album.getArtista() == null)
			throw new ModelException("O artista do álbum é obrigatório.");
		if (album.getFaixas() < 1)
			throw new ModelException("O número de faixas deve ser maior que zero.");
	}
}
