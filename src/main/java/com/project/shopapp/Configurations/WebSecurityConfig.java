package com.project.shopapp.Configurations;

import com.project.shopapp.Model.Role;
import com.project.shopapp.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers("/users/register","/users/login")
                            .permitAll()
                            .requestMatchers(GET,"/categories**").hasAnyRole(Role.USER,Role.ADMIN)
                            .requestMatchers(POST,"/categories/**").hasRole(Role.ADMIN)
                            .requestMatchers(PUT,"/categories/**").hasRole(Role.ADMIN)
                            .requestMatchers(DELETE,"/categories/**").hasRole(Role.ADMIN)
                            .requestMatchers(GET,"/products**").hasAnyRole(Role.USER,Role.ADMIN)
                            .requestMatchers(POST,"/products/**").hasRole(Role.ADMIN)
                            .requestMatchers(PUT,"/products/**").hasRole(Role.ADMIN)
                            .requestMatchers(DELETE,"/products/**").hasRole(Role.ADMIN)
                            .requestMatchers(POST,"/orders/**").hasAnyRole(Role.USER)
                            .requestMatchers(GET,"/orders/**").hasAnyRole(Role.USER,Role.ADMIN)
                            .requestMatchers(PUT,"/orders/**").hasRole(Role.ADMIN)
                            .requestMatchers(DELETE,"/orders/**").hasRole(Role.ADMIN)
                            .requestMatchers(POST,"/orders-detail/**").hasAnyRole(Role.USER)
                            .requestMatchers(GET,"/orders-detail/**").hasAnyRole(Role.USER,Role.ADMIN)
                            .requestMatchers(PUT,"/orders-detail/**").hasRole(Role.ADMIN)
                            .requestMatchers(DELETE,"/orders-detail/**").hasRole(Role.ADMIN)
                            .anyRequest().authenticated();
                });
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization","content-type","x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**",configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return http.build();
    }
}
