package com.lailsonbento.transferenciaapp.repositories;

import com.lailsonbento.transferenciaapp.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}