package it.casadidavide.casadidavide.controller.socio;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.ConsegnaMano;
import it.casadidavide.casadidavide.model.DisponibilitaFondazione;
import it.casadidavide.casadidavide.model.Socio;
import it.casadidavide.casadidavide.service.CampagnaService;
import it.casadidavide.casadidavide.service.ConsegnaManoService;
import it.casadidavide.casadidavide.service.DispFondazioneSevice;
import it.casadidavide.casadidavide.service.SocioService;

@Controller
public class ConsegnaManoController {
	
	@Autowired
	CampagnaService campagnaService;
	
	@Autowired
	SocioService socioService;
	
	@Autowired
	ConsegnaManoService consegnaManoService;
	
	@Autowired
	DispFondazioneSevice dispFondazioneSevice;
	
	/**
	 *  Mostra scelta campagna per la consegna a mano
	 */
	@GetMapping("/soci/consegna-mano-scelta-campagna")
	public String mostraSceltaCampagna(Model model) {
		List<Campagna> campagne = campagnaService.findAll();
		model.addAttribute("campagne", campagne);
		model.addAttribute("title", "Consegne a mano");
		return "soci/consegna-mano-scelta-campagna";
	}
	
	@PostMapping("/soci/consegna-mano")
	public String visualizzaTabellaDisponibilita(@RequestParam("idCampagna") Long idCampagna, Model model) {
        // 1. Recupera la lista filtrata per campagna
        List<DisponibilitaFondazione> listaDisponibilita = dispFondazioneSevice.findByCampagnaId(idCampagna);
        
        // 2. Passa i dati alla vista
        model.addAttribute("disponibilitaList", listaDisponibilita);
        model.addAttribute("idCampagnaSelezionata", idCampagna);
        model.addAttribute("title", "Consegna a mano");
        
        // 3. Ritorna il nome del nuovo file HTML
        return "soci/consegna-mano";
    }
	
	/**
     * GET: Mostra il form "esegui-consegna" per la riga selezionata
     */
    @GetMapping("/soci/esegui-consegna/{id}")
    public String mostraFormConsegna(@PathVariable("id") Long idDisponibilita, Model model) {
        // Recuperiamo la disponibilità per mostrare i dati (prodotto, quantità max)
        DisponibilitaFondazione disp = dispFondazioneSevice.findById(idDisponibilita).get();
        
        if (disp == null) {
            return "redirect:/soci/consegna-mano-scelta-campagna"; // Gestione errore base
        }

        model.addAttribute("disponibilita", disp);
        model.addAttribute("title", "Effettua Consegna");
        return "soci/esegui-consegna";
    }
    
    /**
     * POST: Esegue la logica di business
     * 1. Crea ConsegnaMano
     * 2. Decrementa DisponibilitaFondazione
     */
    @PostMapping("/soci/salva-consegna")
    public String salvaConsegna(
            @RequestParam("idDisponibilita") Long idDisponibilita, 
            @RequestParam("quantita") int quantitaDaConsegnare,
            Principal principal, // L'utente loggato
            Model model) {
            
        // 1. Recupero la disponibilità originale
        DisponibilitaFondazione disp = dispFondazioneSevice.findById(idDisponibilita).get();
        
        // Controllo di sicurezza: quantità sufficiente?
        if (disp.getQuantita() < quantitaDaConsegnare) {
            model.addAttribute("error", "Quantità insufficiente!");
            model.addAttribute("disponibilita", disp);
            return "soci/esegui-consegna";
        }

        // 2. Recupero l'utente loggato (Socio)
        // Assumiamo che il 'name' del principal sia l'username o l'email
        Socio socio = socioService.findByUsername(principal.getName()).get();

        // 3. Creazione nuova riga tabella "consegna_mano"
        ConsegnaMano nuovaConsegna = new ConsegnaMano();
        nuovaConsegna.setCampagna(disp.getCampagna());
        nuovaConsegna.setProdotto(disp.getProdotto());
        nuovaConsegna.setQuantita(quantitaDaConsegnare);
        nuovaConsegna.setSocio(socio); 
        // Imposta data odierna se necessario: nuovaConsegna.setDataConsegna(LocalDate.now());

        // Salvo la consegna (usa il service o repository)
        consegnaManoService.save(nuovaConsegna);

        // 4. Aggiorno (decremento) la disponibilità
        disp.setQuantita(disp.getQuantita() - quantitaDaConsegnare);
        dispFondazioneSevice.save(disp); // Metodo save/update nel service

        // 5. Redirect alla lista della campagna corrente per vederne altre
        // Usiamo un redirect speciale o torniamo alla view simulando la POST precedente
        // Per semplicità, richiamiamo il metodo visualizzaTabellaDisponibilita internamente
        // oppure facciamo un forward.
        
        return visualizzaTabellaDisponibilita(disp.getCampagna().getId(), model);
    }

}
