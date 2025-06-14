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

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Value("${api.endpoint.base-url}")
    private String baseUrl;
    private final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    private final String ROLE_ADMIN = "ROLE_ADMIN";
    private final String ROLE_CUSTOMER = "ROLE_CUSTOMER";

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
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/users/register/**").permitAll()
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/admins/register/**").hasAnyAuthority(this.ROLE_SUPER_ADMIN)
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/admins/**").hasAnyAuthority(this.ROLE_SUPER_ADMIN)
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/orders/{orderId}").hasAnyAuthority(this.ROLE_SUPER_ADMIN, this.ROLE_ADMIN, this.ROLE_CUSTOMER)
                        .requestMatchers(HttpMethod.GET, this.baseUrl + "/products/**").hasAnyAuthority(this.ROLE_SUPER_ADMIN, this.ROLE_ADMIN, this.ROLE_CUSTOMER)
                        .requestMatchers(HttpMethod.POST, this.baseUrl + "/portal/products/**").hasAnyAuthority(this.ROLE_SUPER_ADMIN, this.ROLE_ADMIN)
                        .requestMatchers(HttpMethod.PUT, this.baseUrl + "/portal/products/**").hasAnyAuthority(this.ROLE_SUPER_ADMIN, this.ROLE_ADMIN)
                        .requestMatchers(HttpMethod.DELETE, this.baseUrl + "/portal/products/**").hasAnyAuthority(this.ROLE_SUPER_ADMIN, this.ROLE_ADMIN)
                        // Disallow anything else.
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
