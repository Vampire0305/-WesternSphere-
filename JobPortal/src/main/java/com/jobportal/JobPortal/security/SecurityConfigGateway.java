package com.jobportal.JobPortal.security;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile; // Import this
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity; // For Reactive WebFlux security
import org.springframework.security.config.web.server.ServerHttpSecurity; // For Reactive security
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// This config will ONLY be active when the "apiGateWay" profile is active
@Profile("apigateway")
@Configuration
@EnableWebFluxSecurity // Enables Spring Security for Spring WebFlux applications
public class SecurityConfigGateway {

    // Note: PasswordEncoder and AuthenticationManager might still be needed if Gateway authenticates users
    // For simple routing/filtering, often not. If it needs to authenticate for its own sake:
    // @Bean
    // public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    // @Bean
    // public ReactiveAuthenticationManager authenticationManager(...) { ... }

    @Bean
    public SecurityWebFilterChain securityGatewayFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Disable CSRF for Gateway if not used
                // Public routes that don't need authentication (e.g., login, registration)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/auth/**").permitAll() // Allow auth service endpoints to be reached
                        .pathMatchers("/eureka/**").permitAll() // Eureka client traffic via gateway (if any)
                        .pathMatchers("/actuator/**").permitAll() // Gateway's own actuator is often public
                        .anyExchange().authenticated() // All other requests need authentication
                );
        // If you want HTTP Basic for some reason on the Gateway:
        // .httpBasic(withDefaults()); // Uses Reactive compatible httpBasic

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



    @Bean // This bean needs to be @LoadBalanced for service discovery
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // IMPORTANT: Spring Cloud will automatically add the LoadBalancerInterceptor
        // to any RestTemplate annotated with @LoadBalanced.
        // You only need to add your *custom* interceptors.

        // Create a list to hold your custom interceptors
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

        // Add your JWT interceptor to the list
        // We call the private helper method here, it's NOT a @Bean itself
        interceptors.add(jwtClientHttpRequestInterceptor());

        // Set the interceptors on the RestTemplate.
        // These will run *before* the LoadBalancerInterceptor (which is added later by Spring Cloud).
        // If order matters and you need JWT AFTER LoadBalancer, you'd use a RestTemplateCustomizer.
        // For JWT (authentication before routing), this order is usually fine.
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    // This method creates your JWT interceptor, but it is NOT a @Bean itself.
    // Therefore, Spring will not find multiple ClientHttpRequestInterceptor beans automatically.
    private ClientHttpRequestInterceptor jwtClientHttpRequestInterceptor() {
        return (request, body, execution) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // Check if authentication exists and if it's a JWT (represented by String credentials)
            if (authentication != null && authentication.getCredentials() instanceof String) {
                String jwtToken = (String) authentication.getCredentials();
                // IMPORTANT: Add logic to only add token for internal services, NOT to the AuthService itself
                // (to avoid recursive calls during token acquisition if AnalyticsService needs to login)
                // For example:
                // if (!request.getURI().getHost().contains("authService")) {
                request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
                // }
            }
            return execution.execute(request, body);
        };
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
