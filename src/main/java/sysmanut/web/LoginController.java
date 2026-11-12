package sysmanut.web;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sysmanut.exception.RegraNegocioException;
import sysmanut.service.AutenticacaoService;

@Controller
public class LoginController {
    private final AutenticacaoService auth;
    public LoginController(AutenticacaoService auth) { this.auth = auth; }

    @GetMapping("/login") String form() { return "login"; }

    @PostMapping("/login")
    String entrar(@RequestParam String login, @RequestParam String senha, HttpSession s, Model m) {
        try {
            s.setAttribute("usuario", auth.autenticar(login, senha).getNome());
            return "redirect:/";
        } catch (RegraNegocioException e) {
            m.addAttribute("erros", List.of(e.getMessage()));
            return "login";
        }
    }

    @GetMapping("/logout") String sair(HttpSession s) { s.invalidate(); return "redirect:/login"; }
    @GetMapping("/") String menu() { return "index"; }
}
