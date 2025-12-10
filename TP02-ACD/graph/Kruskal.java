package graph;

import java.util.*;

public class Kruskal {
    public static List<Edge> mst(Graph g) {
        if (g.directed()) throw new IllegalArgumentException("Kruskal works on undirected graphs");
        if (!g.weighted()) throw new IllegalArgumentException("Kruskal requires weighted graph");
        List<Edge> edges = new ArrayList<>();
        for (Edge e : g.edges()) edges.add(e);
        Collections.sort(edges);
        UnionFind uf = new UnionFind(g.V());
        List<Edge> mst = new ArrayList<>();
        for (Edge e : edges) {
            if (uf.union(e.u, e.v)) {
                mst.add(e);
                if (mst.size() == g.V()-1) break;
            }
        }
        return mst;
    }
}
