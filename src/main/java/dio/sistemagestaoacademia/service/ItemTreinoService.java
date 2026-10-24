package dio.sistemagestaoacademia.service;

import dio.sistemagestaoacademia.repository.ItemTreinoRepository;
import org.springframework.stereotype.Service;

@Service //Informa ao Spring Boot que essa classe pertence à camada de regras de negócio.
public class ItemTreinoService {
    private final ItemTreinoRepository itemTreinoRepository;
    public ItemTreinoService(ItemTreinoRepository itemTreinoRepository) {
       this.itemTreinoRepository = itemTreinoRepository;
    }
}
