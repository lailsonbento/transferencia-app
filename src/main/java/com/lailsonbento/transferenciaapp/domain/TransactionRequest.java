package com.lailsonbento.transferenciaapp.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotNull(message = "Value is required")
        @Positive(message = "Value must be positive")
        BigDecimal value,
        @NotNull(message = "Payer ID is required")
        Long fromAccount,
        @NotNull(message = "Payee ID is required")
        Long toAccount
) {}