package graph;

import java.util.*;

public class Dijkstra {
    public static double[] dist(Graph g, int src) {
        if (!g.weighted()) throw new IllegalArgumentException("Dijkstra requires weighted graph");
        int n = g.V();
        double INF = Double.POSITIVE_INFINITY;
        double[] d = new double[n+1];
        Arrays.fill(d, INF);
        d[src] = 0.0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[1]));
        pq.offer(new int[]{src,0});
        // Use a custom PQ with pairs (node, dist). We keep d[] to check outdated entries.
        PriorityQueue<NodeDist> pq2 = new PriorityQueue<>(Comparator.comparingDouble(a -> a.dist));
        pq2.offer(new NodeDist(src, 0.0));
        while (!pq2.isEmpty()) {
            NodeDist nd = pq2.poll();
            int u = nd.node;
            double du = nd.dist;
            if (du != d[u]) continue;
            for (Edge e : g.adj(u)) {
                if (d[e.v] > d[u] + e.weight) {
                    d[e.v] = d[u] + e.weight;
                    pq2.offer(new NodeDist(e.v, d[e.v]));
                }
            }
        }
        return d;
    }

    private static class NodeDist {
        int node;
        double dist;
        NodeDist(int n, double d) { node = n; dist = d; }
    }
}
