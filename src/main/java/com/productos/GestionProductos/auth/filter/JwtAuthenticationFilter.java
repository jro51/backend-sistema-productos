package com.productos.GestionProductos.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productos.GestionProductos.auth.TokenJwtConfig;
import com.productos.GestionProductos.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.productos.GestionProductos.auth.TokenJwtConfig.SECRET_KEY;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    //Este metodo sirve para hacer el intento de autenticacion mediante la request y al final en el return
    //hace el login, o sea esto llama al JpaUserDetailsService donde ahi retorna el User de Spring Security
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = null;
        String password = null;

        try {
            Usuario usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
            username = usuario.getUsername();
            password = usuario.getPassword();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return this.authenticationManager.authenticate(authenticationToken);
    }

    //Este metodo se ejecuta cuando todo salio bien, o sea que hizo un login correcto y debemos de obtener
    //los datos del User de Spring Security además de generar el token y el JSON. Ahora esto debemos de agregar
    //en nuestro SpringSecurityConfig en el addFilter añadir esta misma clase.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String username = user.getUsername();

        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("authorities", new ObjectMapper().writeValueAsString(roles));
        claimsMap.put("username", username);

        Claims claims = Jwts.claims(claimsMap);

        SecretKey key = SECRET_KEY;

        //Aqui creamos el JWT con la llave secreta
        String jwt = Jwts
                .builder()
                .setSubject(username)
                .addClaims(claims)
                .signWith(key)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .compact();

        response.addHeader("Authorization","Bearer " + jwt);

        //Creamos el cuerpo de nuestro JSON
        Map<String, String> body = new HashMap<>();
        body.put("token", jwt);
        body.put("username",username);
        body.put("message","Iniciaste sesion con exitooo!");

        //Generamos la respuesta JSON
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType("application/json");
        response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        //Creamos el cuerpo de nuestro JSON
        Map<String, String> body = new HashMap<>();
        body.put("message","Credenciales incorrectas!");
        body.put("error", failed.getMessage());

        //Generamos la respuesta JSON
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType("application/json");
        response.setStatus(401);
    }
}
