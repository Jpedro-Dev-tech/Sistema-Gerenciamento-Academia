package dio.sistemagestaoacademia.controller;

import dio.sistemagestaoacademia.dto.ItemTreinoCadastroDTO;
import dio.sistemagestaoacademia.dto.ItemTreinoRespostaDTO;
import dio.sistemagestaoacademia.entity.ItemTreino;
import dio.sistemagestaoacademia.service.ItemTreinoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
