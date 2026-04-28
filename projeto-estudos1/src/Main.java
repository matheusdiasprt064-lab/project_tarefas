import java.util.Scanner;
import java.util.Map;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import model.Task;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();
        
        while (true) {
            System.out.println("\n+---------------------------+");
            System.out.println("|     GERENCIADOR DE TAREFAS     |");
            System.out.println("+---------------------------+");
            System.out.println("  [1] Adicionar nova tarefa"); // cSpell:ignore tarefa tarefas
            System.out.println("  [2] Ver todas as tarefas");
            System.out.println("  [3] Editar tarefa");
            System.out.println("  [4] Alternar status da tarefa");
            System.out.println("  [5] Remover tarefa");
            System.out.println("  [6] Sair");
            System.out.println("+---------------------------+");
            System.out.print("Opcao: ");
            int opcao;
            try { opcao = Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Opcao invalida! Digite um numero."); continue; }

            switch (opcao) {
                case 1 -> menuAdicionarTarefa(scanner, taskManager);
                case 2 -> menuListar(scanner, taskManager);
                case 3 -> menuEditar(scanner, taskManager);
                case 4 -> menuAlternarStatus(scanner, taskManager);
                case 5 -> menuRemover(scanner, taskManager);
                case 6 -> {
                    System.out.println("\n=== Lista de Tarefas ===");
                    taskManager.listarTarefas();
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opcao invalida!");
            }
        }
    }

    private static void menuAdicionarTarefa(Scanner scanner, TaskManager taskManager) {
        String titulo = lerTextoObrigatorio(scanner, "Titulo");
        String descricao = lerTextoObrigatorio(scanner, "Descricao");
        String categoria = lerTextoObrigatorio(scanner, "Categoria");
        LocalDate dataVencimento = lerDataObrigatoria(scanner, "Data de vencimento");
        Task nova = new Task(titulo, descricao, categoria, dataVencimento);
        while (true) {
            System.out.println("\n+---- Revisao da Tarefa ----+");
            System.out.printf("  [1] Titulo:    %s%n", nova.getTituloRaw());
            System.out.printf("  [2] Descricao: %s%n", nova.getDescricao());
            System.out.printf("  [3] Categoria: %s%n", nova.getCategoria());
            System.out.printf("  [4] Data:      %s%n", nova.getDataVencimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("+---------------------------+");
            System.out.println("  [5] Confirmar e salvar");
            System.out.println("  Digite 1-4 para editar ou 5 para salvar");
            System.out.print("> ");
            String escolha = scanner.nextLine().trim();
            switch (escolha) {
                case "1" -> { System.out.print("Novo titulo: "); String t = scanner.nextLine().trim(); if (!t.isEmpty()) nova.setTitulo(t); else System.out.println("Titulo nao pode ser vazio."); }
                case "2" -> { System.out.print("Nova descricao: "); String d = scanner.nextLine().trim(); if (!d.isEmpty()) nova.setDescricao(d); else System.out.println("Descricao nao pode ser vazia."); }
                case "3" -> { System.out.print("Nova categoria: "); String c = scanner.nextLine().trim(); if (!c.isEmpty()) nova.setCategoria(c); else System.out.println("Categoria nao pode ser vazia."); }
                case "4" -> {
                    LocalDate novaData = null;
                    while (novaData == null) {
                        System.out.print("Nova data: ");
                        LocalDate d = parsearData(scanner.nextLine().trim());
                        if (d == null) System.out.println("Data invalida!");
                        else if (d.isBefore(LocalDate.now())) System.out.println("A data nao pode ser no passado.");
                        else novaData = d;
                    }
                    nova.setDataVencimento(novaData);
                }
                case "5" -> { taskManager.adicionarTarefa(nova); System.out.println("Tarefa salva!"); }
                default -> System.out.println("Opcao invalida! Digite 1-5.");
            }
            if (escolha.equals("5")) break;
        }
    }

    private static void menuListar(Scanner scanner, TaskManager taskManager) {
        while (true) {
            if (taskManager.isEmpty()) { System.out.println("Nenhuma tarefa disponivel para listar."); return; }
            taskManager.listarTarefas();
            System.out.println("  [1] Titulo  [2] Categoria  [3] Data  [0] Voltar");
            System.out.print("Filtrar por: ");
            String f = scanner.nextLine().trim();
            if (f.equals("0") || f.isEmpty()) return;
            aplicarFiltro(f, scanner, taskManager);
        }
    }

    private static void menuAlternarStatus(Scanner scanner, TaskManager taskManager) {
        while (true) {
            if (taskManager.isEmpty()) { System.out.println("Nenhuma tarefa disponivel para alterar status."); return; }
            taskManager.listarTarefas();
            System.out.println("  Filtrar? [1] Titulo  [2] Categoria  [3] Data  [Enter] Nao");
            System.out.print("> ");
            String f = scanner.nextLine().trim();
            if (!f.isEmpty()) aplicarFiltro(f, scanner, taskManager);
            int idx = lerIndice(scanner, taskManager.tamanho(), "alterar status");
            if (idx == -1) return;
            taskManager.alternarConclusao(idx);
            System.out.println("Status da tarefa alterado!");
            return;
        }
    }

    private static void menuEditar(Scanner scanner, TaskManager taskManager) {
        while (true) {
            if (taskManager.isEmpty()) { System.out.println("Nenhuma tarefa disponivel para editar."); return; }
            taskManager.listarTarefas();
            System.out.println("  Filtrar? [1] Titulo  [2] Categoria  [3] Data  [Enter] Nao");
            System.out.print("> ");
            String f = scanner.nextLine().trim();
            if (!f.isEmpty()) aplicarFiltro(f, scanner, taskManager);
            int idx = lerIndice(scanner, taskManager.tamanho(), "editar");
            if (idx == -1) return;

            String titulo = lerTextoObrigatorio(scanner, "Novo titulo");
            String descricao = lerTextoObrigatorio(scanner, "Nova descricao");
            String categoria = lerTextoObrigatorio(scanner, "Nova categoria");
            LocalDate dataVencimento = lerDataObrigatoria(scanner, "Nova data");
            taskManager.editarTarefa(idx, titulo, descricao, categoria, dataVencimento);
            System.out.println("Tarefa editada!");
            return;
        }
    }

    private static void menuRemover(Scanner scanner, TaskManager taskManager) {
        while (true) {
            if (taskManager.isEmpty()) { System.out.println("Nenhuma tarefa disponivel para remover."); return; }
            taskManager.listarTarefas();
            System.out.println("  Filtrar? [1] Titulo  [2] Categoria  [3] Data  [Enter] Nao");
            System.out.print("> ");
            String f = scanner.nextLine().trim();
            if (!f.isEmpty()) aplicarFiltro(f, scanner, taskManager);
            int idx = lerIndice(scanner, taskManager.tamanho(), "remover");
            if (idx == -1) return;
            taskManager.removerTarefa(idx);
            System.out.println("Tarefa removida!");
            return;
        }
    }

    private static int lerIndice(Scanner scanner, int tamanho, String acao) {
        while (true) {
            System.out.print("Numero da tarefa a " + acao + " (0 para cancelar): ");
            try {
                int idx = Integer.parseInt(scanner.nextLine().trim());
                if (idx == 0) return -1;
                if (idx >= 1 && idx <= tamanho) return idx - 1;
                System.out.println("Numero invalido! Digite entre 1 e " + tamanho + ".");
            } catch (NumberFormatException e) {
                System.out.println("Digite apenas numeros.");
            }
        }
    }

    private static void aplicarFiltro(String tipo, Scanner scanner, TaskManager taskManager) {
        switch (tipo) {
            case "1" -> { System.out.print("Titulo: "); taskManager.filtrar(scanner.nextLine().trim(), "titulo"); }
            case "2" -> { System.out.print("Categoria: "); taskManager.filtrar(scanner.nextLine().trim(), "categoria"); }
            case "3" -> {
                LocalDate dataFiltro = null;
                while (dataFiltro == null) {
                    System.out.print("Data: ");
                    dataFiltro = parsearData(scanner.nextLine().trim());
                    if (dataFiltro == null) System.out.println("Data invalida! Tente novamente.");
                }
                taskManager.filtrarPorData(dataFiltro);
            }
            default -> taskManager.listarTarefas();
        }
    }

    private static String lerTextoObrigatorio(Scanner scanner, String rotulo) {
        while (true) {
            System.out.print(rotulo + ": ");
            String valor = scanner.nextLine().trim();
            if (!valor.isEmpty()) return valor;
            System.out.println(rotulo + " nao pode ficar vazio.");
        }
    }

    private static LocalDate lerDataObrigatoria(Scanner scanner, String rotulo) {
        while (true) {
            System.out.print(rotulo + ": ");
            LocalDate data = parsearData(scanner.nextLine().trim());
            if (data == null) {
                System.out.println("Data invalida! Exemplos: hoje, amanha, +7, +2s, +3m, +1a, ano que vem, daqui 2 anos, sexta, 25/12, 25/12/2025");
            } else if (data.isBefore(LocalDate.now())) {
                System.out.println("A data nao pode ser no passado.");
            } else {
                return data;
            }
        }
    }

    private static LocalDate parsearData(String entrada) {
        LocalDate hoje = LocalDate.now();
        String s = entrada.toLowerCase().trim();

        // Atalhos de texto
        if (s.equals("hoje")) return hoje;
        if (s.equals("amanhã") || s.equals("amanha")) return hoje.plusDays(1);
        if (s.matches("\\+\\d+")) return hoje.plusDays(Long.parseLong(s.substring(1)));

        // Atalhos de anos
        if (s.equals("ano que vem") || s.equals("daqui um ano") || s.equals("daqui 1 ano") ||
            s.equals("em um ano") || s.equals("em 1 ano") || s.equals("prox ano") ||
            s.equals("próx ano") || s.equals("próximo ano") || s.equals("proximo ano"))
            return hoje.plusYears(1);
        if (s.equals("daqui dois anos") || s.equals("daqui 2 anos") || s.equals("em dois anos") || s.equals("em 2 anos"))
            return hoje.plusYears(2);
        if (s.equals("daqui três anos") || s.equals("daqui 3 anos") || s.equals("em três anos") || s.equals("em 3 anos"))
            return hoje.plusYears(3);
        if (s.equals("daqui cinco anos") || s.equals("daqui 5 anos") || s.equals("em cinco anos") || s.equals("em 5 anos"))
            return hoje.plusYears(5);
        if (s.equals("daqui dez anos") || s.equals("daqui 10 anos") || s.equals("em dez anos") || s.equals("em 10 anos"))
            return hoje.plusYears(10);

        // "daqui X anos" ou "em X anos" genérico
        java.util.regex.Matcher mAnos = java.util.regex.Pattern
            .compile("(?:daqui|em)\\s+(\\d+)\\s+anos?").matcher(s);
        if (mAnos.matches()) return hoje.plusYears(Long.parseLong(mAnos.group(1)));

        // "+Xm" = meses, "+Xa" = anos
        if (s.matches("\\+\\d+m")) return hoje.plusMonths(Long.parseLong(s.substring(1, s.length() - 1)));
        if (s.matches("\\+\\d+a")) return hoje.plusYears(Long.parseLong(s.substring(1, s.length() - 1)));
        if (s.matches("\\+\\d+d")) return hoje.plusDays(Long.parseLong(s.substring(1, s.length() - 1)));
        if (s.matches("\\+\\d+s")) return hoje.plusWeeks(Long.parseLong(s.substring(1, s.length() - 1)));

        // Dias da semana (próximo)
        Map<String, DayOfWeek> diasSemana = Map.ofEntries(
            Map.entry("segunda", DayOfWeek.MONDAY), Map.entry("segunda-feira", DayOfWeek.MONDAY), Map.entry("seg", DayOfWeek.MONDAY),
            Map.entry("terça", DayOfWeek.TUESDAY), Map.entry("terca", DayOfWeek.TUESDAY), Map.entry("terça-feira", DayOfWeek.TUESDAY), Map.entry("terca-feira", DayOfWeek.TUESDAY), Map.entry("ter", DayOfWeek.TUESDAY),
            Map.entry("quarta", DayOfWeek.WEDNESDAY), Map.entry("quarta-feira", DayOfWeek.WEDNESDAY), Map.entry("qua", DayOfWeek.WEDNESDAY),
            Map.entry("quinta", DayOfWeek.THURSDAY), Map.entry("quinta-feira", DayOfWeek.THURSDAY), Map.entry("qui", DayOfWeek.THURSDAY),
            Map.entry("sexta", DayOfWeek.FRIDAY), Map.entry("sexta-feira", DayOfWeek.FRIDAY), Map.entry("sex", DayOfWeek.FRIDAY),
            Map.entry("sábado", DayOfWeek.SATURDAY), Map.entry("sabado", DayOfWeek.SATURDAY), Map.entry("sáb", DayOfWeek.SATURDAY), Map.entry("sab", DayOfWeek.SATURDAY),
            Map.entry("domingo", DayOfWeek.SUNDAY), Map.entry("dom", DayOfWeek.SUNDAY)
        );
        if (diasSemana.containsKey(s)) return hoje.with(TemporalAdjusters.next(diasSemana.get(s)));

        // Meses por extenso
        Map<String, Integer> meses = Map.ofEntries(
            Map.entry("janeiro", 1), Map.entry("jan", 1),
            Map.entry("fevereiro", 2), Map.entry("fev", 2),
            Map.entry("março", 3), Map.entry("marco", 3), Map.entry("mar", 3),
            Map.entry("abril", 4), Map.entry("abr", 4),
            Map.entry("maio", 5), Map.entry("mai", 5),
            Map.entry("junho", 6), Map.entry("jun", 6),
            Map.entry("julho", 7), Map.entry("jul", 7),
            Map.entry("agosto", 8), Map.entry("ago", 8),
            Map.entry("setembro", 9), Map.entry("set", 9),
            Map.entry("outubro", 10), Map.entry("out", 10),
            Map.entry("novembro", 11), Map.entry("nov", 11),
            Map.entry("dezembro", 12), Map.entry("dez", 12)
        );

        // "25 de dezembro" ou "25 dezembro"
        java.util.regex.Matcher m = java.util.regex.Pattern
            .compile("(\\d{1,2})\\s+(?:de\\s+)?(\\w+)").matcher(s);
        if (m.matches() && meses.containsKey(m.group(2))) {
            int ano = hoje.getYear();
            LocalDate data = LocalDate.of(ano, meses.get(m.group(2)), Integer.parseInt(m.group(1)));
            return data.isBefore(hoje) ? data.plusYears(1) : data;
        }

        // "dezembro 2025" ou "dez 2025"
        m = java.util.regex.Pattern.compile("(\\w+)\\s+(\\d{4})").matcher(s);
        if (m.matches() && meses.containsKey(m.group(1))) {
            return LocalDate.of(Integer.parseInt(m.group(2)), meses.get(m.group(1)), 1);
        }

        // Somente o dia (ex: "15")
        if (s.matches("\\d{1,2}")) {
            int dia = Integer.parseInt(s);
            try {
                LocalDate data = LocalDate.of(hoje.getYear(), hoje.getMonth(), dia);
                return data.isBefore(hoje) ? data.plusMonths(1) : data;
            } catch (Exception ignored) {}
        }

        // Formatos numéricos
        DateTimeFormatter[] formatos = {
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("d-M-yyyy"),
            DateTimeFormatter.ofPattern("d.M.yyyy"),
            DateTimeFormatter.ofPattern("yyyy/M/d"),
            DateTimeFormatter.ofPattern("yyyy-M-d"),
            DateTimeFormatter.ofPattern("d/M"),
            DateTimeFormatter.ofPattern("d-M")
        };
        for (DateTimeFormatter fmt : formatos) {
            try {
                if (fmt.toString().contains("yyyy")) return LocalDate.parse(s, fmt);
                // sem ano: assume ano atual, se passado usa próximo ano
                LocalDate data = LocalDate.parse(s + "/" + hoje.getYear(),
                    DateTimeFormatter.ofPattern("d/M/yyyy"));
                return data.isBefore(hoje) ? data.plusYears(1) : data;
            } catch (DateTimeParseException ignored) {}
        }
        return null;
    }
}
