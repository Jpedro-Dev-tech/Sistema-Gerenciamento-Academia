package dio.sistemagestaoacademia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "instrutores")
@Getter
@Setter
public class Instrutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "registro_profissional", length = 30)
    private String registroProfissional;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    public Instrutor() {
    }

    public Instrutor(String nome, String email, String senhaHash,
                     String telefone, String registroProfissional, Boolean ativo) {

        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.telefone = telefone;
        this.registroProfissional = registroProfissional;
        this.ativo = ativo;
    }
}