package simbigraph.graphs.views;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import org.jfree.ui.tabbedui.VerticalLayout;
import simbigraph.core.Context;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommunitiesDialog extends JDialog {
    public static final int LARGE_GRAPH_NODES = 50;
    private final JPanel contentPanel = new JPanel();

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
        System.out.println(Context.getGraph());

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
        contentPanel.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Choose algorithm", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
        //panel.setBounds(29, 22, 658, 114);
        contentPanel.add(BorderLayout.WEST, panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JRadioButton betweennessMethod = new JRadioButton("Betweenness");
        betweennessMethod.setSelected(true);
        //betweennessMethod.setBounds(82, 43, 156, 23);
        panel.add(betweennessMethod);

        JRadioButton louvainMethod = new JRadioButton("Louvain");
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
        startButton.setActionCommand("Cancel");

        panel.setBounds(0, 0, 120, 100);

        JPanel clusteringResult = new JPanel();

        contentPanel.add(BorderLayout.CENTER, clusteringResult);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graph g = Context.getGraph();
                int outputResultMethod = -1;
                if(g.getVertexCount() >= LARGE_GRAPH_NODES) {
                    outputResultMethod = JOptionPane.showConfirmDialog(contentPanel, "Graph contains " + g.getVertexCount() + " nodes (equal or more than maximum of " + LARGE_GRAPH_NODES + " nodes), maybe result of clustering don't visualize, but output to file?","Graph too large", JOptionPane.YES_NO_OPTION);
                }

                switch(outputResultMethod) {
                    case JOptionPane.YES_OPTION:
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
}
