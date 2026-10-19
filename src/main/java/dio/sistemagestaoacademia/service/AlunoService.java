package dio.sistemagestaoacademia.service;

import dio.sistemagestaoacademia.dto.AlunoAtualizacaoDTO;
import dio.sistemagestaoacademia.dto.AlunoCadastroDTO;
import dio.sistemagestaoacademia.entity.Aluno;
import dio.sistemagestaoacademia.repository.AlunoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import dio.sistemagestaoacademia.dto.AlunoRespostaDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AlunoService {
    private final AlunoRepository alunoRepository;
    private final PasswordEncoder passwordEncoder;
    public AlunoService(AlunoRepository alunoRepository, PasswordEncoder passwordEncoder) {
        this.alunoRepository = alunoRepository; //Responsável por interagir com o banco de dados para realizar operações relacionadas aos alunos
        this.passwordEncoder = passwordEncoder; //Responsável por criptografar a senha do aluno antes de salvar no banco de dados
    }
    private AlunoRespostaDTO converterParaDTO(Aluno aluno) {

        AlunoRespostaDTO dto = new AlunoRespostaDTO();

        dto.setId(aluno.getId());
        dto.setNome(aluno.getNome());
        dto.setEmail(aluno.getEmail());
        dto.setTelefone(aluno.getTelefone());
        dto.setDataNascimento(aluno.getDataNascimento());
        dto.setSexo(aluno.getSexo());
        dto.setMatricula(aluno.getMatricula());
        dto.setAtivo(aluno.getAtivo());

        return dto;
    }
    public AlunoRespostaDTO buscarAlunoPorId(Long id) {

        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado com ID: " + id
                ));

        return converterParaDTO(aluno);
    }

    public List<AlunoRespostaDTO> buscarAlunoPorNome(String nome) {
       List<Aluno> alunos = alunoRepository.findByNome(nome);//finndByNome consulta os alunos no POSTGRES

       return alunos.stream()//Stream permite processar os elementos da lista
               .map(this::converterParaDTO) //converte cada Aluno em AlunoRespostaDTO.
               .toList(); //cria uma lista e retorna os resultado
    }
    public AlunoRespostaDTO cadastrarAluno(AlunoCadastroDTO dto) {
        if (alunoRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Já existe um aluno cadastrado com o e-mail: " + dto.getEmail()
            ); //interrompe a execução do método e lança uma exceção
        }
        Aluno aluno = new Aluno();
        aluno.setNome(dto.getNome());
        aluno.setEmail(dto.getEmail());
        aluno.setTelefone(dto.getTelefone());
        aluno.setDataNascimento(dto.getDataNascimento());
        aluno.setSexo(dto.getSexo());
        aluno.setMatricula(dto.getMatricula());
        aluno.setSenhaHash(passwordEncoder.encode(dto.getSenha())); //Criptografando a senha do aluno antes de salvar no banco de dados
        aluno.setAtivo(true); //Definindo o status do aluno como ativo
        return converterParaDTO(alunoRepository.save(aluno)); //Salva o aluno no banco de dados e retorna o DTO
        // correspondente e transforma a entidade salva em um DTO de resposta.

    }
    public void deletarAluno(Long id) {
        if(!alunoRepository.existsById(id)) {
            throw new RuntimeException("Aluno não encontrado com ID: " + id);
        }
        alunoRepository.deleteById(id);
    }
    public AlunoRespostaDTO atualizarAluno(Long id, AlunoAtualizacaoDTO dto) {
       Aluno alunoExistente = alunoRepository.findById(id).orElseThrow((
) -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Aluno não encontrado com ID: " + id));
       if (alunoRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
           throw new ResponseStatusException(HttpStatus.CONFLICT, "Este e-mail já está cadastrado por outro aluno.");
       }
            alunoExistente.setNome(dto.getNome());
            alunoExistente.setEmail(dto.getEmail());
            alunoExistente.setTelefone(dto.getTelefone());
            alunoExistente.setDataNascimento(dto.getDataNascimento());
            alunoExistente.setSexo(dto.getSexo());
            alunoExistente.setMatricula(dto.getMatricula());
        return converterParaDTO(alunoRepository.save(alunoExistente)); //persistindo as alterações no banco de dados
    }
}
