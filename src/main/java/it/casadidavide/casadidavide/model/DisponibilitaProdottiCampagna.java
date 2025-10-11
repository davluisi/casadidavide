package it.casadidavide.casadidavide.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
public class DisponibilitaProdottiCampagna {

    @ManyToOne
    @JoinColumn(name = "id_campagna")
    private Campagna campagna;

    @ManyToOne
    @JoinColumn(name = "id_prodotto")
    private Prodotto prodotto;

    @Column
    private int quantita;
    @Column(name="data_caricamento")
    private LocalDate dataCaricamento;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_caricamento")
    private Long id;

    
    // Getters & setters
    
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

	public LocalDate getDataCaricamento() {
		return dataCaricamento;
	}

	public void setDataCaricamento(LocalDate dataCaricamento) {
		this.dataCaricamento = dataCaricamento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	} 

    // Getters e Setters...
}

