package kg.attractor.job_search.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JdbcUserDetailsManager userDetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        manager.setUsersByUsernameQuery("""
            select email, password, enabled
            from users
            where email = ?
            """);

        manager.setAuthoritiesByUsernameQuery("""
            select auth.email, auth.authority
            from (
                select u.email, concat('ROLE_', r.role_name) as authority
                from users u
                join user_role ur on u.id = ur.user_id
                join roles r on ur.role_id = r.id
        
                union
        
                select u.email, a.authority
                from users u
                join user_role ur on u.id = ur.user_id
                join role_authority ra on ur.role_id = ra.role_id
                join authorities a on ra.authority_id = a.id
            ) auth
            where auth.email = ?
            """);

        return manager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/profile", true)
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