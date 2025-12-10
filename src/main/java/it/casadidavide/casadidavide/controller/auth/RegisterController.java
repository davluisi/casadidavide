package it.casadidavide.casadidavide.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.casadidavide.casadidavide.model.Socio;
import it.casadidavide.casadidavide.service.SocioService;

@Controller
public class RegisterController {
	
	@Autowired
	private SocioService socioService;

	@GetMapping("/registrazione")
	public String mostraFormRegister(Model model) {
		model.addAttribute("socio", new Socio());
		model.addAttribute("title", "Registrazione");
		return "auth/register";
	}
	
	@PostMapping("/registrazione")
	public String registraSocio(@ModelAttribute Socio socio,
	                            @RequestParam("confirmPassword") String confirmPassword,
	                            Model model) {
	    if (!socio.getPassword().equals(confirmPassword)) {
	        model.addAttribute("error", "Le password non coincidono.");
	        return "auth/register";
	    }

	    if (socioService.esisteUsername(socio.getUsername())) {
	        model.addAttribute("error", "Username già esistente.");
	        return "auth/register";
	    }

	    socio.setAbilitato(false); // ← qui imposti abilitato a false
	    socioService.salvaSocio(socio);
	    
        model.addAttribute("success", "Registrazione avvenuta. Attendi approvazione da parte dell’amministratore.");


	    return "redirect:/login?success";
	}
}
