package it.casadidavide.casadidavide.service;

import it.casadidavide.casadidavide.model.Cliente;
import it.casadidavide.casadidavide.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }
    
    public List<Cliente> findAllByOrderByNominativo() {
        return clienteRepository.findAllByOrderByNominativoAsc();
    }

    public List<Cliente> cercaPerNominativo(String query) {
        return clienteRepository.findByNominativoContainingIgnoreCaseOrderByNominativoAsc(query);
    }
    
    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }

}
