package com.example.j2ee_frontend_test.config;

import com.example.j2ee_frontend_test.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http, JwtProvider jwtTokenProvider) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider);

        http
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/").permitAll()
//                                .requestMatchers("/introduce").permitAll()
//                                .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**").permitAll()
//                                .requestMatchers("/favicon.ico").permitAll() // Allow favicon.ico request
//                                .requestMatchers("/login", "/validate-login", "/forgot-pass").permitAll()
//                                .requestMatchers("/charities_events","/article/**", "/donate/**", "/submitOrder", "/vnpay-payment/**", "/payment/**", "/certification").permitAll()
//                                .requestMatchers("/transaction", "export/**").permitAll()
//                                .anyRequest().authenticated()
//                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/account/**").hasAnyAuthority("Account Management read")
                                .requestMatchers("/charity-events/**").hasAnyAuthority("Charity Event Management read")
                                .requestMatchers("/posts/**").hasAnyAuthority("Post Management read")
                                .requestMatchers("/disburse/**").hasAnyAuthority("Accounting read")
                                .requestMatchers("/role/**").hasAnyAuthority("Role Management read")
                                .anyRequest().permitAll()
                        )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // Add the JWT filter

        return http.build();
    }
}
