package it.casadidavide.casadidavide.controller;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.casadidavide.casadidavide.model.AggiuntaDispProdotti;
import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.Cliente;
import it.casadidavide.casadidavide.model.DisponibilitaProdottiCampagna;
import it.casadidavide.casadidavide.model.PrenotazioneProdotti;
import it.casadidavide.casadidavide.model.Prodotto;
import it.casadidavide.casadidavide.model.Socio;
import it.casadidavide.casadidavide.service.*;

@Controller
public class AdminController {
	
	
    @Autowired
    private SocioService socioService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private CampagnaService campagnaService;
    @Autowired
    private ProdottoService prodottoService;
    @Autowired
    private PrenotazioneProdottiService prenotazioneProdottiService;
    @Autowired
    private DisponibilitaProdottiCampagnaService disponibilitaProdottiCampagnaService;
    @Autowired
    private AggiuntaDispProdottiService aggiuntaDispProdottiService;
    
    // menu Configurazioni
    @GetMapping("/admin/configurazioni")
    public String mostraConfigurazioni(Model model) {
        model.addAttribute("title", "Configurazioni");
        return "admin/configurazioni";
    }

    // menu Gestione soci
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

    // elimina socio
    @PostMapping("/admin/configurazioni/gestione-soci/elimina/{id}")
    public String eliminaSocio(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        socioService.eliminaSocio(id);
        redirectAttributes.addFlashAttribute("success", "Socio eliminato con successo.");
        return "redirect:/admin/configurazioni/gestione-soci";
    }

    // form modifica socio
    @GetMapping("/admin/configurazioni/gestione-soci/modifica/{id}")
    public String mostraFormModificaSocio(@PathVariable Long id, Model model) {
        Socio socio = socioService.getById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID socio non valido: " + id));
        model.addAttribute("socio", socio);
        model.addAttribute("title", "Modifica Socio");
        return "admin/configurazioni/gestione-soci/modifica-socio";
    }
    
    // salva modifiche al socio
    @PostMapping("/admin/configurazioni/gestione-soci/modifica/{id}")
    public String aggiornaRuoloSocio(@PathVariable Long id, @RequestParam("ruolo") String nuovoRuolo) {
        Socio socio = socioService.getById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID socio non valido: " + id));
        socio.setRuolo(nuovoRuolo);
        socioService.save(socio);
        return "redirect:/admin/configurazioni/gestione-soci";
    }

    // vedi richieste abilitazione
    @GetMapping("/admin/configurazioni/gestione-soci/richieste-abilitazione")
    @PreAuthorize("hasRole('ADMIN')")
    public String richiesteAbilitazione(Model model) {
        List<Socio> sociInAttesa = socioService.findByAbilitatoFalse();
        model.addAttribute("soci", sociInAttesa);
        model.addAttribute("title", "Richieste di Abilitazione");
        return "admin/configurazioni/gestione-soci/richieste-abilitazione";
    }
    
    // abilita account
    @PostMapping("/admin/configurazioni/gestione-soci/abilita-account")
    @PreAuthorize("hasRole('ADMIN')")
    public String abilitaSocio(@RequestParam Long id) {
        socioService.abilitaSocio(id);
        return "redirect:/admin/configurazioni/gestione-soci/richieste-abilitazione";
    }
    
    // elimina richiesta
    @PostMapping("/admin/configurazioni/gestione-soci/elimina-richiesta")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminaSocio(@RequestParam Long id) {
        socioService.eliminaSocio(id);
        return "redirect:/admin/configurazioni/gestione-soci/richieste-abilitazione";
    }
    
    // menu Gestione clienti
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

    
    // Mostra il form per aggiungere un nuovo cliente
    @GetMapping("/admin/configurazioni/gestione-clienti/aggiungi-cliente")
    public String mostraFormAggiuntaCliente(Model model) {
        model.addAttribute("title", "Nuovo sostenitore");
        model.addAttribute("cliente", new Cliente());
        return "admin/configurazioni/gestione-clienti/aggiungi-cliente";
    }
    
