package it.casadidavide.casadidavide.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.casadidavide.casadidavide.model.Cliente;
import it.casadidavide.casadidavide.service.ClienteService;

@Controller
public class GestioneClientiController {
	
	@Autowired
    private ClienteService clienteService;

	/**
	 *  Mostra menu gestione clienti
	 */
    @GetMapping("/admin/configurazioni/gestione-clienti")
    public String mostraGestioneClienti(@RequestParam(name = "query", required = false) String query, Model model) {
        List<Cliente> clienti;

        if (query != null && !query.trim().isEmpty()) {
            clienti = clienteService.cercaPerNominativo(query.trim());
        } else {
            clienti = clienteService.findAllByOrderByNominativo(); // già ordinati
        }

        model.addAttribute("title", "Gestione sostenitori");
        model.addAttribute("clienti", clienti);
        model.addAttribute("query", query); // per mantenere il testo nella barra
        return "admin/configurazioni/gestione-clienti";
    }
    
    /**
     *  Mostra il form per aggiungere un nuovo cliente
     */
    @GetMapping("/admin/configurazioni/gestione-clienti/aggiungi-cliente")
    public String mostraFormAggiuntaCliente(Model model) {
        model.addAttribute("title", "Nuovo sostenitore");
        model.addAttribute("cliente", new Cliente());
        return "admin/configurazioni/gestione-clienti/aggiungi-cliente";
    }
    
    /**
     *  Salvataggio nuovo cliente
     */
    @PostMapping("/admin/configurazioni/gestione-clienti/aggiungi-cliente")
    public String aggiungiCliente(@ModelAttribute Cliente cliente) {
        clienteService.save(cliente); // salva il cliente nel database
        return "redirect:/admin/configurazioni/gestione-clienti";
    }
    
    /**
     *  Mostra la form di modifica del cliente
     */
    @GetMapping("/admin/configurazioni/gestione-clienti/modifica/{id}")
    public String mostraFormModificaCliente(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Sostenitore non trovato con id: " + id));
        model.addAttribute("cliente", cliente);
        model.addAttribute("title", "Modifica sostenitore");
        return "admin/configurazioni/gestione-clienti/modifica-cliente";
    }
    
    /**
     *  Gestisce il salvataggio del cliente modificato
     */
    @PostMapping("/admin/configurazioni/gestione-clienti/modifica/{id}")
    public String salvaModificheCliente(@PathVariable Long id, @ModelAttribute Cliente clienteModificato) {
        Cliente cliente = clienteService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente non trovato con id: " + id));

        cliente.setNominativo(clienteModificato.getNominativo());
        cliente.setEmail(clienteModificato.getEmail());
        cliente.setIndirizzoSpedizione(clienteModificato.getIndirizzoSpedizione());
        cliente.setComuneSpedizione(clienteModificato.getComuneSpedizione());
        cliente.setProvinciaSpedizione(clienteModificato.getProvinciaSpedizione());
        cliente.setNazioneSpedizione(clienteModificato.getNazioneSpedizione());
        cliente.setTelefonoCellulare(clienteModificato.getTelefonoCellulare());
        cliente.setTelefonoFisso(clienteModificato.getTelefonoFisso());
        cliente.setFax(clienteModificato.getFax());
        cliente.setPec(clienteModificato.getPec());
        cliente.setNote(clienteModificato.getNote());

        clienteService.save(cliente);

        return "redirect:/admin/configurazioni/gestione-clienti";
    }
}
