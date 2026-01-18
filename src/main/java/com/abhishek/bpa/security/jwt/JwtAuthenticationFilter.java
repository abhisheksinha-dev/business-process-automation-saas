package com.abhishek.bpa.security.jwt;

import com.abhishek.bpa.security.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        //if no token then continue it will be blocked later by security
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        Claims claims;
        try{
            claims = jwtService.extractClaims(token);
        } catch (Exception e) {
            //Invalid token, skip authentication
            filterChain.doFilter(request, response);
            return;
        }

        // Extract data from token
        UUID userId = UUID.fromString(claims.getSubject());
        UUID organizationId = UUID.fromString((String) claims.get("orgId"));
        String email = (String) claims.get("email");
        String role = (String) claims.get("role");

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(userId, organizationId, email, role);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

        //Create Authentication Object
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticatedUser,
                null,
                authorities
        );

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        //Set authenticated user in security context
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
