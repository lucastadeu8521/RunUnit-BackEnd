package com.rununit.rununit.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/", "/favicon.ico", "/api/auth", "/v3/api-docs",
            "/swagger-ui", "/swagger-ui.html", "/h2-console"
    );

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        logger.debug("Tentativa de acesso ao caminho: {}", path);

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(publicPath ->
                path.equals(publicPath) || path.startsWith(publicPath + "/")
        );

        if (isPublic) {
            logger.debug("Caminho {} é público, pulando filtro de autenticação.", path);
        }
        return isPublic;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (token == null) {
            logger.debug("Nenhum token JWT encontrado no cabeçalho Authorization.");
        }

        if (token != null && jwtTokenUtil.validateToken(token)) {
            logger.info("Token JWT extraído e validado com sucesso. Prosseguindo com autenticação...");
            try {
                String username = jwtTokenUtil.getUsernameFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                logger.info("Usuário {} autenticado com sucesso e contexto de segurança definido.", username);

            } catch (Exception e) {
                logger.error("Erro ao configurar a autenticação do usuário com token: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        // Se o token for nulo ou inválido, o filtro continua, mas o SecurityContext fica vazio.
        // A próxima camada (SecurityConfig com .anyRequest().authenticated()) irá interceptar e retornar 401.
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            logger.debug("Token Bearer encontrado no cabeçalho.");
            return bearerToken.substring(7);
        }
        return null;
    }
}
