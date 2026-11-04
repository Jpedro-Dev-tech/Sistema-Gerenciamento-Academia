package dio.sistemagestaoacademia.controller;

import dio.sistemagestaoacademia.repository.CheckInRepository;
import dio.sistemagestaoacademia.service.CheckInService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/check-ins")
public class CheckInController {
    private final CheckInService checkInService;

    public CheckInController (CheckInService checkInService) {
        this.checkInService = checkInService;
    }
    @PostMapping
    public ResponseEntity<Void> registrarCheckin(@PathVariable Long treinoId) {
        checkInService.registrarCheckIn(treinoId);
        return ResponseEntity.noContent().build();
    }
}
