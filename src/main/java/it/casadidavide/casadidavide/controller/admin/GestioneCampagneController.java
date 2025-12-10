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

import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.service.CampagnaService;

@Controller
public class GestioneCampagneController {
	
	@Autowired
    private CampagnaService campagnaService;

	/**
	 *  Mostra menu Gestione campagne
	 */
    @GetMapping("/admin/configurazioni/gestione-campagne")
    public String mostraGestioneCampagne(@RequestParam(name = "query", required = false) String query, Model model) {
        List<Campagna> campagne;
        
        if (query != null && !query.trim().isEmpty()) {
            campagne = campagnaService.cercaPerDescrizione(query.trim());
        } else {
            campagne = campagnaService.findAll();
        }
        model.addAttribute("title", "Gestione campagne");
        model.addAttribute("campagne", campagne);
        model.addAttribute("query", query); // per mantenere il testo nella barra

        return "admin/configurazioni/gestione-campagne";
    }
    
    /**
     *  Mostra il form per aggiungere una nuova campagna
     */
    @GetMapping("/admin/configurazioni/gestione-campagne/aggiungi-campagna")
    public String mostraFormAggiuntaCampagna(Model model) {
        model.addAttribute("title", "Nuova campagna");
        model.addAttribute("campagna", new Campagna());
        return "admin/configurazioni/gestione-campagne/aggiungi-campagna";
    }
    
    /**
     *  Salvataggio nuova campagna
     */
    @PostMapping("/admin/configurazioni/gestione-campagne/aggiungi-campagna")
    public String aggiungiCampagna(@ModelAttribute Campagna campagna) {
        campagnaService.save(campagna); // salva la campagna nel database
        return "redirect:/admin/configurazioni/gestione-campagne";
    }
    
    /**
     *  Mostra la form di modifica campagna
     */
    @GetMapping("/admin/configurazioni/gestione-campagne/modifica/{id}")
    public String mostraFormModificaCampagne(@PathVariable Long id, Model model) {
        Campagna campagna= campagnaService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente non trovato con id: " + id));
        model.addAttribute("campagna", campagna);
        model.addAttribute("title", "Modifica campagna");
        return "admin/configurazioni/gestione-campagne/modifica-campagna";
    }
    
    /**
     *  Gestisce il salvataggio della campagna modificata
     */
    @PostMapping("/admin/configurazioni/gestione-campagne/modifica/{id}")
    public String salvaModificheCampagna(@PathVariable Long id, @ModelAttribute Campagna campagnaModificata) {
        Campagna campagna = campagnaService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente non trovato con id: " + id));

        campagna.setDescrizioneCampagna(campagnaModificata.getDescrizioneCampagna());
        
        campagnaService.save(campagna);

        return "redirect:/admin/configurazioni/gestione-campagne";
    }
}
