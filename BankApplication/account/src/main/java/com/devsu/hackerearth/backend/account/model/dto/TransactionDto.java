package com.devsu.hackerearth.backend.account.model.dto;

import java.util.Date;

import com.devsu.hackerearth.backend.account.model.Transaction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionDto {

	private Long id;
    private Date date;
	private String type;
	private double amount;
	private double balance;
	private Long accountId;

	public TransactionDto(Transaction transaction) {
		this.id = transaction.getId();
		this.date = transaction.getDate();
		this.type = transaction.getType();
		this.amount = transaction.getAmount();
		this.balance = transaction.getBalance();
		this.accountId = transaction.getAccountId();
	}
}
