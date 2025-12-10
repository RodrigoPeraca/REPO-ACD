import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        System.out.println("=== Teste de Algoritmos de Ordenação ===");
        System.out.println("Escolha o algoritmo:");
        System.out.println("1 - BubbleSort");
        System.out.println("2 - InsertionSort");
        System.out.println("3 - SelectionSort");
        System.out.println("4 - ShellSort");
        System.out.println("5 - HeapSort");
        System.out.println("6 - MergeSort");
        System.out.println("7 - QuickSort");
        System.out.println("8 - CountingSort");
        System.out.println("9 - RadixSort");
        System.out.println("10 - BucketSort");
        System.out.print("Opção: ");
        int opcao = sc.nextInt();

        System.out.print("Número de entradas (tamanho do vetor): ");
        int n = sc.nextInt();

        System.out.print("Quantas execuções deseja realizar? ");
        int repeticoes = sc.nextInt();

        System.out.print("Nome do arquivo de saída (ex: resultados.txt): ");
        String nomeArquivo = sc.next();

        List<Long> tempos = new ArrayList<>();
        System.out.println("Escolha o tipo de vetor:");
        System.out.println("1 - Aleatório");
        System.out.println("2 - Crescente");
        System.out.println("3 - Decrescente");
        int tipo = sc.nextInt();
        int valor;
        for (int i = 0; i < repeticoes; i++) {
            // Gera vetor aleatório
            Integer[] vetorInt = new Integer[n];
            int[] vetorIntPrimitivo = new int[n];
            double[] vetorDouble = new double[n];

            for (int j = 0; j < n; j++) {
                switch(tipo){
                    case 1: //Aleatório
                        valor = random.nextInt(100000);
                        break;
                    case 2: //Crescente
                        valor = j;
                        break;
                    case 3: //Decrescente
                        valor = n - j;
                        break;
                    default:
                        valor = random.nextInt(100000);
                        break;
                }
                vetorInt[j] = valor;
                vetorIntPrimitivo[j] = valor;
                vetorDouble[j] = random.nextDouble(); // usado no bucketSort
            }

            long inicio, fim, tempoExec;

            switch (opcao) {
                case 1 -> { // Bubble
                    inicio = System.nanoTime();
                    Ordenadores.bubbleSort(vetorInt);
                    fim = System.nanoTime();
                }
                case 2 -> { // Insertion
                    inicio = System.nanoTime();
                    Ordenadores.insertionSort(vetorInt);
                    fim = System.nanoTime();
                }
                case 3 -> { // Selection
                    inicio = System.nanoTime();
                    Ordenadores.selectionSort(vetorInt);
                    fim = System.nanoTime();
                }
                case 4 -> { // Shell
                    inicio = System.nanoTime();
                    Ordenadores.shellSort(vetorInt);
                    fim = System.nanoTime();
                }
                case 5 -> { // Heap
                    inicio = System.nanoTime();
                    Ordenadores.heapSort(vetorInt);
                    fim = System.nanoTime();
                }
                case 6 -> { // Merge
                    inicio = System.nanoTime();
                    Ordenadores.mergeSort(vetorInt);
                    fim = System.nanoTime();
                }
                case 7 -> { // Quick
                    inicio = System.nanoTime();
                    Ordenadores.quickSort(vetorInt, 0, vetorInt.length - 1);
                    fim = System.nanoTime();
                }
                case 8 -> { // Counting
                    inicio = System.nanoTime();
                    Ordenadores.countingSort(vetorIntPrimitivo);
                    fim = System.nanoTime();
                }
                case 9 -> { // Radix
                    inicio = System.nanoTime();
                    Ordenadores.radixSort(vetorIntPrimitivo);
                    fim = System.nanoTime();
                }
                case 10 -> { // Bucket
                    inicio = System.nanoTime();
                    Ordenadores.bucketSort(vetorDouble);
                    fim = System.nanoTime();
                }
                default -> {
                    System.out.println("Opção inválida!");
                    return;
                }
            }

            tempoExec = fim - inicio;
            tempos.add(tempoExec);
            System.out.printf("Execução %d concluída em %.3f ms%n", i + 1, tempoExec / 1_000_000.0);
        }

        // Salva resultados no arquivo
        try (PrintWriter out = new PrintWriter(new FileWriter(nomeArquivo))) {
            out.println("Resultados do algoritmo " + getNomeAlgoritmo(opcao));
            out.println("Número de entradas: " + n);
            out.println("Execuções: " + repeticoes);
            out.println("Tempos (ns):");
            for (long t : tempos) out.println(t);
            long media = (long) tempos.stream().mapToLong(Long::longValue).average().orElse(0);
            out.println("\nTempo médio (ns): " + media);
            out.println("Tempo médio (ms): " + (media / 1_000_000.0));
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
        }

        System.out.println("Resultados salvos em: " + nomeArquivo);
    }

    private static String getNomeAlgoritmo(int opcao) {
        return switch (opcao) {
            case 1 -> "BubbleSort";
            case 2 -> "InsertionSort";
            case 3 -> "SelectionSort";
            case 4 -> "ShellSort";
            case 5 -> "HeapSort";
            case 6 -> "MergeSort";
            case 7 -> "QuickSort";
            case 8 -> "CountingSort";
            case 9 -> "RadixSort";
            case 10 -> "BucketSort";
            default -> "Desconhecido";
        };
    }
}
