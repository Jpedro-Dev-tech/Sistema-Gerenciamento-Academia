package dio.sistemagestaoacademia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Table(name = "avaliacoes_antropometricas")
@Getter
@Setter
public class AvaliacaoAntropometrica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrutor_id", nullable = false)
    private Instrutor instrutor;

    @Column(name = "data_avaliacao", nullable = false)
    private LocalDateTime dataAvaliacao = LocalDateTime.now();

    @Column(name = "peso", nullable = false, precision = 5, scale = 2)
    private BigDecimal peso;

    @Column(name = "altura", nullable = false, precision = 4, scale = 2)
    private BigDecimal altura;

    @Column(name = "imc", precision = 4, scale = 2)
    private BigDecimal imc;

    @Column(name = "percentual_gordura", precision = 5, scale = 2)
    private BigDecimal percentualGordura;

    @Column(name = "massa_magra", precision = 6, scale = 2)
    private BigDecimal massaMagra;

    @Column(name = "dobra_triceps", precision = 4, scale = 1)
    private BigDecimal dobraTriceps;

    @Column(name = "dobra_subescapular", precision = 4, scale = 1)
    private BigDecimal dobraSubescapular;

    @Column(name = "dobra_peitoral", precision = 4, scale = 1)
    private BigDecimal dobraPeitoral;

    @Column(name = "dobra_axilar_media", precision = 4, scale = 1)
    private BigDecimal dobraAxilarMedia;

    @Column(name = "dobra_suprailiaca", precision = 4, scale = 1)
    private BigDecimal dobraSuprailiaca;

    @Column(name = "dobra_abdominal", precision = 4, scale = 1)
    private BigDecimal dobraAbdominal;

    @Column(name = "dobra_coxa", precision = 4, scale = 1)
    private BigDecimal dobraCoxa;

    @Column(name = "circ_cintura", precision = 4, scale = 1)
    private BigDecimal circCintura;

    @Column(name = "circ_quadril", precision = 4, scale = 1)
    private BigDecimal circQuadril;

    @Column(name = "circ_braco", precision = 4, scale = 1)
    private BigDecimal circBraco;

    @Column(name = "circ_coxa", precision = 4, scale = 1)
    private BigDecimal circCoxa;


    public AvaliacaoAntropometrica() {
    }

    public AvaliacaoAntropometrica(
            Aluno aluno,
            Instrutor instrutor,
            BigDecimal peso,
            BigDecimal altura
    ) {
        this.aluno = aluno;
        this.instrutor = instrutor;
        this.peso = peso;
        this.altura = altura;
    }
    public BigDecimal calcularIMC() {
        if (peso == null || altura == null ||
                altura.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException(
                    "Peso e altura válidos são necessários para calcular o IMC."
            );
        }
        BigDecimal alturaAoQuadrado = altura.multiply(altura);

        return peso.divide(
                alturaAoQuadrado,
                2,
                RoundingMode.HALF_UP
        );
    }
    public BigDecimal calcularPercentualGordura() {

        // O cálculo é opcional: exige as sete dobras preenchidas.
        if (dobraTriceps == null ||
                dobraSubescapular == null ||
                dobraPeitoral == null ||
                dobraAxilarMedia == null ||
                dobraSuprailiaca == null ||
                dobraAbdominal == null ||
                dobraCoxa == null) {

            return null;
        }

        if (aluno == null ||
                aluno.getDataNascimento() == null ||
                aluno.getSexo() == null) {

            throw new IllegalStateException(
                    "Os dados do aluno são necessários para calcular o percentual de gordura."
            );
        }

        LocalDate dataReferencia = dataAvaliacao != null
                ? dataAvaliacao.toLocalDate()
                : LocalDate.now();

        int idade = Period.between(
                aluno.getDataNascimento(),
                dataReferencia
        ).getYears();

        if (idade < 18 || idade > 61) {
            throw new IllegalStateException(
                    "A idade está fora da faixa adotada para este protocolo."
            );
        }
        BigDecimal somaDobras = dobraTriceps
                .add(dobraSubescapular)
                .add(dobraPeitoral)
                .add(dobraAxilarMedia)
                .add(dobraSuprailiaca)
                .add(dobraAbdominal)
                .add(dobraCoxa);

        if (somaDobras.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException(
                    "A soma das dobras deve ser maior que zero."
            );
        }
        double soma = somaDobras.doubleValue();

        double densidadeCorporal;

        if ("M".equalsIgnoreCase(aluno.getSexo())) {

            densidadeCorporal =
                    1.112
                            - (0.00043499 * soma)
                            + (0.00000055 * soma * soma)
                            - (0.00028826 * idade);

        } else if ("F".equalsIgnoreCase(aluno.getSexo())) {

            densidadeCorporal =
                    1.097
                            - (0.00046971 * soma)
                            + (0.00000056 * soma * soma)
                            - (0.00012828 * idade);

        } else {
            throw new IllegalStateException(
                    "Sexo inválido para o protocolo selecionado."
            );
        }
        if (densidadeCorporal <= 0) {
            throw new IllegalStateException(
                    "Não foi possível calcular uma densidade corporal válida."
            );
        }
        // Equação de Siri
        double percentual = (495.0 / densidadeCorporal) - 450.0;

        if (!Double.isFinite(percentual) ||
                percentual < 0 ||
                percentual > 100) {

            throw new IllegalStateException(
                    "O percentual de gordura calculado é inválido."
            );
        }
        return BigDecimal.valueOf(percentual)
                .setScale(2, RoundingMode.HALF_UP);
    }
}