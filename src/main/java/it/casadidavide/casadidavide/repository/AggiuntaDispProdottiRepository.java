package it.casadidavide.casadidavide.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.casadidavide.casadidavide.model.AggiuntaDispProdotti;
import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.Prodotto;
import it.casadidavide.casadidavide.model.Socio;

public interface AggiuntaDispProdottiRepository extends JpaRepository<AggiuntaDispProdotti, Long> {

    List<AggiuntaDispProdotti> findByCampagna(Campagna campagna);

    List<AggiuntaDispProdotti> findByProdotto(Prodotto prodotto);

    List<AggiuntaDispProdotti> findByCampagnaAndProdottoOrderByDataDesc(Campagna campagna, Prodotto prodotto);
    
    List<AggiuntaDispProdotti> findBySocio(Socio socio);
}
