package graph;

import java.util.*;

public class FordFulkerson {
    // Representa rede por lista de adj com capacidades (usamos estruturas internas).
    static class FlowEdge {
        int to;
        int rev; // índice do edge reverso
        double cap;
        FlowEdge(int to, int rev, double cap) { this.to = to; this.rev = rev; this.cap = cap; }
    }

    public static double maxFlow(int V, List<List<FlowEdge>> adj, int s, int t) {
        double flow = 0;
        while (true) {
            boolean[] seen = new boolean[V+1];
            double pushed = dfs(s, t, Double.POSITIVE_INFINITY, seen, adj);
            if (pushed == 0) break;
            flow += pushed;
        }
        return flow;
    }

    private static double dfs(int u, int t, double f, boolean[] seen, List<List<FlowEdge>> adj) {
        if (u == t) return f;
        seen[u] = true;
        for (FlowEdge e : adj.get(u)) {
            if (e.cap > 1e-9 && !seen[e.to]) {
                double pushed = dfs(e.to, t, Math.min(f, e.cap), seen, adj);
                if (pushed > 0) {
                    e.cap -= pushed;
                    adj.get(e.to).get(e.rev).cap += pushed;
                    return pushed;
                }
            }
        }
        return 0;
    }

    // helpers to build adjacency list for flow
    public static void addEdge(List<List<FlowEdge>> adj, int u, int v, double cap) {
        FlowEdge a = new FlowEdge(v, adj.get(v).size(), cap);
        FlowEdge b = new FlowEdge(u, adj.get(u).size(), 0.0);
        adj.get(u).add(a);
        adj.get(v).add(b);
    }
}
