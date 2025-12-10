package it.casadidavide.casadidavide.repository;

import it.casadidavide.casadidavide.model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Long> {
    Optional<Socio> findByNomeUtente(String username);
    boolean existsByNomeUtente(String username);
    List<Socio> findByAbilitatoFalse();
    List<Socio> findByAbilitatoTrueOrderByNominativoAsc();

    List<Socio> findAllByOrderByNominativoAsc();
	List<Socio> findByAbilitatoTrueAndNominativoContainingIgnoreCaseOrderByNominativoAsc(String nominativo);
	
	Optional<Socio> findByEmail(String email);
	Optional<Socio> findByResetPasswordToken(String token);
}
