package model.dao;

import model.ModelException;
import model.User;

public interface LoginDAO {
	User autenticar(String email, String senha) throws ModelException;
}