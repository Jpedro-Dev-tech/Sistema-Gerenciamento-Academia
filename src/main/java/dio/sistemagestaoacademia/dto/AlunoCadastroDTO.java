package dio.sistemagestaoacademia.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AlunoCadastroDTO {
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
        private String nome;

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Informe um e-mail válido")
        @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
        private String email;

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        private String senha;
        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
        private String telefone;
        @NotNull(message = "A data de nascimento é obrigatória")
        @Past(message = "A data de nascimento deve ser anterior à data atual")
        private LocalDate dataNascimento;
        @NotBlank(message = "O sexo é obrigatório")
        @Pattern(regexp = "^[MF]$", message = "O sexo deve ser M ou F")
        private String sexo;
        @NotBlank(message = "A matrícula é obrigatória")
        @Size(max = 30, message = "A matrícula deve ter no máximo 30 caracteres")
        private String matricula;
}
