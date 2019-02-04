package kyk170030;

import kyk170030.Graph.Vertex;
import kyk170030.Graph.Edge;
import kyk170030.Graph.GraphAlgorithm;
import kyk170030.Graph.Factory;
import kyk170030.Graph.Timer;

import kyk170030.BinaryHeap.Index;
import kyk170030.BinaryHeap.IndexedHeap;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedList;
import java.io.FileNotFoundException;
import java.io.File;

public class MST extends GraphAlgorithm<MST.MSTVertex> {
    String algorithm;
    public long wmst;
    List<Edge> mst;

    MST(Graph g) {
        super(g, new MSTVertex((Vertex) null));
    }

    public static class MSTVertex implements Index, Comparable<MSTVertex>, Factory {
        boolean seen;
        Vertex parent;
        int index;
        int distance;
        Vertex vertex;
        MSTVertex(Vertex u) {
            this.seen=false;
            this.parent=null;
            this.distance=Integer.MAX_VALUE;
            this.vertex=u;
        }

        MSTVertex(MSTVertex u) {  // for prim2
            this.parent = u.parent;
            this.seen = u.seen;
            this.distance=u.distance;
            this.vertex = u.getVertex();
        }

        public MSTVertex make(Vertex u) { return new MSTVertex(u); }

        public void putIndex(int index) {
            this.index=index;
        }

        public int getIndex() { return this.index; }

        public int compareTo(MSTVertex other) {
            if(this.distance > other.distance){
                return 1;
            }
            else if(this.distance==other.distance){
                return 0;
            }
            return -1;
        }


        public Vertex getVertex(){
            return this.vertex;
        }

    }


    public long kruskal() {
        algorithm = "Kruskal";
        Edge[] edgeArray = g.getEdgeArray();
        mst = new LinkedList<>();
        wmst = 0;
        return wmst;
    }

    public long prim3(Vertex s) {
        algorithm = "indexed heaps";
        mst = new LinkedList<>();
        for(Vertex u: g){
            get(u).seen = false;
            get(u).parent = null;
            get(u).distance = Integer.MAX_VALUE;
        }
        get(s).distance = 0;
        wmst = 0;
        IndexedHeap<MSTVertex> q = new IndexedHeap<>(g.size());
        for(Vertex u:g){
            q.add(get(u));
        }
        while(!q.isEmpty()){
            MSTVertex u = q.remove();
            u.seen = true;
            wmst+=u.distance;
            for(Edge e: g.incident(u.getVertex())){
                Vertex v = e.otherEnd(u.getVertex());
                if(!get(v).seen && e.weight < get(v).distance){
                    get(v).distance = e.weight;
                    get(v).parent = u.getVertex();
                    q.decreaseKey(get(v));
//                   q will percolate down here
                }
            }
        }
        return wmst;
    }

    public long prim2(Vertex s) {
        algorithm = "PriorityQueue<Vertex>";
        mst = new LinkedList<>();
        for(Vertex u: g){
            get(u).seen = false;
            get(u).parent = null;
            get(u).distance = Integer.MAX_VALUE;
        }
        get(s).distance = 0;
        wmst = 0;
        PriorityQueue<MSTVertex> q = new PriorityQueue<>();
        q.add(get(s));
        while(!q.isEmpty()){
            MSTVertex u=q.remove();
            //MSTVertex u=(temp.parent==null)?temp:temp.parent;
            Vertex umain=u.vertex;
            if(!get(umain).seen){
                get(umain).seen=true;
                wmst = wmst + u.distance;
                for(Edge e : g.incident(umain)){
                    Vertex v = e.otherEnd(umain);
                    if(!get(v).seen && e.getWeight() < get(v).distance){
                        MSTVertex v1 = new MSTVertex(v);
                        v1.distance = e.getWeight();
                        v1.parent = umain;
                        q.add(v1);
                    }
                }
            }
        }
        return wmst;
    }

    public long prim1(Vertex s) {
        algorithm = "PriorityQueue<Edge>";
        mst = new LinkedList<>();
        for(Vertex u : g){
            get(u).seen=false;
            get(u).parent=null;
        }
        get(s).seen=true;
        wmst = 0;
        PriorityQueue<Edge> q = new PriorityQueue<>();
        for(Edge e : g.incident(s)){
            q.add(e);
        }
        while(!q.isEmpty()){
            Edge e=q.remove();
            Vertex temp=e.fromVertex();
            Vertex u=(get(temp).seen==true)?temp:e.otherEnd(temp);
            Vertex v=e.otherEnd(u);
            if(get(v).seen==true){
                continue;
            }
            get(v).seen=true;
            get(v).parent=u;
            wmst+=e.weight;
            for(Edge e2 : g.incident(v)){
                if(!get(e2.otherEnd(v)).seen){
                    q.add(e2);
                }
            }
        }
        return wmst;
    }

    public static MST mst(Graph g, Vertex s, int choice) {
        MST m = new MST(g);
        switch(choice) {
            case 0:
                m.kruskal();
                break;
            case 1:
                m.prim1(s);
                break;
            case 2:
                m.prim2(s);
                break;
            default:
                m.prim3(s);
                break;
        }
        return m;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;
        int choice = 3;  // Kruskal
        if (args.length == 0 || args[0].equals("-")) {
            in = new Scanner(System.in);
        } else {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        }

        if (args.length > 1) { choice = Integer.parseInt(args[1]); }

        Graph g = Graph.readGraph(in);
        Vertex s = g.getVertex(1);

        Timer timer = new Timer();
        MST m = mst(g, s, choice);
        System.out.println(m.algorithm + "\n" + m.wmst);
        System.out.println(timer.end());
    }
}