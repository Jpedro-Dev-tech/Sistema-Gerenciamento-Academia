package dio.sistemagestaoacademia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingClassificacaoDTO {
    private Integer posicao;
    private Long alunoId;
    private String nomeAluno;
    private Integer totalPontos;
    /**
     * | Atributo      | Finalidade                            |
     * | ------------- | ------------------------------------- |
     * | `posicao`     | Posição do aluno no ranking           |
     * | `alunoId`     | Identificador do aluno                |
     * | `nomeAluno`   | Nome do aluno                         |
     * | `totalPontos` | Soma dos pontos obtidos nos check-ins |
     */
}
