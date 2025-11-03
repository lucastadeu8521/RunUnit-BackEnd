package com.rununit.rununit.infrastructure.security;

import com.rununit.rununit.domain.entities.User;
import com.rununit.rununit.infrastructure.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration.hours}")
    private long EXPIRATION_HOURS;

    @Autowired
    private UserRepository userRepository;

    private SecretKey getSigningKey() {
        if (SECRET_KEY == null || SECRET_KEY.length() < 32) {
            logger.error("ERRO CRÍTICO: Chave secreta JWT não está configurada ou é muito curta. Tamanho atual: {}", SECRET_KEY != null ? SECRET_KEY.length() : 0);
        }
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        long expirationTimeMs = EXPIRATION_HOURS * 60 * 60 * 1000;

        String token = Jwts.builder()
                .subject(user.getLogin().getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTimeMs))
                .signWith(getSigningKey())
                .compact();

        logger.info("Token JWT gerado para o usuário {}. Duração: {} horas.", user.getLogin().getEmail(), EXPIRATION_HOURS);
        return token;
    }

    public boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            logger.warn("Tentativa de validar token nulo ou vazio.");
            return false;
        }
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            logger.debug("Token JWT validado com sucesso.");
            return true;
        } catch (SignatureException e) {
            logger.error("Falha na Validação (401): Assinatura JWT inválida. Chave de validação incorreta. Mensagem: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Falha na Validação (401): Token JWT malformado. Mensagem: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Falha na Validação (401): Token JWT expirado. Mensagem: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Falha na Validação (401): Token JWT não suportado. Mensagem: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Falha na Validação (401): Token JWT vazio ou inválido. Mensagem: {}", e.getMessage());
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public User extractUserFromToken(String token) {
        String email = getUsernameFromToken(token);
        logger.debug("Extraindo User a partir do email: {}", email);
        return userRepository.findByLoginEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado a partir do token"));
    }
}
