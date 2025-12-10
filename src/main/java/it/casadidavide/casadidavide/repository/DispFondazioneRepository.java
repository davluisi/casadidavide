package it.casadidavide.casadidavide.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.DisponibilitaFondazione;
import it.casadidavide.casadidavide.model.Prodotto;

public interface DispFondazioneRepository extends JpaRepository<DisponibilitaFondazione, Long> {
	
	Optional<DisponibilitaFondazione> findByCampagnaAndProdotto(Campagna campagna, Prodotto prodotto);
	
	List<DisponibilitaFondazione> findByCampagnaIdAndQuantitaGreaterThanOrderByProdottoId(Long idCampagna, int qmin);

}
