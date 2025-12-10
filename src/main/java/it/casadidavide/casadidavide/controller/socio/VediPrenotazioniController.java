package it.casadidavide.casadidavide.controller.socio;

import java.io.IOException;
import java.util.List;

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

import it.casadidavide.casadidavide.model.Campagna;
import it.casadidavide.casadidavide.model.PrenotazioneProdotti;
import it.casadidavide.casadidavide.service.CampagnaService;
import it.casadidavide.casadidavide.service.PrenotazioneProdottiService;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class VediPrenotazioniController {
	
	@Autowired
    private CampagnaService campagnaService;
	@Autowired
    private PrenotazioneProdottiService prenotazioneProdottiService;

	/**
	 *  Vedi prenotazioni effettuate per la campagna
	 */
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
    
    /**
     * Esporta il xls le prenotazioni della campagna
     */
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
            String[] headers = {"Socio", "Prodotto", "Quantità", "Sostenitore", "Ricavo (€)", "Data Prenotazione", "Inviata al magazzino", "Note", "Data Spedizione"};
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
                row.createCell(7).setCellValue(p.getNote());
                row.createCell(8).setCellValue(p.getDataSalvataggioTracciamento() != null ? p.getDataPrenotazione().toString() : "");

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
