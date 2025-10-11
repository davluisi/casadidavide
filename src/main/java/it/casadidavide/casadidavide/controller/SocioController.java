package it.casadidavide.casadidavide.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.Cliente;
import it.casadidavide.casadidavide.model.DisponibilitaProdottiCampagna;
import it.casadidavide.casadidavide.model.PrenotazioneProdotti;
import it.casadidavide.casadidavide.model.Prodotto;
import it.casadidavide.casadidavide.model.Socio;
import it.casadidavide.casadidavide.service.CampagnaService;
import it.casadidavide.casadidavide.service.ClienteService;
import it.casadidavide.casadidavide.service.DisponibilitaProdottiCampagnaService;
import it.casadidavide.casadidavide.service.PrenotazioneProdottiService;
import it.casadidavide.casadidavide.service.ProdottoService;
import it.casadidavide.casadidavide.service.SocioService;

@Controller
public class SocioController {
	
	@Autowired
    private CampagnaService campagnaService;
	@Autowired
    private SocioService socioService;
	@Autowired
    private ProdottoService prodottoService;
	@Autowired
    private ClienteService clienteService;
	@Autowired
    private PrenotazioneProdottiService prenotazioneProdottiService;
	@Autowired
    private DisponibilitaProdottiCampagnaService disponibilitaProdottiCampagnaService;
    

	// menu Prenotazione prodotti
    @GetMapping("/soci/prenotazione-prodotti")
    public String mostraMenuPrenotazioni(Model model) {
        List<Campagna> campagne = campagnaService.findAll();
        model.addAttribute("campagne", campagne);
        model.addAttribute("title", "Prenotazione prodotti");
        return "soci/prenotazione-prodotti";
    }
    
    // tabella disponibilità per una specifica campagna
    @GetMapping("/soci/prenotazione-prodotti/disponibilita-prenotazione-campagna/{idCampagna}")
    public String mostraDisponibilitaPerCampagna(@PathVariable Long idCampagna, Model model) {
        Campagna campagna = campagnaService.findById(idCampagna)
            .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata con id: " + idCampagna));
        
        List<DisponibilitaProdottiCampagna> caricamenti = disponibilitaProdottiCampagnaService.findByCampagna(campagna);

        model.addAttribute("title", "Disponibilità per " + campagna.getDescrizioneCampagna());
        model.addAttribute("campagna", campagna);
        model.addAttribute("caricamenti", caricamenti);

        return "soci/disponibilita-prenotazione-campagna";
    }
    
