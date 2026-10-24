package dio.sistemagestaoacademia.service;

import dio.sistemagestaoacademia.dto.RankingClassificacaoDTO;
import dio.sistemagestaoacademia.entity.CheckIn;
import dio.sistemagestaoacademia.entity.Ranking;
import dio.sistemagestaoacademia.repository.CheckInRepository;
import dio.sistemagestaoacademia.repository.RankingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class RankingService {
    private final RankingRepository rankingRepository;
    private final CheckInRepository checkInRepository;

    public RankingService(RankingRepository rankingRepository, CheckInRepository checkInRepository) {
        this.rankingRepository = rankingRepository;
        this.checkInRepository = checkInRepository;
        /**
         * Permite que o Spring injete automaticamente os dois repositórios pel construtor.
         */
    }
    public List<RankingClassificacaoDTO> calcularClassificacao(Long rankingId) {

        // 1. Busca o ranking pelo ID
        Ranking ranking = rankingRepository.findById(rankingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ranking não encontrado com ID: " + rankingId
                ));

        // 2. Busca os check-ins dentro do período do ranking
        List<CheckIn> checkIns = checkInRepository.findByDataBetween(
                ranking.getDataInicio(),
                ranking.getDataFim()
        );

        // 3. Soma os pontos de cada aluno
        Map<Long, RankingClassificacaoDTO> classificacao = new HashMap<>();
        for (CheckIn checkIn : checkIns) {
            Long alunoId = checkIn.getAluno().getId();
            RankingClassificacaoDTO alunoRanking = classificacao.get(alunoId);
            if (alunoRanking == null) {
                alunoRanking = new RankingClassificacaoDTO();
                alunoRanking.setAlunoId(alunoId);
                alunoRanking.setNomeAluno(checkIn.getAluno().getNome());
                alunoRanking.setTotalPontos(0);
                classificacao.put(alunoId, alunoRanking);
            }
            alunoRanking.setTotalPontos(
                    alunoRanking.getTotalPontos() + checkIn.getPontos()
            );
        }

        // 4. Converte o Map em uma lista
        List<RankingClassificacaoDTO> resultado =
                new ArrayList<>(classificacao.values());

        // 5. Ordena pela maior pontuação
        resultado.sort(
                Comparator.comparing(
                                RankingClassificacaoDTO::getTotalPontos
                        ).reversed()
                        .thenComparing(RankingClassificacaoDTO::getAlunoId)
        );

        // 6. Define a posição de cada aluno
        int posicaoAtual = 0;
        Integer pontuacaoAnterior = null;
        for (int i = 0; i < resultado.size(); i++) {
            Integer pontuacaoAtual = resultado.get(i).getTotalPontos();
            // Se a pontuação mudou, atualiza a posição
            if (!pontuacaoAtual.equals(pontuacaoAnterior)) {
                posicaoAtual = i + 1;
            }
            resultado.get(i).setPosicao(posicaoAtual);
            pontuacaoAnterior = pontuacaoAtual;
            /**
             * Esse trecho do codigo faz o segunte, compara a pontuação de cada aluno com a do aluno anterior
             * se a pontuação for diferente, defne uma nova posição.
             * se a pontuação for igual, mantém a mesma posição.
             */
        }
        return resultado;
    }
    public List<RankingClassificacaoDTO> consultarRanking(Long rankingId) {
        return calcularClassificacao(rankingId); //Recebe o ID do ranking que será consultado
    }
}
