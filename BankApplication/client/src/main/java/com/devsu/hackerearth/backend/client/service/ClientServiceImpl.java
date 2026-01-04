package com.devsu.hackerearth.backend.client.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.client.configuration.CustomException;
import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;

	public ClientServiceImpl(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Override
	public List<ClientDto> getAll() {
		//Get all clients
		List<ClientDto> clients = this.clientRepository.findAll().stream().map(ClientDto::new).collect(Collectors.toList());
		return clients;
	}

	@Override
	public ClientDto getById(Long id) {
		// Get clients by id
		Client client = this.clientRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Cliente no encontrado."));
		return new ClientDto(client);
	}

	@Override
	public ClientDto create(ClientDto clientDto) {
		// Create client
		if(this.clientRepository.findByDni(clientDto.getDni()).isPresent()) {
			throw new CustomException(HttpStatus.CONFLICT, "Ya existe un cliente registrado con el mismo DNI.");
		}
		if(this.clientRepository.findByName(clientDto.getName()).isPresent()) {
			throw new CustomException(HttpStatus.CONFLICT, "Ya existe un cliente registrado con el mismo nombre.");
		}
		return new ClientDto(this.clientRepository.save(new Client(clientDto)));
	}

	@Override
	public ClientDto update(Long id, ClientDto clientDto) {
		// Update client
		if(this.clientRepository.findByDniAndIdNot(clientDto.getDni(), id).isPresent()) {
			throw new CustomException(HttpStatus.CONFLICT, "El DNI ingresado está registrado con otro cliente.");
		}
		if(this.clientRepository.findByNameAndIdNot(clientDto.getName(), id).isPresent()) {
			throw new CustomException(HttpStatus.CONFLICT, "El nombre ingresado está registrado con otro cliente.");
		}
		Client oldClient = this.clientRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Cliente no encontrado."));
		oldClient.setActive(clientDto.isActive());
		oldClient.setAddress(clientDto.getAddress());
		oldClient.setAge(clientDto.getAge());
		oldClient.setDni(clientDto.getDni());
		oldClient.setGender(clientDto.getGender());
		oldClient.setName(clientDto.getName());
		oldClient.setPassword(clientDto.getPassword());
		oldClient.setPhone(clientDto.getPhone());
		return new ClientDto(this.clientRepository.save(oldClient));
	}

	@Override
    public ClientDto partialUpdate(Long id, PartialClientDto partialClientDto) {
        // Partial update account
		Client oldClient = this.clientRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Cliente no encontrado."));
		oldClient.setActive(partialClientDto.isActive());
		return new ClientDto(this.clientRepository.save(oldClient));
    }

	@Override
	public void deleteById(Long id) {
		// Delete client
		Client oldClient = this.clientRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Cliente no encontrado."));
		this.clientRepository.delete(oldClient);
	}
}
