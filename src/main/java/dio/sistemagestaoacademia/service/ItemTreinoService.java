package dio.sistemagestaoacademia.service;

import dio.sistemagestaoacademia.dto.ItemTreinoCadastroDTO;
import dio.sistemagestaoacademia.entity.Exercicio;
import dio.sistemagestaoacademia.entity.ItemTreino;
import dio.sistemagestaoacademia.entity.Treino;
import dio.sistemagestaoacademia.repository.ExercicioRepository;
import dio.sistemagestaoacademia.repository.ItemTreinoRepository;
import dio.sistemagestaoacademia.repository.TreinoRepository;
import org.springframework.http.HttpStatus;
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
    public ItemTreino adicionarExercicio(ItemTreinoCadastroDTO dto) {

        Treino treino = treinoRepository.findById(dto.getTreinoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Treino não encontrado com ID: " + dto.getTreinoId()
                ));

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

        return itemTreinoRepository.save(itemTreino);
    }
}
