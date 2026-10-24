package dio.sistemagestaoacademia.service;

import dio.sistemagestaoacademia.dto.AvaliacaoCadastroDTO;
import dio.sistemagestaoacademia.dto.AvaliacaoRespostaDTO;
import dio.sistemagestaoacademia.entity.Aluno;
import dio.sistemagestaoacademia.entity.AvaliacaoAntropometrica;
import dio.sistemagestaoacademia.entity.Instrutor;
import dio.sistemagestaoacademia.repository.AlunoRepository;
import dio.sistemagestaoacademia.repository.AvaliacaoAntropometricaRepository;
import dio.sistemagestaoacademia.repository.InstrutorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AvaliacaoAntropometricaService {
    private final AlunoRepository alunoRepository;
    private final AvaliacaoAntropometricaRepository avaliacaoAntropometricaRepository;
    private final InstrutorRepository instrutorRepository;

    public AvaliacaoAntropometricaService(AvaliacaoAntropometricaRepository avaliacaoAntropometricaRepository,
                                          AlunoRepository alunoRepository,
                                          InstrutorRepository instrutorRepository) {
        this.avaliacaoAntropometricaRepository = avaliacaoAntropometricaRepository;
        this.alunoRepository = alunoRepository;
        this.instrutorRepository = instrutorRepository;
    }
    //Método registrarAvaliçao
     public AvaliacaoRespostaDTO registrarAvaliacao(AvaliacaoCadastroDTO dto) {
        Aluno aluno = alunoRepository.findById(dto.getAlunoId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Aluno não encontrado com ID: " + dto.getAlunoId()));

        String emailInstrutor = SecurityContextHolder.getContext().getAuthentication().getName();

        Instrutor instrutor = instrutorRepository.findByEmail(emailInstrutor)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Instrutor não autenticado."));

         if (!instrutor.getId().equals(dto.getInstrutorId())) {
             throw new ResponseStatusException(
                     HttpStatus.FORBIDDEN,
                     "O instrutor informado não corresponde ao instrutor autenticado."
             );
         }
         AvaliacaoAntropometrica avaliacao = new AvaliacaoAntropometrica();

         avaliacao.setAluno(aluno);
         avaliacao.setInstrutor(instrutor);

         avaliacao.setPeso(dto.getPeso());
         avaliacao.setAltura(dto.getAltura());
         avaliacao.setImc(avaliacao.calcularIMC());
         avaliacao.setMassaMagra(dto.getMassaMagra());

         avaliacao.setDobraTriceps(dto.getDobraTriceps());
         avaliacao.setDobraSubescapular(dto.getDobraSubescapular());
         avaliacao.setDobraPeitoral(dto.getDobraPeitoral());
         avaliacao.setDobraAxilarMedia(dto.getDobraAxilarMedia());
         avaliacao.setDobraSuprailiaca(dto.getDobraSuprailiaca());
         avaliacao.setDobraAbdominal(dto.getDobraAbdominal());
         avaliacao.setDobraCoxa(dto.getDobraCoxa());

         // Calcula o percentual de gordura quando as sete dobras estão preenchidas
         avaliacao.setPercentualGordura(avaliacao.calcularPercentualGordura());
         avaliacao.setCircCintura(dto.getCircCintura());
         avaliacao.setCircQuadril(dto.getCircQuadril());
         avaliacao.setCircBraco(dto.getCircBraco());
         avaliacao.setCircCoxa(dto.getCircCoxa());

         // 5. Salvar a avaliação
         AvaliacaoAntropometrica avaliacaoSalva =
                 avaliacaoAntropometricaRepository.save(avaliacao);

         // 6. Preparar a resposta
         AvaliacaoRespostaDTO resposta = new AvaliacaoRespostaDTO();

         resposta.setId(avaliacaoSalva.getId());
         resposta.setAlunoId(aluno.getId());
         resposta.setInstrutorId(instrutor.getId());
         resposta.setDataAvaliacao(avaliacaoSalva.getDataAvaliacao());

         resposta.setPeso(avaliacaoSalva.getPeso());
         resposta.setAltura(avaliacaoSalva.getAltura());
         resposta.setImc(avaliacaoSalva.getImc());

         resposta.setPercentualGordura(avaliacaoSalva.getPercentualGordura());
         resposta.setMassaMagra(avaliacaoSalva.getMassaMagra());

         resposta.setDobraTriceps(avaliacaoSalva.getDobraTriceps());
         resposta.setDobraSubescapular(avaliacaoSalva.getDobraSubescapular());
         resposta.setDobraPeitoral(avaliacaoSalva.getDobraPeitoral());
         resposta.setDobraAxilarMedia(avaliacaoSalva.getDobraAxilarMedia());
         resposta.setDobraSuprailiaca(avaliacaoSalva.getDobraSuprailiaca());
         resposta.setDobraAbdominal(avaliacaoSalva.getDobraAbdominal());
         resposta.setDobraCoxa(avaliacaoSalva.getDobraCoxa());

         resposta.setCircCintura(avaliacaoSalva.getCircCintura());
         resposta.setCircQuadril(avaliacaoSalva.getCircQuadril());
         resposta.setCircBraco(avaliacaoSalva.getCircBraco());
         resposta.setCircCoxa(avaliacaoSalva.getCircCoxa());

         return resposta;
     }
     //Metodo consltarHistorico
    public List<AvaliacaoRespostaDTO> consultarHistorico(Long alunoId) {

        // Verifica se o aluno existe
        if (!alunoRepository.existsById(alunoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, //se não existe retorna 404
                    "Aluno não encontrado com ID: " + alunoId
            );
        }

        // Busca todas as avaliações do aluno, da mais recente para a mais antiga
        List<AvaliacaoAntropometrica> avaliacoes =
                avaliacaoAntropometricaRepository
                        .findByAlunoIdOrderByDataAvaliacaoDesc(alunoId);

        // Converte as entidades em DTOs de resposta
        return avaliacoes.stream().map(avaliacao -> {

            AvaliacaoRespostaDTO resposta = new AvaliacaoRespostaDTO();

            resposta.setId(avaliacao.getId());
            resposta.setAlunoId(avaliacao.getAluno().getId());
            resposta.setInstrutorId(avaliacao.getInstrutor().getId());
            resposta.setDataAvaliacao(avaliacao.getDataAvaliacao());

            resposta.setPeso(avaliacao.getPeso());
            resposta.setAltura(avaliacao.getAltura());
            resposta.setImc(avaliacao.getImc());

            resposta.setPercentualGordura(avaliacao.getPercentualGordura());
            resposta.setMassaMagra(avaliacao.getMassaMagra());

            resposta.setDobraTriceps(avaliacao.getDobraTriceps());
            resposta.setDobraSubescapular(avaliacao.getDobraSubescapular());
            resposta.setDobraPeitoral(avaliacao.getDobraPeitoral());
            resposta.setDobraAxilarMedia(avaliacao.getDobraAxilarMedia());
            resposta.setDobraSuprailiaca(avaliacao.getDobraSuprailiaca());
            resposta.setDobraAbdominal(avaliacao.getDobraAbdominal());
            resposta.setDobraCoxa(avaliacao.getDobraCoxa());

            resposta.setCircCintura(avaliacao.getCircCintura());
            resposta.setCircQuadril(avaliacao.getCircQuadril());
            resposta.setCircBraco(avaliacao.getCircBraco());
            resposta.setCircCoxa(avaliacao.getCircCoxa());

            return resposta;

        }).toList();
    }
}
