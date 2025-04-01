package com.example.MovieBookingApplication.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.MovieBookingApplication.DTO.TheaterDTO;
import com.example.MovieBookingApplication.Entity.Theater;
import com.example.MovieBookingApplication.Repository.TheaterRepository;

@Service
public class TheaterService {
	
	@Autowired
	private TheaterRepository theaterRepository;
	
	public Theater addTheater(TheaterDTO theaterDTO) {
		
		Theater theater = new Theater();
		theater.setTheaterName(theaterDTO.getTheaterName());
		theater.setTheaterCapacity(theaterDTO.getTheaterCapacity());
		theater.setTheaterLocation(theaterDTO.getTheaterLocation());
		theater.setTheaterScreenType(theaterDTO.getTheaterScreenType());
		
		return theaterRepository.save(theater);
		
		
	}
	
	public List<Theater> getTheaterByLocation(String location){
	Optional<List<Theater>> listofTheaterBox = theaterRepository.findByLocation(location);
		if(listofTheaterBox.isPresent()) {
			return listofTheaterBox.get();
		} else throw new RuntimeException("No theaters found for the location"+location);
	}
	
	public Theater updateTheater(Long id, TheaterDTO theaterDTO) {
		Theater theater = theaterRepository.findById(id)
				.orElseThrow(()-> new RuntimeException("No theater found the id"+id)); 
		theater.setTheaterName(theaterDTO.getTheaterName());
		theater.setTheaterCapacity(theaterDTO.getTheaterCapacity());
		theater.setTheaterLocation(theaterDTO.getTheaterLocation());
		theater.setTheaterScreenType(theaterDTO.getTheaterScreenType());
		
		return theaterRepository.save(theater);
	}
	
	public void deleteTheater(Long id) {
		theaterRepository.deleteById(id);
	}
	

}
