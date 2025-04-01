package com.example.MovieBookingApplication.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MovieBookingApplication.Entity.Theater;

public interface TheaterRepository extends JpaRepository<Theater, Long>{

	Optional<List<Theater>> findByLocation(String location);
}
