package it.casadidavide.casadidavide.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class AggiuntaDispProdotti {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aggiunta")
    private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_socio")
	private Socio socio;
	
	@ManyToOne
	@JoinColumn(name = "id_campagna")
	private Campagna campagna;
	
	@ManyToOne
	@JoinColumn(name = "id_prodotto")
	private Prodotto prodotto;
	
	@Column
	private LocalDate data;
	
	@Column
	private int quantita;
	
	@Column
	private char[] note;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Socio getSocio() {
		return socio;
	}

	public void setSocio(Socio socio) {
		this.socio = socio;
	}

	public Campagna getCampagna() {
		return campagna;
	}

	public void setCampagna(Campagna campagna) {
		this.campagna = campagna;
	}

	public Prodotto getProdotto() {
		return prodotto;
	}

	public void setProdotto(Prodotto prodotto) {
		this.prodotto = prodotto;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public char[] getNote() {
		return note;
	}

	public void setNote(char[] note) {
		this.note = note;
	}
	
	
}
