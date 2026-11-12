package sysmanut.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sysmanut.exception.ValidacaoException;
import sysmanut.service.CalculadoraManutencao;
import sysmanut.service.RegraGarantia;

/** Usa no servidor as regras testadas na etapa 7 (CalculadoraManutencao e RegraGarantia). */
@Controller
public class ManutencaoController {
    private final CalculadoraManutencao calc;
    private final RegraGarantia garantia;
    public ManutencaoController(CalculadoraManutencao calc, RegraGarantia garantia) { this.calc = calc; this.garantia = garantia; }

    @GetMapping("/manutencao") String form() { return "manutencao-form"; }

    @PostMapping("/manutencao/calcular")
    String calcular(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                    @RequestParam String maoDeObra, @RequestParam(required = false) List<String> pecas,
                    @RequestParam String desconto, Model m) {
        try {
            List<BigDecimal> valores = pecas == null ? List.of()
                    : pecas.stream().filter(s -> !s.isBlank()).map(ManutencaoController::dec).toList();
            m.addAttribute("total", calc.calcularTotal(dec(maoDeObra), valores, dec(desconto)));
            m.addAttribute("fimGarantia", garantia.ultimoDiaDaGarantia(data));
        } catch (ValidacaoException e) {
            m.addAttribute("erros", e.getErros());
        } catch (NumberFormatException e) {
            m.addAttribute("erros", List.of("Informe valores numéricos válidos."));
        }
        return "manutencao-form";
    }

    private static BigDecimal dec(String s) { return new BigDecimal(s.trim().replace(',', '.')); }
}
