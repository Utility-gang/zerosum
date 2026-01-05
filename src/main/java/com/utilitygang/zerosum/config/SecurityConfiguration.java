package com.utilitygang.zerosum.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.utilitygang.zerosum.model.User;
import com.utilitygang.zerosum.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${okta.oauth2.issuer}")
    private String issuer;
    @Value("${okta.oauth2.client-id}")
    private String clientId;
    @Autowired
    private UserRepository userRepo;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/images/**", "/stocks/*").permitAll()
                        .requestMatchers("/stocks/*/buy", "/stocks/*/sell").authenticated()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .successHandler((request, response, authentication) -> {
                            DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
                            String email = oidcUser.getEmail();

                            userRepo.findUserByUsername(email)
                                    .orElseGet(() -> userRepo.save(new User(email)));

                            response.sendRedirect("/portfolio");
                        }))

                .logout(logout -> logout
                        .logoutSuccessHandler((req, response, auth) -> {
                            String baseUrl = ServletUriComponentsBuilder
                                    .fromCurrentContextPath()
                                    .build()
                                    .toUriString();

                            response.sendRedirect(issuer + "v2/logout?client_id=" + clientId + "&returnTo=" + baseUrl);
                        }));
        return http.build();
    }
}
