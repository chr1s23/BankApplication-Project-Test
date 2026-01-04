package com.devsu.hackerearth.backend.account.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.devsu.hackerearth.backend.account.model.dto.AccountDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
public class Account extends Base {
    private String number;
	private String type;
	private double initialAmount;
	private boolean isActive;

    @Column(name = "client_id")
    private Long clientId;

    public Account(AccountDto dto) {
        this.setNumber(dto.getNumber());
        this.setType(dto.getType());
        this.setInitialAmount(dto.getInitialAmount());
        this.setActive(dto.isActive());
        this.setClientId(dto.getClientId());
    }
}
