package com.devsu.hackerearth.backend.account.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
public class Transaction extends Base {

	private Date date;
	private String type;
	private double amount;
	private double balance;

	@Column(name = "account_id")
	private Long accountId;


	public Transaction(TransactionDto dto) {
		this.setDate(dto.getDate());
		this.setType(dto.getType());
		this.setBalance(dto.getBalance());
		this.setAmount(dto.getAmount());
		this.setAccountId(dto.getAccountId());
	}
}
