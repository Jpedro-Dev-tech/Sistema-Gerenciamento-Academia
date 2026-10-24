package dio.sistemagestaoacademia.service;

import dio.sistemagestaoacademia.dto.ExercicioAtualizacaoDTO;
import dio.sistemagestaoacademia.dto.ExercicioCadastroDTO;
import dio.sistemagestaoacademia.dto.ExercicioRespostaDTO;
import dio.sistemagestaoacademia.entity.Exercicio;
import dio.sistemagestaoacademia.repository.ExercicioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ExercicioService {
    private final ExercicioRepository exercicioRepository;

    public ExercicioService(ExercicioRepository exercicioRepository) {
        this.exercicioRepository = exercicioRepository;
    }

    /**
     * Metodo recebe um objeto ExercicioCadastroDTO
     * esse objeto contem os dados enviados pelo usuario nome, descrição e grupoMuscular
     * logo em seguida utilizo o construtor
     * finalizando com tipo de retorno que salva no banco de dados
     * O DTO será utilizado para transportar os dados do cadastro até o Service.
     */
    public ExercicioRespostaDTO cadastrarExercicio(ExercicioCadastroDTO dto) {

        Exercicio exercicio = new Exercicio(
                dto.getNome(),
                dto.getGrupoMuscular(),
                dto.getDescricao()
        );

        Exercicio exercicioSalvo = exercicioRepository.save(exercicio);
        return new ExercicioRespostaDTO(
                exercicioSalvo.getId(),
                exercicioSalvo.getNome(),
                exercicioSalvo.getGrupoMuscular(),
                exercicioSalvo.getDescricao()
        );
    }
    public ExercicioRespostaDTO consultarExercicio(Long id) {

        Exercicio exercicio = exercicioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Exercício não encontrado com ID: " + id
                ));

        return new ExercicioRespostaDTO(
                exercicio.getId(),
                exercicio.getNome(),
                exercicio.getGrupoMuscular(),
                exercicio.getDescricao()
        );
    }
    public List<ExercicioRespostaDTO> listarExercicios() {
        return exercicioRepository.findAll()
                .stream() //permite processar os exercicios encontrados
                .map(exercicio -> new ExercicioRespostaDTO( //Map transforma cada entidade Exercicio em um ExercicioRespostaDTO
                        exercicio.getId(),
                        exercicio.getNome(),
                        exercicio.getGrupoMuscular(),
                        exercicio.getDescricao()
                ))
                .toList(); //Reúne os DTOs em uma lista
    }
    public ExercicioRespostaDTO atualizarExercicio(
            Long id, ExercicioAtualizacaoDTO dto) {

        Exercicio exercicio = exercicioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Exercício não encontrado com ID: " + id
                ));

        exercicio.setNome(dto.getNome());
        exercicio.setGrupoMuscular(dto.getGrupoMuscular());
        exercicio.setDescricao(dto.getDescricao());

        Exercicio exercicioAtualizado = exercicioRepository.save(exercicio);

        return new ExercicioRespostaDTO(
                exercicioAtualizado.getId(),
                exercicioAtualizado.getNome(),
                exercicioAtualizado.getGrupoMuscular(),
                exercicioAtualizado.getDescricao()
        );
    }
    public void ExcluirExercicio(Long id) {
        Exercicio exercicio = exercicioRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Exercício não encontrado com ID: " + id));
        exercicioRepository.delete(exercicio);
    }
}
