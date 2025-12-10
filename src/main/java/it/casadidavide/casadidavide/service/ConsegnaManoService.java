package it.casadidavide.casadidavide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.casadidavide.casadidavide.model.ConsegnaMano;
import it.casadidavide.casadidavide.repository.ConsegnaManoRepository;

@Service
public class ConsegnaManoService {
	
	@Autowired
	ConsegnaManoRepository repo;
	
	public ConsegnaMano save(ConsegnaMano cm) {
		return repo.save(cm);
	}

}
