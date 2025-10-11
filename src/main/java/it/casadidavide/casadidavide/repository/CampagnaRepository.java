package it.casadidavide.casadidavide.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.casadidavide.casadidavide.model.Campagna;

public interface CampagnaRepository extends JpaRepository<Campagna, Long> {
	
	List<Campagna> findAllByOrderByIdDesc();

	List<Campagna> findByDescrizioneCampagnaContainingIgnoreCaseOrderByDescrizioneCampagnaDesc(String descrizione);
}
