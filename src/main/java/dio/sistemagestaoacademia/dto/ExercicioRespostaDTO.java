package dio.sistemagestaoacademia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExercicioRespostaDTO {
    private long id;
    private String nome;
    private String descricao;
    private String grupoMuscular;

    public ExercicioRespostaDTO(long id, String nome, String descricao, String grupoMuscular) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.grupoMuscular = grupoMuscular;
    }
}
