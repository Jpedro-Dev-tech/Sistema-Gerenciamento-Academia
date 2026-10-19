package dio.sistemagestaoacademia.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TreinoRespostaDTO {
    private Long id;
    private Long alunoId;
    private Long instrutorId;
    private String nome;
    private String descricao;
    private String diaDaSemana;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Boolean ativo;
}
