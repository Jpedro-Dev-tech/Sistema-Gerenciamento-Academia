package dio.sistemagestaoacademia.controller;

import dio.sistemagestaoacademia.dto.InstrutorCadastroDTO;
import dio.sistemagestaoacademia.dto.InstrutorLoginDTO;
import dio.sistemagestaoacademia.dto.InstrutorRespostaDTO;
import dio.sistemagestaoacademia.service.InstrutorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/instrutores")
public class InstrutorController {
    private InstrutorService instrutorService;
    public InstrutorController(InstrutorService instrutorService) {
        this.instrutorService = instrutorService;
    }
    @PostMapping
    public ResponseEntity<InstrutorRespostaDTO> cadastrarInstrutor(@Valid @RequestBody InstrutorCadastroDTO dto) {
        InstrutorRespostaDTO instrutor = instrutorService.cadastrarInstrutor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(instrutor);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody InstrutorLoginDTO dto) {

        boolean autenticado = instrutorService.login(
                dto.getEmail(),
                dto.getSenha()
        );

        if (!autenticado) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("E-mail ou senha inválidos.");
        }

        return ResponseEntity.ok("Login realizado com sucesso.");
    }
}
