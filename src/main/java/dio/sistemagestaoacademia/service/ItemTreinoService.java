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
}
