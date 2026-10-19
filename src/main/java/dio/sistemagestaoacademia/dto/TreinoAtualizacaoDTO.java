package dio.sistemagestaoacademia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreinoAtualizacaoDTO {
    @NotBlank(message =  "O nome do treino é obrigatório.")
    @Size(max = 150, message =  "O nome deve ter no máximo 150 caracteres.")
    private String nome;
    private String descricao;
    private String diaDaSemana;

}
/**
 * Quando implementamos o método alterarTreino essas são as seguintes funcinalidades
 * Se o treino existe.
 * Se o instrutor autenticado é o responsável pelo treino.
 * Se os dados recebidos são válidos.
 */