package com.devsu.hackerearth.backend.account.model.dto;

import java.util.Date;

import com.devsu.hackerearth.backend.account.model.Transaction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankStatementDto {
    
	private Date date;
	private String client;
	private String accountNumber;
	private String accountType;
	private double initialAmount;
    private boolean isActive;
	private String transactionType;
	private double amount;
	private double balance;

	public BankStatementDto(Transaction transaction) {
		this.date = transaction.getDate();
		this.transactionType = transaction.getType();
		this.amount = transaction.getAmount();
		this.balance = transaction.getBalance();
	}
}
