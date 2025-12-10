package it.casadidavide.casadidavide.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
public class Socio implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_socio")
    private Long id;
    @Column
    private String nominativo;
    @Column(name="nome_utente")
    private String nomeUtente;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String ruolo;
    @Column(name="data_nascita")
    private LocalDate dataNascita;
    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    

	// Residenza
    @Column(name="indirizzo_residenza")
    private String indirizzoResidenza;
    @Column(name="comune_residenza")
    private String comuneResidenza;
    @Column(name="provincia_residenza")
    private String provinciaResidenza;
    @Column(name="cap_residenza")
    private String capResidenza;

    // Domicilio
    @Column(name="indirizzo_domicilio")
    private String indirizzoDomicilio;
    @Column(name="comune_domicilio")
    private String comuneDomicilio;
    @Column(name="provincia_domicilio")
    private String provinciaDomicilio;
    @Column(name="cap_domicilio")
    private String capDomicilio;

    @Column
    private boolean abilitato;

    // Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + this.ruolo);
    }

    // Getters & Setters
    
    public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}
    
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return nomeUtente;
	}

	public Long getId() {
		return id;
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

	public String getNomeUtente() {
		return nomeUtente;
	}

	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getIndirizzoResidenza() {
		return indirizzoResidenza;
	}

	public void setIndirizzoResidenza(String indirizzoResidenza) {
		this.indirizzoResidenza = indirizzoResidenza;
	}

	public String getComuneResidenza() {
		return comuneResidenza;
	}

	public void setComuneResidenza(String comuneResidenza) {
		this.comuneResidenza = comuneResidenza;
	}

	public String getProvinciaResidenza() {
		return provinciaResidenza;
	}

	public void setProvinciaResidenza(String provinciaResidenza) {
		this.provinciaResidenza = provinciaResidenza;
	}

	public String getCapResidenza() {
		return capResidenza;
	}

	public void setCapResidenza(String capResidenza) {
		this.capResidenza = capResidenza;
	}

	public String getIndirizzoDomicilio() {
		return indirizzoDomicilio;
	}

	public void setIndirizzoDomicilio(String indirizzoDomicilio) {
		this.indirizzoDomicilio = indirizzoDomicilio;
	}

	public String getComuneDomicilio() {
		return comuneDomicilio;
	}

	public void setComuneDomicilio(String comuneDomicilio) {
		this.comuneDomicilio = comuneDomicilio;
	}

	public String getProvinciaDomicilio() {
		return provinciaDomicilio;
	}

	public void setProvinciaDomicilio(String provinciaDomicilio) {
		this.provinciaDomicilio = provinciaDomicilio;
	}

	public String getCapDomicilio() {
		return capDomicilio;
	}

	public void setCapDomicilio(String capDomicilio) {
		this.capDomicilio = capDomicilio;
	}

	public boolean isAbilitato() {
		return abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    
}
