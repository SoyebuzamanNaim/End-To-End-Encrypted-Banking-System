package com.bank.config;

import com.bank.security.JweDecryptionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JweDecryptionFilter jweDecryptionFilter;

    public SecurityConfig(JweDecryptionFilter jweDecryptionFilter) {
        this.jweDecryptionFilter = jweDecryptionFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers
                .xssProtection(xss -> xss.disable())
                .contentSecurityPolicy(cps -> cps.policyDirectives("default-src 'self'"))
                .frameOptions(frame -> frame.deny())
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/crypto/**").permitAll()
                .anyRequest().permitAll() // Permitting all for demo
            )
            .addFilterBefore(jweDecryptionFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
