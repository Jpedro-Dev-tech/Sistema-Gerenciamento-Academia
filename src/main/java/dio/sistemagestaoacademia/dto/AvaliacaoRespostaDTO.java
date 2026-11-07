package dio.sistemagestaoacademia.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AvaliacaoRespostaDTO {
    private Long id;
    private Long alunoId;
    private Long instrutorId;
    private LocalDateTime dataAvaliacao;
    private BigDecimal peso;
    private BigDecimal altura;
    private BigDecimal imc;
    private String classificacaoIMC;
    private BigDecimal percentualGordura;
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
    private String observacoes;

}