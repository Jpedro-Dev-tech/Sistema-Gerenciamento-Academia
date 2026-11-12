package dio.sistemagestaoacademia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExercicioAtualizacaoDTO {

    @NotBlank(message = "O nome do exercício é obrigatório")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    private String nome;
    private String descricao;
    @NotBlank(message = "O grupo muscular é obrigatório")
    @Size(max = 150, message = "O grupo muscular deve ter no máximo 150 caracteres")
    private String grupoMuscular;
}