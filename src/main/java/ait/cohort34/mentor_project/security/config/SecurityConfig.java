package ait.cohort34.mentor_project.security.config;

import ait.cohort34.mentor_project.security.filter.TokenFilter;
import ait.cohort34.mentor_project.security.service.CustomWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity
public class SecurityConfig {
    private static final String[] AUTH_WHITELIST = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };final CustomWebSecurity webSecurity;
    final private TokenFilter tokenFilter;
    @Autowired
    public SecurityConfig(CustomWebSecurity webSecurity, TokenFilter tokenFilter) {
        this.webSecurity = webSecurity;
        this.tokenFilter = tokenFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(x->x.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests(x->x
                        .requestMatchers(HttpMethod.GET,"/api/account").permitAll()
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/login","/api/auth/refresh","/api/account").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/account/password","/api/account/user/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/account/users").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/account/user/{id}/role").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/account/user/{id}").permitAll()
                        .anyRequest().authenticated())
                .addFilterAfter(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

