package it.casadidavide.casadidavide.service;

import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.repository.CampagnaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampagnaService {

    @Autowired
    private CampagnaRepository campagnaRepository;

    public List<Campagna> findAll() {
        return campagnaRepository.findAllByOrderByIdDesc();
    }
    
    public List<Campagna> cercaPerDescrizione(String query) {
        return campagnaRepository.findByDescrizioneCampagnaContainingIgnoreCaseOrderByDescrizioneCampagnaDesc(query);
    }

    public Optional<Campagna> findById(Long id) {
        return campagnaRepository.findById(id);
    }

    public Campagna save(Campagna campagna) {
        return campagnaRepository.save(campagna);
    }

    public void deleteById(Long id) {
        campagnaRepository.deleteById(id);
    }
}
