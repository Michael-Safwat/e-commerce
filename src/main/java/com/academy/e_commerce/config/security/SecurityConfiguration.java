package com.academy.e_commerce.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Value("${api.endpoint.base-url}")
    private String baseUrl;

    private final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    private final String ROLE_ADMIN = "ROLE_ADMIN";
    private final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    private static final String CART_BASE_PATH = "/users/{userId}/cart";

    private final CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;
    private final CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint;
    private final CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;

    public SecurityConfiguration(CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint,
                                 CustomBearerTokenAuthenticationEntryPoint customBearerTokenAuthenticationEntryPoint,
                                 CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler){
        this.customBasicAuthenticationEntryPoint = customBasicAuthenticationEntryPoint;
        this.customBearerTokenAuthenticationEntryPoint = customBearerTokenAuthenticationEntryPoint;
        this.customBearerTokenAccessDeniedHandler = customBearerTokenAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(this.customBasicAuthenticationEntryPoint))
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(this.customBearerTokenAuthenticationEntryPoint)
                        .accessDeniedHandler(this.customBearerTokenAccessDeniedHandler))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/login/**").permitAll()
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/reactivate/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, this.baseUrl + "/reset-password/**").permitAll()
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/users/register/**").permitAll()
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/users/verify/**").permitAll()
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/webhook").permitAll()
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/users/{userId}/pay/{orderId}").hasAuthority(this.ROLE_CUSTOMER)
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/portal/register/**").hasAnyAuthority(this.ROLE_SUPER_ADMIN)
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/portal/**").hasAnyAuthority(this.ROLE_SUPER_ADMIN)
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/portal/**").hasAnyAuthority(this.ROLE_SUPER_ADMIN)
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/portal/products/**").hasAnyAuthority(this.ROLE_SUPER_ADMIN, this.ROLE_ADMIN)
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/portal/products/**").hasAnyAuthority(this.ROLE_SUPER_ADMIN, this.ROLE_ADMIN)
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/portal/products/**").hasAnyAuthority(this.ROLE_SUPER_ADMIN, this.ROLE_ADMIN)
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/orders/**").hasAnyAuthority(this.ROLE_SUPER_ADMIN, this.ROLE_ADMIN, this.ROLE_CUSTOMER)
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/orders/users/{userId}/finalizeOrder").hasAnyAuthority(this.ROLE_CUSTOMER)
                        .requestMatchers(HttpMethod.POST, this.baseUrl + CART_BASE_PATH).hasAuthority(this.ROLE_CUSTOMER)
                        .requestMatchers(HttpMethod.GET, this.baseUrl + CART_BASE_PATH).hasAuthority(this.ROLE_CUSTOMER)
                        .requestMatchers(HttpMethod.PATCH, this.baseUrl + CART_BASE_PATH +"/**").hasAuthority(this.ROLE_CUSTOMER)
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/users/{userId}/shippingAddresses").hasAuthority(this.ROLE_CUSTOMER)
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/users/{userId}/addresses/{addressId}").hasAuthority(this.ROLE_CUSTOMER)
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/users/{userId}/addresses").hasAuthority(this.ROLE_CUSTOMER)
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/users/{userId}/addresses/{addressId}").hasAuthority(this.ROLE_CUSTOMER)
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/users/{userId}/addresses/{addressId}").hasAuthority(this.ROLE_CUSTOMER)

                        // Disallow anything else.
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "http://localhost:5173",
                "http://localhost:8081"

        ));        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control", "X-Requested-With", "Accept"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
