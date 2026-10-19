package dio.sistemagestaoacademia.service;

import dio.sistemagestaoacademia.dto.InstrutorCadastroDTO;
import dio.sistemagestaoacademia.dto.InstrutorRespostaDTO;
import dio.sistemagestaoacademia.entity.Instrutor;
import dio.sistemagestaoacademia.repository.InstrutorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service //identifica a classe como responsável pelas regras de negócio.
public class InstrutorService {
    private final InstrutorRepository instrutorRepository;
    private final PasswordEncoder passwordEncoder;

    public InstrutorService(InstrutorRepository instrutorRepository, PasswordEncoder passwordEncoder) {
        this.instrutorRepository = instrutorRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public InstrutorRespostaDTO cadastrarInstrutor(InstrutorCadastroDTO dto) { //recebe os dados do cadastro, cria a entidade e salva o instrutor.
        if (instrutorRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Este e-mail já está cadastrado por outro instrutor."
            );
        }
        Instrutor instrutor = new Instrutor();

        instrutor.setNome(dto.getNome());
        instrutor.setEmail(dto.getEmail());
        instrutor.setSenhaHash(passwordEncoder.encode(dto.getSenha()));
        instrutor.setTelefone(dto.getTelefone());
        instrutor.setRegistroProfissional(dto.getRegistroProfissional());
        instrutor.setAtivo(true);

        Instrutor instrutorSalvo = instrutorRepository.save(instrutor);

        return converterParaDTO(instrutorSalvo);
    }
    public InstrutorRespostaDTO converterParaDTO(Instrutor instrutor) { //transforma a entidade em um objeto de resposta, sem incluir o hash da senha
        InstrutorRespostaDTO dto = new InstrutorRespostaDTO();

        dto.setId(instrutor.getId());
        dto.setNome(instrutor.getNome());
        dto.setEmail(instrutor.getEmail());
        dto.setTelefone(instrutor.getTelefone());
        dto.setRegistroProfissional(instrutor.getRegistroProfissional());
        dto.setAtivo(instrutor.getAtivo());

        return dto;
    }
    public boolean login(String email, String senha) {
        Optional<Instrutor> instrutorOptional = instrutorRepository.findByEmail(email);

        if (instrutorOptional.isEmpty()) {
            return false;
        }
        Instrutor instrutor = instrutorOptional.get();

        if (!Boolean.TRUE.equals(instrutor.getAtivo())) {
            return false;
        }
        return passwordEncoder.matches(senha, instrutor.getSenhaHash());
    }
}
