package dio.sistemagestaoacademia.repository;
import dio.sistemagestaoacademia.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    List<CheckIn> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);
    // Verifica se o aluno já realizou um check-in na data informada.
    boolean existsByAlunoIdAndData(Long alunoId, LocalDate data);
    /**
     * Busca os check-ins realizados entre duas datas.
     *
     * findBy: busca registros no banco de dados.
     * DataBetween: filtra os registros entre a data inicial e a final.
     * LocalDate: representa uma data sem horário.
     * List<CheckIn>: retorna uma lista de check-ins encontrados.
     */


}
