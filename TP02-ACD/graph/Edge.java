package graph;

public class Edge implements Comparable<Edge> {
    public final int u;
    public final int v;
    public final double weight;

    public Edge(int u, int v) {
        this(u, v, 1.0);
    }

    public Edge(int u, int v, double weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge o) {
        return Double.compare(this.weight, o.weight);
    }

    @Override
    public String toString() {
        return String.format("(%d -> %d, w=%.2f)", u, v, weight);
    }
}