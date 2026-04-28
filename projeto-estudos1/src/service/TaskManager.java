package service;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.Base64;
import model.Task;

public class TaskManager {
    private static final Path ARQUIVO = Path.of("tarefas-src.txt");
    private final ArrayList<Task> tarefas = new ArrayList<>(); // cSpell:ignore tarefas

    public TaskManager() {
        carregarTarefas();
    }

    public boolean isEmpty() {
        return tarefas.isEmpty();
    }

    public int tamanho() {
        return tarefas.size();
    }

    public void adicionarTarefa(Task task) {
        tarefas.add(task);
        salvarTarefas();
    }

    public void listarTarefas() {
        if (tarefas.isEmpty()) {
            System.out.println("  Nenhuma tarefa cadastrada.");
            return;
        }
        String sep = "+-----+----------------------+----------------------+--------------+------------+------------+";
        System.out.println("\n" + sep);
        System.out.printf("| %-3s | %-20s | %-20s | %-12s | %-10s | %-10s |%n",
            "#", "Titulo", "Descricao", "Categoria", "Vencimento", "Status");
        System.out.println(sep);
        for (int i = 0; i < tarefas.size(); i++) {
            System.out.printf("| %-3d | %s |%n", (i + 1), tarefas.get(i).getTitulo());
        }
        System.out.println(sep);
    }

    public void alternarConclusao(int index) {
        tarefas.get(index).alternarConclusao();
        salvarTarefas();
    }

    public void editarTarefa(int index, String titulo, String descricao, String categoria, LocalDate dataVencimento) {
        Task tarefa = tarefas.get(index);
        tarefa.setTitulo(titulo);
        tarefa.setDescricao(descricao);
        tarefa.setCategoria(categoria);
        tarefa.setDataVencimento(dataVencimento);
        salvarTarefas();
    }

    public void removerTarefa(int index) {
        tarefas.remove(index);
        salvarTarefas();
    }

    public void filtrar(String termo, String campo) {
        String t = termo.toLowerCase();
        List<ResultadoFiltro> resultado = new ArrayList<>();
        for (int i = 0; i < tarefas.size(); i++) {
            Task task = tarefas.get(i);
            boolean encontrou = campo.equals("titulo")
                ? task.getTituloRaw().toLowerCase().contains(t)
                : task.getCategoria().toLowerCase().contains(t);
            if (encontrou) resultado.add(new ResultadoFiltro(i, task));
        }
        exibirResultado(resultado);
    }

    public void filtrarPorData(LocalDate data) {
        List<ResultadoFiltro> resultado = new ArrayList<>();
        for (int i = 0; i < tarefas.size(); i++) {
            Task task = tarefas.get(i);
            if (task.getDataVencimento().equals(data)) resultado.add(new ResultadoFiltro(i, task));
        }
        exibirResultado(resultado);
    }

    private void exibirResultado(List<ResultadoFiltro> resultado) {
        if (resultado.isEmpty()) { System.out.println("Nenhuma tarefa encontrada."); return; }
        String sep = "+-----+----------------------+----------------------+--------------+------------+------------+";
        System.out.println("\n" + sep);
        System.out.printf("| %-3s | %-20s | %-20s | %-12s | %-10s | %-10s |%n",
            "#", "Titulo", "Descricao", "Categoria", "Vencimento", "Status");
        System.out.println(sep);
        for (int i = 0; i < resultado.size(); i++) {
            ResultadoFiltro item = resultado.get(i);
            System.out.printf("| %-3d | %s |%n", (item.indexOriginal() + 1), item.tarefa().getTitulo());
        }
        System.out.println(sep);
        System.out.println("Use o numero original da tarefa para editar, concluir ou remover.");
    }

    private void salvarTarefas() {
        List<String> linhas = tarefas.stream()
            .map(tarefa -> String.join("|",
                codificar(tarefa.getTituloRaw()),
                codificar(tarefa.getDescricao()),
                codificar(tarefa.getCategoria()),
                tarefa.getDataVencimento().toString(),
                Boolean.toString(tarefa.isConcluida())))
            .toList();
        try {
            Files.write(ARQUIVO, linhas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Erro ao salvar tarefas: " + e.getMessage());
        }
    }

    private void carregarTarefas() {
        if (!Files.exists(ARQUIVO)) return;
        try {
            for (String linha : Files.readAllLines(ARQUIVO, StandardCharsets.UTF_8)) {
                String[] partes = linha.split("\\|", -1);
                if (partes.length != 5) continue;
                tarefas.add(new Task(
                    decodificar(partes[0]),
                    decodificar(partes[1]),
                    decodificar(partes[2]),
                    LocalDate.parse(partes[3]),
                    Boolean.parseBoolean(partes[4])
                ));
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar tarefas salvas: " + e.getMessage());
        }
    }

    private String codificar(String valor) {
        return Base64.getEncoder().encodeToString(valor.getBytes(StandardCharsets.UTF_8));
    }

    private String decodificar(String valor) {
        return new String(Base64.getDecoder().decode(valor), StandardCharsets.UTF_8);
    }

    private record ResultadoFiltro(int indexOriginal, Task tarefa) {
    }
}
