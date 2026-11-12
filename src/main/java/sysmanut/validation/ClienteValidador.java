package sysmanut.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import sysmanut.exception.ValidacaoException;
import sysmanut.model.Cliente;

/**
 * Regras de validação do cliente. Antes estavam dentro de
 * TelaCadastroCliente.salvarCliente() (somente "campos preenchidos").
 */
public class ClienteValidador implements Validador<Cliente> {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    @Override
    public void validar(Cliente cliente) {
        List<String> erros = new ArrayList<>();

        if (cliente == null) {
            erros.add("Cliente não informado.");
            throw new ValidacaoException(erros);
        }

        if (vazio(cliente.getNome())) {
            erros.add("O nome é obrigatório.");
        }
        if (vazio(cliente.getTelefone())) {
            erros.add("O telefone é obrigatório.");
        }
        if (vazio(cliente.getEmail())) {
            erros.add("O e-mail é obrigatório.");
        } else if (!EMAIL.matcher(cliente.getEmail().trim()).matches()) {
            erros.add("O e-mail informado é inválido.");
        }
        if (vazio(cliente.getEndereco())) {
            erros.add("O endereço é obrigatório.");
        }

        if (!erros.isEmpty()) {
            throw new ValidacaoException(erros);
        }
    }

    private static boolean vazio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
