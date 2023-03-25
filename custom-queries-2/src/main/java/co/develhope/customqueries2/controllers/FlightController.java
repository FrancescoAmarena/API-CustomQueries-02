package co.develhope.customqueries2.controllers;

import co.develhope.customqueries2.entities.Flight;
import co.develhope.customqueries2.entities.FlightStatus;
import co.develhope.customqueries2.repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private FlightRepository flightRepository;

    @GetMapping("/provision")
    public ResponseEntity<String> provisionFlights(@RequestParam(required = false, defaultValue = "100") int n) {
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            Flight flight = new Flight();
            flight.setFromAirport(generateRandomString(3, random));
            flight.setToAirport(generateRandomString(3, random));
            flight.setFlightNumber(generateRandomString(6, random));
            flight.setFlightStatus(generateRandomStatus(random));
            flightRepository.save(flight);
        }
        return ResponseEntity.ok("Provisioning completed");
    }

    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("fromAirport").ascending());
        Page<Flight> flights = flightRepository.findAll(paging);
        return ResponseEntity.ok(flights.getContent());
    }

    @GetMapping("/ontime")
    public ResponseEntity<List<Flight>> getOnTimeFlights() {
        List<Flight> flights = flightRepository.findByStatus(Status.ONTIME);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/custom")
    public ResponseEntity<List<Flight>> getCustomFlights(@RequestParam String p1, @RequestParam String p2) {
        List<Flight> flights = flightRepository.findCustomFlights(p1, p2);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/delayed-or-cancelled")
    public ResponseEntity<List<Flight>> getDelayedOrCancelledFlights() {
        List<Flight> flights = flightRepository.findByStatusIn(Arrays.asList(Status.DELAYED, Status.CANCELLED));
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/on-time-or-delayed")
    public ResponseEntity<List<Flight>> getOnTimeOrDelayedFlights() {
        List<Flight> flights = flightRepository.findByStatusIn(Arrays.asList(Status.ONTIME, Status.DELAYED));
        return ResponseEntity.ok(flights);
    }

    private String generateRandomString(int length, Random random) {
        return random.ints(97, 123)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private Status generateRandomStatus(Random random) {
        int index = random.nextInt(Status.values().length);
        return Status.values()[index];
    }

}
