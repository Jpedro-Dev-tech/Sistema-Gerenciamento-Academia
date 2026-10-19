package dio.sistemagestaoacademia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreinoCadastroDTO {

    @NotNull(message = "O ID do aluno é obrigatório.")
    private Long alunoId;
    @NotNull(message = "O ID do instrutor é obrigatório.")
    private Long instrutorId;
    @NotBlank(message = "O nome do treino é obrigatório.")
    private String nome;
    private String descricao;
    private String diaDaSemana;
}