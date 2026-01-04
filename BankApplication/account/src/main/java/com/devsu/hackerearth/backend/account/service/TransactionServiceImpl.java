package com.devsu.hackerearth.backend.account.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.devsu.hackerearth.backend.account.configuration.CustomException;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.ClientDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;

import reactor.core.publisher.Mono;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;

    private final WebClient webClient;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository, WebClient webClient) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.webClient = webClient;
    }

    @Override
    public List<TransactionDto> getAll() {
        // Get all transactions
        List<TransactionDto> transactions = this.transactionRepository.findAll().stream().map(TransactionDto::new)
                .collect(Collectors.toList());
        return transactions;
    }

    @Override
    public TransactionDto getById(Long id) {
        // Get transactions by id
        Transaction transaction = this.transactionRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Transacción no encontrada."));
        return new TransactionDto(transaction);
    }

    @Override
    public TransactionDto create(TransactionDto transactionDto) {
        // Create transaction
        Account account = this.accountRepository.findById(transactionDto.getAccountId()).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "No existe la cuenta asociada a la transacción."));
        Double balance = 0.0;
        TransactionDto lastTransaction = getLastByAccountId(transactionDto.getAccountId());
        if (lastTransaction == null) {
            balance = account.getInitialAmount() + transactionDto.getAmount();
        } else {
            if (lastTransaction.getDate().after(transactionDto.getDate())) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "La fecha de la transacción no es válida. Existe una transación con fecha posterior.");
            }
            balance = lastTransaction.getBalance() + transactionDto.getAmount();
        }
        if (balance < 0.0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Saldo no disponible");
        }
        transactionDto.setBalance(balance);
        return new TransactionDto(this.transactionRepository.save(new Transaction(transactionDto)));
    }

    @Override
    public List<BankStatementDto> getAllByAccountClientIdAndDateBetween(Long clientId, Date dateTransactionStart,
            Date dateTransactionEnd) {
        // Report
        /*List<ReportDto> report = new ArrayList<>();
        List<Account> clientAccounts = this.accountRepository.findAllByClientId(clientId);
        List<Transaction> accountTransactions = this.transactionRepository.findAllByDateBetweenAndAccountIdIn(
                dateTransactionStart, dateTransactionEnd,
                clientAccounts.stream().map(account -> {
                    return account.getId();
                }).collect(Collectors.toList()));
        Map<Long, List<TransactionDto>> accountsGrouped = accountTransactions.stream().map(TransactionDto::new)
                .collect(Collectors.groupingBy(TransactionDto::getAccountId));
        clientAccounts.forEach(account -> {
            report.add(new ReportDto(account.getId(), account.getNumber(), account.getType(),
                    account.getInitialAmount(), account.isActive(), clientId,
                    accountsGrouped.get(account.getId())));
        });
        return report;*/

        if (dateTransactionStart.after(dateTransactionEnd)) throw new CustomException(HttpStatus.BAD_REQUEST, "La fecha inicio del reporte no puede ser posterior a la fecha fin.");

        Mono<ClientDto> clientDto = webClient.get().uri("/api/clients/{id}", clientId).retrieve().bodyToMono(ClientDto.class);
        if (clientDto.block() == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Cliente no encontrado.");
        }

        String clientName = clientDto.block().getName();
        List<BankStatementDto> report = new ArrayList<>();
        List<Account> clientAccounts = this.accountRepository.findAllByClientId(clientId);
        Map<Long, List<Transaction>> accountTransactions = this.transactionRepository.findAllByDateBetweenAndAccountIdInOrderByDateAsc(
                dateTransactionStart, 
                dateTransactionEnd,
                clientAccounts.stream().map(account -> {return account.getId();}
            ).collect(Collectors.toList())).stream().collect(Collectors.groupingBy(Transaction::getAccountId));
        
        clientAccounts.forEach(account -> {
            List<Transaction> transactions = accountTransactions.get(account.getId());
            transactions.forEach(transaction -> {
                report.add(new BankStatementDto(
                    transaction.getDate(),
                    clientName,
                    account.getNumber(), 
                    account.getType(),
                    account.getInitialAmount(), 
                    account.isActive(),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getBalance()
                ));
            });
        });
        return report;
    }

    @Override
    public TransactionDto getLastByAccountId(Long accountId) {
        // If you need it
        Optional<Transaction> lastTransaction = this.transactionRepository
                .findFirstByAccountIdOrderByIdDescDateDesc(accountId);
        return lastTransaction.isPresent() ? new TransactionDto(lastTransaction.get()) : null;
    }

}
