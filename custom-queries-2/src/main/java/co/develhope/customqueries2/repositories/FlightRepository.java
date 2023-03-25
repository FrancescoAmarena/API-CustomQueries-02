package co.develhope.customqueries2.repositories;

import co.develhope.customqueries2.entities.Flight;
import co.develhope.customqueries2.entities.FlightStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByStatus(FlightStatus status);

    List<Flight> findByStatusIn(List<FlightStatus> statuses);

    @Query("SELECT f FROM Flight f WHERE f.status = ?1 OR f.status = ?2")
    List<Flight> findCustomFlights(FlightStatus p1, FlightStatus p2);

}
