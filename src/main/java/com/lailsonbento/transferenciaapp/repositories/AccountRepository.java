package com.lailsonbento.transferenciaapp.repositories;

import com.lailsonbento.transferenciaapp.domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

}