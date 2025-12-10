package it.casadidavide.casadidavide.controller.socio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.DisponibilitaFondazione;
import it.casadidavide.casadidavide.model.PrenotazioneProdotti;
import it.casadidavide.casadidavide.model.Reso;
import it.casadidavide.casadidavide.model.Socio;
import it.casadidavide.casadidavide.model.Prodotto;

import it.casadidavide.casadidavide.service.CampagnaService;
import it.casadidavide.casadidavide.service.DispFondazioneSevice;
import it.casadidavide.casadidavide.service.PrenotazioneProdottiService;
import it.casadidavide.casadidavide.service.ResoService;
import it.casadidavide.casadidavide.service.SocioService;

@Controller
public class ResiController {
	
	@Autowired
    private CampagnaService campagnaService;
	@Autowired
	private SocioService socioService;
	@Autowired
    private PrenotazioneProdottiService prenotazioneProdottiService;
	@Autowired
	private ResoService resoService;
	@Autowired
	private DispFondazioneSevice dispFondazioneService;
	
	/**
	 * Mostra menu prenotazione prodotti
	 */
    @GetMapping("/soci/resi")
    public String mostraMenuResi(Model model) {
        List<Campagna> campagne = campagnaService.findAll();
        model.addAttribute("campagne", campagne);
        model.addAttribute("title", "Resi");
        return "soci/resi";
    }
    
    /**
	 *  Vedi spedizioni effettuate per la campagna
	 */
    @GetMapping("/soci/resi/vedi-spedizioni/{idCampagna}")
    public String vediSpedizioniPerCampagna(@PathVariable Long idCampagna, Model model) {
        Campagna campagna = campagnaService.findById(idCampagna)
                .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata"));

        List<PrenotazioneProdotti> spedizioni = prenotazioneProdottiService
                .findPrenotazioniSpedite(campagna);

        model.addAttribute("campagna", campagna);
        model.addAttribute("spedizioni", spedizioni);
        model.addAttribute("title", "Elenco spedizioni effettuate");
        return "soci/vedi-spedizioni-campagna";
    }
    
    /**
     *  Mostra form di reso
     */
    @GetMapping("/soci/resi/vedi-spedizioni/{idCampagna}/effettua-reso/{idSpedizione}")
    public String mostraFormReso(
            @PathVariable Long idCampagna,
            @PathVariable Long idSpedizione,
            Model model) {

        Campagna campagna = campagnaService.findById(idCampagna)
                .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata"));
        PrenotazioneProdotti spedizione = prenotazioneProdottiService.findById(idSpedizione)
                .orElseThrow(() -> new IllegalArgumentException("Spedizione non trovata"));

        model.addAttribute("campagna", campagna);
        model.addAttribute("spedizione", spedizione);
        model.addAttribute("title", "Effettua reso");

        return "soci/effettua-reso";
    }
    
    /**
     *  Salva il reso
     */
    @PostMapping("/soci/resi/vedi-spedizioni/{idCampagna}/effettua-reso/{idSpedizione}")
    public String salvaReso(
            @RequestParam int quantita,
            @PathVariable Long idSpedizione,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails currentUser) {

        Socio socio = socioService.findByNomeUtente(currentUser.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Socio non trovato"));

        // Recupera la quantità della prenotazione
        PrenotazioneProdotti spedizione = prenotazioneProdottiService
                .findById(idSpedizione)
                .orElseThrow(() -> new IllegalArgumentException("Disponibilità non trovata"));

        // Controlla che la quantità richiesta sia disponibile
        if (quantita > spedizione.getQuantita()) {
            redirectAttributes.addFlashAttribute("error", "Quantità richiesta non disponibile.");
            return "redirect:/soci/resi/vedi-spedizioni/" + spedizione.getCampagna().getId();
        }
        

        // Crea e salva il reso
        Reso reso= new Reso();
        reso.setQuantita(quantita);
        reso.setSocio(socio);
        reso.setData(LocalDate.now());
        reso.setSpedizione(spedizione);

        resoService.save(reso);
        
        // Aggiorna la disponibilità in fondazione
        Prodotto prodotto = spedizione.getProdotto();
        Campagna campagna = spedizione.getCampagna();
        
        Optional<DisponibilitaFondazione> dispFondazione = dispFondazioneService
        		.findByCampagnaAndProdotto(campagna, prodotto);
        if(dispFondazione.isPresent()) {
        	DisponibilitaFondazione esistente = dispFondazione.get();
        	esistente.setQuantita(esistente.getQuantita() + quantita);
            dispFondazioneService.save(esistente);
        }
        else {
        	DisponibilitaFondazione nuova = new DisponibilitaFondazione();
        	nuova.setCampagna(campagna);
        	nuova.setProdotto(prodotto);
        	nuova.setQuantita(quantita);
        	dispFondazioneService.save(nuova);
        }
        
        // Aggiorna la quantità reale della prenotazione
        spedizione.setQuantitaReale(spedizione.getQuantitaReale() - quantita);
        prenotazioneProdottiService.save(spedizione);

        redirectAttributes.addFlashAttribute("success", "Reso salvto con successo.");
        return "redirect:/soci/resi/vedi-spedizioni/" + spedizione.getCampagna().getId();
    }

}
