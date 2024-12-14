package com.lailsonbento.transferenciaapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    private BigDecimal value;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "from_account_id", updatable = false, nullable = false)
    private Account fromAccount;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "to_account_id", updatable = false, nullable = false)
    private Account toAccount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Transaction(Account fromAccount, Account toAccount, BigDecimal value) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.value = value;
    }
}