package dio.sistemagestaoacademia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter // Getter e Setter São anotações do Lombok que geram automaticamente os métodos de acesso aos atributos.
@Setter
public class InstrutorRespostaDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String registroProfissional;
    private Boolean ativo;
}