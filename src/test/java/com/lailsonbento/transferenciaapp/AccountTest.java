package com.lailsonbento.transferenciaapp;

import com.lailsonbento.transferenciaapp.domain.Account;
import com.lailsonbento.transferenciaapp.domain.AccountType;
import com.lailsonbento.transferenciaapp.domain.User;
import com.lailsonbento.transferenciaapp.exceptions.AccountException;
import com.lailsonbento.transferenciaapp.exceptions.InsufficientBalanceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class AccountTest {

    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2);
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100).setScale(2);
    private static final BigDecimal FIFTY = new BigDecimal(50).setScale(2);
    
    public static final User LAILSON = new User("Lailson Bento", "123456789", "lailson.bento@example.com");
    public static final User JOAO = new User("João Pé de Feijão", "123456789", "joao@example.com");

    @Test
    public void shouldTransferBetweenAccounts() {
        Account fromAccount = new Account(LAILSON, AccountType.USER, ONE_HUNDRED);
        Account toAccount = new Account(JOAO, AccountType.MERCHANT, ZERO);

        assertDoesNotThrow(() -> fromAccount.transferTo(toAccount, FIFTY));
        assertEquals(FIFTY, fromAccount.getBalance());
        assertEquals(FIFTY, toAccount.getBalance());
    }

    @Test
    public void shouldTransferFromAccountWithInsufficientBalance() {
        Account fromAccount = new Account(LAILSON, AccountType.USER, ZERO);
        Account toAccount = new Account(JOAO, AccountType.MERCHANT, ZERO);

        assertThrows(InsufficientBalanceException.class, () -> fromAccount.transferTo(toAccount, ONE_HUNDRED), "Insufficient balance");
    }

    @Test
    public void shouldNotTransferBetweenSameAccount() {
        Account fromAccount = new Account(LAILSON, AccountType.USER, ONE_HUNDRED);
        Account toAccount = new Account(LAILSON, AccountType.USER, ZERO);

        assertThrows(AccountException.class, () -> fromAccount.transferTo(toAccount, FIFTY), "Payer and Payee must be different");
    }

    @Test
    public void shouldDebitFromUserAccount() {
        Account account = new Account(LAILSON, AccountType.USER, ONE_HUNDRED);

        account.debit(FIFTY);

        assertEquals(FIFTY, account.getBalance());
    }

    @Test
    public void shouldNotDebitFromAccountWithInsufficientBalance() {
        Account account = new Account(LAILSON, AccountType.USER, ZERO);

        assertThrows(InsufficientBalanceException.class, () -> account.debit(FIFTY), "Insufficient balance");
    }

    @Test
    public void shouldNotDebitFromMerchantAccount() {
        Account account = new Account(LAILSON, AccountType.MERCHANT, ONE_HUNDRED);

        assertThrows(AccountException.class, () -> account.debit(FIFTY), "Merchant account cannot be debited");
    }

    @Test
    public void shouldCreditToUserAccount() {
        Account account = new Account(LAILSON, AccountType.USER, ZERO);

        account.credit(FIFTY);

        assertEquals(FIFTY, account.getBalance());
    }

    @Test
    public void shouldCreditToMerchantAccount() {
        Account account = new Account(LAILSON, AccountType.MERCHANT, ZERO);

        account.credit(FIFTY);

        assertEquals(FIFTY, account.getBalance());
    }
}
