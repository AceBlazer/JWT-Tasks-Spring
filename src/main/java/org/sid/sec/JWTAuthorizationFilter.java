package org.sid.sec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;




import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;




import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;




import com.sun.javafx.collections.MappingChange.Map;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest req,
			HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {

		
		
		res.addHeader("Access-Control-Allow-Origin", "*");
		res.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization");
		res.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, authorization");
		
		
		
		if(req.getMethod().equals("OPTIONS")) {
			res.setStatus(HttpServletResponse.SC_OK);
		}
		
		else {
			

			String jwtToken = req.getHeader(SecurityConstants.HEADER_STRING);
			System.out.println(jwtToken);
			if(jwtToken == null || !jwtToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
				chain.doFilter(req, res); 
				return;
			} 
			
			Claims claims=Jwts.parser()
			.setSigningKey(SecurityConstants.SECRET)
			.parseClaimsJws(jwtToken.replace(SecurityConstants.TOKEN_PREFIX, ""))
			.getBody();
			String username = claims.getSubject();
			ArrayList<java.util.Map<String,String>> roles = (ArrayList<java.util.Map<String,String>>) 
			claims.get("roles");
			Collection<GrantedAuthority> auths = new ArrayList<>();
			roles.forEach(r-> {
				auths.add(new SimpleGrantedAuthority(r.get("authority")));
			});
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null,auths);
			SecurityContextHolder.getContext().setAuthentication(authToken);
			chain.doFilter(req, res);
			
			
		}
		
		
		
		
		
	}

}
