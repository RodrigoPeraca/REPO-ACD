package graph;

import java.util.List;

public interface Graph {
    int V(); // número de vértices (assume-se vértices indexados 1..V)
    int E(); // número de arestas/arestas dirigidas
    boolean directed();
    boolean weighted();

    void addEdge(int u, int v); // sem peso
    void addEdge(int u, int v, double w); // com peso

    List<Edge> adj(int u); // lista de arestas saindo de u

    Iterable<Edge> edges(); // todas as arestas
    void print();
}
