package dio.sistemagestaoacademia.service;

import dio.sistemagestaoacademia.dto.TreinoAtualizacaoDTO;
import dio.sistemagestaoacademia.dto.TreinoCadastroDTO;
import dio.sistemagestaoacademia.dto.TreinoRespostaDTO;
import dio.sistemagestaoacademia.entity.Aluno;
import dio.sistemagestaoacademia.entity.Instrutor;
import dio.sistemagestaoacademia.entity.Treino;
import dio.sistemagestaoacademia.repository.AlunoRepository;
import dio.sistemagestaoacademia.repository.InstrutorRepository;
import dio.sistemagestaoacademia.repository.ItemTreinoRepository;
import dio.sistemagestaoacademia.repository.TreinoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import dio.sistemagestaoacademia.entity.ItemTreino;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TreinoService {
    private final TreinoRepository treinoRepository;
    private final AlunoRepository alunoRepository;
    private final InstrutorRepository instrutorRepository;
    private final ItemTreinoRepository itemTreinoRepository;
    private Boolean ativo;

    public TreinoService(AlunoRepository alunoRepository, InstrutorRepository instrutorRepository, TreinoRepository treinoRepository, ItemTreinoRepository itemTreinoRepository) {
        this.alunoRepository = alunoRepository;
        this.instrutorRepository = instrutorRepository;
        this.treinoRepository = treinoRepository;
        this.itemTreinoRepository = itemTreinoRepository;
    }
    public TreinoRespostaDTO criarTreino(TreinoCadastroDTO dto) {
        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Aluno não encontrado com ID: " + dto.getAlunoId()));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String emailInstrutor = authentication.getName();

        Instrutor instrutor = instrutorRepository.findByEmail(emailInstrutor)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Instrutor autenticado não encontrado."
                ));

        if (!instrutor.getId().equals(dto.getInstrutorId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "O instrutor informado não corresponde ao instrutor autenticado."
            );
        }

        Treino treino = new Treino();

        treino.setAluno(aluno);
        treino.setInstrutor(instrutor);
        treino.setNome(dto.getNome());
        treino.setDescricao(dto.getDescricao());
        treino.setObjetivo(dto.getObjetivo());
        treino.setDiaDaSemana(dto.getDiaDaSemana());
        treino.setDataCriacao(LocalDateTime.now());
        treino.setDataAtualizacao(null);
        treino.setAtivo(true);

        Treino treinoSalvo = treinoRepository.save(treino);

        TreinoRespostaDTO resposta = new TreinoRespostaDTO();

        resposta.setId(treinoSalvo.getId());
        resposta.setAlunoId(treinoSalvo.getAluno().getId());
        resposta.setInstrutorId(treinoSalvo.getInstrutor().getId());
        resposta.setNome(treinoSalvo.getNome());
        resposta.setDescricao(treinoSalvo.getDescricao());
        resposta.setDiaDaSemana(treinoSalvo.getDiaDaSemana());
        resposta.setObjetivo(treinoSalvo.getObjetivo());
        resposta.setDataCriacao(treinoSalvo.getDataCriacao());
        resposta.setDataAtualizacao(treinoSalvo.getDataAtualizacao());
        resposta.setAtivo(treinoSalvo.getAtivo());

        return resposta;

    }
    public TreinoRespostaDTO alterarTreino(Long id, TreinoAtualizacaoDTO dto) {
        // Busca o treino no banco de dados
        Treino treino = treinoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Treino não encontrado com ID: " + id
                ));
        // Identifica o instrutor autenticado
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String emailInstrutor = authentication.getName();

        // Verifica se o instrutor autenticado é responsável pelo treino
        if (!treino.getInstrutor().getEmail().equals(emailInstrutor)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não tem permissão para alterar este treino."
            );
        }
        // Atualiza os dados do treino
        treino.setNome(dto.getNome());
        treino.setDescricao(dto.getDescricao());
        treino.setObjetivo(dto.getObjetivo());
        treino.setDiaDaSemana(dto.getDiaDaSemana());
        // Atualiza o estado do treino somente se informado no DTO
        if (dto.getAtivo() != null) {
            treino.setAtivo(dto.getAtivo());
        }
        // Registra a data e o horário da atualização
        treino.setDataAtualizacao(LocalDateTime.now());

        // Salva as alterações no PostgreSQL
        Treino treinoAtualizado = treinoRepository.save(treino);

        // Converte a entidade para o DTO de resposta
        TreinoRespostaDTO resposta = new TreinoRespostaDTO();

        resposta.setId(treinoAtualizado.getId());
        resposta.setAlunoId(treinoAtualizado.getAluno().getId());
        resposta.setInstrutorId(treinoAtualizado.getInstrutor().getId());
        resposta.setNome(treinoAtualizado.getNome());
        resposta.setDescricao(treinoAtualizado.getDescricao());
        resposta.setDiaDaSemana(treinoAtualizado.getDiaDaSemana());
        resposta.setObjetivo(treinoAtualizado.getObjetivo());
        resposta.setDataCriacao(treinoAtualizado.getDataCriacao());
        resposta.setDataAtualizacao(treinoAtualizado.getDataAtualizacao());
        resposta.setAtivo(treinoAtualizado.getAtivo());

        return resposta;
    }
    public List<TreinoRespostaDTO> visualizarTreino() {

        // Identifica o usuário autenticado
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // Verifica se o usuário possui o perfil ALUNO
        boolean ehAluno = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ALUNO"));

        if (!ehAluno) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Somente alunos podem visualizar seus treinos."
            );
        }

        // Obtém o e-mail do aluno autenticado
        String emailAluno = authentication.getName();

        // Busca o aluno no PostgreSQL
        Aluno aluno = alunoRepository.findByEmail(emailAluno)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno autenticado não encontrado."
                ));

        // Busca somente os treinos pertencentes ao aluno
        List<Treino> treinos = treinoRepository.findByAlunoId(aluno.getId());

        // Converte os treinos encontrados para DTOs de resposta
        return treinos.stream()
                .map(treino -> {

                    TreinoRespostaDTO resposta = new TreinoRespostaDTO();

                    resposta.setId(treino.getId());
                    resposta.setAlunoId(treino.getAluno().getId());
                    resposta.setInstrutorId(treino.getInstrutor().getId());
                    resposta.setNome(treino.getNome());
                    resposta.setDescricao(treino.getDescricao());
                    resposta.setObjetivo(treino.getObjetivo());
                    resposta.setDiaDaSemana(treino.getDiaDaSemana());
                    resposta.setDataCriacao(treino.getDataCriacao());
                    resposta.setDataAtualizacao(treino.getDataAtualizacao());
                    resposta.setAtivo(treino.getAtivo());

                    return resposta;
                })
                .toList();
    }
    @Transactional(readOnly = true)
    public byte[] gerarFichaPDF(Long treinoId) {

        // Busca o treino no PostgreSQL
        Treino treino = treinoRepository.findById(treinoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Treino não encontrado com ID: " + treinoId
                ));

        // Identifica o usuário autenticado
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String emailUsuario = authentication.getName();

        // Verifica os perfis do usuário
        boolean ehAluno = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ALUNO"));

        boolean ehInstrutor = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_INSTRUTOR"));

        // Verifica se o aluno é o proprietário do treino
        boolean alunoAutorizado = ehAluno
                && treino.getAluno().getEmail().equals(emailUsuario);

        // Verifica se o instrutor é responsável pelo treino
        boolean instrutorAutorizado = ehInstrutor
                && treino.getInstrutor().getEmail().equals(emailUsuario);

        // Bloqueia usuários sem permissão
        if (!alunoAutorizado && !instrutorAutorizado) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não tem permissão para gerar a ficha deste treino."
            );
        }

        // Consulta os exercícios associados ao treino
        List<ItemTreino> itens =
                itemTreinoRepository.findByTreinoIdOrderByOrdemAsc(treinoId);
        // Armazena o PDF em memória
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document documento = new Document();

        try {

            // Associa o documento ao fluxo de saída
            PdfWriter.getInstance(documento, outputStream);

            // Abre o documento para escrita
            documento.open();

            // Título da ficha
            documento.add(new Paragraph("FICHA DE TREINO"));
            documento.add(new Paragraph(" "));

            // Informações do aluno e do instrutor
            documento.add(new Paragraph(
                    "Aluno: " + treino.getAluno().getNome()
            ));

            documento.add(new Paragraph(
                    "Instrutor: " + treino.getInstrutor().getNome()
            ));

            // Informações do treino
            documento.add(new Paragraph(
                    "Treino: " + treino.getNome()
            ));

            documento.add(new Paragraph(
                    "Objetivo: " + (treino.getObjetivo() != null
                            ? treino.getObjetivo() : "Não informado")
            ));

            documento.add(new Paragraph(
                    "Dia da semana: " + (treino.getDiaDaSemana() != null
                            ? treino.getDiaDaSemana() : "Não informado")
            ));

            documento.add(new Paragraph(" "));

            // Cria uma tabela com cinco colunas
            PdfPTable tabela = new PdfPTable(5);
            tabela.setWidthPercentage(100);

            // Cabeçalhos da tabela
            tabela.addCell(new PdfPCell(new Phrase("Ordem")));
            tabela.addCell(new PdfPCell(new Phrase("Exercício")));
            tabela.addCell(new PdfPCell(new Phrase("Séries")));
            tabela.addCell(new PdfPCell(new Phrase("Repetições")));
            tabela.addCell(new PdfPCell(new Phrase("Carga (kg)")));

            // Adiciona os exercícios do treino
            for (ItemTreino item : itens) {

                tabela.addCell(String.valueOf(item.getOrdem()));

                tabela.addCell(item.getExercicio().getNome());

                tabela.addCell(String.valueOf(item.getSeries()));

                tabela.addCell(String.valueOf(item.getRepeticoes()));

                tabela.addCell(item.getCarga() != null
                        ? String.valueOf(item.getCarga())
                        : "-");
            }

            // Insere a tabela no documento
            documento.add(tabela);

            documento.add(new Paragraph(" "));

            // Adiciona as observações dos exercícios
            documento.add(new Paragraph("OBSERVAÇÕES"));

            for (ItemTreino item : itens) {

                if (item.getObservacoes() != null
                        && !item.getObservacoes().isBlank()) {

                    documento.add(new Paragraph(
                            item.getOrdem() + " - "
                                    + item.getExercicio().getNome()
                                    + ": " + item.getObservacoes()
                    ));
                }
            }

        } catch (DocumentException e) {

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao gerar a ficha de treino em PDF.",
                    e
            );

        } finally {

            // Fecha o documento
            documento.close();
        }

         // Retorna o PDF como um array de bytes
        return outputStream.toByteArray();
    }
}
