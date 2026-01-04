package com.devsu.hackerearth.backend.account.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.account.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    public Optional<Transaction> findFirstByAccountIdOrderByIdDescDateDesc(Long accountId);

    public List<Transaction> findAllByDateBetweenAndAccountIdInOrderByDateAsc(Date initDate, Date endDate, List<Long> accountIds);
}
