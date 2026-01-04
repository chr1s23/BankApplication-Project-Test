package com.devsu.hackerearth.backend.account.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.account.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    public List<Account> findAllByClientId(Long clientId); 

    public Optional<Account> findByNumber(String number);

    public Optional<Account> findByNumberAndIdNot(String number, Long id);
    
}
