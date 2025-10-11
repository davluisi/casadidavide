package it.casadidavide.casadidavide.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.casadidavide.casadidavide.model.Prodotto;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {

	List<Prodotto> findAllByOrderByDescrizioneProdottoAsc();
	List<Prodotto> findByDescrizioneProdottoContainingIgnoreCaseOrderByDescrizioneProdottoAsc(String descrizioneProdotto);
}
