package com.devsu.hackerearth.backend.account.model.dto;

import com.devsu.hackerearth.backend.account.model.Account;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDto {

	private Long id;
	private String number;
	private String type;
	private double initialAmount;
	private boolean isActive;
	private Long clientId;

	public AccountDto(Account account) {
		this.id = account.getId();
		this.number = account.getNumber();
		this.type = account.getType();
		this.initialAmount = account.getInitialAmount();
		this.isActive = account.isActive();
		this.clientId = account.getClientId();
	}
}
