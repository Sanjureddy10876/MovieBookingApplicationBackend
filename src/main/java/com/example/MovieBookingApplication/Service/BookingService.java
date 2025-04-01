package com.example.MovieBookingApplication.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.MovieBookingApplication.DTO.BookingDTO;
import com.example.MovieBookingApplication.Entity.Booking;
import com.example.MovieBookingApplication.Entity.BookingStatus;
import com.example.MovieBookingApplication.Entity.Show;
import com.example.MovieBookingApplication.Entity.User;
import com.example.MovieBookingApplication.Repository.BookingRepository;
import com.example.MovieBookingApplication.Repository.ShowRepository;
import com.example.MovieBookingApplication.Repository.UserRepository;

@Service
public class BookingService {
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private ShowRepository showRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Booking createBooking(BookingDTO bookingDTO) {
		
		Show show = showRepository.findById(bookingDTO.getShowId())
				.orElseThrow(()-> new RuntimeException("Show not found"));
		
		if(!isSeatsAvaliable(show.getId(), bookingDTO.getNumberOfSeats())) {
			throw new RuntimeException("Not enough seat are avalible");
			
		}
		
		if(bookingDTO.getSeatNumbers().size()!= bookingDTO.getNumberOfSeats()) {
			throw new RuntimeException("seat Numbers and Numbers of seats must be equal");
		}
		
		validateDuplicateSeats(show.getId(), bookingDTO.getSeatNumbers());
		
		User user = userRepository.findById(bookingDTO.getUserId())
								  .orElseThrow(()-> new RuntimeException("User not found"));
		Booking booking = new Booking();
		booking.setUser(user);
		booking.setShow(show);
		booking.setNumberOfSeats(bookingDTO.getNumberOfSeats());
		booking.setPrice(calculateTotalAmount(show.getPrice(), bookingDTO.getNumberOfSeats()));
		booking.setBookingTime(LocalDateTime.now());
		booking.setBookingStatus(BookingStatus.PENDING);
		
		return bookingRepository.save(booking);
		
		
	}
	
	public List<Booking> getUserBookings(Long userid){
		return bookingRepository.findByUserId(userid);
	}
	
	public List<Booking> getShowBookings(Long showid){
		return bookingRepository.findByShowId(showid);
	}
	public Booking confirmBooking(Long bookingid) {

		Booking booking = bookingRepository.findById(bookingid)
							.orElseThrow(()-> new RuntimeException("No comfimed booking are the by id"+bookingid));
		if(booking.getBookingStatus()!= BookingStatus.PENDING) {
			throw new RuntimeException("Booking is not in pending");
		}
		
		//PayMent Process
		booking.setBookingStatus(BookingStatus.CONFIRMED);
		
		return bookingRepository.save(booking);
	}
	
	public Booking cancelBooking(Long bookingid) {
		Booking booking = bookingRepository.findById(bookingid) 
				.orElseThrow(()-> new RuntimeException("Booking not found"));
		
		validateCancellation(booking);
		booking.setBookingStatus(BookingStatus.CANCELLED);
		return bookingRepository.save(booking);
	}
	
	public List<Booking> getBookingsByStatus(BookingStatus bookingStatus){
		return bookingRepository.findByBookingStatus(bookingStatus);
	}
	
	public void validateCancellation(Booking booking) {
		LocalDateTime showTime = booking.getShow().getShowTime();
		LocalDateTime deadlineTime = showTime.minusHours(2);
		
		if(LocalDateTime.now().isAfter(deadlineTime)) {
			throw new RuntimeException("Cannot cancel the booking");
		}
		if(booking.getBookingStatus()== BookingStatus.CANCELLED ) {
			throw new RuntimeException("Booking Already Cancelled");
		}
		
	}
	
	public boolean isSeatsAvaliable(Long showid, Integer numberOfSeats) {

		Show show = showRepository.findById(showid)
				.orElseThrow(()-> new RuntimeException("Show not found"));
		int bookedSeats = show.getBookings().stream()
				.filter(booking -> booking.getBookingStatus()!= BookingStatus.CANCELLED)
				.mapToInt(Booking::getNumberOfSeats)
				.sum();
		
		return (show.getTheater().getTheaterCapacity() - bookedSeats)>= numberOfSeats;
		
	}
	
	public void validateDuplicateSeats(Long showId, List<String> seatNumbers) {
		Show show = showRepository.findById(showId)
				.orElseThrow(()-> new RuntimeException("Show not found"));
		
		Set<String> occupiedSeats = show.getBookings().stream()
										.filter(b->b.getBookingStatus()!= BookingStatus.CANCELLED)
										.flatMap(b->b.getSeatNumbers().stream())
										.collect(Collectors.toSet());
		
		List<String> duplicateSeats = seatNumbers.stream()
												 .filter(occupiedSeats::contains)
												 .collect(Collectors.toList());
		if(!duplicateSeats.isEmpty()) {
			throw new RuntimeException("Seats are already booked");
		}
		
	}
	
	public Double calculateTotalAmount(Double price, Integer numberOfSeats) {
		return price * numberOfSeats;
		
	}
	

}
