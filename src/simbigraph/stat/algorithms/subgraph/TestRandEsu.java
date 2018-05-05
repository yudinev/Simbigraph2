package simbigraph.stat.algorithms.subgraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import simbigraph.graphs.prefAttachment.GenNonIntBA;
import simbigraph.graphs.prefAttachment.PrefferentialAttachment;

class MyHashSet<V> extends HashSet<V> {
	private boolean checked = false;

	public void setCheck(boolean b) {
		checked = b;
	}

	public void check() {
		checked = true;
	}

	public boolean IsChecked() {
		return checked;
	}
}

public class TestRandEsu {

	static FileWriter writeFile;

	public static void main(String[] args) throws IOException {
		Graph<Long, Long> graph;
		// graph = Test.initGraphLong();
		// graph = getReshotka(5, 5);
		graph = Test.initGraphLong("MyAs.net");

		File logFile = new File("AS-count.txt");
		writeFile = new FileWriter(logFile);

		// graph = Test.initGraphLong("BA10000.net");
		// graph = GenNPA(new double[]{0.,0.,1.},1000);

		/*
		 * graph = new UndirectedSparseGraph<Long, Long>(); graph.addEdge(1l,
		 * 1l, 2l); graph.addEdge(2l, 2l, 3l); graph.addEdge(3l, 3l, 1l);
		 * 
		 * graph.addEdge(4l, 1l, 4l); graph.addEdge(5l, 1l, 5l);
		 * 
		 * graph.addEdge(6l, 3l, 9l); graph.addEdge(7l, 3l, 8l);
		 * 
		 * 
		 * graph.addEdge(8l, 2l, 7l); graph.addEdge(9l, 2l, 6l);
		 * 
		 * 
		 */
		/*
		 * graph.addEdge(1l, 1l, 2l); graph.addEdge(2l, 2l, 3l);
		 * graph.addEdge(3l, 3l, 1l); graph.addEdge(7l, 3l, 7l);
		 * graph.addEdge(4l, 3l, 4l); graph.addEdge(5l, 3l, 5l);
		 * graph.addEdge(6l, 3l, 6l);
		 */

		// graph = Test.initGraphLong("mygraphs//my_polBlog1.net");
		// graph = Test.initGraphLong("RealGraphs//PGPgiantcompo.net");
		System.out.println("E=" + graph.getEdgeCount() + " V=" + graph.getVertexCount());
		// long t1 = System.currentTimeMillis();
		enumirateSubgraphs(graph, 3);
		writeFile.close();

		// System.out.println("t=" + (t1 - System.currentTimeMillis()));
		System.out.println(num);
	}

