package dio.sistemagestaoacademia.dto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AvaliacaoCadastroDTO {
    @NotNull(message = "O ID do aluno é obrigtorio.")
    @Positive(message = "O ID do aluno deve ser positivo.")
    private Long alunoId;

    @NotNull(message = "O ID do instrutor é obrigatório.")
    @Positive(message = "O ID do instrutor deve ser positivo.")
    private Long instrutorId;

    @NotNull(message = "O peso é obrigatório.")
    @DecimalMin(value = "0.01", message = "O peso deve ser maior que zero.")
    private BigDecimal peso;

    @NotNull(message = "A altura é obrigatória.")
    @DecimalMin(value = "0.01", message = "A altura deve ser maior que zero.")
    private BigDecimal altura;
    @DecimalMin(value = "0.01", message = "A massa magra deve ser maior que zero.")
    private BigDecimal massaMagra;
    @DecimalMin(value = "0.01", message = "A dobra do tríceps deve ser maior que zero.")
    private BigDecimal dobraTriceps;
    @DecimalMin(value = "0.01", message = "A dobra subescapular deve ser maior que zero.")
    private BigDecimal dobraSubescapular;
    @DecimalMin(value = "0.01", message = "A dobra peitoral deve ser maior que zero.")
    private BigDecimal dobraPeitoral;
    @DecimalMin(value = "0.01", message = "A dobra axilar média deve ser maior que zero.")
    private BigDecimal dobraAxilarMedia;
    @DecimalMin(value = "0.01", message = "A dobra supra-ilíaca deve ser maior que zero.")
    private BigDecimal dobraSuprailiaca;
    @DecimalMin(value = "0.01", message = "A dobra abdominal deve ser maior que zero.")
    private BigDecimal dobraAbdominal;
    @DecimalMin(value = "0.01", message = "A dobra da coxa deve ser maior que zero.")
    private BigDecimal dobraCoxa;

    @DecimalMin(value = "0.01", message = "A circunferência da cintura deve ser maior que zero.")
    private BigDecimal circCintura;

    @DecimalMin(value = "0.01", message = "A circunferência do quadril deve ser maior que zero.")
    private BigDecimal circQuadril;

    @DecimalMin(value = "0.01", message = "A circunferência do braço deve ser maior que zero.")
    private BigDecimal circBraco;

    @DecimalMin(value = "0.01", message = "A circunferência da coxa deve ser maior que zero.")
    private BigDecimal circCoxa;

    private String observacoes;
}
