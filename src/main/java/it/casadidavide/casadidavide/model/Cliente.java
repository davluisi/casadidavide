package it.casadidavide.casadidavide.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;
    @Column
    private String nominativo;
    @Column(name="indirizzo_spedizione")
    private String indirizzoSpedizione;
    @Column(name="comune_spedizione")
    private String comuneSpedizione;
    @Column(name="provincia_spedizione")
    private String provinciaSpedizione;
    @Column(name="nazione_spedizione")
    private String nazioneSpedizione;
    @Column(name="telefono_cellulare")
    private String telefonoCellulare;
    @Column(name="telefono_fisso")
    private String telefonoFisso;
    @Column
    private String email;
    @Column
    private String fax;
    @Column
    private String pec;
    @Column
    private String note;
    
    
    // Getters e Setters...
    
    
    
	public Long getId() {
		return id;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNominativo() {
		return nominativo;
	}
	public void setNominativo(String nominativo) {
		this.nominativo = nominativo;
	}
	public String getIndirizzoSpedizione() {
		return indirizzoSpedizione;
	}
	public void setIndirizzoSpedizione(String indirizzoSpedizione) {
		this.indirizzoSpedizione = indirizzoSpedizione;
	}
	public String getComuneSpedizione() {
		return comuneSpedizione;
	}
	public void setComuneSpedizione(String comuneSpedizione) {
		this.comuneSpedizione = comuneSpedizione;
	}
	public String getProvinciaSpedizione() {
		return provinciaSpedizione;
	}
	public void setProvinciaSpedizione(String provinciaSpedizione) {
		this.provinciaSpedizione = provinciaSpedizione;
	}
	public String getNazioneSpedizione() {
		return nazioneSpedizione;
	}
	public void setNazioneSpedizione(String nazioneSpedizione) {
		this.nazioneSpedizione = nazioneSpedizione;
	}
	public String getTelefonoCellulare() {
		return telefonoCellulare;
	}
	public void setTelefonoCellulare(String telefonoCellulare) {
		this.telefonoCellulare = telefonoCellulare;
	}
	public String getTelefonoFisso() {
		return telefonoFisso;
	}
	public void setTelefonoFisso(String telefonoFisso) {
		this.telefonoFisso = telefonoFisso;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getPec() {
		return pec;
	}
	public void setPec(String pec) {
		this.pec = pec;
	}

}

