package it.casadidavide.casadidavide.repository;

import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.DisponibilitaProdottiCampagna;
import it.casadidavide.casadidavide.model.Prodotto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface DisponibilitaProdottiCampagnaRepository extends JpaRepository<DisponibilitaProdottiCampagna, Long> {

	List<DisponibilitaProdottiCampagna> findByCampagnaAndQuantitaGreaterThanOrderByProdotto_DescrizioneProdotto(Campagna campagna, int quantita);
	void deleteByCampagnaAndProdotto(Campagna campagna, Prodotto prodotto);

	Optional<DisponibilitaProdottiCampagna> findByCampagnaAndProdotto(Campagna campagna, Prodotto prodotto);

	@Modifying
	@Query("UPDATE DisponibilitaProdottiCampagna d SET d.quantita = :quantita, d.dataCaricamento = :dataCaricamento WHERE d.campagna.id = :idCampagna AND d.prodotto.id = :idProdotto")
	void updateQuantitaAndData(@Param("idCampagna") Long idCampagna,
	                           @Param("idProdotto") Long idProdotto,
	                           @Param("quantita") Integer quantita,
	                           @Param("dataCaricamento") LocalDate dataCaricamento);

	Optional<DisponibilitaProdottiCampagna> findByCampagnaIdAndProdottoId(Long idCampagna, Long idProdotto);
	
}

