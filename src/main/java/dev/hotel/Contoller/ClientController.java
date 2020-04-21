package dev.hotel.Contoller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import antlr.StringUtils;
import dev.hotel.entite.Client;
import dev.hotel.entite.Reservation;
import dev.hotel.repository.ClientRepository;

@RestController
@RequestMapping("/clients")
public class ClientController {

	private ClientRepository clientRepository;

	public ClientController(ClientRepository clientRepository) {
		super();
		this.clientRepository = clientRepository;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/lister")
	public List<Client> listerClients(@RequestParam("start") Integer start, @RequestParam("size") Integer size) {
		return clientRepository.findAll(PageRequest.of(start, size)).toList();
	}

	/// Pour test manuellement request :
	/// http://localhost:8080/clients/UUID?uuid=dcf129f1a2f947dc82651d844244b192 ou
	/// dcf129f1-a2f9-47dc-8265-1d844244b192
	@GetMapping("{uuid}")
	public ResponseEntity<Client> clientByUUID(@PathVariable UUID uuid) {
		Optional<Client> clt = clientRepository.findById(uuid);
		if (clt.isPresent()) {
			return ResponseEntity.ok().body(clt.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PostMapping
	public ResponseEntity<?> postClient(@RequestBody Client client) {

		if (client.getNom().length() < 2 || client.getPrenoms().length() < 2) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
					.body("Le Nom et Le Prenom doivent contenir plus de 2 Charactere");
		} else {
			clientRepository.save(client);
			return ResponseEntity.status(HttpStatus.ACCEPTED).header("verifNomPrenom", "Le Client est valide").body(client);

		}
	}

}
