package com.devsu.hackerearth.backend.client.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.client.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    public Optional<Client> findByDni(String dni);

    public Optional<Client> findByDniAndIdNot(String dni, Long id);

    public Optional<Client> findByName(String name);

    public Optional<Client> findByNameAndIdNot(String dni, Long id);

}
