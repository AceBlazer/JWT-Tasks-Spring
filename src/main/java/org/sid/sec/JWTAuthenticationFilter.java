package org.sid.sec;

import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sid.entities.AppUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	
	private AuthenticationManager am;
	
	
	
	public JWTAuthenticationFilter(AuthenticationManager am) {
		super();
		this.am = am;
	}


	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		
		AppUser user = null;
		try{
			user = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		System.out.println("****************");
		System.out.println("un: "+user.getUsername());
		System.out.println("pw: "+user.getPassword());
		return am.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
	}
	
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		User springUser=(User)authResult.getPrincipal();
		
		//generer le token
		String jwtToken= Jwts.builder()
				.setSubject(springUser.getUsername())
				.setExpiration(new Date (System.currentTimeMillis()+SecurityConstants.EXPIRATION_TIME))
				.signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, SecurityConstants.SECRET)
				.claim("roles",springUser.getAuthorities())
				.compact();
		
		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX+jwtToken);
		System.out.println("success");
	}
	
}
