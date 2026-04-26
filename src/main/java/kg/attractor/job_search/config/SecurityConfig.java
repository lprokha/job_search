package kg.attractor.job_search.config;

import kg.attractor.job_search.service.impl.AuthUserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthUserDetailsServiceImpl authUserDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(authUserDetailsService)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            boolean isEmployer = authentication.getAuthorities().stream()
                                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_EMPLOYER"));

                            if (isEmployer) {
                                response.sendRedirect("/employer/resumes");
                            } else {
                                response.sendRedirect("/vacancies");
                            }
                        })
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/forbidden")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/register",
                                "/vacancies",
                                "/forgot-password",
                                "/reset-password",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/accounts").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/vacancies", "/api/vacancies/**").permitAll()

                        .requestMatchers("/profile/**").authenticated()

                        .requestMatchers("/resumes/**").hasRole("APPLICANT")
                        .requestMatchers("/my-vacancies/**").hasRole("EMPLOYER")
                        .requestMatchers("/employer/resumes/**").hasRole("EMPLOYER")

                        .requestMatchers(HttpMethod.POST, "/api/resumes").hasAuthority("CREATE_RESUME")
                        .requestMatchers(HttpMethod.PUT, "/api/resumes/**").hasAuthority("UPDATE_RESUME")
                        .requestMatchers(HttpMethod.DELETE, "/api/resumes/**").hasAuthority("DELETE_RESUME")
                        .requestMatchers(HttpMethod.POST, "/api/responses").hasAuthority("RESPOND_TO_VACANCY")

                        .requestMatchers(HttpMethod.POST, "/api/vacancies").hasAuthority("CREATE_VACANCY")
                        .requestMatchers(HttpMethod.PUT, "/api/vacancies/**").hasAuthority("UPDATE_VACANCY")
                        .requestMatchers(HttpMethod.DELETE, "/api/vacancies/**").hasAuthority("DELETE_VACANCY")
                        .requestMatchers(HttpMethod.GET, "/api/responses/vacancy/**").hasAuthority("VIEW_VACANCY_RESPONSES")

                        .requestMatchers(HttpMethod.PUT, "/api/accounts/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/accounts/*/avatar").authenticated()

                        .anyRequest().authenticated()
                );

        return http.build();
    }
}