package it.casadidavide.casadidavide.model;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prodotto")
    private Long id;
    @Column(name="descrizione_prodotto")
    private String descrizioneProdotto;
    @Column(name="costo_prodotto")
    private BigDecimal costoProdotto;
    @Column(name="prezzo_vendita")
    private BigDecimal prezzoVendita;
    @Column
    private String produttore;
    
    
    // Getters & setters
    
    
	public Long getId() {
		return id;
	}
	public BigDecimal getPrezzoVendita() {
		return prezzoVendita;
	}
	public void setPrezzoVendita(BigDecimal prezzoVendita) {
		this.prezzoVendita = prezzoVendita;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescrizioneProdotto() {
		return descrizioneProdotto;
	}
	public void setDescrizioneProdotto(String descrizioneProdotto) {
		this.descrizioneProdotto = descrizioneProdotto;
	}
	public BigDecimal getCostoProdotto() {
		return costoProdotto;
	}
	public void setCostoProdotto(BigDecimal costoProdotto) {
		this.costoProdotto = costoProdotto;
	}
	public String getProduttore() {
		return produttore;
	}
	public void setProduttore(String produttore) {
		this.produttore = produttore;
	}
}
