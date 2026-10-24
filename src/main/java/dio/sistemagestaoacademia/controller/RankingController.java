package dio.sistemagestaoacademia.controller;

import dio.sistemagestaoacademia.dto.RankingClassificacaoDTO;
import dio.sistemagestaoacademia.service.RankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/rankings")
@RestController
public class RankingController {
    private RankingService rankingService;
    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<List<RankingClassificacaoDTO>> consultarRanking(@PathVariable Long id){
        List<RankingClassificacaoDTO> classificacao = rankingService.consultarRanking(id);
        return ResponseEntity.ok(classificacao);
    }
}
