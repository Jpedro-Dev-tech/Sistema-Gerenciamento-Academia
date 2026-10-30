package dio.sistemagestaoacademia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/alunos").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/instrutores/login").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
/**
 * | Configuração                   | Significado                                          |
 * | ------------------------------ | ---------------------------------------------------- |
 * | `POST /api/alunos`             | Permite cadastrar alunos sem autenticação            |
 * | `POST /api/instrutores/login`  | Permite acessar o endpoint de login sem autenticação |
 * | `anyRequest().authenticated()` | Exige autenticação nas demais requisições            |
 * | `httpBasic()`                  | Habilita autenticação HTTP Basic                     |
 */