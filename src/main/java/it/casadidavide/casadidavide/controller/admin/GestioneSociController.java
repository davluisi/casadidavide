package it.casadidavide.casadidavide.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.casadidavide.casadidavide.model.Socio;
import it.casadidavide.casadidavide.service.SocioService;

@Controller
public class GestioneSociController {
	
	@Autowired
    private SocioService socioService;

	/** 
	 * Mostra menu gestione soci 
	 */
    @GetMapping("/admin/configurazioni/gestione-soci")
    public String mostraGestioneSoci(@RequestParam(name = "query", required = false) String query, Model model) {
        List<Socio> sociAbilitati;
        
        if (query != null && !query.trim().isEmpty()) {
            sociAbilitati = socioService.cercaAbilitatiPerNominativo(query.trim());
        } else {
            sociAbilitati = socioService.findAllAbilitati(); // già ordinati
        }
        
        model.addAttribute("query", query); // per mantenere il testo nella barra
        model.addAttribute("title", "Gestione soci");
        model.addAttribute("soci", sociAbilitati);
        return "admin/configurazioni/gestione-soci";
    }
    
    /** 
     * Mostra form modifica socio
     */
    @GetMapping("/admin/configurazioni/gestione-soci/modifica/{id}")
    public String mostraFormModificaSocio(@PathVariable Long id, Model model) {
        Socio socio = socioService.getById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID socio non valido: " + id));
        model.addAttribute("socio", socio);
        model.addAttribute("title", "Modifica Socio");
        return "admin/configurazioni/gestione-soci/modifica-socio";
    }
    
    /**
     *  Salva modifiche al socio
     */
    @PostMapping("/admin/configurazioni/gestione-soci/modifica/{id}")
    public String aggiornaRuoloSocio(@PathVariable Long id, @RequestParam("ruolo") String nuovoRuolo) {
        Socio socio = socioService.getById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID socio non valido: " + id));
        socio.setRuolo(nuovoRuolo);
        socioService.save(socio);
        return "redirect:/admin/configurazioni/gestione-soci";
    }
    
    /**
     *  Vedi richieste abilitazione
     */
    @GetMapping("/admin/configurazioni/gestione-soci/richieste-abilitazione")
    @PreAuthorize("hasRole('ADMIN')")
    public String richiesteAbilitazione(Model model) {
        List<Socio> sociInAttesa = socioService.findByAbilitatoFalse();
        model.addAttribute("soci", sociInAttesa);
        model.addAttribute("title", "Richieste di Abilitazione");
        return "admin/configurazioni/gestione-soci/richieste-abilitazione";
    }
    
    /**
     *  Abilita account socio
     */
    @PostMapping("/admin/configurazioni/gestione-soci/abilita-account")
    @PreAuthorize("hasRole('ADMIN')")
    public String abilitaSocio(@RequestParam Long id) {
        socioService.abilitaSocio(id);
        return "redirect:/admin/configurazioni/gestione-soci/richieste-abilitazione";
    }
    
    /**
     *  Elimina richiesta abilitazione
     */
    @PostMapping("/admin/configurazioni/gestione-soci/elimina-richiesta")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminaSocio(@RequestParam Long id) {
        socioService.eliminaSocio(id);
        return "redirect:/admin/configurazioni/gestione-soci/richieste-abilitazione";
    }
    
}
