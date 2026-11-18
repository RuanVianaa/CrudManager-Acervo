package model.dao;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import model.ModelException;
import model.User;

public class MySQLUserDAO implements UserDAO {

	@Override
	public boolean save(User user) throws ModelException {

		DBHandler db = new DBHandler();

		String sqlInsert = "INSERT INTO users (nome, sexo, email, senha) " + "VALUES (?, ?, ?, SHA2(?, 256));";

		db.prepareStatement(sqlInsert);
		db.setString(1, user.getName());
		db.setString(2, user.getGender());
		db.setString(3, user.getEmail());
		db.setString(4, user.getPassword());

		return db.executeUpdate() > 0;
	}

	@Override
	public boolean update(User user) throws ModelException {

		DBHandler db = new DBHandler();

		String sqlUpdate = "UPDATE users " + "SET nome = ?, " + "sexo = ?, " + "email = ? " + "WHERE id = ?";

		db.prepareStatement(sqlUpdate);

		db.setString(1, user.getName());
		db.setString(2, user.getGender());
		db.setString(3, user.getEmail());
		db.setInt(4, user.getId());

		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(User user) throws ModelException {

		DBHandler db = new DBHandler();

		String sqlDelete = " DELETE FROM users " + " WHERE id = ?;";

		db.prepareStatement(sqlDelete);
		db.setInt(1, user.getId());

		try {

			return db.executeUpdate() > 0;

		} catch (ModelException e) {
			if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
				return false;
			}

			throw e;
		}
	}

	@Override
	public List<User> listAll() throws ModelException {

		DBHandler db = new DBHandler();

		List<User> users = new ArrayList<User>();

		// Declara um instrução SQL
		String sqlQuery = "SELECT * FROM users";

		db.createStatement();

		db.executeQuery(sqlQuery);

		while (db.next()) {
			User u = createUser(db);
			users.add(u);
		}

		return users;
	}

	@Override
	public User findById(int id) throws ModelException {

		DBHandler db = new DBHandler();

		String sql = "SELECT * FROM users WHERE id = ?";

		db.prepareStatement(sql);
		db.setInt(1, id);
		db.executeQuery();

		User u = null;
		while (db.next()) {
			u = createUser(db);
			break;
		}

		return u;
	}

	private User createUser(DBHandler db) throws ModelException {
		User u = new User(db.getInt("id"));
		u.setName(db.getString("nome"));
		u.setGender(db.getString("sexo"));
		u.setEmail(db.getString("email"));

		return u;
	}

	@Override
	public List<User> listPage(int page, int pageSize) throws ModelException {
		DBHandler db = new DBHandler();
		List<User> users = new ArrayList<User>();

		db.prepareStatement("SELECT * FROM users ORDER BY id LIMIT ? OFFSET ?;");
		db.setInt(1, pageSize);
		db.setInt(2, (page - 1) * pageSize);
		db.executeQuery();

		while (db.next()) {
			users.add(createUser(db));
		}

		db.close();
		return users;
	}

	@Override
	public int countAll() throws ModelException {
		DBHandler db = new DBHandler();

		db.createStatement();
		db.executeQuery("SELECT COUNT(*) AS total FROM users;");

		int total = 0;
		if (db.next()) {
			total = db.getInt("total");
		}

		db.close();
		return total;
	}
}
