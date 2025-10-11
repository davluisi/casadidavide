package it.casadidavide.casadidavide.repository;

import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.PrenotazioneProdotti;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrenotazioneProdottiRepository extends JpaRepository<PrenotazioneProdotti, Long> {
	List<PrenotazioneProdotti> findByCampagnaOrderByDataPrenotazioneDescProdotto_DescrizioneProdottoAscSocio_NominativoAsc(Campagna campagna);

	List<PrenotazioneProdotti> findByElencoPasTrueAndTracciamentoSpedizioneIsNullOrderByDataPrenotazioneDesc();

	List<PrenotazioneProdotti> findByElencoPasFalseOrderByDataPrenotazioneDesc();

}
