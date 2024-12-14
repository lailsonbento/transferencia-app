package com.lailsonbento.transferenciaapp.exceptions;

public class TransactionNotAuthorizedException extends RuntimeException {

    public TransactionNotAuthorizedException(String message) {
        super(message);
    }
}
