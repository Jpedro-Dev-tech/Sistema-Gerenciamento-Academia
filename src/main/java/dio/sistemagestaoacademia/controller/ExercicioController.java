package dio.sistemagestaoacademia.controller;

import dio.sistemagestaoacademia.dto.ExercicioAtualizacaoDTO;
import dio.sistemagestaoacademia.dto.ExercicioCadastroDTO;
import dio.sistemagestaoacademia.dto.ExercicioRespostaDTO;
import dio.sistemagestaoacademia.entity.Exercicio;
import dio.sistemagestaoacademia.service.ExercicioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/exercicios")
@RestController
public class ExercicioController {
    private final ExercicioService exercicioService;

    public  ExercicioController(ExercicioService exercicioService) {
        this.exercicioService = exercicioService;
    }
    @PostMapping
    public ResponseEntity<ExercicioRespostaDTO> cadastrarExercicio(@RequestBody ExercicioCadastroDTO dto) {
        ExercicioRespostaDTO exercicio = exercicioService.cadastrarExercicio(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(exercicio);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ExercicioRespostaDTO> getExercicio(@PathVariable Long id) {
        ExercicioRespostaDTO exercicio = exercicioService.consultarExercicio(id);
        return ResponseEntity.ok(exercicio);
    }
    @GetMapping
    public ResponseEntity<List<ExercicioRespostaDTO>> listarExercicios() {
        List<ExercicioRespostaDTO> exercicios = exercicioService.listarExercicios();
        return ResponseEntity.ok(exercicios);
    }
    @PutMapping("/{id}")//Define o endpoint responsável por atualizar um exercício existente.
    public ResponseEntity<ExercicioRespostaDTO> atulizarExercicio(@PathVariable Long id, @RequestBody ExercicioAtualizacaoDTO dto) {
        ExercicioRespostaDTO exercicio = exercicioService.atualizarExercicio(id, dto);
        return ResponseEntity.ok(exercicio);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarExercicio(@PathVariable Long id) {
        exercicioService.ExcluirExercicio(id);
        return ResponseEntity.noContent().build();
    }
}
