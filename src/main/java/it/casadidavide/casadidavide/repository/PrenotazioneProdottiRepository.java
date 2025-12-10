package it.casadidavide.casadidavide.repository;

import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.PrenotazioneProdotti;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PrenotazioneProdottiRepository extends JpaRepository<PrenotazioneProdotti, Long> {
	List<PrenotazioneProdotti> findByCampagnaOrderByDataPrenotazioneDescProdotto_DescrizioneProdottoAscSocio_NominativoAsc(Campagna campagna);

	List<PrenotazioneProdotti> findByElencoPasTrueAndTracciamentoSpedizioneIsNullOrderByDataPrenotazioneDesc();

	@Query("SELECT p FROM PrenotazioneProdotti p WHERE p.campagna = :campagna AND p.elencoPas = true AND p.tracciamentoSpedizione IS NOT NULL ORDER BY p.dataPrenotazione DESC")
	List<PrenotazioneProdotti> findPrenotazioniSpedite(@Param("campagna") Campagna campagna);	
	
	List<PrenotazioneProdotti> findByElencoPasFalseOrderByDataPrenotazioneDesc();

}
