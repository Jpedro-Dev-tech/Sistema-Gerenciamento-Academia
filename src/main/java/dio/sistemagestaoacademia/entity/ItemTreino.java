package dio.sistemagestaoacademia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "itens_treino")
@Getter
@Setter
public class ItemTreino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) // Carregamento preguiçoso.Ao buscar entidade no banco.
    @JoinColumn(name = "treino_id", nullable = false) //@JoinColumn(name = "treino_id"): define a coluna de chave estrangeira (FK) na tabela desta entidade.
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
    @Column(name = "carga")
    private Double carga;
    private String observacoes;
    @Column(name = "concluido", nullable = false)
    private Boolean concluido = false;

    public ItemTreino(Treino treino, Exercicio exercicio, int ordem, int series, int repeticoes, String observacoes, Double carga, Boolean concluido) {
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
