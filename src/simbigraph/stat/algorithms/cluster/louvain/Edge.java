package simbigraph.stat.algorithms.cluster.louvain;

public class Edge implements Cloneable{
    public int v;
    double weight;
    public int next;
    Edge(){}
    public Object clone(){
        Edge temp=null;
        try{
            temp = (Edge)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
