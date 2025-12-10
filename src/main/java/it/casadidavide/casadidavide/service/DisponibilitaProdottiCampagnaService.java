package it.casadidavide.casadidavide.service;

import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.DisponibilitaProdottiCampagna;
import it.casadidavide.casadidavide.model.Prodotto;
import it.casadidavide.casadidavide.repository.DisponibilitaProdottiCampagnaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DisponibilitaProdottiCampagnaService {

    @Autowired
    private DisponibilitaProdottiCampagnaRepository repository;

    public List<DisponibilitaProdottiCampagna> findAll() {
        return repository.findAll();
    }

    public DisponibilitaProdottiCampagna save(DisponibilitaProdottiCampagna dpc) {
        return repository.save(dpc);
    }
    
    public Optional<DisponibilitaProdottiCampagna> findById(Long id) {
        return repository.findById(id);
    }


    public void deleteById(Long id) {
        repository.deleteById(id);
    }

	public List<DisponibilitaProdottiCampagna> findByCampagna(Campagna campagna) {
        return repository.findByCampagnaAndQuantitaGreaterThanOrderByProdotto_DescrizioneProdotto(campagna, 0);
	}

	@Transactional
	public void deleteByCampagnaAndProdotto(Campagna campagna, Prodotto prodotto) {
        repository.deleteByCampagnaAndProdotto(campagna, prodotto);
	}

	public Optional<DisponibilitaProdottiCampagna> findByCampagnaAndProdotto(Campagna campagna, Prodotto prodotto) {
        return repository.findByCampagnaAndProdotto(campagna, prodotto);
	}

	@Transactional
	public void updateQuantitaAndData(Long idCampagna, Long idProdotto, Integer quantita, LocalDate dataCaricamento) {
	    repository.updateQuantitaAndData(idCampagna, idProdotto, quantita, dataCaricamento);
	}
	
	@Transactional
	public void decrementaQuantita(Long idCampagna, Long idProdotto, int quantitaDaTogliere) {
	    DisponibilitaProdottiCampagna dpc = repository
	        .findByCampagnaIdAndProdottoId(idCampagna, idProdotto)
	        .orElseThrow(() -> new IllegalArgumentException("Disponibilità non trovata"));

	    dpc.setQuantita(dpc.getQuantita() - quantitaDaTogliere);
	    dpc.setDataCaricamento(LocalDate.now());
	    repository.save(dpc);
	}


}
