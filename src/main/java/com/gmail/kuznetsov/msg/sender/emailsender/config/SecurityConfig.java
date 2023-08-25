package com.gmail.kuznetsov.msg.sender.emailsender.config;

import com.gmail.kuznetsov.msg.sender.emailsender.config.filter.JwtRequestFilter;
import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.supports.Roles;
import com.gmail.kuznetsov.msg.sender.emailsender.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Конфигурация прав доступа пользователей
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig  {
    private final UserService service;
    private final JwtRequestFilter jwtRequestFilter;
    private final String ADMIN = Roles.ADMIN.name();

    /**
     * Настройка цепочек по правам доступа
     * @param http HttpSecurity
     * @return бин SecurityFilterChain
     * @throws Exception любые исключения, например, при проблемах передачи данных по сети
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource()).and()
                .authorizeRequests()
                .antMatchers("/admin/emails/send").hasRole(ADMIN)
                .antMatchers("/admin/messages").hasRole(ADMIN)
                .antMatchers("/admin/messages/*").hasRole(ADMIN)
                .antMatchers("/admin/pdf").hasRole(ADMIN)
                .antMatchers("/users/admin").hasRole(ADMIN)
                .antMatchers("/users/admin/**").hasRole(ADMIN)
                .antMatchers("/login").permitAll()
                .anyRequest().permitAll()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling();
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     *
     * @param configuration стандартный бин SpringFrameworkSecurity AuthenticationConfiguration
     * @return менеджер конфигураций аутентификации (стандартный бин SpringFrameworkSecurity)
     * @throws Exception -
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * @return бин, отвечающий за однонаправленное хэширование паролей
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(service);
        return provider;
    }

    /**
     * @return бин, конфигурирующий cors-политику для браузеров
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
