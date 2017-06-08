package ua.artcode.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ua.artcode.security.filters.JWTAuthenticationFilter;
import ua.artcode.security.filters.JWTLoginFilter;
import ua.artcode.security.service.TokenAuthenticationService;

/**
 * Created by v21k on 06.06.17.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenAuthenticationService tokenAuthenticationService;
    private final ObjectMapper objectMapper;

    @Autowired
    public WebSecurityConfig(TokenAuthenticationService tokenAuthenticationService, ObjectMapper objectMapper) {
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/register").permitAll()
                    .antMatchers(HttpMethod.POST, "/login").permitAll()
                    .antMatchers(
                           "/courses/add",
                            "/courses/lessons/add")
                        .hasRole("TEACHER")
                    .antMatchers(
                            "/run-class",
                            "/courses/lessons/send-solution-and-run-tests",
                            "/courses/lessons/send-solution-and-run-tests/**",
                            "/subscribe")
                        .hasRole("STUDENT")
                .anyRequest()
                    .authenticated()
                .and()
                // We filter the api/login requests
                .addFilterBefore(new JWTLoginFilter("/login", authenticationManager(), tokenAuthenticationService),
                        UsernamePasswordAuthenticationFilter.class)
                // And filter other requests to check the presence of JWT in header
                .addFilterBefore(new JWTAuthenticationFilter(tokenAuthenticationService),
                        UsernamePasswordAuthenticationFilter.class);
        http.csrf().disable();
    }


}