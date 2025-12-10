package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdjMatrixGraph implements Graph {
    private final int V;
    private int E;
    private final boolean directed;
    private final boolean weighted;
    private final double[][] weight;
    private static final double INF = Double.POSITIVE_INFINITY;

    public AdjMatrixGraph(int V, boolean directed, boolean weighted) {
        this.V = V;
        this.directed = directed;
        this.weighted = weighted;
        this.E = 0;
        weight = new double[V + 1][V + 1];
        for (int i = 0; i <= V; i++) for (int j = 0; j <= V; j++) weight[i][j] = INF;
    }

    @Override
    public int V() { return V; }

    @Override
    public int E() { return E; }

    @Override
    public boolean directed() { return directed; }

    @Override
    public boolean weighted() { return weighted; }

    @Override
    public void addEdge(int u, int v) { addEdge(u, v, 1.0); }

    @Override
    public void addEdge(int u, int v, double w) {
        if (!weighted && w != 1.0) throw new IllegalArgumentException("Graph not weighted");
        if (weight[u][v] == INF) E++;
        weight[u][v] = w;
        if (!directed) {
            weight[v][u] = w;
        }
    }

    @Override
    public List<Edge> adj(int u) {
        List<Edge> list = new ArrayList<>();
        for (int v = 1; v <= V; v++) {
            if (weight[u][v] != INF) list.add(new Edge(u, v, weight[u][v]));
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public Iterable<Edge> edges() {
        List<Edge> list = new ArrayList<>();
        for (int u = 1; u <= V; u++) {
            for (int v = 1; v <= V; v++) {
                if (weight[u][v] != INF) {
                    if (directed) list.add(new Edge(u, v, weight[u][v]));
                    else if (u <= v) list.add(new Edge(u, v, weight[u][v]));
                }
            }
        }
        return list;
    }

    @Override
    public void print() {
        System.out.println("Adjacency Matrix Graph (V=" + V + ", E=" + E + ", directed=" + directed + ", weighted=" + weighted + ")");
        for (int i = 1; i <= V; i++) {
            for (int j = 1; j <= V; j++) {
                System.out.print((weight[i][j] == INF ? "INF" : String.format("%.0f", weight[i][j])) + "\t");
            }
            System.out.println();
        }
    }

    public double[][] getMatrix() {
        return weight;
    }
}
