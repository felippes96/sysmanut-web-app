package sysmanut.web;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sysmanut.exception.*;
import sysmanut.model.Cliente;
import sysmanut.service.ClienteService;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService service;
    public ClienteController(ClienteService service) { this.service = service; }

    @GetMapping
    String listar(@RequestParam(defaultValue = "") String q, Model m) {
        String f = q.toLowerCase();
        List<Cliente> lista = service.listar().stream()
                .filter(c -> (c.getNome() + c.getEmail()).toLowerCase().contains(f)).toList();
        m.addAttribute("clientes", lista).addAttribute("q", q);
        return "clientes";
    }

    @GetMapping("/novo") String novo(Model m) { m.addAttribute("cliente", new Cliente()); return "cliente-form"; }

    @GetMapping("/{id}/editar")
    String editar(@PathVariable int id, Model m) {
        try { m.addAttribute("cliente", service.buscarPorId(id)); return "cliente-form"; }
        catch (RegraNegocioException e) { return "redirect:/clientes"; }
    }

    @PostMapping
    String salvar(@ModelAttribute Cliente cliente, Model m) {
        try {
            if (cliente.getIdCliente() == 0) service.cadastrar(cliente); else service.atualizar(cliente);
            return "redirect:/clientes";
        } catch (ValidacaoException e) {
            m.addAttribute("erros", e.getErros());
        } catch (RegraNegocioException e) {
            m.addAttribute("erros", List.of(e.getMessage()));
        }
        return "cliente-form";
    }

    @PostMapping("/{id}/excluir")
    String excluir(@PathVariable int id) {
        try { service.excluir(id); } catch (RegraNegocioException ignorado) { /* já removido */ }
        return "redirect:/clientes";
    }
}
