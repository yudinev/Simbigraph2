package simbigraph.stat.algorithms.subgraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import simbigraph.graphs.prefAttachment.GenNonIntBA;
import simbigraph.graphs.prefAttachment.PrefferentialAttachment;

public class TestRandEsuDir3 {
	static FileWriter writeFile;
	public static void main(String[] args) throws IOException {
		Graph<Long, Long> graph;
		/*graph = new SparseGraph<Long, Long>();
		
		graph.addEdge(1l, 1l, 2l, EdgeType.UNDIRECTED);
	    graph.addEdge(2l, 2l, 3l, EdgeType.UNDIRECTED);
		graph.addEdge(3l, 3l, 1l, EdgeType.UNDIRECTED);
		  
		graph.addEdge(4l, 1l, 4l, EdgeType.DIRECTED);
		graph.addEdge(5l, 1l, 5l, EdgeType.DIRECTED);

		graph.addEdge(6l, 3l, 9l, EdgeType.DIRECTED);
		graph.addEdge(7l, 3l, 8l, EdgeType.DIRECTED);
		 
		  
		graph.addEdge(8l, 2l, 7l, EdgeType.DIRECTED);
		graph.addEdge(9l, 2l, 6l, EdgeType.DIRECTED);
		*/
		
		//graph =	 Test.initGraphLong("C:\\Users\\yudinev\\Desktop\\Диссертация МАША\\Программы\\GraphStats-master\\RealGraphs\\Email_obr_new.net");
	    graph = Test.initGraphLong("my_polBlog1.net");

		
		/*graph.addEdge(4l, 2l, 1l, EdgeType.DIRECTED);
		graph.addEdge(5l, 1l,3l, EdgeType.DIRECTED);
		graph.addEdge(6l, 3l,2l,EdgeType.DIRECTED);*/
		
		// graph = Test.initGraphLong();
		// graph = getReshotka(5, 5);
	//	graph = Test.initGraphLong("MyAs.net");
		
		File logFile = new File("PolBlogs.txt");
		writeFile = new FileWriter(logFile);


	//	graph = Test.initGraphLong("BA10000.net");
	//	graph = GenNPA(new double[]{0.,0.,1.},1000);

		/*
		  graph = new UndirectedSparseGraph<Long, Long>(); 
		  graph.addEdge(1l, 1l, 2l);
		  graph.addEdge(2l, 2l, 3l);
		  graph.addEdge(3l, 3l, 1l);
		  
		  graph.addEdge(4l, 1l, 4l);
		  graph.addEdge(5l, 1l, 5l);

		  graph.addEdge(6l, 3l, 9l);
		  graph.addEdge(7l, 3l, 8l);
		  
		  
		  graph.addEdge(8l, 2l, 7l);
		  graph.addEdge(9l, 2l, 6l);
		 

*/		
		/* graph.addEdge(1l, 1l, 2l);
		 graph.addEdge(2l, 2l, 3l);
		 graph.addEdge(3l, 3l, 1l);
		 graph.addEdge(7l, 3l, 7l);
		 graph.addEdge(4l, 3l, 4l);
		 graph.addEdge(5l, 3l, 5l);
		 graph.addEdge(6l, 3l, 6l);*/
		 

		// graph = Test.initGraphLong("mygraphs//my_polBlog1.net");
		// graph = Test.initGraphLong("RealGraphs//PGPgiantcompo.net");
		System.out.println("PolBlogs");
		System.out.println("E=" + graph.getEdgeCount() + " V=" + graph.getVertexCount());
		long t1 = System.currentTimeMillis();
		enumirateSubgraphs(graph, 3);
		writeFile.close();

		System.out.println("t=" + (t1 - System.currentTimeMillis()));
		//System.out.println(num);
		System.out.println("-------------------");
	/*	System.out.println("" + c1);
		System.out.println("" + c2);
		System.out.println("" + c3);
		System.out.println("" + c4);
		System.out.println("" + c5);
		System.out.println("" + c6);
		System.out.println("" + c7);
		System.out.println("" + c8);
		System.out.println("" + c9);
		System.out.println("" + c10);
		System.out.println("" + c11);
		System.out.println("" + c12);
		System.out.println("" + c13);*/// return 0;
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
		
		//Graph graph = new UndirectedSparseGraph<Long, Long>();
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

	static int c1 = 0;
	static int c2 = 0;
	static	int c3 = 0;
	static int c4 = 0;
	static int c5 = 0;
	static int c6 = 0;
	static int c7 = 0;
	static int c8 = 0;
	static int c9 = 0;
	static int c10 = 0;
	static int c11 = 0;
	static int c12 = 0;
	static int c13 = 0;

	public static int searchOrientedTypesOfSubgraphs(Graph<Long,Long> graph, Long n1, Long n2, Long n3) {

					Long v1= null; Long v2 = null; Long v3= null;
					if(graph.isNeighbor(n1, n2)&&(graph.isNeighbor(n1, n3))){v1=n1;v2=n2;v3=n3;}
					else if(graph.isNeighbor(n2, n1)&&(graph.isNeighbor(n2, n3))){v1=n2;v2=n1;v3=n3;}
					else if(graph.isNeighbor(n3, n1)&&(graph.isNeighbor(n3, n2))){v1=n3;v2=n2;v3=n1;}
					else System.out.println("wrong");
					
					Long i = graph.findEdge(v1, v2);
					if (i == null)
						i = graph.findEdge(v2, v1);
					
					Long j = graph.findEdge(v1, v3);
					if (j == null)
						j = graph.findEdge(v3, v1);
					
					

					// choose pattern
					if ((graph.getEdgeType(j).equals(EdgeType.UNDIRECTED))
							&& (graph.getEdgeType(i).equals(EdgeType.UNDIRECTED))) // это
																					// ребра
					{
						if (!graph.isNeighbor(v2, v3))
							c1++;

						else {
							Long o = graph.findEdge(v2, v3);
							if (o == null)
								o = graph.findEdge(v3, v2);
							if (graph.getEdgeType(o).equals(EdgeType.UNDIRECTED))
								c7++;
							else
								c10++;

						}
					} else if ((graph.getEdgeType(j).equals(EdgeType.UNDIRECTED))// одна
																					// из
																					// связей
																					// -
																					// ребро
							|| (graph.getEdgeType(i).equals(EdgeType.UNDIRECTED))) {
						Long l = null, e = null;
						// определяю где ребро, а где дуга
						if (graph.getEdgeType(j).equals(EdgeType.UNDIRECTED)) {
							e = j;
							l = i;
						} else {
							e = i;
							l = j;
						}

						Long end_l = graph.getOpposite(v1, l);
						Long end_e = graph.getOpposite(v1, e);

						Object dest = graph.getDest(l);
						if (dest == end_l) {
							if (!graph.isNeighbor(v2, v3))
								c2++;
							else {
								Long o1 = graph.findEdge(end_l, end_e);
								if (o1 == null)
									o1 = graph.findEdge(end_e, end_l);

								if (graph.getEdgeType(o1).equals(EdgeType.DIRECTED)) {
									Long dest2 = graph.getDest(o1);
									if (dest2 == end_l)
										c12++;
									else
										c8++;

								} else
									c10++;
							}
						} else {
							if (!graph.isNeighbor(v2, v3))
								c4++;
							else {
								Long o1 = graph.findEdge(end_l, end_e);
								if (o1 == null)
									o1 = graph.findEdge(end_e, end_l);

								if (graph.getEdgeType(o1).equals(EdgeType.DIRECTED)) {
									Object dest2 = graph.getDest(o1);
									if (dest2 == end_l)
										c8++;
									else
										c9++;

								} else
									c10++;
							}
							// если основа - дуги
						}
					}
					else if ((graph.getSource(i) == v1) && (graph.getSource(j) == v1)) {// обе
																					// дуги

						if (!graph.isNeighbor(v2, v3))
							c3++;
						else {
							Long o1 = graph.findEdge(v2, v3);
							if (o1 == null)
								o1 = graph.findEdge(v3, v2);
							if (graph.getEdgeType(o1).equals(EdgeType.UNDIRECTED))
								c9++;
							else
								c11++;
						}
					}

					else if ((graph.getDest(i) == v1) && (graph.getDest(j) == v1)) {
						if (!graph.isNeighbor(v2, v3))
							c5++;
						else {
							Long o1 = graph.findEdge(v2, v3);
							if (o1 == null)
								o1 = graph.findEdge(v3, v2);
							if (graph.getEdgeType(o1).equals(EdgeType.UNDIRECTED))
								c12++;
							else
								c11++;
						}

					} else {
						Long in = null, out = null;
						// определяю где входящая, а где исходящая дуга
						if (graph.getSource(i) == v1) {
							out = i;
							in = j;
						} else {
							out = j;
							in = i;
						}

						Long end_in = graph.getOpposite(v1, in);
						Long end_out = graph.getOpposite(v1, out);
						if (!graph.isNeighbor(v2, v3))
							c6++;
						else {
							Long o1 = graph.findEdge(end_in, end_out);
							if (o1 == null)
								o1 = graph.findEdge(end_out, end_in);

							if (graph.getEdgeType(o1).equals(EdgeType.DIRECTED)) {
								Object dest2 = graph.getDest(o1);
								if (dest2 == end_in)
									c13++;
								else
									c11++;

							} else
								c8++;
						}

					}

					 return 0;
	}
	
	private static void extendSubgraph(MyHashSet<Long> Vsub, Set<Long> Vext, Long v, Graph<Long, Long> graph) throws IOException {

		/*if ((Vsub.size() == 2) && Math.random() < 0.5)
			Vsub.check();*/
		
		if (Vsub.size() == 2) {
			
			writeFile.write(""+c1+" "+c2+" "+c3+" "+c4+" "+c5+" "+c6+" "+c7+" "+c8+" "+c9+" "+c10+" "+c11+" "+c12+" "+c13+"\n");

			
			
			//System.out.println(""+num);
			num=0;
			c1=0;
			c2=0;
			c3=0;
			c4=0;
			c5=0;
			c6=0;
			c7=0;
			c8=0;
			c9=0;
			c10=0;
			c11=0;
			c12=0;
			c13=0;
			//System.out.println("---");
			writeFile.write(""+Vext.size()+" ");
			//System.out.print(" "+ Vext.size()+" ");

		}
		// 1
		if (Vsub.size() == 3) {// num++;
			
			// проверить треугольник или нет
			Long[] mass = Vsub.toArray(new Long[0]);
			searchOrientedTypesOfSubgraphs(graph,mass[0],mass[1],mass[2]);
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