package it.casadidavide.casadidavide.controller.magazzino;

import java.io.IOException;
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
import it.casadidavide.casadidavide.model.PrenotazioneProdotti;
import it.casadidavide.casadidavide.service.CampagnaService;
import it.casadidavide.casadidavide.service.PrenotazioneProdottiService;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ConsegneElaborateController {
	
	@Autowired
	private CampagnaService campagnaService;
	@Autowired
    private PrenotazioneProdottiService prenotazioneProdottiService;
	
	/**
	 *  Mostra pagina per scegliere la campagna (consegne elaborate)
	 */
    @GetMapping("/corrieri/scegli-campagna-effettuate")
    public String scegliCampagnaEffettuate(Model model) {
        List<Campagna> campagne = campagnaService.findAll()
            .stream()
            .sorted(Comparator.comparing(Campagna::getId).reversed())
            .collect(Collectors.toList());

        model.addAttribute("campagne", campagne);
        model.addAttribute("title", "Selezione campagna");
        return "corrieri/scegli-campagna-effettuate";
    }

	/**
	 *  Vedi consegne elaborate (GET) filtrate per campagna
	 */
    @GetMapping("/corrieri/consegne-effettuate")
    public String mostraConsegneEffettuatePerCampagnaGet(@RequestParam Long idCampagna, Model model) {
        Campagna campagna = campagnaService.findById(idCampagna)
                .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata"));

        List<PrenotazioneProdotti> consegneEffettuate = prenotazioneProdottiService.findAll().stream()
                .filter(p -> p.isElencoPas()
                          && p.getTracciamentoSpedizione() != null
                          && !p.getTracciamentoSpedizione().isBlank()
                          && p.getCampagna().equals(campagna))
                .sorted(Comparator.comparing(PrenotazioneProdotti::getDataSalvataggioTracciamento).reversed())
                .collect(Collectors.toList());

        model.addAttribute("title", "Spedizioni effettuate - " + campagna.getDescrizioneCampagna());
        model.addAttribute("campagna", campagna);
        model.addAttribute("consegneEffettuate", consegneEffettuate);

        return "corrieri/consegne-effettuate";
    }
    
    // Vedi consegne elaborate (POST) filtrate per campagna
    @PostMapping("/corrieri/consegne-effettuate")
    public String mostraConsegneEffettuatePerCampagna(
            @RequestParam Long idCampagna,
            Model model) {

        Campagna campagna = campagnaService.findById(idCampagna)
                .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata"));

        List<PrenotazioneProdotti> consegneEffettuate = prenotazioneProdottiService.findAll().stream()
                .filter(p -> p.isElencoPas()
                          && p.getTracciamentoSpedizione() != null
                          && !p.getTracciamentoSpedizione().isBlank()
                          && p.getCampagna().equals(campagna))
                .sorted(Comparator.comparing(PrenotazioneProdotti::getDataSalvataggioTracciamento).reversed())
                .collect(Collectors.toList());

        model.addAttribute("title", "Spedizioni effettuate - " + campagna.getDescrizioneCampagna());
        model.addAttribute("campagna", campagna);
        model.addAttribute("consegneEffettuate", consegneEffettuate);

        return "corrieri/consegne-effettuate";
    }
    
    /**
     *  Esporta in xls le spedizioni elaborate per una campagna
     */
    @GetMapping("/corrieri/consegne-effettuate/esporta-xls/{idCampagna}")
    public void esportaConsegneEffettuateXls(@PathVariable Long idCampagna, HttpServletResponse response) throws IOException {

        Campagna campagna = campagnaService.findById(idCampagna)
                .orElseThrow(() -> new IllegalArgumentException("Campagna non trovata"));

        List<PrenotazioneProdotti> consegneEffettuate = prenotazioneProdottiService.findAll().stream()
                .filter(p -> p.isElencoPas()
                          && p.getTracciamentoSpedizione() != null
                          && !p.getTracciamentoSpedizione().isBlank()
                          && p.getCampagna().equals(campagna))
                .sorted(Comparator.comparing(PrenotazioneProdotti::getDataSalvataggioTracciamento).reversed())
                .collect(Collectors.toList());

        // Imposta intestazioni HTTP per il download
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "spedizioni_effettuate_" + campagna.getDescrizioneCampagna().replaceAll("\\s+", "_") + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // Crea workbook Excel
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Spedizioni Effettuate");

            // Intestazioni
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Prodotto", "Quantità", "Sostenitore","Email", "Indirizzo", "Data Prenotazione", "Tracciamento"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Dati
            int rowNum = 1;
            for (PrenotazioneProdotti p : consegneEffettuate) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getProdotto().getDescrizioneProdotto());
                row.createCell(1).setCellValue(p.getQuantita());
                row.createCell(2).setCellValue(p.getCliente().getNominativo());
                row.createCell(3).setCellValue(p.getCliente().getEmail());
                row.createCell(4).setCellValue(p.getCliente().getIndirizzoSpedizione());
                row.createCell(5).setCellValue(p.getDataPrenotazione() != null ? p.getDataPrenotazione().toString() : "");
                row.createCell(6).setCellValue(p.getTracciamentoSpedizione());
            }

            // Adatta larghezza colonne
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(response.getOutputStream());
        }
    }
}
