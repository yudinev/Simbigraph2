package simbigraph.stat.algorithms.cluster.louvain;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FormatConverter<E> {
    public static final double EDGE_WEIGHT = 1.0;

    public Graph<Number, Number> convertToPajekFormat(Graph<?, E> graph) {
        Graph g = new UndirectedSparseGraph<Number, Number>();

        Map uniqueNodes = new HashMap<Object, Number>();

        int i = 0;
        Iterator itNodes = graph.getVertices().iterator();
        while (itNodes.hasNext()) {
            uniqueNodes.put(itNodes.next(), i);
            g.addVertex(i);
            ++i;
        }


        for (E e : graph.getEdges()) {
            Pair<?> p = graph.getEndpoints(e);
            g.addEdge(e, (Number) uniqueNodes.get(p.getFirst()), (Number) uniqueNodes.get(p.getSecond()));
        }

        return g;
    }

    public Edge[] convertToEdgeFormat(Graph<?, E> graph) {
        Edge[] edges = new Edge[graph.getEdgeCount()];

        Map uniqueNodes = new HashMap<Object, Number>();

        Iterator itNodes = graph.getVertices().iterator();
        for(int i = 0; itNodes.hasNext(); ++i) {
            uniqueNodes.put(itNodes.next(), i);
        }

        int i = 0;
        for(E e : graph.getEdges()) {
            edges[i] = new Edge();
            Pair<?> p = graph.getEndpoints(e);
            edges[i].v = (int)uniqueNodes.get(p.getFirst());
            edges[i].weight = EDGE_WEIGHT;
            edges[i].next = (int)uniqueNodes.get(p.getSecond());
            ++i;
        }

        return edges;
    }
}
