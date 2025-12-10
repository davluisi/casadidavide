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
public class Reso {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reso")
    private Long id;
	
	@ManyToOne
    @JoinColumn(name = "id_spedizione")
    private PrenotazioneProdotti spedizione;
	
	@ManyToOne
    @JoinColumn(name = "id_socio")
    private Socio socio;
	
	@Column
	private int quantita;
	
	@Column
	private LocalDate data;
	
	

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

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

	public PrenotazioneProdotti getSpedizione() {
		return spedizione;
	}

	public void setSpedizione(PrenotazioneProdotti spedizione) {
		this.spedizione = spedizione;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}
	
}
