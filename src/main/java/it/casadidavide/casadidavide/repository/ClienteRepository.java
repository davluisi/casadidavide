package it.casadidavide.casadidavide.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.casadidavide.casadidavide.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	List<Cliente> findAllByOrderByNominativoAsc();
	List<Cliente> findByNominativoContainingIgnoreCaseOrderByNominativoAsc(String nominativo);

}