    // salvataggio nuovo cliente
    @PostMapping("/admin/configurazioni/gestione-clienti/aggiungi-cliente")
    public String aggiungiCliente(@ModelAttribute Cliente cliente) {
        clienteService.save(cliente); // salva il cliente nel database
        return "redirect:/admin/configurazioni/gestione-clienti";
    }
    
    // elimina cliente
    @PostMapping("/admin/configurazioni/gestione-clienti/elimina/{id}")
    public String eliminaCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        clienteService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Sostenitore eliminato con successo.");
        return "redirect:/admin/configurazioni/gestione-clienti";
    }

    // Mostra la pagina di modifica con i dati del cliente
    @GetMapping("/admin/configurazioni/gestione-clienti/modifica/{id}")
    public String mostraFormModificaCliente(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Sostenitore non trovato con id: " + id));
        model.addAttribute("cliente", cliente);
        model.addAttribute("title", "Modifica sostenitore");
        return "admin/configurazioni/gestione-clienti/modifica-cliente";
    }

    // Gestisce il salvataggio del cliente modificato
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
    
    
    // menu Gestione campagne
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
    
    // Mostra il form per aggiungere una nuova campagna
    @GetMapping("/admin/configurazioni/gestione-campagne/aggiungi-campagna")
    public String mostraFormAggiuntaCampagna(Model model) {
        model.addAttribute("title", "Nuova campagna");
        model.addAttribute("campagna", new Campagna());
        return "admin/configurazioni/gestione-campagne/aggiungi-campagna";
    }
    
    // salvataggio nuova campagna
    @PostMapping("/admin/configurazioni/gestione-campagne/aggiungi-campagna")
    public String aggiungiCampagna(@ModelAttribute Campagna campagna) {
        campagnaService.save(campagna); // salva la campagna nel database
        return "redirect:/admin/configurazioni/gestione-campagne";
    }
    
    // elimina campagna
    @PostMapping("/admin/configurazioni/gestione-campagne/elimina/{id}")
    public String eliminaCampagna(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        campagnaService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Campagna eliminata con successo.");
        return "redirect:/admin/configurazioni/gestione-campagne";
    }
    
    // Mostra la pagina di modifica con i dati della campagna
    @GetMapping("/admin/configurazioni/gestione-campagne/modifica/{id}")
    public String mostraFormModificaCampagne(@PathVariable Long id, Model model) {
        Campagna campagna= campagnaService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente non trovato con id: " + id));
        model.addAttribute("campagna", campagna);
        model.addAttribute("title", "Modifica campagna");
        return "admin/configurazioni/gestione-campagne/modifica-campagna";
    }

    // Gestisce il salvataggio della campagna modificata
    @PostMapping("/admin/configurazioni/gestione-campagne/modifica/{id}")
    public String salvaModificheCampagna(@PathVariable Long id, @ModelAttribute Campagna campagnaModificata) {
        Campagna campagna = campagnaService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente non trovato con id: " + id));

        campagna.setDescrizioneCampagna(campagnaModificata.getDescrizioneCampagna());
        
        campagnaService.save(campagna);

        return "redirect:/admin/configurazioni/gestione-campagne";
    }
    
    // menu Gestione prodotti
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
    
    // Mostra il form per aggiungere un nuovo prodotto
    @GetMapping("/admin/configurazioni/gestione-prodotti/aggiungi-prodotto")
    public String mostraFormAggiuntaProdotto(Model model) {
        model.addAttribute("title", "Nuovo prodotto");
        model.addAttribute("prodotto", new Prodotto());
        return "admin/configurazioni/gestione-prodotti/aggiungi-prodotto";
    }
    
    // salvataggio nuovo prodotto
    @PostMapping("/admin/configurazioni/gestione-prodotti/aggiungi-prodotto")
    public String aggiungiProdotto(@ModelAttribute Prodotto prodotto) {
    	prodottoService.save(prodotto); // salva il prodotto nel database
        return "redirect:/admin/configurazioni/gestione-prodotti";
    }
    
    // elimina prodotto
    @PostMapping("/admin/configurazioni/gestione-prodotti/elimina/{id}")
    public String eliminaProdotto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    	prodottoService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Prodotto eliminato con successo.");
        return "redirect:/admin/configurazioni/gestione-prodotti";
    }

    // Mostra la pagina di modifica con i dati del prodotto
    @GetMapping("/admin/configurazioni/gestione-prodotti/modifica/{id}")
    public String mostraFormModificaProdotto(@PathVariable Long id, Model model) {
    	Prodotto prodotto = prodottoService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato con id: " + id));
        model.addAttribute("prodotto", prodotto);
        model.addAttribute("title", "Modifica prodotto");
        return "admin/configurazioni/gestione-prodotti/modifica-prodotto";
    }

    // Gestisce il salvataggio del prodotto modificato
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
    
    // menu Gestione disponibilità
    @GetMapping("/admin/configurazioni/gestione-disponibilita")
    public String mostraGestioneDisponibilita(Model model) {
        List<Campagna> campagne = campagnaService.findAll();
        model.addAttribute("title", "Gestione disponibilità");
        model.addAttribute("campagne", campagne);
        return "admin/configurazioni/gestione-disponibilita";
    }
    
    // tabella disponibilità per una specifica campagna
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



    // mostra il form per aggiungere una disponibilità
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


    // salva nuova disponibilità
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

    // mostra storico aggiunte
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
        model.addAttribute("title", "Storico caricamenti di " + prodotto.getDescrizioneProdotto() 
        		+ " per " + campagna.getDescrizioneCampagna());

        return "admin/configurazioni/gestione-disponibilita/storico-caricamenti-campagna-prodotto";
    }

    // mostra pagina invio prenotazioni al magazzino
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
        return "admin/invia-prenotazioni-a-magazzino";
    }
    
 // Riceve la campagna selezionata e reindirizza alla pagina di invio
    @PostMapping("/admin/invia-prenotazioni-a-magazzino")
    public String gestisciCampagnaScelta(@RequestParam Long idCampagna) {
        // reindirizza al GET corretto con parametro
        return "redirect:/admin/invia-prenotazioni-a-magazzino?idCampagna=" + idCampagna;
    }

    
    // elabora invio prenotazioni al magazzino
    @PostMapping("/admin/conferma-invio-prenotazioni-a-magazzino")
    public String inviaPrenotazioniAlMagazzino(@RequestParam(value = "prenotazioniIds", required = false) List<Long> idsSelezionati) {
        if (idsSelezionati != null && !idsSelezionati.isEmpty()) {
            for (Long id : idsSelezionati) {
                prenotazioneProdottiService.setElencoPasTrue(id);
            }
        }
        return "redirect:/admin/scegli-campagna-invio-ordini";
    }

    // mostra form modifica caricamento
    @GetMapping("/admin/configurazioni/gestione-disponibilita/modifica-caricamento/{idAggiunta}")
    public String mostraFormModificaCaricamento(@PathVariable Long idAggiunta, Model model) {
        AggiuntaDispProdotti aggiunta = aggiuntaDispProdottiService.findById(idAggiunta)
            .orElseThrow(() -> new IllegalArgumentException("Caricamento non trovato"));

        model.addAttribute("carico", aggiunta);
        model.addAttribute("title", "Modifica caricamento");
        return "admin/configurazioni/gestione-disponibilita/modifica-caricamento";
    }

    // salva modifica al caricamento
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

    // elimina un caricamento
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

    // Mostra pagina per scegliere la campagna per cui inviare ordini al magazzino
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
}
