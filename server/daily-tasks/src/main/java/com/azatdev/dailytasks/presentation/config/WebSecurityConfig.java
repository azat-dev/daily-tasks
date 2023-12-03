package com.azatdev.dailytasks.presentation.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import com.azatdev.dailytasks.data.repositories.data.UsersRepositoryImpl;
import com.azatdev.dailytasks.data.repositories.persistence.jpa.JpaUsersRepository;
import com.azatdev.dailytasks.domain.interfaces.repositories.user.UsersRepository;
import com.azatdev.dailytasks.presentation.security.services.CustomUserDetailsService;
import com.azatdev.dailytasks.presentation.security.services.CustomUserDetailsServiceImpl;
import com.azatdev.dailytasks.presentation.security.services.JwtAuthenticationFilter;
import com.azatdev.dailytasks.presentation.security.services.jwt.DateTimeProvider;
import com.azatdev.dailytasks.presentation.security.services.jwt.JWTService;
import com.azatdev.dailytasks.presentation.security.services.jwt.JWTServiceImpl;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
        CustomUserDetailsService customUserDetailsService,
        JWTService tokenProvider
    ) {
        return new JwtAuthenticationFilter(
            tokenProvider,
            customUserDetailsService
        );
    }

    @Bean
    SecurityFilterChain filterChain(
        HttpSecurity http,
        JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {
        return http.cors(corse -> corse.disable())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(
                requests -> requests.requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/token",
                    "/api/auth/token/refresh",
                    "/api/auth/token/verify"
                )
                    .permitAll()
                    .requestMatchers("/api/**")
                    .authenticated()
            )
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            )
            .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        CustomUserDetailsService customUserDetailsService,
        PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new RequestAttributeSecurityContextRepository();
    }

    @Bean
    public UsersRepository usersRepository(JpaUsersRepository jpaUsersRepository) {
        return new UsersRepositoryImpl(jpaUsersRepository);
    }

    @Bean
    public CustomUserDetailsService userDetailsService(UsersRepository usersRepository) {
        return new CustomUserDetailsServiceImpl(usersRepository);
    }

    @Bean
    public JWTService jwtService(
        @Value("${app.security.jwt.secret}") String jwtSecret,
        @Value("${app.security.jwt.accessToken.expirationInMs}") long jwtAccessTokenExpirationInMs,
        @Value("${app.security.jwt.refreshToken.expirationInMs}") long jwtRefreshTokenExpirationInMs

    ) {

        DateTimeProvider dateTimeProvider = () -> new Date().getTime();
        return new JWTServiceImpl(
            jwtSecret,
            jwtAccessTokenExpirationInMs,
            jwtRefreshTokenExpirationInMs,
            dateTimeProvider
        );
    }
}
