package com.example.Portal.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;
@EnableWebSecurity
@Configuration
@SuppressWarnings("removal")
public class SecurityConfig {

    RequestFilter requestFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public SecurityConfig(RequestFilter requestFilter){
        this.requestFilter = requestFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable()
          .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/add").permitAll()
                        .requestMatchers(HttpMethod.GET, "/newhire").permitAll()
                        .requestMatchers(HttpMethod.GET, "/birthdays").permitAll()
                        .requestMatchers(HttpMethod.GET, "/workanniversary").permitAll()
                        .requestMatchers(HttpMethod.POST, "/holidayadd").permitAll()
                        .requestMatchers(HttpMethod.POST, "/holidays").permitAll()
                        .requestMatchers(HttpMethod.GET, "/view").hasAnyAuthority("ADMIN","MANAGER","EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/Employee/{id}/view").hasAnyAuthority("ADMIN","MANAGER","EMPLOYEE")
                        .requestMatchers(HttpMethod.DELETE, "/Employee/{id}/delete").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/Employee/{id}/update").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .anyRequest().authenticated()
        ).logout().disable();
        http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
//        }
    }
}

