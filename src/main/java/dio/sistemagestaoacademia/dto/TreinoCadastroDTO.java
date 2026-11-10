package dio.sistemagestaoacademia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreinoCadastroDTO {

    @NotNull(message = "O ID do aluno é obrigatório.")
    @Positive(message = "O ID do aluno deve ser maior que zero.")
    private Long alunoId;
    @NotNull(message = "O ID do instrutor é obrigatório.")
    @Positive(message = "O ID do instrutor deve ser maior que zero.")
    private Long instrutorId;
    @NotBlank(message = "O nome do treino é obrigatório.")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
    private String nome;
    private String descricao;
    private String diaDaSemana;
    private String objetivo;
}