package com.employee.ems.config;


import com.employee.ems.service.CustomerUserDetailsService;
import com.employee.ems.service.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    private JwtFilter filter;


    private final String[] publicEndpoints = {
            "/api/v1/reg/register",
            "/api/v1/reg/login",
            "/api/v1/reg/logout",
            "/api/v1/employees/passengers/excel",
            "/api/v1/employees/create",
            "/api/v1/employees/getAll",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"

    };

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(customerUserDetailsService);
        authProvider.setPasswordEncoder(getEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityConfig(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(publicEndpoints).permitAll()
                            .requestMatchers(HttpMethod.PUT, "/api/v1/employees/{id}").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/employees/{id}").hasRole("ADMIN")
                            .anyRequest().authenticated();
                })
                .authenticationProvider(authProvider())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable); // ✅ Modern way to disable CSRF

        return http.build(); // ✅ Always call build() after config
    }


//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
//        config.setAllowedHeaders(Arrays.asList(
//                HttpHeaders.AUTHORIZATION,
//                HttpHeaders.CONTENT_TYPE,
//                HttpHeaders.ACCEPT
//        ));
//        config.setAllowedMethods(Arrays.asList(
//                HttpMethod.GET.name(),
//                HttpMethod.POST.name(),
//                HttpMethod.PUT.name(),
//                HttpMethod.DELETE.name()
//        ));
//        config.setExposedHeaders(Arrays.asList(HttpHeaders.AUTHORIZATION));
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }

}

