package dio.sistemagestaoacademia.config;

import dio.sistemagestaoacademia.entity.Instrutor;
import dio.sistemagestaoacademia.repository.InstrutorRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class InstrutorUserDetailsService implements UserDetailsService {

    private final InstrutorRepository instrutorRepository;

    public InstrutorUserDetailsService(InstrutorRepository instrutorRepository) {
        this.instrutorRepository = instrutorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Instrutor instrutor = instrutorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Instrutor não encontrado"
                ));

        return User.builder()
                .username(instrutor.getEmail())
                .password(instrutor.getSenhaHash())
                .roles("INSTRUTOR")
                .disabled(!Boolean.TRUE.equals(instrutor.getAtivo()))
                .build();
    }
}
