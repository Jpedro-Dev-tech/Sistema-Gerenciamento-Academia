package dio.sistemagestaoacademia.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemTreinoCadastroDTO {

    @NotNull(message = "O ID do treino é obrigatório")
    @Positive(message = "O ID do treino deve ser positivo")
    private Long treinoId;

    @NotNull(message = "O ID do exercício é obrigatório")
    @Positive(message = "O ID do exercício deve ser positivo")
    private Long exercicioId;

    @Positive(message = "A ordem deve ser maior que zero")
    private int ordem;

    @Positive(message = "A quantidade de séries deve ser maior que zero")
    private int series;

    @PositiveOrZero(message = "A carga não pode ser negativa")
    private Double carga;

    private String observacoes;

    @Positive(message = "A quantidade de repetições deve ser maior que zero")
    private int repeticoes;
}