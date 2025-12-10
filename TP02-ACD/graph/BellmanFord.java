package graph;

import java.util.*;

public class BellmanFord {
    public static double[] dist(Graph g, int src) {
        if (!g.weighted()) throw new IllegalArgumentException("Bellman-Ford requires weighted graph");
        int n = g.V();
        double INF = Double.POSITIVE_INFINITY;
        double[] d = new double[n+1];
        Arrays.fill(d, INF);
        d[src] = 0.0;
        List<Edge> edges = new ArrayList<>();
        for (Edge e : g.edges()) edges.add(e);
        for (int i = 1; i <= n-1; i++) {
            boolean changed = false;
            for (Edge e : edges) {
                if (d[e.u] != INF && d[e.v] > d[e.u] + e.weight) {
                    d[e.v] = d[e.u] + e.weight;
                    changed = true;
                }
            }
            if (!changed) break;
        }
        // Detect negative cycle: if can relax further
        for (Edge e : edges) {
            if (d[e.u] != INF && d[e.v] > d[e.u] + e.weight) {
                throw new RuntimeException("Graph contains a negative weight cycle");
            }
        }
        return d;
    }
}
