package se.jensen.sofi_n.social_app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // stäng av csrf
                .csrf(cfrs -> cfrs.disable())
                .authorizeHttpRequests(auth -> auth
                        //tillåt skapa user utan att vara inloggad
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        // tillåt nå swagger utan inlogg
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**", //json api definition
                                "/swagger-ui.html"
                        ).permitAll()
                        //kräv inlogg för allt annat
                        .anyRequest().authenticated()
                ).formLogin(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
