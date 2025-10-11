package it.casadidavide.casadidavide.service;

import it.casadidavide.casadidavide.model.Socio;
import it.casadidavide.casadidavide.repository.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SocioRepository socioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Socio socio = socioRepository.findByNomeUtente(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));

        if (!socio.isAbilitato()) {
            throw new DisabledException("Utente non ancora abilitato dall'amministratore");
        }

        return socio;
    }
}
