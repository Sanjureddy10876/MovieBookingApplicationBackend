package com.example.MovieBookingApplication.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.MovieBookingApplication.Entity.Movie;

@Repository 
public interface MovieRepository extends JpaRepository<Movie, Long> {
	
	public Optional<List<Movie>> findByGenre(String genre);
	
	public Optional<List<Movie>> findByLanguage(String language);
	
	public Optional<Movie> findByName(String title);

}
