package dev.hotel.Contoller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.hotel.entite.Reservation;
import dev.hotel.repository.ClientRepository;
import dev.hotel.repository.ReservationRepository;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
	private ReservationRepository reservationRepository;

	public ReservationController(ClientRepository clientRepository) {
		super();
		this.reservationRepository = reservationRepository;
	}

	@PostMapping
	public ResponseEntity<?> postResevation(@RequestBody Reservation reserv) {

		Reservation reserv1 = new Reservation(reserv.getDateDebut(), reserv.getDateFin(), reserv.getClient(),
				reserv.getChambres());
		this.reservationRepository.save(reserv1);

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

}
