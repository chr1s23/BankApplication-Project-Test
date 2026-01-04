package com.devsu.hackerearth.backend.account.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.devsu.hackerearth.backend.account.configuration.CustomException;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.ClientDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;

import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final WebClient webClient;

    public AccountServiceImpl(AccountRepository accountRepository, WebClient webClient) {
        this.accountRepository = accountRepository;
        this.webClient = webClient;
    }

    @Override
    public List<AccountDto> getAll() {
        // Get all accounts
        List<AccountDto> accounts = this.accountRepository.findAll().stream().map(AccountDto::new)
                .collect(Collectors.toList());
        return accounts;
    }

    @Override
    public AccountDto getById(Long id) {
        // Get accounts by id
        Account account = this.accountRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Cuenta no encontrada."));
        return new AccountDto(account);
    }

    @Override
    public AccountDto create(AccountDto accountDto) {
        // Create account
        if (this.accountRepository.findByNumber(accountDto.getNumber()).isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT, "El número de cuenta ya está registrado.");
        }
        if (accountDto.getInitialAmount() < 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "El saldo inicial de la cuenta no puede ser menor a 0.");
        }
        Mono<ClientDto> clientDto = webClient.get().uri("/api/clients/{id}", accountDto.getClientId()).retrieve().bodyToMono(ClientDto.class);
        if (clientDto.block() == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Cliente no encontrado.");
        }
        return new AccountDto(this.accountRepository.save(new Account(accountDto)));
    }

    @Override
    public AccountDto update(AccountDto accountDto) {
        // Update account
        if (this.accountRepository.findByNumberAndIdNot(accountDto.getNumber(), accountDto.getId()).isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT,
                    "El número de cuenta ingresado está asociado con otra cuenta.");
        }
        if (accountDto.getInitialAmount() < 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "El saldo inicial de la cuenta no puede ser menor a 0.");
        }
        Mono<ClientDto> clientDto = webClient.get().uri("/api/clients/{id}", accountDto.getClientId()).retrieve().bodyToMono(ClientDto.class);
        if (clientDto.block() == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Cliente no encontrado.");
        }
        Account oldAccount = this.accountRepository.findById(accountDto.getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Cuenta no encontrada."));
        oldAccount.setActive(accountDto.isActive());
        oldAccount.setClientId(accountDto.getClientId());
        oldAccount.setInitialAmount(accountDto.getInitialAmount());
        oldAccount.setNumber(accountDto.getNumber());
        oldAccount.setType(accountDto.getType());
        return new AccountDto(this.accountRepository.save(oldAccount));
    }

    @Override
    public AccountDto partialUpdate(Long id, PartialAccountDto partialAccountDto) {
        // Partial update account
        Account oldAccount = this.accountRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Cuenta no encontrada."));
        oldAccount.setActive(partialAccountDto.isActive());
        return new AccountDto(this.accountRepository.save(oldAccount));
    }

    @Override
    public void deleteById(Long id) {
        // Delete account
        Account oldAccount = this.accountRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Cliente no encontrado."));;
        this.accountRepository.delete(oldAccount);
    }

}
