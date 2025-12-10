package it.casadidavide.casadidavide.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuConfigurazioniController {

	/**
	 *  Mostra menu configurazioni
	 */
    @GetMapping("/admin/configurazioni")
    public String mostraConfigurazioni(Model model) {
        model.addAttribute("title", "Configurazioni");
        return "admin/configurazioni";
    }
}
