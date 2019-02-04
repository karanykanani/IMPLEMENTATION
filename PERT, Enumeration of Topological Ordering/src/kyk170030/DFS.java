package kyk170030;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import rbk.Graph;
import rbk.Graph.Edge;
import rbk.Graph.Factory;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Vertex;


public class DFS extends GraphAlgorithm<DFS.DFSVertex> {
    /**
     * topological numbering for vertices
     */
    private int topNum;
    /**
     * vertex descending ordered by their finish times
     */
    private List<Vertex> finishList;


    /**
     * boolean to indicate whether the graph is cyclic
     */
    private boolean isCyclic;

    /**
     * DFS Vertex class that contains additional attributes for graph vertex
     *
     */
    public static class DFSVertex implements Factory {
        int cno; //connected component number
        int top; //topological order
        boolean seen; //boolean to store whether is vertex already seen
        Vertex parent; //parent of current DFSVertex
        public DFSVertex(Vertex u) {
            this.top = 0;
            this.seen = false;
            this.parent = null;
        }
        public DFSVertex make(Vertex u) {
            return new DFSVertex(u);
        }
    }

    public DFS(Graph g) {
        super(g, new DFSVertex(null));
    }

    /**
     * member function to perform dfs
     */
    void depthFirstSearch() {
        topNum = g.size();
        finishList = new LinkedList<>();
        for(Vertex u : g) {
            get(u).seen = false;
            get(u).parent = null;
            get(u).top = 0;
        }
        Iterator<Vertex> it = g.iterator();
        while(it.hasNext()) {
            Vertex u = it.next();
            if(!get(u).seen) {
                dfs_visit(u);
            }
        }
    }


    /**
     * Perform DFS on provided graph and return DFS object
     * @param g - graph that you need to compute DFS on
     * @return DFS object
     */
    public static DFS depthFirstSearch(Graph g) {
        DFS d = new DFS(g);
        d.depthFirstSearch();
        return d;
    }

    /**
     * function to perform dfs visit of graph
     * @param u - vertex
     */
    private void dfs_visit(Vertex u){
        get(u).seen = true;
        for(Edge e : g.outEdges(u)) {
            Vertex v = e.toVertex();
            if(!get(v).seen) {
                get(v).parent = u;
                dfs_visit(v);
            }
            else {
                if(get(v).top==0) {
                    isCyclic = true;
                }
            }
        }
        get(u).top = topNum--;
        ((LinkedList<Vertex>) finishList).addFirst(u);
    }

    /**
     * Member function to find topological order
     * @return finishList or null if graph is not a DAG
     */
    public List<Vertex> topologicalOrder1() {
        if(!g.isDirected()) {
            return null;
        }
        isCyclic = false;
        depthFirstSearch();
        if(isCyclic) {
            return null;
        }
        else {
            return finishList;
        }

    }

    /**
     * Finds topological order of given graph. Returns null if g not a DAG
     * @param g - Graph
     * @return topological order of DAG or null if g is not a DAG
     */
    public static List<Vertex> topologicalOrder1(Graph g) throws Exception {
        DFS d = new DFS(g);
        List<Vertex> topoList = d.topologicalOrder1();
        if(topoList == null) {
            throw new Exception("Graph is not a DAG");
        }
        else {
            return topoList;
        }
    }

}