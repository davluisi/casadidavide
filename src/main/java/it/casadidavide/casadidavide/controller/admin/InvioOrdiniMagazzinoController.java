package it.casadidavide.casadidavide.controller.admin;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.PrenotazioneProdotti;
import it.casadidavide.casadidavide.service.CampagnaService;
import it.casadidavide.casadidavide.service.PrenotazioneProdottiService;

@Controller
public class InvioOrdiniMagazzinoController {
	
	@Autowired
    private CampagnaService campagnaService;
	@Autowired
    private PrenotazioneProdottiService prenotazioneProdottiService;

	/**
	 *  Mostra pagina invio prenotazioni al magazzino
	 */
    @GetMapping("/admin/invia-prenotazioni-a-magazzino")
    public String mostraPaginaInvioPrenotazioni(@RequestParam Long idCampagna, Model model) {
    	Campagna campagna = campagnaService.findById(idCampagna)
                .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata"));

        List<PrenotazioneProdotti> prenotazioni = prenotazioneProdottiService
            //.findByElencoPasFalseOrderByDataPrenotazioneDesc()
            .findAll()
            .stream().filter(p -> !(p.isElencoPas())
            					&& p.getTracciamentoSpedizione() == null
            					&& p.getCampagna().equals(campagna))
            .sorted(Comparator.comparing(PrenotazioneProdotti::getDataPrenotazione).reversed())
            .collect(Collectors.toList());
        model.addAttribute("title", "Invio prenotazioni");
        model.addAttribute("prenotazioni", prenotazioni);
        model.addAttribute("campagna", campagna);
        return "admin/invia-prenotazioni-a-magazzino";
    }
    
    /**
     *  Mostra pagina per scegliere la campagna per cui inviare ordini al magazzino
     */
    @GetMapping("/admin/scegli-campagna-invio-ordini")
    public String scegliCampagnaDaSpedire(Model model) {
        List<Campagna> campagne = campagnaService.findAll()
            .stream()
            .sorted(Comparator.comparing(Campagna::getId).reversed()) // dalla più recente
            .collect(Collectors.toList());

        model.addAttribute("campagne", campagne);
        model.addAttribute("title", "Selezione campagna");
        return "admin/scegli-campagna-invio-ordini";
    }
    
    /**
     *  Riceve la campagna selezionata e reindirizza alla pagina di invio
     */
    @PostMapping("/admin/invia-prenotazioni-a-magazzino")
    public String gestisciCampagnaScelta(@RequestParam Long idCampagna) {
        // reindirizza al GET corretto con parametro
        return "redirect:/admin/invia-prenotazioni-a-magazzino?idCampagna=" + idCampagna;
    }
    
    /**
     *  Elabora invio prenotazioni al magazzino
     */
    @PostMapping("/admin/conferma-invio-prenotazioni-a-magazzino")
    public String inviaPrenotazioniAlMagazzino(@RequestParam(value = "prenotazioniIds", required = false) List<Long> idsSelezionati) {
        if (idsSelezionati != null && !idsSelezionati.isEmpty()) {
            for (Long id : idsSelezionati) {
                prenotazioneProdottiService.setElencoPasTrue(id);
            }
        }
        return "redirect:/home";
    }
}
