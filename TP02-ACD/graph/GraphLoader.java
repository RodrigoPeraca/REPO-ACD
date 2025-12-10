package graph;

import java.io.*;
import java.util.*;

public class GraphLoader {
    // lê arquivo no formato .gr (p sp V E \n a u v w ...)
    public static AdjListGraph loadAdjListFromGR(String path, boolean directed, boolean weighted) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        int V = -1;
        AdjListGraph g = null;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("c")) continue;
            if (line.startsWith("p")) {
                String[] parts = line.split("\\s+");
                V = Integer.parseInt(parts[2]);
                g = new AdjListGraph(V, directed, weighted);
            } else if (line.startsWith("a") || line.startsWith("e")) {
                String[] parts = line.split("\\s+");
                int u = Integer.parseInt(parts[1]);
                int v = Integer.parseInt(parts[2]);
                double w = weighted ? Double.parseDouble(parts[3]) : 1.0;
                g.addEdge(u, v, w);
            }
        }
        br.close();
        if (g == null) throw new IOException("Formato inválido ou sem linha 'p'");
        return g;
    }
}
