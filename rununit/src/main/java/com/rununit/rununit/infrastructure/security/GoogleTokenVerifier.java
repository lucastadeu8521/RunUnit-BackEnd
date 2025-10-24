package com.rununit.rununit.infrastructure.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;


@Component
public class GoogleTokenVerifier {
    private final GoogleIdTokenVerifier verifier;
    public GoogleTokenVerifier(@Value("${google.client.id}") String clientId) {
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }
    public GoogleIdToken.Payload verify(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                return idToken.getPayload();
            }
        } catch (GeneralSecurityException e) {
            System.err.println("Erro de Segurança na Verificação do Google Token: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro de I/O na Verificação do Google Token: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro Inesperado na Verificação do Google Token: " + e.getMessage());
        }
        return null;
    }
}