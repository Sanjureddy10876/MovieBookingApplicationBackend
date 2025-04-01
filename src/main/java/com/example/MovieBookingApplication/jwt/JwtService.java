package com.example.MovieBookingApplication.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.MovieBookingApplication.Entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${jwt.secret}")
	private String secretKey;
	
	@Value("${jwt.expiration}")
	private Long jwtexpiraction;
	

	public String extractUsername(String jwtToken) {
		
		return extractClaim(jwtToken, Claims::getSubject);
		
	}
	
	public boolean isTokenValid(String jwtToken, User userdetails) {
		// TODO Auto-generated method stub
		return false;
	}

	private <T> T extractClaim(String jwtToken, Function<Claims, T> claimResolver) {
		
		final Claims claims = exactAllClaims(jwtToken);
		return claimResolver.apply(claims);
		
	}
	
	private Claims exactAllClaims(String jwtToken) {
		return Jwts
					.parser()
					.verifyWith(getSigninKey())
					.build()
					.parseSignedClaims(jwtToken)
					.getPayload();
		
	}
	public SecretKey getSigninKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}
	
	public String generateToken(Map<String, Object> extaClaims, UserDetails userDetails) { 
		
		return Jwts
					.builder()
					.claims(extaClaims)
					.subject(userDetails.getUsername())
					.issuedAt(new Date(System.currentTimeMillis()))
					.expiration(new Date(System.currentTimeMillis()+jwtexpiraction))
					.signWith(getSigninKey())
					.compact();
	}
	
	public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
		final String username = extractUsername(jwtToken); 
		return (userDetails.getUsername().equals(username) && isTokenExpired(jwtToken));
	}
	private boolean isTokenExpired(String jwtToken) {
		return extractExpiration(jwtToken).before(new Date());
		
	}
	
	private Date extractExpiration(String jwtToken) {
		return extractClaim(jwtToken, Claims::getExpiration);
		
	}
}


