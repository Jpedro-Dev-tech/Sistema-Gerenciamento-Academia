package dio.sistemagestaoacademia.repository;

import dio.sistemagestaoacademia.entity.CheckIn;
import dio.sistemagestaoacademia.entity.Treino;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
}
