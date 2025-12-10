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

import it.casadidavide.casadidavide.model.Prodotto;
import it.casadidavide.casadidavide.service.ProdottoService;

@Controller
public class GestioneProdottiController {
	
	@Autowired
    private ProdottoService prodottoService;

	/**
	 *  Mostra menu gestione prodotti
	 */
    @GetMapping("/admin/configurazioni/gestione-prodotti")
    public String mostraGestioneProdotti(@RequestParam(name = "query", required = false) String query, Model model) {
        List<Prodotto> prodotti;
        
        if (query != null && !query.trim().isEmpty()) {
            prodotti = prodottoService.cercaPerDescrizioneProdotto(query.trim());
        } else {
            prodotti = prodottoService.findAllByOrderByDescrizioneProdotto(); // già ordinati
        }
        model.addAttribute("query", query); // per mantenere il testo nella barra
        model.addAttribute("title", "Gestione prodotti");
        model.addAttribute("prodotti", prodotti);
        return "admin/configurazioni/gestione-prodotti";
    }
    
    /** 
     * Mostra il form per aggiungere un nuovo prodotto
     */
    @GetMapping("/admin/configurazioni/gestione-prodotti/aggiungi-prodotto")
    public String mostraFormAggiuntaProdotto(Model model) {
        model.addAttribute("title", "Nuovo prodotto");
        model.addAttribute("prodotto", new Prodotto());
        return "admin/configurazioni/gestione-prodotti/aggiungi-prodotto";
    }
    
    /**
     *  Salvataggio nuovo prodotto
     */
    @PostMapping("/admin/configurazioni/gestione-prodotti/aggiungi-prodotto")
    public String aggiungiProdotto(@ModelAttribute Prodotto prodotto) {
    	prodottoService.save(prodotto); // salva il prodotto nel database
        return "redirect:/admin/configurazioni/gestione-prodotti";
    }
    
    /**
     *  Mostra la form di modifica prodotto
     */
    @GetMapping("/admin/configurazioni/gestione-prodotti/modifica/{id}")
    public String mostraFormModificaProdotto(@PathVariable Long id, Model model) {
    	Prodotto prodotto = prodottoService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato con id: " + id));
        model.addAttribute("prodotto", prodotto);
        model.addAttribute("title", "Modifica prodotto");
        return "admin/configurazioni/gestione-prodotti/modifica-prodotto";
    }
    
    /**
     *  Gestisce il salvataggio del prodotto modificato
     */
    @PostMapping("/admin/configurazioni/gestione-prodotti/modifica/{id}")
    public String salvaModificheProdotto(@PathVariable Long id, @ModelAttribute Prodotto prodottoModificato) {
    	Prodotto prodotto = prodottoService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato con id: " + id));

    	prodotto.setDescrizioneProdotto(prodottoModificato.getDescrizioneProdotto());
    	prodotto.setCostoProdotto(prodottoModificato.getCostoProdotto());
    	prodotto.setPrezzoVendita(prodottoModificato.getPrezzoVendita());
    	prodotto.setProduttore(prodottoModificato.getProduttore());
        
    	prodottoService.save(prodotto);

        return "redirect:/admin/configurazioni/gestione-prodotti";
    }
}
