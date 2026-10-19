package dio.sistemagestaoacademia.repository;

import dio.sistemagestaoacademia.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    List<Aluno> findByNome(String nome);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
}
