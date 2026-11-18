package model.dao;

import model.ModelException;
import model.User;

public class MySQLLoginDAO implements LoginDAO {

    @Override
    public User autenticar(String email, String senha) throws ModelException {

        DBHandler db = new DBHandler();

        String sql = "SELECT u.id, u.nome, u.sexo, u.email "
                   + "FROM users u "
                   + "INNER JOIN logins l ON l.user_id = u.id "
                   + "WHERE u.email = ? AND l.senha = SHA2(?, 256);";

        db.prepareStatement(sql);
        db.setString(1, email);
        db.setString(2, senha);
        db.executeQuery();

        User user = null;

        if (db.next()) {
            user = new User(db.getInt("id"));
            user.setName(db.getString("nome"));
            user.setGender(db.getString("sexo"));
            user.setEmail(db.getString("email"));
        }

        db.close();
        return user;
    }
}