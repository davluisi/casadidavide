package it.casadidavide.casadidavide.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.DisponibilitaFondazione;
import it.casadidavide.casadidavide.model.Prodotto;
import it.casadidavide.casadidavide.repository.DispFondazioneRepository;

@Service
public class DispFondazioneSevice {
	
	@Autowired
	private DispFondazioneRepository repository;
	
	public Optional<DisponibilitaFondazione> findByCampagnaAndProdotto(Campagna campagna, Prodotto prodotto) {
        return repository.findByCampagnaAndProdotto(campagna, prodotto);
	}
	
	public DisponibilitaFondazione save(DisponibilitaFondazione disp) {
		return repository.save(disp);
	}
	
	public List<DisponibilitaFondazione> findByCampagnaId(Long idCampagna){
		return repository.findByCampagnaIdAndQuantitaGreaterThanOrderByProdottoId(idCampagna, 0);
	}

	public Optional<DisponibilitaFondazione> findById(Long idDisponibilita) {
		return repository.findById(idDisponibilita);
	}

}
