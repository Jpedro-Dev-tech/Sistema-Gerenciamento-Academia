package dio.sistemagestaoacademia.controller;

import dio.sistemagestaoacademia.dto.AlunoAtualizacaoDTO;
import dio.sistemagestaoacademia.dto.AlunoCadastroDTO;
import dio.sistemagestaoacademia.dto.AlunoRespostaDTO;
import dio.sistemagestaoacademia.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //define a classe como um controlador REST, permitindo que ela manipule requisições HTTP e retorne respostas no formato JSON.
@RequestMapping("/api/alunos") //define o caminho base para todas as rotas do controlador. Neste caso, todas as rotas começarão com "/api/alunos".
public class AlunoController {
    private final AlunoService alunoService;
    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }
    @PostMapping
    public ResponseEntity<AlunoRespostaDTO> cadastrarAluno(@Valid @RequestBody AlunoCadastroDTO dto) { //@Valid Executa as validações definidas no DTO
        AlunoRespostaDTO aluno = alunoService.cadastrarAluno(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(aluno);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AlunoRespostaDTO> buscarAlunoPorId(@PathVariable Long id) {
        AlunoRespostaDTO aluno = alunoService.buscarAlunoPorId(id);
        return ResponseEntity.ok(aluno);
    }
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<AlunoRespostaDTO>> buscarAlunoPorNome(@PathVariable String nome) {
        List<AlunoRespostaDTO> alunos = alunoService.buscarAlunoPorNome(nome);
        return ResponseEntity.ok(alunos);
    }
    @PutMapping("/{id}")
    public ResponseEntity<AlunoRespostaDTO> atualizarAluno(
            @PathVariable Long id,
            @Valid @RequestBody AlunoAtualizacaoDTO dto) {
        AlunoRespostaDTO aluno = alunoService.atualizarAluno(id, dto);
        return ResponseEntity.ok(aluno);
    }
}
