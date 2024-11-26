package fr.plb.ecom_user.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CircuitBreakerConfiguration {

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCircuitBreaker() {
        return factory -> factory.configure(builder -> builder
                .circuitBreakerConfig(CircuitBreakerConfig.custom()
                        .failureRateThreshold(50)
                        .slowCallDurationThreshold(Duration.ofMillis(30))
                        .slowCallRateThreshold(50)
                        .automaticTransitionFromOpenToHalfOpenEnabled(true)
                        .slidingWindowSize(10)
                        .minimumNumberOfCalls(5)
                        .build()
                ),"gateway");
    }

}
