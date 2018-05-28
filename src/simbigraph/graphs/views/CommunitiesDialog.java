package simbigraph.graphs.views;

import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import org.apache.commons.collections15.Transformer;
import org.jfree.ui.tabbedui.VerticalLayout;
import simbigraph.core.Context;
import simbigraph.stat.algorithms.cluster.louvain.Edge;
import simbigraph.stat.algorithms.cluster.louvain.FormatConverter;
import simbigraph.stat.algorithms.cluster.louvain.LouvainClusterer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.base.Functions;

public class CommunitiesDialog extends JDialog {
    public static final int LARGE_GRAPH_NODES_COUNT = 50;
    private final JPanel contentPanel = new JPanel();
    JRadioButton betweennessMethod;
    JRadioButton louvainMethod;

    VisualizationViewer<Number, Number> vv;
    LoadingCache<Number, Color> vertexPaints;
    LoadingCache<Number, Color> edgePaints;

    public static void main(String[] args) {
        try {
            CommunitiesDialog dialog = new CommunitiesDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CommunitiesDialog() {
        this.vertexPaints = CacheBuilder.newBuilder().build(CacheLoader.from(Functions.constant(Color.white)));
        this.edgePaints = CacheBuilder.newBuilder().build(CacheLoader.from(Functions.constant(Color.blue)));

        setAlwaysOnTop(true);
        setResizable(false);
        setModal(true);
        setTitle("Communities clustering");
        setBounds(100, 100, 800, 600);

        //will be button 'Run' that be ask the user draw or write in clustering result in file if graph too large
        //also will be panel with radio buttons for choose clustering method
        //graph will get from Context

        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel);
        contentPanel.setLayout(new VerticalLayout()); //new BorderLayout());

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Choose algorithm", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
        //panel.setBounds(29, 22, 658, 114);
        contentPanel.add(BorderLayout.WEST, panel);
        contentPanel.add(BorderLayout.WEST, Box.createVerticalGlue());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        betweennessMethod = new JRadioButton("Betweenness");
        betweennessMethod.setSelected(true);
        //betweennessMethod.setBounds(82, 43, 156, 23);
        panel.add(betweennessMethod);

        louvainMethod = new JRadioButton("Louvain");
        //louvainMethod.setBounds(82, 69, 156, 23);
        panel.add(louvainMethod);

        ButtonGroup group = new ButtonGroup();
        group.add(betweennessMethod);
        group.add(louvainMethod);

        JButton startButton = new JButton("Clustering");
        //startButton.setBounds(527, 57, 100, 25);
        //panel.add(Box.createHorizontalStrut(100));
        //panel.add(BorderLayout.CENTER, startButton);
        panel.add(BorderLayout.CENTER, Box.createHorizontalBox().add(startButton));
        //startButton.setActionCommand("Cancel");

        panel.setBounds(0, 0, 120, 100);

        JPanel clusteringResult = new JPanel();
        contentPanel.add(Box.createHorizontalBox().add(clusteringResult));

        clusteringResult.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));

        FormatConverter fc = new FormatConverter<Number>();

        // configureLayout(fc.convertToPajekFormat(new UndirectedSparseGraph()), clusteringResult);

        AggregateLayout<Number, Number> layout = configureLayout(fc.convertToPajekFormat(new UndirectedSparseGraph()));
        configureViewer(layout);
        addViewer(clusteringResult);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graph g = Context.getGraph();

                int outputResultMethod = -1;
                if(g.getVertexCount() >= LARGE_GRAPH_NODES_COUNT) {
                    outputResultMethod = JOptionPane.showConfirmDialog(contentPanel, "Graph contains " + g.getVertexCount() + " nodes (equal or more than maximum of " + LARGE_GRAPH_NODES_COUNT + " nodes), maybe result of clustering don't visualize, but output to file?","Graph too large", JOptionPane.YES_NO_OPTION);
                }

                Set<Set<Number>> communities = startClustering(g);

                System.out.println(communities);

