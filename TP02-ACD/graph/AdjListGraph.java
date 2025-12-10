package graph;

import java.util.*;

public class AdjListGraph implements Graph {
    private final int V;
    private int E;
    private final boolean directed;
    private final boolean weighted;
    private final List<List<Edge>> adj;

    public AdjListGraph(int V, boolean directed, boolean weighted) {
        this.V = V;
        this.directed = directed;
        this.weighted = weighted;
        this.E = 0;
        this.adj = new ArrayList<>(V + 1);
        for (int i = 0; i <= V; i++) adj.add(new ArrayList<>());
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
        adj.get(u).add(new Edge(u, v, w));
        if (!directed) {
            adj.get(v).add(new Edge(v, u, w));
        }
        E++;
    }

    @Override
    public List<Edge> adj(int u) {
        return Collections.unmodifiableList(adj.get(u));
    }

    @Override
    public Iterable<Edge> edges() {
        List<Edge> list = new ArrayList<>();
        for (int u = 1; u <= V; u++) {
            for (Edge e : adj.get(u)) {
                if (directed) list.add(e);
                else {
                    // in undirected add each edge once (u < v)
                    if (u <= e.v) list.add(e);
                }
            }
        }
        return list;
    }

    @Override
    public void print() {
        System.out.println("Adjacency List Graph (V=" + V + ", E=" + E + ", directed=" + directed + ", weighted=" + weighted + ")");
        for (int u = 1; u <= V; u++) {
            System.out.print(u + ": ");
            for (Edge e : adj.get(u)) System.out.print(e.v + "(" + e.weight + ") ");
            System.out.println();
        }
    }
}
