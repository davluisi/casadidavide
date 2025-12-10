package it.casadidavide.casadidavide.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

	@GetMapping({"/", "/login"})
	public String mostraFormLogin (Model model) {
		model.addAttribute("title", "CasaDiDavide");
		return "auth/login";
	}
}
