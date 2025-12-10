package it.casadidavide.casadidavide.controller.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import it.casadidavide.casadidavide.model.Socio;
import it.casadidavide.casadidavide.service.EmailService;
import it.casadidavide.casadidavide.service.SocioService;

@Controller
public class ResetPasswordController {
	
	@Autowired
	private SocioService socioService;
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/recupera-password")
    public String mostraFormRecuperoPassword(Model model) {
        model.addAttribute("title", "Recupera credenziali");
        return "auth/recupera-password";
    }
	
    @PostMapping("/recupera-password")
    public String processaRecuperoPassword(@RequestParam("email") String email, Model model) {
        
        // Definiamo il dominio di base per il link
        String urlBase = "https://www.casadidavide.org"; 
        
        try {
            // 1. Cerca il socio e genera/salva il token (usa il metodo che abbiamo creato)
            Socio socio = socioService.generaTokenResetPassword(email);
            
            // 2. Se il socio esiste...
            if (socio != null) {
                // 3. Creare il link di reset completo
                String resetLink = urlBase + "/reset-password?token=" + socio.getResetPasswordToken();
                
                // 4. Inviare l'email usando il nuovo metodo dell'EmailService
                emailService.inviaEmailResetPassword(
                    socio.getNominativo(), 
                    socio.getEmail(), 
                    resetLink,
                    socio.getUsername()
                );
            }
            // Se socio == null (email non trovata), non facciamo nulla.
            // Il messaggio di conferma è lo stesso per motivi di sicurezza.

        } catch (Exception e) {
            // Un errore generico nel caso l'invio email fallisca
            System.err.println("Errore durante il processo di recupero: " + e.getMessage());
            // Non mostriamo l'errore all'utente, solo il messaggio generico.
        }
        
        // Messaggio di sicurezza: Mostra questo in ogni caso.
        model.addAttribute("messaggio", 
            "Se l'indirizzo email è presente nel nostro sistema, riceverai a breve un link per il recupero delle credenziali.");
        model.addAttribute("title", "Recupera credenziali");
        
        return "auth/recupera-password";
    }
	
    /**
     * Mostra la pagina finale di reset password.
     * Verifica che il token sia valido prima di mostrare il form.
     */
    @GetMapping("/reset-password")
    public String mostraFormResetPassword(@RequestParam("token") String token, Model model) {
        
        // 1. Verifica che il token esista
        Optional<Socio> socioOpt = socioService.findByResetPasswordToken(token);
        
        if (socioOpt.isEmpty()) {
            // 2. Se il token non è valido o è scaduto,
            //    rimanda al login con un messaggio di errore.
            model.addAttribute("errore", "Link di reset non valido o scaduto.");
            return "auth/recupera-password";
        }

        // 3. Se il token è valido, passa il token alla pagina
        model.addAttribute("token", token);
        model.addAttribute("title", "Reimposta Password");
        return "auth/reset-password";
    }

    /**
     * Processa la nuova password.
     * Riceve token, password e conferma password dal form.
     */
    @PostMapping("/reset-password")
    public String processaResetPassword(
                            @RequestParam("token") String token,
                            @RequestParam("password") String password,
                            @RequestParam("confirmPassword") String confirmPassword,
                            Model model) {

        // 1. Cerca di nuovo il socio tramite il token
        Optional<Socio> socioOpt = socioService.findByResetPasswordToken(token);

        if (socioOpt.isEmpty()) {
            // 2. Se il token nel frattempo è "sparito", errore.
            model.addAttribute("errore", "Errore: richiesta non valida.");
            return "auth/reset-password";
        }

        // 3. Verifica che le password coincidano
        if (!password.equals(confirmPassword)) {
            model.addAttribute("errore", "Le password non coincidono.");
            model.addAttribute("token", token); // Ri-passa il token al form
            return "auth/reset-password";
        }
        
        // 4. TUTTO OK! Aggiorna la password.
        Socio socio = socioOpt.get();
        socioService.aggiornaPassword(socio, password);

        // 5. Riporta l'utente al login con un messaggio di successo
        
        return "redirect:/login?resetSuccess";
    }

}