	private static Graph<Long, Long> GenNPA(double[] d, int step) {
		Factory<Long> vertexFactory = new Factory<Long>() { // фабрика
			// для
			// создания
			// вершин
			int i = 0;

			public Long create() {
				return new Long(i++);
			}
		};
		Factory<Long> edgeFactory = new Factory<Long>() { // фабрика
			// для
			// создания
			// ребер
			int i = 0;

			public Long create() {
				return new Long(i++);
			}
		};

		// Graph graph = new UndirectedSparseGraph<Long, Long>();
		GenNonIntBA<Long, Long> genBA = new GenNonIntBA<Long, Long>(vertexFactory, edgeFactory, d, new PrefferentialAttachment() {

			@Override
			public double f(int k) {
				return k;
			}

			@Override
			public int getM() {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		Graph<Long, Long> graph = genBA.evolve(step, seed_graph());

		return graph;
	}

	static Graph<Long, Long> seed_graph() {
		Graph<Long, Long> gr = new UndirectedSparseGraph<Long, Long>();
		for (int i = -1; i > -5; i--) {
			Long n = new Long(i);
			gr.addVertex(n);
		}
		int l = -1;
		Object[] mass = gr.getVertices().toArray();
		for (int i = 0; i < mass.length - 1; i++)
			for (int j = i + 1; j < mass.length; j++)
				if (i != j)
					gr.addEdge(new Long(l--), (Long) mass[i], (Long) mass[j]);

		return gr;
	}

	private static Graph<Long, Long> getReshotka(int w, int h) {
		int edge = 0;
		int count = 0;
		Graph graph = new UndirectedSparseGraph<Long, Long>();
		Long[][] mass = new Long[w][h];
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				Long v = new Long(count++);
				graph.addVertex(v);
				mass[i][j] = v;
			}
		}
		for (int i = 0; i < mass.length; i++) {
			for (int j = 0; j < mass.length - 1; j++) {
				graph.addEdge(new Long(edge++), mass[i][j], mass[i][j + 1]);

			}
		}
		for (int j = 0; j < mass.length; j++) {
			for (int i = 0; i < mass.length - 1; i++) {
				graph.addEdge(new Long(edge++), mass[i][j], mass[i + 1][j]);

			}
		}
		System.out.println("V=" + graph.getVertexCount() + "   " + "E= " + graph.getEdgeCount());
		return graph;
	}

	private static void enumirateSubgraphs(Graph<Long, Long> graph, int i) throws IOException {
		// 1
		for (Long v : graph.getVertices()) {
			// 2
			Set<Long> Vext = new HashSet();
			for (Long n : graph.getNeighbors(v)) {
				if (n > v)
					Vext.add(n);
			}
			// 3
			MyHashSet<Long> V = new MyHashSet();
			V.add(v);
			// if(Math.random()<0.5)
			extendSubgraph(V, Vext, v, graph);
		}
	}

	static int num = 0;

	private static void extendSubgraph(MyHashSet<Long> Vsub, Set<Long> Vext, Long v, Graph<Long, Long> graph)
			throws IOException {

		/*
		 * if ((Vsub.size() == 2) && Math.random() < 0.5) Vsub.check();
		 */

		if (Vsub.size() == 2) {
			writeFile.write("" + num + "\n");
			// System.out.println(""+num);
			num = 0;
			// System.out.println("---");
			writeFile.write("" + Vext.size() + " ");
			// System.out.print(" "+ Vext.size()+" ");

		}
		// 1
		if (Vsub.size() == 3) {// num++;

			/*
			 * for (Long long1 : Vsub) { System.out.print(":" + long1 + " "); }
			 */

			// System.out.print("checked:" + Vsub.IsChecked());
			// проверить треугольник или нет
			Object[] mass = Vsub.toArray();
			if (graph.isNeighbor((Long) mass[0], (Long) mass[1]) && graph.isNeighbor((Long) mass[2], (Long) mass[1])
					&& graph.isNeighbor((Long) mass[2], (Long) mass[0])) {
				num++;
				// System.out.println(" triangle?" + true);

			} else {
				// System.out.println(" triangle?" + false);

			}
			return;
		}

		// 2
		while (Vext.size() > 0) {
			// Long w = Vext.stream().findAny().get();

			for (Iterator<Long> it = Vext.iterator(); it.hasNext();) {
				Long w = (Long) it.next();
				// 3
				it.remove();
				// 4
				Set<Long> _Vext = new HashSet(Vext);

				Set<Long> tempSet = new HashSet(graph.getNeighbors(w));
				Set<Long> tempSet2 = new HashSet();
				for (Long v_ : Vsub)
					for (Long v2_ : graph.getNeighbors(v_))
						if (tempSet.contains(v2_))
							tempSet2.add(v2_);

				tempSet.addAll(new HashSet(Vsub));
				tempSet.removeAll(tempSet2);

				for (Iterator iterator = tempSet.iterator(); iterator.hasNext();) {
					Long u = (Long) iterator.next();
					if (u <= v || Vsub.contains(u) || u == w) {
						iterator.remove();
					}

				}
				_Vext.addAll(tempSet);
				_Vext.remove(w);

				// 5

				MyHashSet<Long> newVsub = new MyHashSet<Long>();
				newVsub.addAll(Vsub);
				newVsub.setCheck(Vsub.IsChecked());

				newVsub.add(w);
				extendSubgraph(newVsub, _Vext, v, graph);
			}
		}

	}
}