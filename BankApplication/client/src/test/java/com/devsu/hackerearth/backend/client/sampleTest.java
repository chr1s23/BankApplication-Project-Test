package com.devsu.hackerearth.backend.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.devsu.hackerearth.backend.client.configuration.CustomException;
import com.devsu.hackerearth.backend.client.controller.ClientController;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class sampleTest {

	private ClientService clientService = mock(ClientService.class);
	private ClientController clientController = new ClientController(clientService);

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createClientTest() {
        // Arrange
        ClientDto newClient = new ClientDto(1L, "Dni", "Name", "Password", "Gender", 1, "Address", "9999999999", true);
        ClientDto createdClient = new ClientDto(1L, "Dni", "Name", "Password", "Gender", 1, "Address", "9999999999", true);
        when(clientService.create(newClient)).thenReturn(createdClient);

        // Act
        ResponseEntity<ClientDto> response = clientController.create(newClient);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdClient, response.getBody());
    }

    @Test
    void getClientTest() {
        //Arrange
        ClientDto existingClient = new ClientDto(1L, "Dni", "Name", "Password", "Gender", 1, "Address", "9999999999", true);
        when(clientService.getById(1L)).thenReturn(existingClient);

        //Act
        ResponseEntity<ClientDto> response = clientController.get(1L);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(existingClient, response.getBody());

        verify(clientService).getById(1L);
    }

    @Test
    void getClientTestException() {
        //Arrange
        when(clientService.getById(1L)).thenThrow(new CustomException(HttpStatus.NOT_FOUND, "Cliente no encontrado."));

        //Act
        try {
            ResponseEntity<ClientDto> response = clientController.get(1L);
        } catch (CustomException e) {
            //Assert
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
            assertTrue(e.getMessage().contains("Cliente no encontrado"));
        }     

        verify(clientService).getById(1L);
    }

    @Test
    void getAllClientsIntegrationTest() throws Exception {
        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8001/api/clients")).andReturn();

        //Assert
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void createClientIntegrationTest() throws Exception {
        //Arrange
        ClientDto newClient = new ClientDto(1L, "Dni", "Name", "Password", "Gender", 1, "Address", "9999999999", true);
        ObjectMapper mapper = new ObjectMapper();
        String request = mapper.writeValueAsString(newClient);

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8001/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request)).andReturn();

        //Assert
        assertEquals(201, result.getResponse().getStatus());
    }
}
