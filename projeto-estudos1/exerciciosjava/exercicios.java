import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;

public class exercicios {

    static final String ARQUIVO = "tarefas.txt";
    static final int MAX_TAREFAS = 100;
    static final int MAX_CARACTERES = 100;
    static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    static ArrayList<String> tarefas = new ArrayList<>();
    static ArrayList<Boolean> concluidas = new ArrayList<>();
    static ArrayList<LocalDateTime> datasCriacao = new ArrayList<>();
    static ArrayList<LocalDateTime> datasExpiracao = new ArrayList<>();

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        carregarTarefas();
        verificarExpiradas();
        int opcao;
        do {
            mostrarMenu();
            opcao = lerInteiro();
            switch (opcao) {
                case 1: adicionarTarefa(); break;
                case 2: listarTarefas(); break;
                case 3: editarTarefa(); break;
                case 4: marcarConcluida(); break;
                case 5: buscarTarefa(); break;
                case 6: removerTarefa(); break;
                case 7: System.out.println("\nAte logo!"); break;
                default: System.out.println("\nOpcao invalida. Tente novamente.");
            }
        } while (opcao != 7);
    }

    static void mostrarMenu() {
        System.out.println("\n=============================");
        System.out.println("     GERENCIADOR DE TAREFAS  ");
        System.out.println("=============================");
        System.out.println(" 1. Adicionar tarefa");
        System.out.println(" 2. Listar tarefas");
        System.out.println(" 3. Editar tarefa");
        System.out.println(" 4. Marcar como concluida");
        System.out.println(" 5. Buscar tarefa");
        System.out.println(" 6. Remover tarefa");
        System.out.println(" 7. Sair");
        System.out.println("=============================");
        System.out.print("Escolha uma opcao: ");
    }

    static void adicionarTarefa() {
        if (tarefas.size() >= MAX_TAREFAS) {
            System.out.println("\nLimite de " + MAX_TAREFAS + " tarefas atingido.");
            return;
        }
        System.out.print("\nDigite a tarefa (max " + MAX_CARACTERES + " caracteres): ");
        String tarefa = sc.nextLine().trim();
        if (tarefa.isEmpty()) {
            System.out.println("Tarefa nao pode ser vazia.");
            return;
        }
        if (tarefa.length() > MAX_CARACTERES) {
            System.out.println("Tarefa muito longa. Maximo de " + MAX_CARACTERES + " caracteres.");
            return;
        }

        LocalDateTime expiracao = null;
        System.out.print("Deseja definir uma data de expiracao? (s/n): ");
        String resp = sc.nextLine().trim().toLowerCase();
        if (resp.equals("s")) {
            expiracao = lerDataExpiracao();
            if (expiracao == null) return;
        }

        tarefas.add(tarefa);
        concluidas.add(false);
        LocalDateTime criadaEm = LocalDateTime.now();
        datasCriacao.add(criadaEm);
        datasExpiracao.add(expiracao);
        salvarTarefas();
        System.out.println("Tarefa adicionada em: " + criadaEm.format(FORMATO));
        if (expiracao != null)
            System.out.println("Expira em: " + expiracao.format(FORMATO));
    }

    static LocalDateTime lerDataExpiracao() {
        System.out.println("Formato: dd/MM/yyyy HH:mm (ex: 31/12/2025 18:00)");
        System.out.print("Data de expiracao: ");
        String entrada = sc.nextLine().trim();
        try {
            LocalDateTime data = LocalDateTime.parse(entrada, FORMATO);
            if (data.isBefore(LocalDateTime.now())) {
                System.out.println("A data de expiracao deve ser no futuro.");
                return null;
            }
            return data;
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data invalido.");
            return null;
        }
    }

    static void listarTarefas() {
        if (tarefas.isEmpty()) {
            System.out.println("\nNenhuma tarefa cadastrada.");
            return;
        }
        System.out.println("\n--- Lista de Tarefas ---");
        for (int i = 0; i < tarefas.size(); i++) {
            String status = concluidas.get(i) ? "[X]" : "[ ]";
            String criacao = "  Criada em: " + datasCriacao.get(i).format(FORMATO);
            String expiracao = datasExpiracao.get(i) != null
                ? " | Expira em: " + datasExpiracao.get(i).format(FORMATO) + expiracaoStatus(i)
                : "";
            System.out.println((i + 1) + ". " + status + " " + tarefas.get(i));
            System.out.println(criacao + expiracao);
        }
        long pendentes = concluidas.stream().filter(c -> !c).count();
        System.out.println("\nTotal: " + tarefas.size() + " | Pendentes: " + pendentes + " | Concluidas: " + (tarefas.size() - pendentes));
    }

    static String expiracaoStatus(int i) {
        LocalDateTime exp = datasExpiracao.get(i);
        if (exp == null) return "";
        if (LocalDateTime.now().isAfter(exp)) return " [EXPIRADA]";
        return "";
    }

    static void verificarExpiradas() {
        boolean removeu = false;
        for (int i = tarefas.size() - 1; i >= 0; i--) {
            LocalDateTime exp = datasExpiracao.get(i);
            if (exp != null && LocalDateTime.now().isAfter(exp) && !concluidas.get(i)) {
                System.out.println("\n[AVISO] Tarefa expirada removida: \"" + tarefas.get(i) + "\" (expirou em " + exp.format(FORMATO) + ")");
                tarefas.remove(i);
                concluidas.remove(i);
                datasCriacao.remove(i);
                datasExpiracao.remove(i);
                removeu = true;
            }
        }
        if (removeu) salvarTarefas();
    }

    static void editarTarefa() {
        if (tarefas.isEmpty()) {
            System.out.println("\nNenhuma tarefa cadastrada.");
            return;
        }
        listarTarefas();
        System.out.print("\nDigite o numero da tarefa a editar: ");
        int index = lerInteiro();
        if (index < 1 || index > tarefas.size()) {
            System.out.println("Numero invalido.");
            return;
        }
        System.out.print("Nova descricao: ");
        String nova = sc.nextLine().trim();
        if (nova.isEmpty()) {
            System.out.println("Descricao nao pode ser vazia.");
            return;
        }
        if (nova.length() > MAX_CARACTERES) {
            System.out.println("Tarefa muito longa. Maximo de " + MAX_CARACTERES + " caracteres.");
            return;
        }

        System.out.print("Deseja alterar a data de expiracao? (s/n): ");
        String resp = sc.nextLine().trim().toLowerCase();
        if (resp.equals("s")) {
            System.out.print("Deseja definir uma nova data de expiracao? (s/n): ");
            String resp2 = sc.nextLine().trim().toLowerCase();
            if (resp2.equals("s")) {
                LocalDateTime novaExp = lerDataExpiracao();
                if (novaExp == null) return;
                datasExpiracao.set(index - 1, novaExp);
            } else {
                datasExpiracao.set(index - 1, null);
            }
        }

        tarefas.set(index - 1, nova);
        salvarTarefas();
        System.out.println("Tarefa editada com sucesso!");
    }

    static void marcarConcluida() {
        if (tarefas.isEmpty()) {
            System.out.println("\nNenhuma tarefa cadastrada.");
            return;
        }
        listarTarefas();
        System.out.print("\nDigite o numero da tarefa: ");
        int index = lerInteiro();
        if (index < 1 || index > tarefas.size()) {
            System.out.println("Numero invalido.");
            return;
        }
        boolean atual = concluidas.get(index - 1);
        concluidas.set(index - 1, !atual);
        salvarTarefas();
        System.out.println("Tarefa marcada como " + (!atual ? "concluida" : "pendente") + "!");
    }

    static void buscarTarefa() {
        if (tarefas.isEmpty()) {
            System.out.println("\nNenhuma tarefa cadastrada.");
            return;
        }
        System.out.print("\nDigite o termo de busca: ");
        String termo = sc.nextLine().trim().toLowerCase();
        if (termo.isEmpty()) {
            System.out.println("Termo de busca nao pode ser vazio.");
            return;
        }
        System.out.println("\n--- Resultados ---");
        boolean encontrou = false;
        for (int i = 0; i < tarefas.size(); i++) {
            if (tarefas.get(i).toLowerCase().contains(termo)) {
                String status = concluidas.get(i) ? "[X]" : "[ ]";
                System.out.println((i + 1) + ". " + status + " " + tarefas.get(i));
                System.out.println("   Criada em: " + datasCriacao.get(i).format(FORMATO) + expiracaoStatus(i));
                encontrou = true;
            }
        }
        if (!encontrou)
            System.out.println("Nenhuma tarefa encontrada com \"" + termo + "\".");
    }

    static void removerTarefa() {
        if (tarefas.isEmpty()) {
            System.out.println("\nNenhuma tarefa cadastrada.");
            return;
        }
        listarTarefas();
        System.out.print("\nDigite o numero da tarefa a remover: ");
        int index = lerInteiro();
        if (index < 1 || index > tarefas.size()) {
            System.out.println("Numero invalido.");
            return;
        }
        System.out.print("Confirma remocao de \"" + tarefas.get(index - 1) + "\"? (s/n): ");
        String confirmacao = sc.nextLine().trim().toLowerCase();
        if (!confirmacao.equals("s")) {
            System.out.println("Remocao cancelada.");
            return;
        }
        tarefas.remove(index - 1);
        concluidas.remove(index - 1);
        datasCriacao.remove(index - 1);
        datasExpiracao.remove(index - 1);
        salvarTarefas();
        System.out.println("Tarefa removida com sucesso!");
    }

    static int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Entrada invalida. Digite um numero: ");
            }
        }
    }

    static void salvarTarefas() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (int i = 0; i < tarefas.size(); i++) {
                String exp = datasExpiracao.get(i) != null ? datasExpiracao.get(i).format(FORMATO) : "null";
                bw.write(codificar(tarefas.get(i)) + "|" + concluidas.get(i) + "|" + datasCriacao.get(i).format(FORMATO) + "|" + exp + "\n");
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar tarefas: " + e.getMessage());
        }
    }

    static void carregarTarefas() {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split("\\|", -1);
                if (partes.length == 4) {
                    try {
                        tarefas.add(decodificar(partes[0]));
                        concluidas.add(Boolean.parseBoolean(partes[1]));
                        datasCriacao.add(LocalDateTime.parse(partes[2], FORMATO));
                        datasExpiracao.add(partes[3].equals("null") ? null : LocalDateTime.parse(partes[3], FORMATO));
                    } catch (DateTimeParseException e) {
                        System.out.println("Linha ignorada por data invalida: " + linha);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar tarefas: " + e.getMessage());
        }
    }

    static String codificar(String valor) {
        return Base64.getEncoder().encodeToString(valor.getBytes(StandardCharsets.UTF_8));
    }

    static String decodificar(String valor) {
        try {
            return new String(Base64.getDecoder().decode(valor), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return valor;
        }
    }
}
