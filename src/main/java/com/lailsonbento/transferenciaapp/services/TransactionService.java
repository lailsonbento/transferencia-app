package com.lailsonbento.transferenciaapp.services;

import com.lailsonbento.transferenciaapp.domain.TransactionRequest;
import com.lailsonbento.transferenciaapp.domain.User;
import com.lailsonbento.transferenciaapp.exceptions.AccountNotFoundException;
import com.lailsonbento.transferenciaapp.exceptions.TransactionNotAuthorizedException;
import com.lailsonbento.transferenciaapp.repositories.AccountRepository;
import com.lailsonbento.transferenciaapp.repositories.TransactionRepository;
import com.lailsonbento.transferenciaapp.utils.WebClientUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Value("${app.authorization.url}")
    private String authorizationUrl;

    @Value("${app.notification.url}")
    private String notificationUrl;

    @Transactional
    public void transfer(TransactionRequest request) {
        var fromAccount = accountRepository.findById(request.payerId())
                .orElseThrow(() -> new AccountNotFoundException(String.format("Payer id %s not found", request.payerId())));
        var toAccount = accountRepository.findById(request.payeeId())
                .orElseThrow(() -> new AccountNotFoundException(String.format("Payee id %s not found", request.payeeId())));

        log.info("Transfering {} from {} to {}", request.value(), fromAccount, toAccount);
        var transaction = fromAccount.transferTo(toAccount, request.value());

        authorizeTransaction();

        transactionRepository.saveAndFlush(transaction);
        log.info("Transaction saved {}", transaction);

        notifyUsers(fromAccount.getUser(), toAccount.getUser());
    }

    private void authorizeTransaction() {
        log.info("Authorizing transaction");
        WebClient.create(authorizationUrl)
                .get()
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        _ -> Mono.error(new TransactionNotAuthorizedException("Transaction not authorized")))
                .bodyToMono(String.class)
                .block();
    }

    private void notifyUsers(User payer, User payee) {
        try {
            log.info("Notifying users about transaction {} to {}", payer, payee);
            WebClientUtils
                    .doAsync(notificationUrl, HttpMethod.POST, String.class)
                    .thenAccept(_ -> log.info("Notified users about transaction {}, {}", payer, payee));
        } catch (Exception e) {
            log.error("Error when notifying users {}, {}", payer, payee);
            log.error(e.getMessage(), e);
        }
    }
}