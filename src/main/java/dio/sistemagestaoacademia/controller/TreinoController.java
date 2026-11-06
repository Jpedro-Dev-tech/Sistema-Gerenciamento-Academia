package dio.sistemagestaoacademia.controller;
import dio.sistemagestaoacademia.dto.TreinoAtualizacaoDTO;
import dio.sistemagestaoacademia.dto.TreinoCadastroDTO;
import dio.sistemagestaoacademia.dto.TreinoRespostaDTO;
import dio.sistemagestaoacademia.service.TreinoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

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
    @GetMapping("/meus-treinos")//Define uma rota HTTP para consultar os treinos do aluno autenticado.
    public ResponseEntity<List<TreinoRespostaDTO>> visualizarTreino() {
        List<TreinoRespostaDTO> treinos = treinoService.visualizarTreino();
        return ResponseEntity.ok(treinos);
    }
    @GetMapping("/{id}/ficha-pdf")
    public ResponseEntity<byte[]> gerarFichaPDF(@PathVariable Long id) {

        // Solicita ao Service a geração do PDF
        byte[] pdf = treinoService.gerarFichaPDF(id);

        // Define o nome do arquivo
        String nomeArquivo = "ficha_treino_" + id + ".pdf";

        // Retorna o arquivo PDF para download
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + nomeArquivo + "\""
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }
}
