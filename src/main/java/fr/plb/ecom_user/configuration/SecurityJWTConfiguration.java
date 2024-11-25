package fr.plb.ecom_user.configuration;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static fr.plb.ecom_user.configuration.security.SecurityUtils.JWT_ALGORITHM;

@Configuration
public class SecurityJWTConfiguration {

    @Value("${application.jwt.base64-secret}")
    private String jwtKey;

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(JWT_ALGORITHM).build();
        return token -> {
            try {
                return nimbusJwtDecoder.decode(token);
            } catch (JwtException e) {
                throw e;
            }
        };
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }
}
