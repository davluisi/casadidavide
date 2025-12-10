package it.casadidavide.casadidavide.controller.magazzino;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.Cliente;
import it.casadidavide.casadidavide.model.PrenotazioneProdotti;
import it.casadidavide.casadidavide.service.CampagnaService;
import it.casadidavide.casadidavide.service.ClienteService;
import it.casadidavide.casadidavide.service.EmailService;
import it.casadidavide.casadidavide.service.PrenotazioneProdottiService;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ConsegneDaElaborareController {
	
	@Autowired
	private CampagnaService campagnaService;
	@Autowired
    private PrenotazioneProdottiService prenotazioneProdottiService;
	@Autowired
	private ClienteService clienteService;
	@Autowired
    private EmailService emailService;
	
	/**
	 *  Mostra pagina per scegliere la campagna (ordini da spedire)
	 */
    @GetMapping("/corrieri/scegli-campagna-da-spedire")
    public String scegliCampagnaDaSpedire(Model model) {
        List<Campagna> campagne = campagnaService.findAll()
            .stream()
            .sorted(Comparator.comparing(Campagna::getId).reversed()) // dalla più recente
            .collect(Collectors.toList());

        model.addAttribute("campagne", campagne);
        model.addAttribute("title", "Selezione campagna");
        return "corrieri/scegli-campagna-da-spedire";
    }

	/**
	 *  Vedi ordini da spedire (GET) filtrati per campagna
	 */
    @GetMapping("/corrieri/consegne-da-elaborare")
    public String consegneDaElaborarePerCampagnaGet(@RequestParam Long idCampagna,
    					@RequestParam(defaultValue = "data") String sort, Model model) {
        Campagna campagna = campagnaService.findById(idCampagna)
                .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata"));

        List<PrenotazioneProdotti> prenotazioni = prenotazioneProdottiService
                .getPrenotazioniDaElaborare().stream()
                .filter(p -> p.getCampagna().equals(campagna))
                // .sorted(Comparator.comparing(PrenotazioneProdotti::getDataPrenotazione).reversed())
                .collect(Collectors.toList());
        
        Comparator<PrenotazioneProdotti> comparator;
        if ("sostenitore".equals(sort)) {
            comparator = Comparator.comparing(p -> p.getCliente().getNominativo());
        } else {
            // Default: ordina per data (come prima)
            comparator = Comparator.comparing(PrenotazioneProdotti::getDataPrenotazione).reversed();
        }
        prenotazioni = prenotazioni.stream().sorted(comparator).collect(Collectors.toList());

        model.addAttribute("title", "Ordini da spedire - " + campagna.getDescrizioneCampagna());
        model.addAttribute("campagna", campagna);
        model.addAttribute("prenotazioni", prenotazioni);
        model.addAttribute("currentSort", sort);

        return "corrieri/consegne-da-elaborare";
    }
    
    /**
     *  Vedi ordini da spedire (POST) filtrati per campagna
     */
    @PostMapping("/corrieri/consegne-da-elaborare")
    public String consegneDaElaborarePerCampagna(
            @RequestParam Long idCampagna, @RequestParam(defaultValue = "data") String sort,
            Model model) {

        Campagna campagna = campagnaService.findById(idCampagna)
                .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata"));

        List<PrenotazioneProdotti> prenotazioni = prenotazioneProdottiService
                .getPrenotazioniDaElaborare().stream()
                .filter(p -> p.getCampagna().equals(campagna))
                // .sorted(Comparator.comparing(PrenotazioneProdotti::getDataPrenotazione).reversed()) 
                .collect(Collectors.toList());
        
        Comparator<PrenotazioneProdotti> comparator;
        if ("sostenitore".equals(sort)) {
            comparator = Comparator.comparing(p -> p.getCliente().getNominativo());
        } else {
            // Default: ordina per data
            comparator = Comparator.comparing(PrenotazioneProdotti::getDataPrenotazione).reversed();
        }
        prenotazioni = prenotazioni.stream().sorted(comparator).collect(Collectors.toList());

        model.addAttribute("title", "Ordini da spedire - " + campagna.getDescrizioneCampagna());
        model.addAttribute("campagna", campagna);
        model.addAttribute("prenotazioni", prenotazioni);
        model.addAttribute("currentSort", sort);

        return "corrieri/consegne-da-elaborare";
    }
    
    /**
     *  Visualizza i dettagli di un cliente
     */
    @GetMapping("/corrieri/dettagli-cliente/{idCliente}")
    public String mostraDettagliCliente(@PathVariable Long idCliente, @RequestParam(required = false) Long idCampagna, Model model) {
        Cliente cliente = clienteService.findById(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("Cliente non trovato"));

        if (idCampagna != null) {
            Campagna campagna = campagnaService.findById(idCampagna)
                    .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata"));
            model.addAttribute("campagna", campagna);
        }
        model.addAttribute("cliente", cliente);
        model.addAttribute("title", "Dettagli sostenitore");

        return "corrieri/dettagli-cliente";
    }
    
    /**
     *  Mostra form inserimento tracciamento spedizione
     */
    @GetMapping("/corrieri/elabora-spedizione/{idPrenotazione}")
    public String mostraFormTracciamentoSpedizione(@PathVariable Long idPrenotazione, Model model) {
        PrenotazioneProdotti prenotazione = prenotazioneProdottiService.findById(idPrenotazione)
            .orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata"));

        model.addAttribute("prenotazione", prenotazione);
        model.addAttribute("title", "Elabora spedizione");

        return "corrieri/elabora-spedizione";
    }
    
    /**
     *  Salva tracciamento spedizione
     */
    @PostMapping("/corrieri/elabora-spedizione/{idPrenotazione}")
    public String salvaTracciamentoSpedizione(@PathVariable Long idPrenotazione,
                                              @RequestParam String tracciamentoSpedizione) {
        
        // Inizio Blocco Try-Catch
        PrenotazioneProdotti prenotazione;
        try {
            prenotazione = prenotazioneProdottiService.findById(idPrenotazione)
                .orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata con ID: " + idPrenotazione));

            prenotazione.setTracciamentoSpedizione(tracciamentoSpedizione);
            prenotazione.setDataSalvataggioTracciamento(LocalDate.now());
            prenotazioneProdottiService.save(prenotazione);

        } catch (Exception e) {
            // Se il salvataggio fallisce, non inviare l'email e mostra un errore
            System.err.println("Errore durante il salvataggio della spedizione: " + e.getMessage());
             return "redirect:/corrieri/consegne";
        }
        
        
        if (prenotazione.getCliente().getEmail()!=null) {
        	try {
        		emailService.inviaEmailConfermaSpedizione(prenotazione);
        	} catch (Exception e_mail) {
        		System.err.println("WARN: Salvataggio ordine " + idPrenotazione + " OK, ma invio email fallito." 
                                + e_mail.getMessage());
        	}
        }

        // torna alla lista della stessa campagna (GET)
        return "redirect:/corrieri/consegne-da-elaborare?idCampagna=" + prenotazione.getCampagna().getId();
    }
    
    /**
     *  Esporta in XLS le consegne da elaborare per una campagna
     */
    @GetMapping("/corrieri/consegne-da-elaborare/esporta-xls/{idCampagna}")
    public void esportaConsegneDaElaborareXls(@PathVariable Long idCampagna, HttpServletResponse response) throws IOException {

        Campagna campagna = campagnaService.findById(idCampagna)
                .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata"));

        List<PrenotazioneProdotti> prenotazioni = prenotazioneProdottiService
                .getPrenotazioniDaElaborare().stream()
                .filter(p -> p.getCampagna().equals(campagna))
                .sorted(Comparator.comparing(PrenotazioneProdotti::getDataPrenotazione).reversed())
                .collect(Collectors.toList());

        // Imposta intestazioni HTTP per il download
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "ordini_da_spedire_" + campagna.getDescrizioneCampagna().replaceAll("\\s+", "_") + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // Crea workbook Excel
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Consegne da elaborare");

            // Intestazioni
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Prodotto", "Quantità", "Data Prenotazione", "Sostenitore", "Email", "Indirizzo Spedizione", "Note"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Dati
            int rowNum = 1;
            for (PrenotazioneProdotti p : prenotazioni) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getProdotto().getDescrizioneProdotto());
                row.createCell(1).setCellValue(p.getQuantita());
                row.createCell(2).setCellValue(p.getDataPrenotazione() != null ? p.getDataPrenotazione().toString() : "");
                row.createCell(3).setCellValue(p.getCliente().getNominativo());
                row.createCell(4).setCellValue(p.getCliente().getEmail());
                row.createCell(5).setCellValue(p.getCliente().getIndirizzoSpedizione());
                row.createCell(6).setCellValue(p.getNote());

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
