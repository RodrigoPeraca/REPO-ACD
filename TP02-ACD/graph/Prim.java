package graph;

import java.util.*;

public class Prim {
    public static List<Edge> mst(Graph g, int start) {
        if (g.directed()) throw new IllegalArgumentException("Prim works on undirected graphs");
        if (!g.weighted()) throw new IllegalArgumentException("Prim requires weighted graph");
        boolean[] used = new boolean[g.V()+1];
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.weight));
        List<Edge> mst = new ArrayList<>();
        used[start] = true;
        for (Edge e : g.adj(start)) pq.offer(e);
        while (!pq.isEmpty()) {
            Edge e = pq.poll();
            if (used[e.v]) continue;
            mst.add(e);
            used[e.v] = true;
            for (Edge next : g.adj(e.v)) {
                if (!used[next.v]) pq.offer(next);
            }
        }
        return mst;
    }
}
