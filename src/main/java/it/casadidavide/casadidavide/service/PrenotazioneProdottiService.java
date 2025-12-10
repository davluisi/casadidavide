package it.casadidavide.casadidavide.service;

import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.PrenotazioneProdotti;
import it.casadidavide.casadidavide.repository.PrenotazioneProdottiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrenotazioneProdottiService {

    @Autowired
    private PrenotazioneProdottiRepository repository;

    public List<PrenotazioneProdotti> findAll() {
        return repository.findAll();
    }

    public Optional<PrenotazioneProdotti> findById(Long id) {
        return repository.findById(id);
    }

    public PrenotazioneProdotti save(PrenotazioneProdotti prenotazione) {
        return repository.save(prenotazione);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
    public List<PrenotazioneProdotti> findByCampagnaOrderByDataDesc(Campagna campagna) {
        return repository.findByCampagnaOrderByDataPrenotazioneDescProdotto_DescrizioneProdottoAscSocio_NominativoAsc(campagna);
    }

    public List<PrenotazioneProdotti> getPrenotazioniDaElaborare() {
        return repository
            .findByElencoPasTrueAndTracciamentoSpedizioneIsNullOrderByDataPrenotazioneDesc();
    }

	public List<PrenotazioneProdotti> findByElencoPasFalseOrderByDataPrenotazioneDesc() {
		return repository
				.findByElencoPasFalseOrderByDataPrenotazioneDesc();
	}
	
	public List<PrenotazioneProdotti> findPrenotazioniSpedite(Campagna campagna) {
		return repository
				.findPrenotazioniSpedite(campagna);
	}

	public void setElencoPasTrue(Long id) {
	    PrenotazioneProdotti p = repository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata"));
	        p.setElencoPas(true);
	        repository.save(p);
	}


}
