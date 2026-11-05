package dio.sistemagestaoacademia.service;

import dio.sistemagestaoacademia.entity.Aluno;
import dio.sistemagestaoacademia.entity.CheckIn;
import dio.sistemagestaoacademia.entity.ItemTreino;
import dio.sistemagestaoacademia.entity.Treino;
import dio.sistemagestaoacademia.repository.AlunoRepository;
import dio.sistemagestaoacademia.repository.CheckInRepository;
import dio.sistemagestaoacademia.repository.ItemTreinoRepository;
import dio.sistemagestaoacademia.repository.TreinoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class CheckInService {
    private final CheckInRepository checkInRepository;
    private final AlunoRepository alunoRepository;
    private final ItemTreinoRepository itemTreinoRepository;
    private final TreinoRepository treinoRepository;

    public CheckInService(CheckInRepository checkInRepository,
                          AlunoRepository alunoRepository,
                          TreinoRepository treinoRepository,
                          ItemTreinoRepository itemTreinoRepository)
    {
        this.checkInRepository = checkInRepository;
        this.alunoRepository = alunoRepository;
        this.itemTreinoRepository = itemTreinoRepository;
        this.treinoRepository = treinoRepository;
    }
    @Transactional
    public void registrarCheckIn(Long treinoId) {

        // 1. Identificar o usuário autenticado.
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean ehAluno = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ALUNO"));

        if (!ehAluno) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Apenas alunos podem registrar check-in."
            );
        }

        // 2. Buscar o aluno autenticado.
        Aluno aluno = alunoRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado."
                ));

        // 3. Buscar o treino informado.
        Treino treino = treinoRepository.findById(treinoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Treino não encontrado."
                ));

        // 4. Verificar se o treino pertence ao aluno.
        if (!treino.getAluno().getId().equals(aluno.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Este treino não pertence ao aluno autenticado."
            );
        }

        // 5. Verificar se já existe check-in hoje (RN06).
        LocalDate hoje = LocalDate.now();

        if (checkInRepository.existsByAlunoIdAndData(aluno.getId(), hoje)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "O aluno já realizou um check-in hoje."
            );
        }

        // 6. Buscar os exercícios do treino.
        List<ItemTreino> itens =
                itemTreinoRepository.findByTreinoIdOrderByOrdemAsc(treinoId);

        // Impedir check-in em treinos sem exercícios.
        if (itens.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O treino não possui exercícios cadastrados."
            );
        }

        // 7. Verificar se todos os exercícios foram concluídos (RN05).
        boolean todosConcluidos = itens.stream()
                .allMatch(item -> Boolean.TRUE.equals(item.getConcluido()));

        if (!todosConcluidos) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Todos os exercícios devem estar concluídos para registrar o check-in."
            );
        }

        // 8. Criar o check-in com exatamente 1 ponto (RN08).
        CheckIn checkIn = new CheckIn(aluno, hoje, 1, treino);

        checkInRepository.save(checkIn);

        // 9. Reiniciar os exercícios após o check-in (RN07).
        for (ItemTreino item : itens) {
            item.setConcluido(false);
        }

        itemTreinoRepository.saveAll(itens);
    }
}
