package dio.sistemagestaoacademia.service;

import dio.sistemagestaoacademia.dto.TreinoAtualizacaoDTO;
import dio.sistemagestaoacademia.dto.TreinoCadastroDTO;
import dio.sistemagestaoacademia.dto.TreinoRespostaDTO;
import dio.sistemagestaoacademia.entity.Aluno;
import dio.sistemagestaoacademia.entity.Instrutor;
import dio.sistemagestaoacademia.entity.Treino;
import dio.sistemagestaoacademia.repository.AlunoRepository;
import dio.sistemagestaoacademia.repository.InstrutorRepository;
import dio.sistemagestaoacademia.repository.ItemTreinoRepository;
import dio.sistemagestaoacademia.repository.TreinoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TreinoService {
    private final TreinoRepository treinoRepository;
    private final AlunoRepository alunoRepository;
    private final InstrutorRepository instrutorRepository;
    private final ItemTreinoRepository itemTreinoRepository;

    public TreinoService(AlunoRepository alunoRepository, InstrutorRepository instrutorRepository, TreinoRepository treinoRepository, ItemTreinoRepository itemTreinoRepository) {
        this.alunoRepository = alunoRepository;
        this.instrutorRepository = instrutorRepository;
        this.treinoRepository = treinoRepository;
        this.itemTreinoRepository = itemTreinoRepository;
    }
    public TreinoRespostaDTO criarTreino(TreinoCadastroDTO dto) {
        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Aluno não encontrado com ID: " + dto.getAlunoId()));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String emailInstrutor = authentication.getName();

        Instrutor instrutor = instrutorRepository.findByEmail(emailInstrutor)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Instrutor autenticado não encontrado."
                ));

        if (!instrutor.getId().equals(dto.getInstrutorId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "O instrutor informado não corresponde ao instrutor autenticado."
            );
        }

        Treino treino = new Treino();

        treino.setAluno(aluno);
        treino.setInstrutor(instrutor);
        treino.setNome(dto.getNome());
        treino.setDescricao(dto.getDescricao());
        treino.setObjetivo(dto.getObjetivo());
        treino.setDiaDaSemana(dto.getDiaDaSemana());
        treino.setDataCriacao(LocalDateTime.now());
        treino.setDataAtualizacao(null);
        treino.setAtivo(true);

        Treino treinoSalvo = treinoRepository.save(treino);

        TreinoRespostaDTO resposta = new TreinoRespostaDTO();

        resposta.setId(treinoSalvo.getId());
        resposta.setAlunoId(treinoSalvo.getAluno().getId());
        resposta.setInstrutorId(treinoSalvo.getInstrutor().getId());
        resposta.setNome(treinoSalvo.getNome());
        resposta.setDescricao(treinoSalvo.getDescricao());
        resposta.setDiaDaSemana(treinoSalvo.getDiaDaSemana());
        resposta.setObjetivo(treinoSalvo.getObjetivo());
        resposta.setDataCriacao(treinoSalvo.getDataCriacao());
        resposta.setDataAtualizacao(treinoSalvo.getDataAtualizacao());
        resposta.setAtivo(treinoSalvo.getAtivo());

        return resposta;

    }
    public TreinoRespostaDTO alterarTreino(Long id, TreinoAtualizacaoDTO dto) {
        // Busca o treino no banco de dados
        Treino treino = treinoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Treino não encontrado com ID: " + id
                ));
        // Identifica o instrutor autenticado
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String emailInstrutor = authentication.getName();

        // Verifica se o instrutor autenticado é responsável pelo treino
        if (!treino.getInstrutor().getEmail().equals(emailInstrutor)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não tem permissão para alterar este treino."
            );
        }
        // Atualiza os dados do treino
        treino.setNome(dto.getNome());
        treino.setDescricao(dto.getDescricao());
        treino.setObjetivo(dto.getObjetivo());
        treino.setDiaDaSemana(dto.getDiaDaSemana());

        // Registra a data e o horário da atualização
        treino.setDataAtualizacao(LocalDateTime.now());

        // Salva as alterações no PostgreSQL
        Treino treinoAtualizado = treinoRepository.save(treino);

        // Converte a entidade para o DTO de resposta
        TreinoRespostaDTO resposta = new TreinoRespostaDTO();

        resposta.setId(treinoAtualizado.getId());
        resposta.setAlunoId(treinoAtualizado.getAluno().getId());
        resposta.setInstrutorId(treinoAtualizado.getInstrutor().getId());
        resposta.setNome(treinoAtualizado.getNome());
        resposta.setDescricao(treinoAtualizado.getDescricao());
        resposta.setDiaDaSemana(treinoAtualizado.getDiaDaSemana());
        resposta.setObjetivo(treinoAtualizado.getObjetivo());
        resposta.setDataCriacao(treinoAtualizado.getDataCriacao());
        resposta.setDataAtualizacao(treinoAtualizado.getDataAtualizacao());
        resposta.setAtivo(treinoAtualizado.getAtivo());

        return resposta;
    }
    public List<TreinoRespostaDTO> visualizarTreino() {

        // Identifica o usuário autenticado
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // Verifica se o usuário possui o perfil ALUNO
        boolean ehAluno = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ALUNO"));

        if (!ehAluno) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Somente alunos podem visualizar seus treinos."
            );
        }

        // Obtém o e-mail do aluno autenticado
        String emailAluno = authentication.getName();

        // Busca o aluno no PostgreSQL
        Aluno aluno = alunoRepository.findByEmail(emailAluno)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno autenticado não encontrado."
                ));

        // Busca somente os treinos pertencentes ao aluno
        List<Treino> treinos = treinoRepository.findByAlunoId(aluno.getId());

        // Converte os treinos encontrados para DTOs de resposta
        return treinos.stream()
                .map(treino -> {

                    TreinoRespostaDTO resposta = new TreinoRespostaDTO();

                    resposta.setId(treino.getId());
                    resposta.setAlunoId(treino.getAluno().getId());
                    resposta.setInstrutorId(treino.getInstrutor().getId());
                    resposta.setNome(treino.getNome());
                    resposta.setDescricao(treino.getDescricao());
                    resposta.setObjetivo(treino.getObjetivo());
                    resposta.setDiaDaSemana(treino.getDiaDaSemana());
                    resposta.setDataCriacao(treino.getDataCriacao());
                    resposta.setDataAtualizacao(treino.getDataAtualizacao());
                    resposta.setAtivo(treino.getAtivo());

                    return resposta;
                })
                .toList();
    }
}
