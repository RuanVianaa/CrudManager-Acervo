package model;

import java.util.Date;

public class Artista {
	private int id;
	private String nome;
	private Date dataNascimento;
	private String pais;
	private boolean ativo;
	private String biografia;
	private User user;
	
	public Artista() {}
	
	public Artista(int id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public boolean isAtivo() {
	    return ativo;
	}
	public void setAtivo(boolean ativo) {
	    this.ativo = ativo;
	}
	public String getBiografia() {
		return biografia;
	}
	public void setBiografia(String biografia) {
		this.biografia = biografia;
	}
	public int getId() {
		return id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

}
