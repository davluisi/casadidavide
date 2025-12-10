package it.casadidavide.casadidavide.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ConsegnaMano {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_consegna_mano")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="id_socio")
	private Socio socio;
	
	@ManyToOne
	@JoinColumn(name="id_campagna")
	private Campagna campagna;
	
	@ManyToOne
	@JoinColumn(name="id_prodotto")
	private Prodotto prodotto;
	
	@Column
	private int quantita;

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

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	
}
