/* Driver code for PERT algorithm (LP4)
 * @author
 */

// change package to your netid
package kyk170030;

import rbk.Graph;
import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;


import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

//import static sxs178130.DFS.depthFirstSearch;

public class PERT extends GraphAlgorithm<PERT.PERTVertex> {

    private List<Vertex> finishList;
    public static class PERTVertex implements Factory {
        private int ec;
        private int lc;
        private int slack;
        private int duration;


        public PERTVertex(Vertex u) {
            this.ec = 0;
            this.lc = 0;
            this.slack = 0;
            this.duration = 0;
        }
        public PERTVertex make(Vertex u) { return new PERTVertex(u); }
    }

    public PERT(Graph g) {
        super(g, new PERTVertex(null));
    }

    public void setDuration(Vertex u, int d) {
        get(u).duration = d;
    }

    public int getDuration(Vertex u){
        return get(u).duration;
    }

    /**
     * called after calling the constructor
     * @return
     */
    public boolean pert() {
        Vertex s=g.getVertex(1);
        Vertex t=g.getVertex(g.size());
        int m=g.edgeSize();
        for(int i=2;i<g.size();i++){
            g.addEdge(s, g.getVertex(i), 1, ++m);
            g.addEdge(g.getVertex(i), t, 1, ++m);
        }
        // g.printGraph(false);
        DFS d = new DFS(g);
        this.finishList = d.topologicalOrder1();
        if(this.finishList==null){
            return true;
        }
        Iterator<Vertex> descItr = ((LinkedList<Vertex>) this.finishList).descendingIterator();
        for(Vertex u : g){
            get(u).ec = 0;
        }
        for(Vertex u : finishList){
            for(Edge e : g.incident(u)){
                Vertex v = e.otherEnd(u);
                if(get(u).ec + get(v).duration > get(v).ec){
                    get(v).ec = get(u).ec + get(v).duration;
                }
            }
        }
        int maxTime=get(t).ec;
        for(Vertex u : g){
            get(u).lc = maxTime;
        }
        while(descItr.hasNext()){
            Vertex u = descItr.next();
            for(Edge e : g.incident(u)){
                Vertex v = e.otherEnd(u);
                if(get(v).lc - get(v).duration < get(u).lc){
                    get(u).lc = get(v).lc - get(v).duration;
                }
            }
            get(u).slack=get(u).lc - get(u).ec;
        }
        return false;
    }

    /**
     * returns earliest completion time of u
     * @param u
     * @return
     */
    public int ec(Vertex u) {
        return get(u).ec;
    }

    /**
     * returns latest completion time of u
     * @param u
     * @return
     */
    public int lc(Vertex u) {
        return get(u).lc;
    }

    /**
     * returns slack of u
     * @param u
     * @return
     */
    public int slack(Vertex u) {
        return get(u).slack;
    }

    /**
     * returns Length of Critical Path
     * @return
     */
    public int criticalPath() {
        Vertex t = g.getVertex(g.size());
        return get(t).ec;
    }

    /**
     * returns true if u on critical path
     * @param u
     * @return
     */
    public boolean critical(Vertex u) {
        return get(u).slack==0;
    }

    /**
     * returns Number of Critical Nodes in Graph
     * @return
     */
    public int numCritical() {
        int count=0;
        for(Vertex u : g){
            if(get(u).slack==0){
                count++;
            }
        }
        return count;
    }

    // setDuration(u, duration[u.getIndex()]);
    public static PERT pert(Graph g, int[] duration) {
        PERT p = new PERT(g);
        for(Vertex u : g){
            p.setDuration(u, duration[u.getIndex()]);
        }
        boolean NDAG = p.pert();
        if(NDAG){
            return null;
        }
        return p;
    }

    public static void main(String[] args) throws Exception {
        String graph = "11 12   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1 0";
        // String graph="10 12\t1 3 0\t1 8 0\t6 8 0\t6 10 0\t3 2 0\t8 2 0\t8 5 0 \t2 4 0\t5 4 0\t5 10 0\t4 7 0\t10 9 0 0 3 2 3 2 1 3 2 1 0";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);
        Graph g = Graph.readDirectedGraph(in);

        g.printGraph(false);

        PERT p = new PERT(g);
        for(Vertex u: g) {
            p.setDuration(u, in.nextInt());
        }
        // Run PERT algorithm.  Returns null if g is not a DAG
        if(p.pert()) {
            System.out.println("Invalid graph: not a DAG");
        } else {
            System.out.println("Number of critical vertices: " + p.numCritical());
            System.out.println("u\tEC\tLC\tSlack\tCritical");
            for(Vertex u: g) {
                System.out.println(u + "\t" + p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
            }
        }
    }
}