    // mostra form di prenotazione prodotto
    @GetMapping("/soci/prenotazione-prodotti/disponibilita-prenotazione-campagna/{idCampagna}/prenota-prodotto/{idProdotto}")
    public String mostraFormPrenotazioneProdotto(
            @PathVariable Long idCampagna,
            @PathVariable Long idProdotto,
            Model model) {

        Campagna campagna = campagnaService.findById(idCampagna)
                .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata"));
        Prodotto prodotto = prodottoService.findById(idProdotto)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));

        DisponibilitaProdottiCampagna caricamento = disponibilitaProdottiCampagnaService
                .findByCampagnaAndProdotto(campagna, prodotto)
                .orElseThrow(() -> new IllegalArgumentException("Disponibilità non trovata"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String usernameLoggato = auth.getName();

        List<Cliente> clienti;

        if ("Magazzino".equalsIgnoreCase(usernameLoggato)) {
            // Cerca solo il cliente con nominativo "AAA Vendita PAS"
            clienti = clienteService.findAll().stream()
                    .filter(c -> "AAA Vendita PAS".equalsIgnoreCase(c.getNominativo()))
                    .collect(Collectors.toList());
        } else {
            // Tutti i clienti, ordinati per nominativo
            clienti = clienteService.findAll().stream()
                    .sorted(Comparator.comparing(Cliente::getNominativo))
                    .collect(Collectors.toList());
        }

        model.addAttribute("clienti", clienti);


        model.addAttribute("campagna", campagna);
        model.addAttribute("prodotto", prodotto);
        model.addAttribute("caricamento", caricamento);
        model.addAttribute("clienti", clienti);
        model.addAttribute("title", "Prenota prodotto: " + prodotto.getDescrizioneProdotto());

        return "soci/prenota-prodotto";
    }
    
    // salva la prenotazione
    @PostMapping("/soci/prenotazione-prodotti/disponibilita-prenotazione-campagna/{idCampagna}/prenota-prodotto/{idProdotto}")
    public String salvaPrenotazione(
            @PathVariable Long idCampagna,
            @PathVariable Long idProdotto,
            @RequestParam int quantita,
            @RequestParam Long idCliente,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails currentUser) {

        Campagna campagna = campagnaService.findById(idCampagna)
                .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata"));

        Prodotto prodotto = prodottoService.findById(idProdotto)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));

        Cliente cliente = clienteService.findById(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("Cliente non trovato"));

        Socio socio = socioService.findByNomeUtente(currentUser.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Socio non trovato"));

        // Recupera la disponibilità
        DisponibilitaProdottiCampagna disponibilita = disponibilitaProdottiCampagnaService
                .findByCampagnaAndProdotto(campagna, prodotto)
                .orElseThrow(() -> new IllegalArgumentException("Disponibilità non trovata"));

        // Controlla che la quantità richiesta sia disponibile
        if (disponibilita.getQuantita() < quantita) {
            redirectAttributes.addFlashAttribute("error", "Quantità richiesta non disponibile.");
            return "redirect:/soci/prenotazione-prodotti/disponibilita-prenotazione-campagna/" + idCampagna;
        }

        // Aggiorna la disponibilità
        disponibilita.setQuantita(disponibilita.getQuantita() - quantita);
        disponibilitaProdottiCampagnaService.save(disponibilita);

        // Crea e salva la prenotazione
        PrenotazioneProdotti prenotazione = new PrenotazioneProdotti();
        prenotazione.setCampagna(campagna);
        prenotazione.setProdotto(prodotto);
        prenotazione.setCliente(cliente);
        prenotazione.setSocio(socio);
        prenotazione.setQuantita(quantita);
        prenotazione.setRicavo(prodotto.getPrezzoVendita().multiply(BigDecimal.valueOf(quantita)));
        prenotazione.setDataPrenotazione(LocalDate.now());

        prenotazioneProdottiService.save(prenotazione);

        redirectAttributes.addFlashAttribute("success", "Prenotazione salvata con successo.");
        return "redirect:/soci/prenotazione-prodotti/disponibilita-prenotazione-campagna/" + idCampagna;
    }

    // Vedi prenotazioni effettuate per la campagna
    @GetMapping("/soci/prenotazione-prodotti/vedi-prenotazioni/{idCampagna}")
    public String vediPrenotazioniPerCampagna(@PathVariable Long idCampagna, Model model) {
        Campagna campagna = campagnaService.findById(idCampagna)
                .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata"));

        List<PrenotazioneProdotti> prenotazioni = prenotazioneProdottiService
                .findByCampagnaOrderByDataDesc(campagna);

        model.addAttribute("campagna", campagna);
        model.addAttribute("prenotazioni", prenotazioni);
        model.addAttribute("title", "Elenco prenotazioni effettuate");
        return "soci/vedi-prenotazioni-campagna";
    }

    @PostMapping("/admin/vedi-prenotazioni/{id}/toggle-elenco-pas")
    public String toggleElencoPas(@PathVariable Long id) {
        PrenotazioneProdotti prenotazione = prenotazioneProdottiService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata"));

        prenotazione.setElencoPas(!prenotazione.isElencoPas()); // inverte il valore booleano
        prenotazioneProdottiService.save(prenotazione); // salva la modifica

        return "redirect:/soci/prenotazione-prodotti/vedi-prenotazioni/" + prenotazione.getCampagna().getId();
    }

    // esporta il xls le prenotazioni della campagna
    @GetMapping("/soci/prenotazione-prodotti/esporta-xls/{idCampagna}")
    public void esportaPrenotazioniCampagna(@PathVariable Long idCampagna, HttpServletResponse response) throws IOException {

        Campagna campagna = campagnaService.findById(idCampagna)
                .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata"));

        List<PrenotazioneProdotti> prenotazioni = prenotazioneProdottiService
                .findByCampagnaOrderByDataDesc(campagna);

        // Imposta intestazioni HTTP per il download
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "prenotazioni_" + campagna.getDescrizioneCampagna().replaceAll("\\s+", "_") + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // Crea workbook Excel
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Prenotazioni");

            // Intestazioni
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Socio", "Prodotto", "Quantità", "Cliente", "Ricavo (€)", "Data Prenotazione", "Inviata al magazzino"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Dati
            int rowNum = 1;
            for (PrenotazioneProdotti p : prenotazioni) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getSocio().getNominativo());
                row.createCell(1).setCellValue(p.getProdotto().getDescrizioneProdotto());
                row.createCell(2).setCellValue(p.getQuantita());
                row.createCell(3).setCellValue(p.getCliente().getNominativo());
                row.createCell(4).setCellValue(p.getRicavo() != null ? p.getRicavo().doubleValue() : 0);
                row.createCell(5).setCellValue(p.getDataPrenotazione() != null ? p.getDataPrenotazione().toString() : "");
                row.createCell(6).setCellValue(p.isElencoPas() ? "✓" : "");
            }

            // Adatta larghezza colonne automaticamente
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Scrivi nel flusso di output HTTP
            workbook.write(response.getOutputStream());
        }
    }

    

}
