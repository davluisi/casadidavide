package it.casadidavide.casadidavide.controller.admin;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.casadidavide.casadidavide.model.AggiuntaDispProdotti;
import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.Prodotto;
import it.casadidavide.casadidavide.service.AggiuntaDispProdottiService;
import it.casadidavide.casadidavide.service.DisponibilitaProdottiCampagnaService;

@Controller
public class GestioneAggiunteDispController {
	
	@Autowired
    private AggiuntaDispProdottiService aggiuntaDispProdottiService;
	@Autowired
    private DisponibilitaProdottiCampagnaService disponibilitaProdottiCampagnaService;

	/**
	 *  Mostra form modifica caricamento
	 */
    @GetMapping("/admin/configurazioni/gestione-disponibilita/modifica-caricamento/{idAggiunta}")
    public String mostraFormModificaCaricamento(@PathVariable Long idAggiunta, Model model) {
        AggiuntaDispProdotti aggiunta = aggiuntaDispProdottiService.findById(idAggiunta)
            .orElseThrow(() -> new IllegalArgumentException("Caricamento non trovato"));

        model.addAttribute("carico", aggiunta);
        model.addAttribute("title", "Modifica caricamento");
        return "admin/configurazioni/gestione-disponibilita/modifica-caricamento";
    }
    
    /**
     *  Salva modifica al caricamento
     */
    @PostMapping("/admin/configurazioni/gestione-disponibilita/modifica-caricamento/{idAggiunta}")
    public String salvaModificaCaricamento(@PathVariable Long idAggiunta,
                                           @RequestParam int nuovaQuantitaCaricamento,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate nuovaData) {

        AggiuntaDispProdotti aggiunta = aggiuntaDispProdottiService.findById(idAggiunta)
            .orElseThrow(() -> new IllegalArgumentException("Caricamento non trovato"));

        Campagna campagna = aggiunta.getCampagna();
        Prodotto prodotto = aggiunta.getProdotto();
        
        int vecchiaDisp = disponibilitaProdottiCampagnaService.findByCampagnaAndProdotto(campagna, prodotto).get().getQuantita();

        int differenza = nuovaQuantitaCaricamento - aggiunta.getQuantita();

        // aggiorna la disponibilità totale nella tabella principale
        disponibilitaProdottiCampagnaService.updateQuantitaAndData(
            campagna.getId(), 
            prodotto.getId(), 
            vecchiaDisp + differenza, 
            nuovaData
        );

        // aggiorna l’aggiunta specifica
        aggiunta.setQuantita(nuovaQuantitaCaricamento);
        aggiunta.setData(nuovaData);
        aggiuntaDispProdottiService.save(aggiunta);

        return "redirect:/admin/configurazioni/gestione-disponibilita/storico/" 
                + campagna.getId() + "/" + prodotto.getId();
    }
    
    /*
     *  Elimina un caricamento e aggiorna la disponibilità
     */
    @PostMapping("/admin/configurazioni/gestione-disponibilita/elimina-caricamento/{idAggiunta}")
    public String eliminaCaricamento(@PathVariable Long idAggiunta) {
        AggiuntaDispProdotti aggiunta = aggiuntaDispProdottiService.findById(idAggiunta)
            .orElseThrow(() -> new IllegalArgumentException("Caricamento non trovato"));

        Campagna campagna = aggiunta.getCampagna();
        Prodotto prodotto = aggiunta.getProdotto();

        // diminuisci la quantità nella tabella disponibilità_prodotti_campagna
        disponibilitaProdottiCampagnaService.decrementaQuantita(
            campagna.getId(),
            prodotto.getId(),
            aggiunta.getQuantita()
        );

        // elimina la riga dell’aggiunta
        aggiuntaDispProdottiService.deleteById(idAggiunta);

        return "redirect:/admin/configurazioni/gestione-disponibilita/storico/" 
                + campagna.getId() + "/" + prodotto.getId();
    }
}
