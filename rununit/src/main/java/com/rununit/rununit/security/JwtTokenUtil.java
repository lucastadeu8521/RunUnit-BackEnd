package com.rununit.rununit.security;

import com.rununit.rununit.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    private final String SECRET_KEY = "meuSegredo123"; // de preferência usar no application.properties

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // expira em 1h
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
//ok