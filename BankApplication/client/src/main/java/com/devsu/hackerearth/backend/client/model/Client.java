package com.devsu.hackerearth.backend.client.model;

import javax.persistence.Entity;

import com.devsu.hackerearth.backend.client.model.dto.ClientDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
public class Client extends Person {
	private String password;
	private boolean isActive;

	public Client(ClientDto dto) {
		this.setDni(dto.getDni());
		this.setName(dto.getName());
		this.setAddress(dto.getAddress());
		this.setActive(dto.isActive());
		this.setAge(dto.getAge());
		this.setPassword(dto.getPassword());
		this.setGender(dto.getGender());
		this.setPhone(dto.getPhone());
	}
}
