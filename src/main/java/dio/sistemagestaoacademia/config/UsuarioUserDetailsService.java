package dio.sistemagestaoacademia.config;

import dio.sistemagestaoacademia.entity.Aluno;
import dio.sistemagestaoacademia.entity.Instrutor;
import dio.sistemagestaoacademia.repository.AlunoRepository;
import dio.sistemagestaoacademia.repository.InstrutorRepository;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UsuarioUserDetailsService implements UserDetailsService {

    private final AlunoRepository alunoRepository;
    private final InstrutorRepository instrutorRepository;

    public UsuarioUserDetailsService(
            AlunoRepository alunoRepository,
            InstrutorRepository instrutorRepository) {

        this.alunoRepository = alunoRepository;
        this.instrutorRepository = instrutorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Optional<Aluno> aluno = alunoRepository.findByEmail(email);
        Optional<Instrutor> instrutor = instrutorRepository.findByEmail(email);

        // Impede ambiguidades entre alunos e instrutores
        if (aluno.isPresent() && instrutor.isPresent()) {
            throw new UsernameNotFoundException(
                    "E-mail cadastrado em mais de um perfil"
            );
        }

        // Autenticação do aluno
        if (aluno.isPresent()) {

            Aluno usuario = aluno.get();

            return User.builder()
                    .username(usuario.getEmail())
                    .password(usuario.getSenhaHash())
                    .roles("ALUNO")
                    .disabled(!Boolean.TRUE.equals(usuario.getAtivo()))
                    .build();
        }

        // Autenticação do instrutor
        if (instrutor.isPresent()) {

            Instrutor usuario = instrutor.get();

            return User.builder()
                    .username(usuario.getEmail())
                    .password(usuario.getSenhaHash())
                    .roles("INSTRUTOR")
                    .disabled(!Boolean.TRUE.equals(usuario.getAtivo()))
                    .build();
        }

        throw new UsernameNotFoundException(
                "Usuário não encontrado"
        );
    }
}