package graph;

import java.util.*;

public class DFS {
    public static List<Integer> run(Graph g, int s) {
        boolean[] visited = new boolean[g.V() + 1];
        List<Integer> order = new ArrayList<>();
        dfs(g, s, visited, order);
        return order;
    }

    private static void dfs(Graph g, int u, boolean[] visited, List<Integer> order) {
        visited[u] = true;
        order.add(u);
        for (Edge e : g.adj(u)) {
            if (!visited[e.v]) dfs(g, e.v, visited, order);
        }
    }
}
