package dio.sistemagestaoacademia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration//identifica a classe como responsável por configurações do Spring.
public class PasswordConfig {

    @Bean //disponibiliza o PasswordEncoder para outras classes.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //utiliza o algoritmo BCrypt para gerar e verificar hashes de senhas.
    }
}