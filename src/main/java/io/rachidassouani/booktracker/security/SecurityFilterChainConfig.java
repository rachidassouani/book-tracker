package io.rachidassouani.booktracker.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth ->  auth
                        .requestMatchers("/books").hasAuthority("User")
                        .anyRequest().authenticated())
                .formLogin(form -> {
                    form.loginPage("/login")
                            .usernameParameter("email")
                            .permitAll();
                })
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }
}
