package dio.sistemagestaoacademia.dto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AvaliacaoCadastroDTO {
    @NotNull(message = "O ID do aluno é obrigtorio.")
    private Long alunoId;
    @NotNull(message = "O ID do instrutor é obrigatório.")
    private Long instrutorId;
    @NotNull(message = "O peso é obrigatório.")
    @DecimalMin(value = "0.01", message = "O peso deve ser maior que zero.")
    private BigDecimal peso;
    @NotNull(message = "A altura é obrigatória.")
    @DecimalMin(value = "0.01", message = "A altura deve ser maior que zero.")
    private BigDecimal altura;
    private BigDecimal massaMagra;
    private BigDecimal dobraTriceps;
    private BigDecimal dobraSubescapular;
    private BigDecimal dobraPeitoral;
    private BigDecimal dobraAxilarMedia;
    private BigDecimal dobraSuprailiaca;
    private BigDecimal dobraAbdominal;
    private BigDecimal dobraCoxa;
    private BigDecimal circCintura;
    private BigDecimal circQuadril;
    private BigDecimal circBraco;
    private BigDecimal circCoxa;
}
