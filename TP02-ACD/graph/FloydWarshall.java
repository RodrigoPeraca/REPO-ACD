package graph;

import java.util.*;

public class FloydWarshall {
    public static double[][] run(Graph g) {
        if (!g.weighted()) throw new IllegalArgumentException("Floyd-Warshall expects weighted");
        int n = g.V();
        double INF = Double.POSITIVE_INFINITY;
        double[][] d = new double[n+1][n+1];
        for (int i = 1; i <= n; i++) for (int j = 1; j <= n; j++) d[i][j] = (i==j?0:INF);
        for (Edge e : g.edges()) d[e.u][e.v] = Math.min(d[e.u][e.v], e.weight);
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (d[i][k] + d[k][j] < d[i][j]) d[i][j] = d[i][k] + d[k][j];
                }
            }
        }
        return d;
    }
}
