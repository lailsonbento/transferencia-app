package com.lailsonbento.transferenciaapp;

import com.lailsonbento.transferenciaapp.domain.Account;
import com.lailsonbento.transferenciaapp.domain.AccountType;
import com.lailsonbento.transferenciaapp.domain.User;
import com.lailsonbento.transferenciaapp.repositories.AccountRepository;
import com.lailsonbento.transferenciaapp.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountTestIT {

    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2);
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100).setScale(2);

    private Account ACCOUNT_PAYER;
    private Account ACCOUNT_PAYEE;

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();

        ACCOUNT_PAYEE = accountRepository.save(
                new Account(
                        new User("Lailson Bento", "123145668300", "lailsonteste@gmail.com"),
                        AccountType.MERCHANT, ZERO
                )
        );
        ACCOUNT_PAYER = accountRepository.save(
                new Account(
                        new User("Joao Teste", "456145668300", "joaoteste@gmail.com"),
                        AccountType.USER, ONE_HUNDRED)
        );
    }


    @Test
    public void shouldTransferBetweenUsers() {

    }
}
