package dio.sistemagestaoacademia.repository;

import dio.sistemagestaoacademia.entity.Instrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InstrutorRepository extends JpaRepository<Instrutor, Long> {
    boolean existsByEmail(String email);
    Optional<Instrutor> findByEmail(String email);
}