package com.jobportal.JobPortal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // ✅ important
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;
@Profile("!apigateway")
@Configuration
@EnableMethodSecurity // ✅ add this to make @PreAuthorize work
@EnableTransactionManagement
public class SecurityConfig {

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("innnn");
        http
                // Explicitly configure CSRF
                .csrf().disable()
                // Configure session management
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Authorize requests
                .authorizeRequests(auth -> auth
                        // 1. Permit ALL Eureka client-server communication paths
                        .antMatchers("/eureka/**").permitAll()

                        // 2. Permit specific public API endpoints for AuthService
                        .antMatchers("/api/auth/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/api/students").permitAll()
                        .antMatchers("/api/students/health").permitAll()

                        // 3. Require authentication for the Eureka dashboard UI and root path
//                        .antMatchers("/actuator/**").authenticated() // Secures Actuator endpoints

                        // 4. Require authentication for all other /api endpoints
                        .antMatchers("/api/students/**").authenticated()
                        .antMatchers("/api/recruiters/**").authenticated()
                        .antMatchers("/api/jobPosts/**").authenticated()
                        .antMatchers("/api/notify/**").authenticated()

                        // 5. Catch-all: Any other request not covered above should be authenticated
                        .anyRequest().permitAll()
                );

        // Add your JWT filter before Spring Security's UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        System.out.println("outttt");

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

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
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
    }

