package it.casadidavide.casadidavide.controller.magazzino;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuMagazzinoController {

	/**
	 *  Apri nenu consegne
	 */
    @GetMapping("/corrieri/consegne")
    public String mostraMenuConsegne(Model model) {
        model.addAttribute("title", "Menu magazzino");
        return "corrieri/consegne";
    }
}
