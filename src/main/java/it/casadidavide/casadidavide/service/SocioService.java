package it.casadidavide.casadidavide.service;

import it.casadidavide.casadidavide.model.Socio;
import it.casadidavide.casadidavide.repository.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SocioService {

    @Autowired
    private SocioRepository socioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<Socio> findAllByOrderByNominativo() {
        return socioRepository.findAllByOrderByNominativoAsc();
    }

    public List<Socio> cercaAbilitatiPerNominativo(String query) {
        return socioRepository.findByAbilitatoTrueAndNominativoContainingIgnoreCaseOrderByNominativoAsc(query);
    }

    public Socio salvaSocio(Socio socio) {
        socio.setPassword(passwordEncoder.encode(socio.getPassword()));
        socio.setAbilitato(false); // L'admin dovrà abilitarlo manualmente
        return socioRepository.save(socio);
    }
    
    public void save(Socio socio) {
        socioRepository.save(socio);
    }


    public List<Socio> getSociNonAbilitati() {
        return socioRepository.findByAbilitatoFalse();
    }

    public Optional<Socio> getById(Long id) {
        return socioRepository.findById(id);
    }

    public void abilitaSocio(Long id) {
        socioRepository.findById(id).ifPresent(s -> {
            s.setAbilitato(true);
            socioRepository.save(s);
        });
    }

    public boolean esisteUsername(String username) {
        return socioRepository.existsByNomeUtente(username);
    }

    public List<Socio> findByAbilitatoFalse() {
        return socioRepository.findByAbilitatoFalse();
    }
    
    public void eliminaSocio(Long id) {
        socioRepository.deleteById(id);
    }
    
    public List<Socio> findAllAbilitati() {
        return socioRepository.findByAbilitatoTrueOrderByNominativoAsc();
    }

	public Optional<Socio> findByNomeUtente(String name) {
        return socioRepository.findByNomeUtente(name);
	}
	
	// ... (sotto il tuo ultimo metodo, findByNomeUtente)

    /**
     * Cerca un Socio tramite il suo indirizzo email.
     */
    public Optional<Socio> findByEmail(String email) {
        return socioRepository.findByEmail(email);
    }

    /**
     * Genera un token di reset password per un utente,
     * lo salva nel DB e restituisce il socio aggiornato.
     */
    public Socio generaTokenResetPassword(String email) {
        Optional<Socio> socioOpt = socioRepository.findByEmail(email);
        
        if (socioOpt.isPresent()) {
            Socio socio = socioOpt.get();
            
            // 1. Genera un token unico (usiamo java.util.UUID)
            String token = java.util.UUID.randomUUID().toString();
            
            // 2. Imposta il token sul socio
            socio.setResetPasswordToken(token);
            
            // 3. Salva le modifiche nel database
            //    Usiamo il metodo 'save' già esistente
            socioRepository.save(socio);
            
            return socio;
        }
        
        // 4. Se l'email non esiste, restituisce null
        return null; 
    }
    
 // ... (dopo la fine del metodo generaTokenResetPassword(...) )

    /**
     * Cerca un Socio tramite il suo token di reset password.
     */
    public Optional<Socio> findByResetPasswordToken(String token) {
        return socioRepository.findByResetPasswordToken(token);
    }

    /**
     * Aggiorna la password di un Socio, la cifra,
     * e annulla il token di reset.
     */
    public void aggiornaPassword(Socio socio, String nuovaPassword) {
        // 1. Cifra la nuova password (usando il Bean già @Autowired)
        socio.setPassword(passwordEncoder.encode(nuovaPassword));
        
        // 2. FONDAMENTALE: Annulla il token per non farlo riutilizzare
        socio.setResetPasswordToken(null);
        
        // 3. Salva il socio con la nuova password e il token nullo
        socioRepository.save(socio);
    }

	public Optional<Socio> findByUsername(String name) {
		return socioRepository.findByNomeUtente(name);
	}
}
