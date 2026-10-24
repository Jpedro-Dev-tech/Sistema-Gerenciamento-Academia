package dio.sistemagestaoacademia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemTreinoRespostaDTO {
    private Long id;
    private Long treinoId;
    private Long exercicioId;
    private String nomeExercicio;
    private int ordem;
    private int series;
    private int repeticoes;
    private Double carga;
    private String observacoes;
    private Boolean concluido;

    public ItemTreinoRespostaDTO(
            Long id,
            Long treinoId,
            Long exercicioId,
            String nomeExercicio,
            int ordem,
            int series,
            int repeticoes,
            Double carga,
            String observacoes,
            Boolean concluido) {

        this.id = id;
        this.treinoId = treinoId;
        this.exercicioId = exercicioId;
        this.nomeExercicio = nomeExercicio;
        this.ordem = ordem;
        this.series = series;
        this.repeticoes = repeticoes;
        this.carga = carga;
        this.observacoes = observacoes;
        this.concluido = concluido;
    }
}
