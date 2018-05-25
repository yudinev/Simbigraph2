package simbigraph.stat.algorithms.cluster.louvain;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;

public class FormatConverter<E> {
    public Edge[] convertFromJungToEdgeFormat(Graph<Number, E> g) {
        Edge[] edges = new Edge[g.getEdgeCount()];

        int i = 0;
        for (E e:g.getEdges()) {
            edges[i] = new Edge();
            Pair<Number> p = g.getEndpoints(e);
            edges[i].v = (int)p.getFirst();
            edges[i].weight = 1.0;
            edges[i].next = (int)p.getSecond();
            ++i;
        }

        return edges;
    }
}
