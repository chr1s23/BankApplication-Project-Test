package com.devsu.hackerearth.backend.account.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportDto {
    private Long id;
    private String number;
	private String type;
	private double initialAmount;
	private boolean isActive;
    private Long client;
    private List<TransactionDto> transactions;
}
