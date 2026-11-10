package dio.sistemagestaoacademia.config;

import jakarta.servlet.DispatcherType;
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

                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()

                        // Cadastro de alunos e instrutores
                        .requestMatchers(HttpMethod.POST, "/api/alunos").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/instrutores").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/instrutores/login").permitAll()

                        // Somente instrutores podem criar e alterar treinos
                        .requestMatchers(HttpMethod.POST, "/api/treinos")
                        .hasRole("INSTRUTOR")

                        .requestMatchers(HttpMethod.PUT, "/api/treinos/*")
                        .hasRole("INSTRUTOR")

                        // Somente alunos podem consultar seus próprios treinos
                        .requestMatchers(HttpMethod.GET, "/api/treinos/meus-treinos")
                        .hasRole("ALUNO")

                        // Alunos e instrutores podem solicitar fichas em PDF
                        .requestMatchers(HttpMethod.GET, "/api/treinos/*/ficha-pdf")
                        .hasAnyRole("ALUNO", "INSTRUTOR")

                        // Demais endpoints exigem autenticação
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}