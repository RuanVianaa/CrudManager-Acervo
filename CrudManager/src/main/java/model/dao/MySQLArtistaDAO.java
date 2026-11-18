package model.dao;

import java.util.List;

import model.Artista;
import model.ModelException;
import model.User;
import java.util.ArrayList;

public class MySQLArtistaDAO implements ArtistaDAO {
	
	private static final String SELECT_JOIN =
			  " SELECT a.id AS artista_id, a.nome AS artista_nome, a.nasc_date, "
			+ "        a.pais, a.ativo, a.biografia, a.user_id, "
			+ "        u.nome AS user_nome, u.sexo, u.email "
			+ " FROM artistas a "
			+ " INNER JOIN users u ON a.user_id = u.id "
			+ " ORDER BY a.nome, a.id ";

	@Override
	public boolean save(Artista artista) throws ModelException {
		validar(artista);

		DBHandler db = new DBHandler();
		String sqlInsert = "INSERT INTO artistas VALUES (DEFAULT, ?, ?, ?, ?, ?, ?);";
		db.prepareStatement(sqlInsert);

		db.setString(1, artista.getNome());
		db.setDate(2, artista.getDataNascimento());
		db.setString(3, artista.getPais());
		db.setInt(4, artista.isAtivo() ? 1 : 0);
		db.setString(5, artista.getBiografia());
		db.setInt(6, artista.getUser().getId());

		return db.executeUpdate() > 0;
	}



	@Override
	public boolean update(Artista artista) throws ModelException {
		validar(artista);

		DBHandler db = new DBHandler();
		String sqlUpdate = " UPDATE artistas " + " SET nome = ?, nasc_date = ?, pais = ?, "
				+ "     ativo = ?, biografia = ?, user_id = ? " + " WHERE id = ?;";
		db.prepareStatement(sqlUpdate);

		db.setString(1, artista.getNome());
		db.setDate(2, artista.getDataNascimento());
		db.setString(3, artista.getPais());
		db.setInt(4, artista.isAtivo() ? 1 : 0);
		db.setString(5, artista.getBiografia());
		db.setInt(6, artista.getUser().getId());
		db.setInt(7, artista.getId());

		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Artista artista) throws ModelException {
		DBHandler db = new DBHandler();
		db.prepareStatement(" DELETE FROM artistas WHERE id = ?;");
		db.setInt(1, artista.getId());

		return db.executeUpdate() > 0;
	}
	
	private void validar(Artista artista) throws ModelException {
		if (artista.getDataNascimento() == null)
			throw new ModelException("A data de nascimento é obrigatória.");
		if (artista.getUser() == null)
			throw new ModelException("O curador do artista é obrigatório.");
	}

	@Override
	public List<Artista> listAll() throws ModelException {
		DBHandler db = new DBHandler();
		List<Artista> artistas = new ArrayList<Artista>();

		db.createStatement();
		db.executeQuery(SELECT_JOIN + ";");

		while (db.next()) {
			artistas.add(montarArtista(db));
		}

		db.close();
		return artistas;
	}

	@Override
	public List<Artista> listPage(int page, int pageSize) throws ModelException {
		DBHandler db = new DBHandler();
		List<Artista> artistas = new ArrayList<Artista>();

		db.prepareStatement(SELECT_JOIN + " LIMIT ? OFFSET ?;");
		db.setInt(1, pageSize);
		db.setInt(2, (page - 1) * pageSize);
		db.executeQuery();

		while (db.next()) {
			artistas.add(montarArtista(db));
		}

		db.close();
		return artistas;
	}

	@Override
	public int countAll() throws ModelException {
		DBHandler db = new DBHandler();

		db.createStatement();
		db.executeQuery("SELECT COUNT(*) AS total FROM artistas;");

		int total = 0;
		if (db.next()) {
			total = db.getInt("total");
		}

		db.close();
		return total;
	}

	@Override
	public Artista findById(int id) throws ModelException {
		DBHandler db = new DBHandler();

		db.prepareStatement("SELECT * FROM artistas WHERE id = ?;");
		db.setInt(1, id);
		db.executeQuery();

		Artista a = null;
		if (db.next()) {
			a = new Artista(id);
			a.setNome(db.getString("nome"));
			a.setDataNascimento(db.getDate("nasc_date"));
			a.setPais(db.getString("pais"));
			a.setAtivo(db.getInt("ativo") == 1);
			a.setBiografia(db.getString("biografia"));

			UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
			a.setUser(userDAO.findById(db.getInt("user_id")));
		}

		db.close();
		return a;
	}

	private Artista montarArtista(DBHandler db) throws ModelException {
		User user = new User(db.getInt("user_id"));
		user.setName(db.getString("user_nome"));
		user.setGender(db.getString("sexo"));
		user.setEmail(db.getString("email"));

		Artista a = new Artista(db.getInt("artista_id"));
		a.setNome(db.getString("artista_nome"));
		a.setDataNascimento(db.getDate("nasc_date"));
		a.setPais(db.getString("pais"));
		a.setAtivo(db.getInt("ativo") == 1);
		a.setBiografia(db.getString("biografia"));
		a.setUser(user);

		return a;
	}
}
