package it.casadidavide.casadidavide.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.casadidavide.casadidavide.model.AggiuntaDispProdotti;
import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.Prodotto;
import it.casadidavide.casadidavide.repository.AggiuntaDispProdottiRepository;

@Service
public class AggiuntaDispProdottiService {

    @Autowired
    private AggiuntaDispProdottiRepository aggiuntaDispProdottiRepository;

    public void save(AggiuntaDispProdotti aggiunta) {
        aggiuntaDispProdottiRepository.save(aggiunta);
    }

    public List<AggiuntaDispProdotti> findAll() {
        return aggiuntaDispProdottiRepository.findAll();
    }

    public List<AggiuntaDispProdotti> findByCampagna(Campagna campagna) {
        return aggiuntaDispProdottiRepository.findByCampagna(campagna);
    }

    public List<AggiuntaDispProdotti> findByProdotto(Prodotto prodotto) {
        return aggiuntaDispProdottiRepository.findByProdotto(prodotto);
    }

    public List<AggiuntaDispProdotti> findByCampagnaAndProdotto(Campagna campagna, Prodotto prodotto) {
        return aggiuntaDispProdottiRepository.findByCampagnaAndProdottoOrderByDataDesc(campagna, prodotto);
    }

	public Optional<AggiuntaDispProdotti> findById(Long idAggiunta) {
		return aggiuntaDispProdottiRepository.findById(idAggiunta);
	}

	public void deleteById(Long idAggiunta) {
		aggiuntaDispProdottiRepository.deleteById(idAggiunta);
	}
}
