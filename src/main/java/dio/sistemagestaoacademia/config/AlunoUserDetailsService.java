package dio.sistemagestaoacademia.config;

import dio.sistemagestaoacademia.entity.Aluno;
import dio.sistemagestaoacademia.repository.AlunoRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AlunoUserDetailsService implements UserDetailsService {

    private final AlunoRepository alunoRepository;

    public AlunoUserDetailsService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Aluno aluno = alunoRepository.findByEmail(email) //Busca o aluno pelo email cadastrado
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Aluno não encontrado"
                ));

        return User.builder()
                .username(aluno.getEmail())
                .password(aluno.getSenhaHash())//Utiliza o hash da senha armazenada no PostgreSQL.
                                              // O Spring Security poderá compará-lo com a senha informada durante a autenticação, usando o PasswordEncoder configurado.
                .roles("ALUNO")
                .disabled(!Boolean.TRUE.equals(aluno.getAtivo()))//impede autenticação de alunos inativos.
                .build();
    }
}
