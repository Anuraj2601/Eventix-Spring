package com.example.eventix.config;


import com.example.eventix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTAuthFilter jwtAuthFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request-> request.requestMatchers("/auth/**","/public/**","/clubs/**").permitAll()

                        .requestMatchers("/api/messages/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/verify-account").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/regenerate-otp").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/photo").permitAll()
                        .requestMatchers(HttpMethod.GET,"/static/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/president/getAllElections").hasAnyAuthority("student","member", "oc", "president", "ADMIN", "treasurer")
                        .requestMatchers("/api/voters/election/**").permitAll()

//                         .requestMatchers("/president/").hasAnyAuthority("president", "student", "ADMIN", "treasurer")
//                         .requestMatchers("/admin/**").hasAnyAuthority( "student", "ADMIN", "treasurer")
                        .requestMatchers("/president/getAllEvents").hasAnyAuthority("member", "oc", "president", "student", "ADMIN", "treasurer")

                        .requestMatchers("/president/getAllEventOcs").hasAnyAuthority("member", "oc", "president", "student", "treasurer","ADMIN", "secretary")
                        .requestMatchers("/president/getAllAnnouncements").hasAnyAuthority("member", "oc", "president", "student", "treasurer","ADMIN", "secretary")
                        .requestMatchers("/president/releaseElection/**").hasAnyAuthority("student","president", "treasurer","ADMIN")

                        .requestMatchers("/president/**").hasAnyAuthority("member", "oc", "president", "student", "treasurer","ADMIN", "secretary")
                        .requestMatchers("/student/**").hasAnyAuthority("member", "oc", "president", "student", "treasurer","ADMIN", "secretary")

                        .requestMatchers("/admin/**").hasAnyAuthority("member", "oc", "president", "student", "treasurer","ADMIN", "secretary")

                        .requestMatchers("/user/**").hasAnyAuthority("USER","student", "president", "treasurer", "ADMIN")
                        .requestMatchers("/adminuser/**").hasAnyAuthority("member", "oc", "president", "student", "treasurer","ADMIN", "secretary","USER")
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .anyRequest().permitAll())
                .sessionManagement(manager->manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthFilter, UsernamePasswordAuthenticationFilter.class
                );

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
