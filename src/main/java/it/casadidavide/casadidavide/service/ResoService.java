package it.casadidavide.casadidavide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.casadidavide.casadidavide.model.Reso;
import it.casadidavide.casadidavide.repository.ResoRepository;

@Service
public class ResoService {

	@Autowired
    private ResoRepository repository;

	public Reso save(Reso reso) {
        return repository.save(reso);
    }
}
