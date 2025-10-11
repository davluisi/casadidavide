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
}
