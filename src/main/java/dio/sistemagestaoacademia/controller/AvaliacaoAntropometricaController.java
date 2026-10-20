package dio.sistemagestaoacademia.controller;

import dio.sistemagestaoacademia.dto.AvaliacaoCadastroDTO;
import dio.sistemagestaoacademia.dto.AvaliacaoRespostaDTO;
import dio.sistemagestaoacademia.service.AvaliacaoAntropometricaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoAntropometricaController {
    private final AvaliacaoAntropometricaService avaliacaoAntropometricaService;

    public AvaliacaoAntropometricaController(AvaliacaoAntropometricaService avaliacaoAntropometricaService) {
        this.avaliacaoAntropometricaService = avaliacaoAntropometricaService;
    }
    @PostMapping
    public ResponseEntity<AvaliacaoRespostaDTO> registrarAvaliacao(
            @Valid @RequestBody AvaliacaoCadastroDTO dto) {
        AvaliacaoRespostaDTO resposta = avaliacaoAntropometricaService.registrarAvaliacao(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resposta);
    }
    @GetMapping("/historico/{alunoId}")//Define a rota de consulta
    public ResponseEntity<List<AvaliacaoRespostaDTO>> consultarHistorico(@PathVariable Long alunoId) {
        List<AvaliacaoRespostaDTO> historico =avaliacaoAntropometricaService.consultarHistorico(alunoId);
        return ResponseEntity.ok(historico);//Retorna um 200 OK se tudo estiver Ok
    }

}
