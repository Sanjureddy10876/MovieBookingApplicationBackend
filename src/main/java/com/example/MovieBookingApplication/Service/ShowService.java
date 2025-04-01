package com.example.MovieBookingApplication.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.MovieBookingApplication.DTO.ShowDTO;
import com.example.MovieBookingApplication.Entity.Movie;
import com.example.MovieBookingApplication.Entity.Show;
import com.example.MovieBookingApplication.Entity.Theater;
import com.example.MovieBookingApplication.Repository.MovieRepository;
import com.example.MovieBookingApplication.Repository.ShowRepository;
import com.example.MovieBookingApplication.Repository.TheaterRepository;

@Service
public class ShowService {
	
	
	@Autowired
	private ShowRepository showRepository;

	@Autowired
	private TheaterRepository theaterRepository;
	
	@Autowired
	private MovieRepository movieRepository;
	
	
	public Show createShow(ShowDTO showDTO) {
		Theater theater = theaterRepository.findById(showDTO.getTheaterId())
				.orElseThrow(()-> new RuntimeException("No Theater found"+showDTO.getTheaterId()));
		
		Movie movie = movieRepository.findById(showDTO.getMovieId())
				.orElseThrow(()-> new RuntimeException("No Movies found"+showDTO.getMovieId()));
		
		Show show = new Show();
		show.setShowTime(showDTO.getShowTime());
		show.setPrice(showDTO.getPrice());
		show.setMovie(movie);
		show.setTheater(theater);
		return showRepository.save(show);
		
	}
	
	public List<Show> getAllShows() {
		return showRepository.findAll();
		
	}
	
	public List<Show> getShowsByMovie(Long movieid){
		Optional<List<Show>> showListBox = showRepository.findByMovieId(movieid);
		if(showListBox.isPresent()) {
			return showListBox.get();
		} else {
			throw new RuntimeException("No shows available for the movie");
		}
		
	}
	
	public List<Show> getShowsByTheater(Long theaterid){
		Optional<List<Show>> showListBox = showRepository.findByTheaterId(theaterid);
		if(showListBox.isPresent()) {
			return showListBox.get();
			
		} else {
			throw new RuntimeException("No shows avaliable for the theater");
		}
		
	}

	public Show updateShow(Long id, ShowDTO showDTO){
		
		Show show = showRepository.findById(id)
				.orElseThrow(()-> new RuntimeException("No show found for the id"+id));
		Movie movie = movieRepository.findById(id)
				.orElseThrow(()-> new RuntimeException("No Movie found for the id "+id));
		Theater theater = theaterRepository.findById(id)
				.orElseThrow(()-> new RuntimeException("No Theater found for the id"+id));
		
		show.setShowTime(showDTO.getShowTime());
		show.setPrice(showDTO.getPrice());
		show.setMovie(movie);
		show.setTheater(theater);
		
	return showRepository.save(show);
	}
	
	public void deleteShow(Long id) {
		if(!showRepository.existsById(id)) {
			throw new RuntimeException("No Shows avalible for the id"+id); 
		}
		if(!theaterRepository.existsById(id)) {
			throw new RuntimeException("Can't delete show with existing bookings");
		}
		showRepository.deleteById(id);
	}
}
