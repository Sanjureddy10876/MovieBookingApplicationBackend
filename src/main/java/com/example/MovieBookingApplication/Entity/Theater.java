package com.example.MovieBookingApplication.Entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "theaters")
public class Theater {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String theaterName;
	private String theaterLocation;
	private Integer theaterCapacity;
	private String theaterScreenType;
	private String location;
	
	@OneToMany(mappedBy = "theater", fetch = FetchType.LAZY)
	private List<Show> show;
}
