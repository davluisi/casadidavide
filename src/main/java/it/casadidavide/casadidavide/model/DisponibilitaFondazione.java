package it.casadidavide.casadidavide.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DisponibilitaFondazione {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_disponibilita")
    private Long id;
	
	@ManyToOne
    @JoinColumn(name = "id_campagna")
    private Campagna campagna;
	
	@ManyToOne
    @JoinColumn(name = "id_prodotto")
    private Prodotto prodotto;
	
	@Column
    private int quantita;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
