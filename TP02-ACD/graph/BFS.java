package graph;

import java.util.*;

public class BFS {
    public static List<Integer> run(Graph g, int s) {
        boolean[] visited = new boolean[g.V() + 1];
        List<Integer> order = new ArrayList<>();
        Queue<Integer> q = new LinkedList<>();
        visited[s] = true;
        q.offer(s);
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (Edge e : g.adj(u)) {
                if (!visited[e.v]) {
                    visited[e.v] = true;
                    q.offer(e.v);
                }
            }
        }
        return order;
    }
}
