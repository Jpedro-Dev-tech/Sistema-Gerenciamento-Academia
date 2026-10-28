package dio.sistemagestaoacademia.controller;
import dio.sistemagestaoacademia.dto.ItemTreinoCadastroDTO;
import dio.sistemagestaoacademia.dto.ItemTreinoRespostaDTO;
import dio.sistemagestaoacademia.service.ItemTreinoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/itens-treino")
@RestController
public class ItemTreinoController {
    private final ItemTreinoService itemTreinoService;
    public ItemTreinoController(ItemTreinoService itemTreinoService) {
        this.itemTreinoService = itemTreinoService;
    }
    @PostMapping
    public ResponseEntity<ItemTreinoRespostaDTO> adicionarExercicio(@RequestBody ItemTreinoCadastroDTO dto){
         ItemTreinoRespostaDTO resposta = itemTreinoService.adicionarExercicio(dto);
         return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerExercicio(@PathVariable Long id){
        itemTreinoService.removerExercicio(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/treino/{treinoId}")
    public ResponseEntity<List<ItemTreinoRespostaDTO>> listarExerciciosPorTreino(
            @PathVariable Long treinoId) {
        List<ItemTreinoRespostaDTO> resposta = itemTreinoService.listarExerciciosPorTreino(treinoId);
        return ResponseEntity.ok(resposta);
    }
}
