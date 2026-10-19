package dio.sistemagestaoacademia.controller;
import dio.sistemagestaoacademia.dto.TreinoAtualizacaoDTO;
import dio.sistemagestaoacademia.dto.TreinoCadastroDTO;
import dio.sistemagestaoacademia.dto.TreinoRespostaDTO;
import dio.sistemagestaoacademia.service.TreinoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/treinos")
@RestController
public class TreinoController {
    private final TreinoService treinoService;

    public TreinoController(TreinoService treinoService) {
        this.treinoService = treinoService;
    }
    @PostMapping
    public ResponseEntity<TreinoRespostaDTO> criarTreino(@Valid @RequestBody TreinoCadastroDTO dto){

        TreinoRespostaDTO treino = treinoService.criarTreino(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(treino);
    }
    @PutMapping("/{id}")
    public ResponseEntity<TreinoRespostaDTO> alterarTreino(@PathVariable Long id, @Valid @RequestBody TreinoAtualizacaoDTO dto){
        TreinoRespostaDTO treino = treinoService.alterarTreino(id, dto);
        return ResponseEntity.ok(treino);
    }
}
