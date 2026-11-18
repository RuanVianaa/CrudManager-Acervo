package model;

import java.util.Date;

public class Album {
	private int id;
	private String titulo;
	private Date dataLancamento;
	private int faixas;
	private String formato;
	private boolean explicito;
	private String descricao;
	private Artista artista;
	
	public Album() {}
	
	public Album(int id) {
		this.id = id;
	}
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Date getDataLancamento() {
		return dataLancamento;
	}
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	public int getFaixas() {
		return faixas;
	}
	public void setFaixas(int faixas) {
		this.faixas = faixas;
	}
	public String getFormato() {
		return formato;
	}
	public void setFormato(String formato) {
		this.formato = formato;
	}
	public boolean isExplicito() {
	    return explicito;
	}
	public void setExplicito(boolean explicito) {
	    this.explicito = explicito;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public int getId() {
		return id;
	}
	public Artista getArtista() {
		return artista;
	}
	public void setArtista(Artista artista) {
		this.artista = artista;
	}

}
