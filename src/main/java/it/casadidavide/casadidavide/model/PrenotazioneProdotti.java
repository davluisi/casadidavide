package it.casadidavide.casadidavide.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
public class PrenotazioneProdotti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prenotazione")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_prodotto")
    private Prodotto prodotto;

    @ManyToOne
    @JoinColumn(name = "id_campagna")
    private Campagna campagna;

    @ManyToOne
    @JoinColumn(name = "id_socio")
    private Socio socio;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @Column
    private int quantita;
    @Column(name="data_prenotazione")
    private LocalDate dataPrenotazione;
    
    @Column
    private BigDecimal ricavo;
    
    @Column(name="elenco_pas")
    private boolean elencoPas;
    
    @Column(name="tracciamento_spedizione")
    private String tracciamentoSpedizione;
    
    @Column(name="data_salvataggio_tracciamento")
    private LocalDate dataSalvataggioTracciamento;
    
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
	public boolean isElencoPas() {
		return elencoPas;
	}
	public void setElencoPas(boolean elencoPas) {
		this.elencoPas = elencoPas;
	}
	public String getTracciamentoSpedizione() {
		return tracciamentoSpedizione;
	}
	public void setTracciamentoSpedizione(String tracciamentoSpedizione) {
		this.tracciamentoSpedizione = tracciamentoSpedizione;
	}
	public LocalDate getDataSalvataggioTracciamento() {
		return dataSalvataggioTracciamento;
	}
	public void setDataSalvataggioTracciamento(LocalDate dataSalvataggioTracciamento) {
		this.dataSalvataggioTracciamento = dataSalvataggioTracciamento;
	}
	public BigDecimal getRicavo() {
		return ricavo;
	}
	public void setRicavo(BigDecimal ricavo) {
		this.ricavo = ricavo;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Prodotto getProdotto() {
		return prodotto;
	}
	public void setProdotto(Prodotto prodotto) {
		this.prodotto = prodotto;
	}
	public Campagna getCampagna() {
		return campagna;
	}
	public void setCampagna(Campagna campagna) {
		this.campagna = campagna;
	}
	public Socio getSocio() {
		return socio;
	}
	public void setSocio(Socio socio) {
		this.socio = socio;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public int getQuantita() {
		return quantita;
	}
	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}
	public LocalDate getDataPrenotazione() {
		return dataPrenotazione;
	}
	public void setDataPrenotazione(LocalDate dataPrenotazione) {
		this.dataPrenotazione = dataPrenotazione;
	}

}

