package graph;

import java.io.IOException;
import java.util.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.println("TP2 - Grafos (implementações) - Menu");
        System.out.println("1) Carregar grafo .gr (lista de adjacência)");
        System.out.println("2) Executar exemplos simples pré-definidos");
        System.out.println("0) Sair");
        int opt = sc.nextInt();
        Graph g = null;
        while (opt != 0) {
            if (opt == 1) {
                System.out.print("Caminho do arquivo .gr: ");
                String path = sc.next();
                System.out.print("Direcionado? (true/false): ");
                boolean directed = sc.nextBoolean();
                System.out.print("Ponderado? (true/false): ");
                boolean weighted = sc.nextBoolean();
                g = GraphLoader.loadAdjListFromGR(path, directed, weighted);
                System.out.println("Grafo carregado:");
                g.print();
            } else if (opt == 2) {
                g = sampleGraph();
                g.print();
            } else {
                System.out.println("Opção inválida");
            }

            if (g != null) {
                System.out.println("Algoritmos disponíveis:");
                System.out.println("1) BFS");
                System.out.println("2) DFS");
                System.out.println("3) Topological Sort");
                System.out.println("4) Kruskal (MST)");
                System.out.println("5) Prim (MST)");
                System.out.println("6) Dijkstra");
                System.out.println("7) Bellman-Ford");
                System.out.println("8) Floyd-Warshall");
                System.out.println("9) Ford-Fulkerson (fluxo máximo) - requer construção de rede manual");
                System.out.println("0) Voltar");
                System.out.print("Escolha: ");
                int a = sc.nextInt();
                long start, end;
                switch (a) {
                    case 1:
                        System.out.print("Fonte: ");
                        int s1 = sc.nextInt();
                        start = System.nanoTime();
                        List<Integer> bfs = BFS.run(g, s1);
                        end = System.nanoTime();
                        System.out.println("BFS order: " + bfs);
                        System.out.println("Tempo (ms): " + (end - start) / 1e6);
                        break;
                    case 2:
                        System.out.print("Fonte: ");
                        int s2 = sc.nextInt();
                        start = System.nanoTime();
                        List<Integer> dfs = DFS.run(g, s2);
                        end = System.nanoTime();
                        System.out.println("DFS order: " + dfs);
                        System.out.println("Tempo (ms): " + (end - start) / 1e6);
                        break;
                    case 3:
                        start = System.nanoTime();
                        List<Integer> topo = TopologicalSort.sort(g);
                        end = System.nanoTime();
                        if (topo.isEmpty()) System.out.println("Grafo contém ciclo (não é DAG).");
                        else System.out.println("Topo order: " + topo);
                        System.out.println("Tempo (ms): " + (end - start) / 1e6);
                        break;
                    case 4:
                        start = System.nanoTime();
                        List<Edge> mstK = Kruskal.mst(g);
                        end = System.nanoTime();
                        System.out.println("Kruskal MST edges: " + mstK);
                        System.out.println("Tempo (ms): " + (end - start) / 1e6);
                        break;
                    case 5:
                        System.out.print("Start vertex: ");
                        int st = sc.nextInt();
                        start = System.nanoTime();
                        List<Edge> mstP = Prim.mst(g, st);
                        end = System.nanoTime();
                        System.out.println("Prim MST edges: " + mstP);
                        System.out.println("Tempo (ms): " + (end - start) / 1e6);
                        break;
                    case 6:
                        System.out.print("Fonte: ");
                        int ds = sc.nextInt();
                        start = System.nanoTime();
                        double[] dd = Dijkstra.dist(g, ds);
                        end = System.nanoTime();
                        printDistances(dd);
                        System.out.println("Tempo (ms): " + (end - start) / 1e6);
                        break;
                    case 7:
                        System.out.print("Fonte: ");
                        int bs = sc.nextInt();
                        start = System.nanoTime();
                        try {
                            double[] bd = BellmanFord.dist(g, bs);
                            end = System.nanoTime();
                            printDistances(bd);
                        } catch (RuntimeException ex) {
                            System.out.println("Erro: " + ex.getMessage());
                        } finally {
                            end = System.nanoTime();
                            System.out.println("Tempo (ms): " + (end - start) / 1e6);
                        }
                        break;
                    case 8:
                        start = System.nanoTime();
                        double[][] allPairs = FloydWarshall.run(g);
                        end = System.nanoTime();
                        printMatrix(allPairs);
                        System.out.println("Tempo (ms): " + (end - start) / 1e6);
                        break;
                    case 9:
                        System.out.println("Construção manual da rede de fluxo (exemplo pequeno).");
                        // comportamento de exemplo: construir rede simples com 4 vértices
                        List<List<FordFulkerson.FlowEdge>> adj = new ArrayList<>();
                        int V = 4;
                        for (int i = 0; i <= V; i++) adj.add(new ArrayList<>());
                        FordFulkerson.addEdge(adj, 1, 2, 40);
                        FordFulkerson.addEdge(adj, 1, 3, 20);
                        FordFulkerson.addEdge(adj, 2, 3, 10);
                        FordFulkerson.addEdge(adj, 2, 4, 30);
                        FordFulkerson.addEdge(adj, 3, 4, 20);
                        start = System.nanoTime();
                        double maxflow = FordFulkerson.maxFlow(V, adj, 1, 4);
                        end = System.nanoTime();
                        System.out.println("Max flow: " + maxflow);
                        System.out.println("Tempo (ms): " + (end - start) / 1e6);
                        break;
                    default:
                        System.out.println("Voltando...");
                }
            }

            System.out.println("\nMenu principal:");
            System.out.println("1) Carregar grafo .gr (lista de adjacência)");
            System.out.println("2) Executar exemplos simples pré-definidos");
            System.out.println("0) Sair");
            opt = sc.nextInt();
        }
        System.out.println("Encerrando.");
    }

    private static void printDistances(double[] d) {
        for (int i = 1; i < d.length; i++) {
            System.out.println(i + ": " + (Double.isInfinite(d[i]) ? "INF" : d[i]));
        }
    }

    private static void printMatrix(double[][] m) {
        for (int i = 1; i < m.length; i++) {
            for (int j = 1; j < m.length; j++) {
                System.out.print((Double.isInfinite(m[i][j]) ? "INF" : m[i][j]) + "\t");
            }
            System.out.println();
        }
    }

    private static AdjListGraph sampleGraph() {
        AdjListGraph g = new AdjListGraph(5, false, true);
        g.addEdge(1,2,2);
        g.addEdge(1,3,4);
        g.addEdge(2,3,1);
        g.addEdge(2,4,7);
        g.addEdge(3,5,3);
        g.addEdge(4,5,1);
        return g;
    }
}
