package batch_2.capstone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Encrypts passwords before saving to DB
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF so your JavaScript 'fetch' POST requests aren't blocked
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Allow anyone to see the login/signup pages and static assets
                        .requestMatchers("/signup", "/login", "/css/**", "/js/**").permitAll()

                        // Allow the registration API so new users can create accounts
                        .requestMatchers("/api/investment/register").permitAll()

                        // Everything else (Dashboard, Buy Shares, etc.) requires a login
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        // Spring looks for an input with name="username" in your HTML
                        .usernameParameter("username")
                        .defaultSuccessUrl("/index", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}