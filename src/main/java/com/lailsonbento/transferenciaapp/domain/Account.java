package com.lailsonbento.transferenciaapp.domain;

import com.lailsonbento.transferenciaapp.exceptions.AccountException;
import com.lailsonbento.transferenciaapp.exceptions.InsufficientBalanceException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "account")
@NoArgsConstructor
@Getter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal balance;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private AccountType accountType = AccountType.USER;

    @Version
    private Long version;

    public Account(User user, AccountType accountType, BigDecimal balance) {
        this.balance = balance;
        this.user = user;
        this.accountType = accountType;
    }

    public Transaction transferTo(Account toAccount, BigDecimal value) {
        validateTransfer(toAccount);

        this.debit(value);
        toAccount.credit(value);

        return new Transaction(this, toAccount, value);
    }

    public void debit(BigDecimal value) {
        if (isMerchant()) {
            throw new AccountException("Merchant account cannot be debited");
        }

        if (balance.subtract(value).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        this.balance = this.balance.subtract(value);
    }

    public void credit(BigDecimal value) {
        this.balance = this.balance.add(value);
    }

    private void validateTransfer(Account toAccount) {
        if (isTransactionBetweenSameAccount(toAccount)) {
            throw new AccountException("Payer and Payee must be different");
        }

        if (isMerchant()) {
            throw new AccountException("Payer cannot be a merchant");
        }
    }

    private boolean isTransactionBetweenSameAccount(Account toAccount) {
        return Objects.equals(this, toAccount);
    }

    public boolean isMerchant() {
        return AccountType.MERCHANT.equals(this.accountType);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Account account)) return false;
        return Objects.equals(id, account.id) && Objects.equals(user, account.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }
}