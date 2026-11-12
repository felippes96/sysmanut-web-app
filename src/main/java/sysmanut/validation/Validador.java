package sysmanut.validation;

import sysmanut.exception.ValidacaoException;

/** Contrato de validação de um objeto de domínio. Permite trocar/estender regras sem mexer nos serviços. */
public interface Validador<T> {

    /** @throws ValidacaoException se houver qualquer campo inválido */
    void validar(T objeto);
}
