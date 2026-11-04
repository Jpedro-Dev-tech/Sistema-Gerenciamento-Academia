package dio.sistemagestaoacademia.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "check_ins",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_checkin_aluno_data",
                        columnNames = {"aluno_id", "data"}
                )
        }
)@Getter
@Setter
public class CheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;
    @Column(name = "data", nullable = false)
    private LocalDate data;
    @Column(name = "pontos", nullable = false)
    private int pontos;

    public CheckIn(Aluno aluno, LocalDate data, int pontos) {
        this.aluno = aluno;
        this.data = data;
        this.pontos = pontos;
    }

    public CheckIn() {
    }
}
