package it.casadidavide.casadidavide.service;

import it.casadidavide.casadidavide.model.Prodotto;
import it.casadidavide.casadidavide.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdottoService {

    @Autowired
    private ProdottoRepository prodottoRepository;
    
    public List<Prodotto> findAllByOrderByDescrizioneProdotto() {
        return prodottoRepository.findAllByOrderByDescrizioneProdottoAsc();
    }

    public List<Prodotto> cercaPerDescrizioneProdotto(String query) {
        return prodottoRepository.findByDescrizioneProdottoContainingIgnoreCaseOrderByDescrizioneProdottoAsc(query);
    }

    public List<Prodotto> findAll() {
        return prodottoRepository.findAll();
    }

    public Optional<Prodotto> findById(Long id) {
        return prodottoRepository.findById(id);
    }

    public Prodotto save(Prodotto prodotto) {
        return prodottoRepository.save(prodotto);
    }

    public void deleteById(Long id) {
        prodottoRepository.deleteById(id);
    }
}
