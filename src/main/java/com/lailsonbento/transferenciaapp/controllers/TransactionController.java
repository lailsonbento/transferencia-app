package com.lailsonbento.transferenciaapp.controllers;

import com.lailsonbento.transferenciaapp.domain.TransactionRequest;
import com.lailsonbento.transferenciaapp.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransactionRequest request) {
        transactionService.transfer(request);
        return ResponseEntity.accepted().build();
    }
}