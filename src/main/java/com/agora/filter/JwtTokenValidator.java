package com.agora.filter;

import com.agora.util.JwtUtils;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// authorities parsing removed; using UserDetails authorities instead
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
 

public class JwtTokenValidator extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService; // agregado

    public JwtTokenValidator(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader("Authorization");

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);

            try {
                DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);

                String username = jwtUtils.extractUsername(decodedJWT);
                // Safely extract authorities claim (may be null)
                String stringAuthorities = "";
                try {
                    var claim = jwtUtils.getSpecificClaim(decodedJWT, "authorities");
                    if (claim != null && claim.asString() != null) {
                        stringAuthorities = claim.asString();
                    }
                } catch (Exception ignored) {
                    // leave empty authorities if parsing fails
                }

                // If you wanted to use authorities from token, you can parse them here.

                // DEBUG
                System.out.println("===== JWT VALIDATOR DEBUG =====");
                System.out.println("Token recibido: " + jwtToken);
                System.out.println("Email extraído del token: " + username);
                System.out.println("Authorities (token): " + (stringAuthorities == null || stringAuthorities.isBlank() ? "<none>" : stringAuthorities));
                System.out.println("================================");

                // Cargar UserDetails real y ponerlo como principal (proteger errores)
                UserDetails userDetails;
                try {
                    userDetails = userDetailsService.loadUserByUsername(username);
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado o token inválido");
                    return;
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);

            } catch (JWTVerificationException e) {
                // Manejar token inválido
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
                return;
            } catch (Exception e) {
                // Cualquier otro error en el parsing del token o validación -> devolver 401 en lugar de propagar 500
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error validando token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}

