package dio.sistemagestaoacademia.service;

import dio.sistemagestaoacademia.dto.ItemTreinoCadastroDTO;
import dio.sistemagestaoacademia.dto.ItemTreinoRespostaDTO;
import dio.sistemagestaoacademia.entity.Exercicio;
import dio.sistemagestaoacademia.entity.ItemTreino;
import dio.sistemagestaoacademia.entity.Treino;
import dio.sistemagestaoacademia.repository.ExercicioRepository;
import dio.sistemagestaoacademia.repository.ItemTreinoRepository;
import dio.sistemagestaoacademia.repository.TreinoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service //Informa ao Spring Boot que essa classe pertence à camada de regras de negócio.
public class ItemTreinoService {
    private final ItemTreinoRepository itemTreinoRepository;
    private final TreinoRepository treinoRepository;
    private final ExercicioRepository exercicioRepository;

    public ItemTreinoService(ItemTreinoRepository itemTreinoRepository,  TreinoRepository treinoRepository, ExercicioRepository exercicioRepository) {
       this.itemTreinoRepository = itemTreinoRepository;
       this.exercicioRepository = exercicioRepository;
       this.treinoRepository = treinoRepository;
    }
    public ItemTreinoRespostaDTO adicionarExercicio(ItemTreinoCadastroDTO dto) {

        Treino treino = treinoRepository.findById(dto.getTreinoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Treino não encontrado com ID: " + dto.getTreinoId()
                ));
        // Identifica o usuário autenticado
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String emailInstrutor = authentication.getName();

// Verifica se o instrutor autenticado é responsável pelo treino
        if (!treino.getInstrutor().getEmail().equals(emailInstrutor)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não tem permissão para adicionar exercícios a este treino."
            );
        }
        Exercicio exercicio = exercicioRepository.findById(dto.getExercicioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Exercício não encontrado com ID: " + dto.getExercicioId()
                ));

        ItemTreino itemTreino = new ItemTreino(
                treino,
                exercicio,
                dto.getOrdem(),
                dto.getSeries(),
                dto.getRepeticoes(),
                dto.getObservacoes(),
                dto.getCarga(),
                false
        );

        ItemTreino itemSalvo = itemTreinoRepository.save(itemTreino);

        return new ItemTreinoRespostaDTO(
                itemSalvo.getId(),
                treino.getId(),
                exercicio.getId(),
                exercicio.getNome(),
                itemSalvo.getOrdem(),
                itemSalvo.getSeries(),
                itemSalvo.getRepeticoes(),
                itemSalvo.getCarga(),
                itemSalvo.getObservacoes(),
                itemSalvo.getConcluido()
        );
    }
    public void removerExercicio(Long id) {

        // Busca o item de treino pelo ID
        ItemTreino itemTreino = itemTreinoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Item de treino não encontrado com ID: " + id
                ));

        // Identifica o instrutor autenticado
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String emailInstrutor = authentication.getName();

        // Verifica se o instrutor é responsável pelo treino
        if (!itemTreino.getTreino().getInstrutor().getEmail().equals(emailInstrutor)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não tem permissão para remover exercícios deste treino."
            );
        }

        // Remove somente o item de treino
        itemTreinoRepository.delete(itemTreino);
    }
    public List<ItemTreinoRespostaDTO> listarExerciciosPorTreino(Long treinoId) {

        // Verifica se o treino existe
        Treino treino = treinoRepository.findById(treinoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Treino não encontrado com ID: " + treinoId
                ));

        // Identifica o usuário autenticado
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String emailUsuario = authentication.getName();

// Verifica se o usuário possui o perfil ALUNO
        boolean ehAluno = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ALUNO"));

// Verifica se o usuário possui o perfil INSTRUTOR
        boolean ehInstrutor = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_INSTRUTOR"));

// Verifica se o aluno autenticado é proprietário do treino
        boolean alunoResponsavel = ehAluno &&
                treino.getAluno().getEmail().equals(emailUsuario);

// Verifica se o instrutor autenticado é responsável pelo treino
        boolean instrutorResponsavel = ehInstrutor &&
                treino.getInstrutor().getEmail().equals(emailUsuario);

// Bloqueia usuários que não possuem permissão
        if (!alunoResponsavel && !instrutorResponsavel) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não tem permissão para consultar os exercícios deste treino."
            );
        }

        // Busca os itens associados ao treino
        List<ItemTreino> itens =
                itemTreinoRepository.findByTreinoIdOrderByOrdemAsc(treinoId);

        // Converte os itens para DTOs de resposta
        return itens.stream()
                .map(item -> new ItemTreinoRespostaDTO(
                        item.getId(),
                        item.getTreino().getId(),
                        item.getExercicio().getId(),
                        item.getExercicio().getNome(),
                        item.getOrdem(),
                        item.getSeries(),
                        item.getRepeticoes(),
                        item.getCarga(),
                        item.getObservacoes(),
                        item.getConcluido()
                ))
                .collect(Collectors.toList());
    }
    public void marcarExercicioConcluido(Long itemTreinoId) {

        // Busca o item de treino pelo ID
        ItemTreino itemTreino = itemTreinoRepository.findById(itemTreinoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Item de treino não encontrado com ID: " + itemTreinoId
                ));

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
                    "Somente alunos podem marcar exercícios como concluídos."
            );
        }

        String emailAluno = authentication.getName();

        // Verifica se o item pertence ao treino do aluno autenticado
        if (!itemTreino.getTreino().getAluno().getEmail().equals(emailAluno)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não tem permissão para concluir exercícios de outro aluno."
            );
        }

        // Marca o exercício como concluído
        itemTreino.setConcluido(true);

        // Salva a alteração no PostgreSQL
        itemTreinoRepository.save(itemTreino);
    }
}
