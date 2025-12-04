package com.productos.GestionProductos.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productos.GestionProductos.auth.SimpleGrantedAuthorityJsonCreator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.productos.GestionProductos.auth.TokenJwtConfig.SECRET_KEY;

public class JwtValidationFilter extends BasicAuthenticationFilter {

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    //Este metodo nos sirve para que al momento de realizar alguna consulta como crear o editar, mediante el token
    //valide que sea correcto para que se lleve acabo las operaciones que uno debe hacer.
    //
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer ")){
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            String username = claims.getSubject();
            Object authoritiesClaims = claims.get("authorities");

            Collection<? extends GrantedAuthority> roles = Arrays.asList(new ObjectMapper()
                    //Lo que hace el addMixIn es combinar el constructor del SimpleGrantedAuthorityJsonCreator con
                    //el SimpleGrantedAuthority, ya que nos pide que sea exactamente "role"
                    .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                    .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class)) ;

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, roles);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);
        }catch (JwtException e){
            Map<String, String> body = new HashMap<>();
            body.put("message", "El token es invalido");
            body.put("error", e.getMessage());

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(401);
            response.setContentType("application/json");
        }
    }
}
