package it.casadidavide.casadidavide.controller.admin;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.casadidavide.casadidavide.model.AggiuntaDispProdotti;
import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.DisponibilitaProdottiCampagna;
import it.casadidavide.casadidavide.model.Prodotto;
import it.casadidavide.casadidavide.model.Socio;
import it.casadidavide.casadidavide.service.AggiuntaDispProdottiService;
import it.casadidavide.casadidavide.service.CampagnaService;
import it.casadidavide.casadidavide.service.DisponibilitaProdottiCampagnaService;
import it.casadidavide.casadidavide.service.ProdottoService;
import it.casadidavide.casadidavide.service.SocioService;

@Controller
public class GestioneDisponibilitaController {
	
	@Autowired
    private CampagnaService campagnaService;
	@Autowired
    private DisponibilitaProdottiCampagnaService disponibilitaProdottiCampagnaService;
	@Autowired
    private SocioService socioService;
	@Autowired
    private ProdottoService prodottoService;
	@Autowired
    private AggiuntaDispProdottiService aggiuntaDispProdottiService;
	
	/**
	 * Mostra menu gestione disponibilità
	 */
    @GetMapping("/admin/configurazioni/gestione-disponibilita")
    public String mostraGestioneDisponibilita(Model model) {
        List<Campagna> campagne = campagnaService.findAll();
        model.addAttribute("title", "Gestione disponibilità");
        model.addAttribute("campagne", campagne);
        return "admin/configurazioni/gestione-disponibilita";
    }
    
    /**
     * Mostra tabella disponibilità per una specifica campagna
     */
    @GetMapping("/admin/configurazioni/gestione-disponibilita/disponibilita-campagna/{idCampagna}")
    public String mostraDisponibilitaPerCampagna(@PathVariable Long idCampagna, Model model) {
        Campagna campagna = campagnaService.findById(idCampagna)
            .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata con id: " + idCampagna));
        
        List<DisponibilitaProdottiCampagna> caricamenti = disponibilitaProdottiCampagnaService.findByCampagna(campagna);

        model.addAttribute("title", "Disponibilità per " + campagna.getDescrizioneCampagna());
        model.addAttribute("campagna", campagna);
        model.addAttribute("caricamenti", caricamenti);

        return "admin/configurazioni/gestione-disponibilita/disponibilita-campagna";
    }
    
    /**
     *  Mostra il form per aggiungere una disponibilità
     */
    @GetMapping("/admin/configurazioni/gestione-disponibilita/aggiungi/{idCampagna}")
    public String mostraFormAggiungiDisponibilita(@PathVariable Long idCampagna, Model model) {
        Campagna campagna = campagnaService.findById(idCampagna)
                .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata"));

        List<Prodotto> prodotti = prodottoService.findAll();
        List<Socio> soci = socioService.findAllAbilitati(); // recupera solo soci abilitati

        model.addAttribute("campagna", campagna);
        model.addAttribute("prodotti", prodotti);
        model.addAttribute("soci", soci);
        model.addAttribute("title", "Aggiungi disponibilità");

        return "admin/configurazioni/gestione-disponibilita/aggiungi-disponibilita";
    }
    
    /**
     *  Salva nuova disponibilità
     */
    @PostMapping("/admin/configurazioni/gestione-disponibilita/aggiungi/{idCampagna}")
    public String salvaNuovaDisponibilita(
            @PathVariable Long idCampagna,
            @RequestParam Long idProdotto,
            @RequestParam Long idSocio,
            @RequestParam int quantita,
            @RequestParam("dataCaricamento") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataCaricamento,
            @RequestParam(name = "note", required = false) String note,
            RedirectAttributes redirectAttributes) {

        Campagna campagna = campagnaService.findById(idCampagna)
                .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata"));

        Prodotto prodotto = prodottoService.findById(idProdotto)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));

        Socio socio = socioService.getById(idSocio)
                .orElseThrow(() -> new IllegalArgumentException("Socio non trovato"));

        // 1. aggiorna o inserisce nella tabella disponibilità
        Optional<DisponibilitaProdottiCampagna> esistenteOpt =
                disponibilitaProdottiCampagnaService.findByCampagnaAndProdotto(campagna, prodotto);

        if (esistenteOpt.isPresent()) {
            DisponibilitaProdottiCampagna esistente = esistenteOpt.get();
            int nuovaQuantita = esistente.getQuantita() + quantita;
            esistente.setQuantita(nuovaQuantita);
            esistente.setDataCaricamento(dataCaricamento);
            disponibilitaProdottiCampagnaService.save(esistente);
        } else {
            DisponibilitaProdottiCampagna nuova = new DisponibilitaProdottiCampagna();
            nuova.setCampagna(campagna);
            nuova.setProdotto(prodotto);
            nuova.setQuantita(quantita);
            nuova.setDataCaricamento(dataCaricamento);
            disponibilitaProdottiCampagnaService.save(nuova);
        }

        // 2. salva sempre nella tabella aggiunte
        AggiuntaDispProdotti aggiunta = new AggiuntaDispProdotti();
        aggiunta.setCampagna(campagna);
        aggiunta.setProdotto(prodotto);
        aggiunta.setQuantita(quantita);
        aggiunta.setData(dataCaricamento);
        aggiunta.setSocio(socio);
        aggiunta.setNote(note != null ? note.toCharArray() : null); // gestisce null

        aggiuntaDispProdottiService.save(aggiunta);

        redirectAttributes.addFlashAttribute("success", "Disponibilità aggiornata con successo.");
        return "redirect:/admin/configurazioni/gestione-disponibilita/disponibilita-campagna/" + idCampagna;
    }
    
    /**
     *  Mostra storico aggiunte
     */
    @GetMapping("/admin/configurazioni/gestione-disponibilita/storico/{idCampagna}/{idProdotto}")
    public String mostraStoricoAggiunte(@PathVariable Long idCampagna,
                                        @PathVariable Long idProdotto,
                                        Model model) {

        Campagna campagna = campagnaService.findById(idCampagna)
            .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata"));

        Prodotto prodotto = prodottoService.findById(idProdotto)
            .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));

        List<AggiuntaDispProdotti> storico = aggiuntaDispProdottiService.findByCampagnaAndProdotto(campagna, prodotto);

        model.addAttribute("campagna", campagna);
        model.addAttribute("prodotto", prodotto);
        model.addAttribute("storico", storico);
        model.addAttribute("title", "Storico caricamenti");

        return "admin/configurazioni/gestione-disponibilita/storico-caricamenti-campagna-prodotto";
    }

}
