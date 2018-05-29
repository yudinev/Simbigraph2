package simbigraph.stat.algorithms.cluster.betweenness;

import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

import java.util.*;

public class BetweennessClusterer {
    Graph graph;

    public BetweennessClusterer(Graph graph) {
        this.graph = graph;
    }

    public Set<Set<Number>> start(Graph graph) {
        Map result = new HashMap<Double, Set<Set<Number>>>(graph.getEdgeCount());

        for(int i = 0; i <= graph.getEdgeCount(); ++i) {
            Set<Set<Number>> cl = cluster(graph, i);
            result.put(modularity((UndirectedSparseGraph) graph, cl), cl);
        }

        double maxQ = (Double)result.keySet().stream().max(Comparator.naturalOrder()).get();

        return (Set<Set<Number>>) result.get(maxQ);
    }

    private Set<Set<Number>> cluster(Graph graph, int numEdgesToRemove) {
        EdgeBetweennessClusterer<Number, Number> clusterer = new EdgeBetweennessClusterer<>(numEdgesToRemove);
        Set<Set<Number>> clusterSet = clusterer.transform(graph);
        List<Number> edges = clusterer.getEdgesRemoved();

        return clusterSet;
    }

    private double modularity(UndirectedSparseGraph graph, Set<Set<Number>> communities /*Set<Number> removeEdges*/) {
        double Q = 0.0;
        int m = graph.getEdgeCount();

        for(Iterator v1It = graph.getVertices().iterator(); v1It.hasNext();) {
            Number v1 = (Number)v1It.next();
            for(Iterator v2It = graph.getVertices().iterator(); v2It.hasNext();) {
                Number v2 = (Number)v2It.next();

                Q += ((graph.isNeighbor(v1, v2) ? 1.0 : 0.0) - (double)(graph.degree(v1) * graph.degree(v2)) / (2.0 * m)) * (sameCluster(communities, v1, v2) ? 1.0 : 0.0);
            }
        }

        return Q / (2.0 * m);
    }

    private boolean sameCluster(Set<Set<Number>> communities, Number v1, Number v2) {
        for(Iterator clIt = communities.iterator(); clIt.hasNext();) {
            Set<Number> cluster = (Set<Number>) clIt.next();
            if(cluster.contains(v1) && cluster.contains(v2)) {
                return true;
            }
        }

        return false;
    }
}
