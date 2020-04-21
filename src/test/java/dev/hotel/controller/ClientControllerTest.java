package dev.hotel.controller;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.id.UUIDGenerator;
import org.hibernate.type.UUIDBinaryType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import dev.hotel.Contoller.ClientController;
import dev.hotel.entite.Client;
import dev.hotel.repository.ClientRepository;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	ClientRepository clientRepository;

	@Test
	void testListerClients() throws Exception {
		List<Client> listMock = Arrays.asList(new Client("Chaussette", "Semelle"));
		Mockito.when(clientRepository.findAll(PageRequest.of(0, 1))).thenReturn(new PageImpl<>(listMock));

		mockMvc.perform(MockMvcRequestBuilders.get("/clients/lister?start=0&size=1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].nom").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].nom").value("Chaussette"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].prenoms").value("Semelle"));
	}

	@Test
	void testClientByUUID() throws Exception {
		UUID uuid = UUID.fromString("dcf129f1-a2f9-47dc-8265-1d844244b192");
		Mockito.when(clientRepository.findById(uuid)).thenReturn(Optional.of(new Client("Odd", "Ross")));

		mockMvc.perform(MockMvcRequestBuilders.get("/clients/dcf129f1-a2f9-47dc-8265-1d844244b192"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.nom").value("Odd"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.prenoms").value("Ross"));
	}

	@Test
	void testPostClient() throws Exception {
		String jsonBodyTrue = "{\"nom\" : \"Chaussette\",\"prenoms\" : \"Semelle\"}";
		String jsonBodyFalse = "{\"nom\" : \"C\",\"prenoms\" : \"Semelle\"}";
		mockMvc.perform(
				MockMvcRequestBuilders.post("/clients").contentType(MediaType.APPLICATION_JSON).content(jsonBodyTrue))
				.andExpect(MockMvcResultMatchers.status().isAccepted())
				.andExpect(MockMvcResultMatchers.header().stringValues("verifNomPrenom", "Le Client est valide"));
		mockMvc.perform(
				MockMvcRequestBuilders.post("/clients").contentType(MediaType.APPLICATION_JSON).content(jsonBodyFalse))
				.andExpect(MockMvcResultMatchers.status().isNotAcceptable()).andExpect(MockMvcResultMatchers.content()
						.string("Le Nom et Le Prenom doivent contenir plus de 2 Charactere"));
	}

}
