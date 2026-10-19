package dio.sistemagestaoacademia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "exercicios")
@Getter
@Setter
public class Exercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome", nullable = false, length = 150)
    private String nome;
    @Column(name = "grupo_muscular", nullable = false, length = 150)
    private String grupo_Muscular;
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    public Exercicio(String nome, String grupo_Muscular, String descricao) {
        this.nome = nome;
        this.grupo_Muscular = grupo_Muscular;
        this.descricao = descricao;
    }
    public Exercicio() {

    }
}
