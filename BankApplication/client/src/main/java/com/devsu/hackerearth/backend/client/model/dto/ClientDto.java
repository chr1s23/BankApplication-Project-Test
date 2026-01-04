package com.devsu.hackerearth.backend.client.model.dto;

import com.devsu.hackerearth.backend.client.model.Client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientDto {

	private Long id;
	private String dni;
	private String name;
	private String password;
	private String gender;
	private int age;
	private String address;
	private String phone;
	private boolean isActive;

	public ClientDto(Client client) {
		this.id = client.getId();
		this.name = client.getName();
		this.dni = client.getDni();
		this.password = client.getPassword();
		this.gender = client.getGender();
		this.age = client.getAge();
		this.address = client.getAddress();
		this.phone = client.getPhone();
		this.isActive = client.isActive();
	}
}
