package graph;

import java.util.*;

public class TopologicalSort {
    public static List<Integer> sort(Graph g) {
        if (!g.directed()) throw new IllegalArgumentException("Topological sort requires directed graph");
        int n = g.V();
        int[] indeg = new int[n+1];
        for (Edge e : g.edges()) indeg[e.v]++;
        Queue<Integer> q = new LinkedList<>();
        for (int i = 1; i <= n; i++) if (indeg[i] == 0) q.offer(i);
        List<Integer> topo = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            topo.add(u);
            for (Edge e : g.adj(u)) {
                indeg[e.v]--;
                if (indeg[e.v] == 0) q.offer(e.v);
            }
        }
        if (topo.size() != n) return Collections.emptyList(); // ciclo
        return topo;
    }
}
