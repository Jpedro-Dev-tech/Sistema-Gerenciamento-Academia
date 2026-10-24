package dio.sistemagestaoacademia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemTreinoCadastroDTO {
    private Long treinoId;
    private Long exercicioId;
    private int ordem;
    private int series;
    private Double carga;
    private String observacoes;
    private int repeticoes;
}
