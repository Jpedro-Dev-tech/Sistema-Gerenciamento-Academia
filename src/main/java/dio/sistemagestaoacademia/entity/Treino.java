package dio.sistemagestaoacademia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "treinos")
@Getter
@Setter
public class Treino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrutor_id", nullable = false)
    private Instrutor instrutor;
    @Column(name = "nome", nullable = false, length = 150)
    private String nome;
    @Column(name = "descricao", nullable = true, columnDefinition = "TEXT")
    private String descricao;
    @Column(name = "dia_da_semana")
    private String diaDaSemana;
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
    @Column(name = "objetivo", columnDefinition = "TEXT")
    private String objetivo;

    public Treino(Aluno aluno, Instrutor instrutor, String nome, String descricao, String diaDaSemana, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, Boolean ativo, String objetivo) {
        this.aluno = aluno;
        this.instrutor = instrutor;
        this.nome = nome;
        this.descricao = descricao;
        this.diaDaSemana = diaDaSemana;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.ativo = ativo;
        this.objetivo = objetivo;
    }
    public  Treino() {

    }
}