                switch(outputResultMethod) {
                    case JOptionPane.YES_OPTION:
                        FormatConverter fc = new FormatConverter<Number>();
                        AggregateLayout<Number, Number> layout = configureLayout(fc.convertToPajekFormat(g));
                        setLayout(layout);
                        visualize(fc.convertToPajekFormat(g), communities, layout);
                        break;
                    case JOptionPane.NO_OPTION:
                        break;
                    case JOptionPane.DEFAULT_OPTION:
                    default:
                        break;
                }

            }
        });
    }

    private Set<Set<Number>> startClustering(Graph g) {
        if(louvainMethod.isSelected()) {
            LouvainClusterer lv = new LouvainClusterer();
            FormatConverter fc = new FormatConverter<Number>();
            Edge[] edges = fc.convertToEdgeFormat(g);

            lv.init(edges, g.getVertexCount(), 3); //10
            Set<Set<Number>> s = lv.louvain();

            return s;
        }

        if(betweennessMethod.isSelected()) {
            return null;
        }

        return null;
    }

    // ---

    private AggregateLayout<Number, Number> configureLayout(Graph graph) {
        // panel.removeAll();
        FRLayout frl = new FRLayout(graph);
        AggregateLayout<Number, Number> layout = new AggregateLayout(frl);

        // this.vv.setBackground(Color.white);
        // Random r = new Random();

        // JPanel panel = new JPanel();

        // add(panel, BorderLayout.SOUTH);
        // panel.setSize(400, 300);

        return layout;
    }

    private void setLayout(AggregateLayout<Number, Number> layout) {
        //this.vv = new VisualizationViewer(layout);
        vv.setGraphLayout(layout);
    }

    private void configureViewer(AggregateLayout<Number, Number> layout) {
        this.vv = new VisualizationViewer(layout);
        //vv.setGraphLayout(layout);

        Random r = new Random();

        // this.vv.setBackground(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
        // this.vv.getRenderContext().setVertexFillPaintTransformer((Transformer<Number, Paint>) vertexPaints);
        this.vv.getRenderContext().setVertexFillPaintTransformer(new Transformer<Number, Paint>() {
            public Paint transform(Number number) {
                Random r = new Random();
                try {
                    return vertexPaints.get(number); // new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)); // Color.white;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return Color.white;
            }
        });
        // this.vv.getRenderContext().setVertexDrawPaintTransformer((Transformer<Number, Paint>) v -> vv.getPickedVertexState().isPicked(v) ? Color.cyan : Color.BLACK);
        this.vv.getRenderContext().setVertexDrawPaintTransformer(new Transformer<Number, Paint>() {
            public Paint transform(Number number) {
                return vv.getPickedVertexState().isPicked(number) ? Color.cyan : Color.black;
            }
        });

        DefaultModalGraphMouse<Number, Number> gm = new DefaultModalGraphMouse();
        this.vv.setGraphMouse(gm);
    }

    private void addViewer(JPanel panel) {
        panel.removeAll();

        panel.setLayout(new BorderLayout());

        panel.add(this.vv);
        panel.setVisible(true);
    }

    // ---

    private void visualize(Graph graph, Set<Set<Number>> communities, AggregateLayout<Number, Number> layout) {

        //contentPanel.add(panel);

        recolor(graph, layout, communities, true);
    }

    private void groupCluster(AggregateLayout<Number, Number> layout, Set<Number> vertices) {
        if (vertices.size() < layout.getGraph().getVertexCount()) {
            Point2D center = layout.transform(vertices.iterator().next()); // apply(vertices.iterator().next());
            Graph<Number, Number> subGraph = (Graph) SparseMultigraph.getFactory().create(); // get();
            Iterator var5 = vertices.iterator();

            while (var5.hasNext()) {
                Number v = (Number) var5.next();
                subGraph.addVertex(v);
            }

            Layout<Number, Number> subLayout = new CircleLayout(subGraph);
            subLayout.setInitializer(this.vv.getGraphLayout());
            subLayout.setSize(new Dimension(vertices.size()*10, vertices.size()*10));
            layout.put(subLayout, center);
            this.vv.repaint();
        }
    }

    public void recolor(Graph graph, AggregateLayout<Number, Number> layout, Set<Set<Number>> clSet, boolean groupClusters) {
        Graph<Number, Number> g = graph; //layout.getGraph();
        layout.removeAll();
        layout.setGraph(g);
        Random r = new Random();

        int i = 0;
        Iterator cIt;
        for(cIt = clSet.iterator(); cIt.hasNext(); ++i) {
            Set<Number> vertices = (Set) cIt.next();
            Color c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            colorCluster(vertices, c);
            if(groupClusters) {
                groupCluster(layout, vertices);
            }
        }
    }

    private void colorCluster(Set<Number> vertices, Color c) {
        Iterator var3 = vertices.iterator();

        while (var3.hasNext()) {
            Number v = (Number) var3.next();
            this.vertexPaints.put(v, c);
        }

    }
}
