package dio.sistemagestaoacademia.repository;

import dio.sistemagestaoacademia.entity.ItemTreino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemTreinoRepository extends JpaRepository<ItemTreino, Long> {
    List<ItemTreino>findByTreinoIdOrderByOrdemAsc(Long treinoId);
}
