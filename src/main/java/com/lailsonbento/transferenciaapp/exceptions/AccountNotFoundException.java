package com.lailsonbento.transferenciaapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String message) {}
}
