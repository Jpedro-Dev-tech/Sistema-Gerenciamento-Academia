package dio.sistemagestaoacademia.repository;

import dio.sistemagestaoacademia.entity.AvaliacaoAntropometrica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvaliacaoAntropometricaRepository extends JpaRepository<AvaliacaoAntropometrica, Long> {
    List<AvaliacaoAntropometrica> findByAlunoIdOrderByDataAvaliacaoDesc(Long alunoId); //O Spring Data JPA interpreta automaticamente o nome desse método
    //permitirá buscar todas as avaliações antropométricas de um aluno, começando pela mais recente.
}
