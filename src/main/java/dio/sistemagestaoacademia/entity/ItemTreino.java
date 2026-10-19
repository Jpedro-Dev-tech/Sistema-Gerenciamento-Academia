package dio.sistemagestaoacademia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_treino")
@Getter
@Setter
public class ItemTreino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treino_id", nullable = false)
    private Treino treino;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercicio_id", nullable = false)
    private Exercicio exercicio;
    @Column(name = "ordem", nullable = false)
    private int ordem;
    @Column(name = "series", nullable = false)
    private int series;
    @Column(name = "repeticoes", nullable = false)
    private int repeticoes;
    @Column(name = "carga", precision = 6, scale = 2)
    private BigDecimal carga;
    private String observacoes;
    @Column(name = "concluido", nullable = false)
    private Boolean concluido = false;

    public ItemTreino(Treino treino, Exercicio exercicio, int ordem, int series, int repeticoes, String observacoes, BigDecimal carga, Boolean concluido) {
        this.treino = treino;
        this.exercicio = exercicio;
        this.ordem = ordem;
        this.series = series;
        this.repeticoes = repeticoes;
        this.observacoes = observacoes;
        this.carga = carga;
        this.concluido = concluido;
    }

    public ItemTreino() {
    }
}
