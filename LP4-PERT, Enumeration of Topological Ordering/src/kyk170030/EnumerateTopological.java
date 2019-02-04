/* Starter code for enumerating topological orders of a DAG
 
 */

package kyk170030;
import rbk.Graph;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Vertex;
import rbk.Graph.Factory;

import java.io.File;
import java.util.Scanner;

public class EnumerateTopological extends GraphAlgorithm<EnumerateTopological.EnumVertex> {
    boolean print;  // Set to true to print array in visit
    long count;      // Number of permutations or combinations visited
    Selector sel;

    public EnumerateTopological(Graph g) {
        super(g, new EnumVertex());
        print = true;
        count = 0;
        sel = new Selector();

    }

    static class EnumVertex implements Factory {
        int inDegree;
        boolean visited;

        EnumVertex() {

        }

        EnumVertex(Vertex u) {

            this.inDegree = u.inDegree();
            this.visited =false;
        }
        public EnumVertex make(Vertex u) { return new EnumVertex(u);	}
    }

    class Selector extends Enumerate.Approver<Vertex> {
        @Override
        public boolean select(Vertex u) {
            // if the vertex doesnt have any edge incoming to it from the nodes before it till the place it is moving to

            if(!get(u).visited && get(u).inDegree == 0) {

                get(u).visited = true;

                for (Graph.Edge e: g.incident(u)) {
                    get(e.otherEnd(u)).inDegree--;
                }

                return true;
            }
            return false;
        }

        @Override
        public void unselect(Vertex u) {

            get(u).visited = false;
            for (Graph.Edge e: g.incident(u)) {
                get(e.otherEnd(u)).inDegree++;
            }
        }

        @Override
        public void visit(Vertex[] arr, int k) {
            count++;
            if(print) {
                for(Vertex u: arr) {
                    System.out.print(u + " ");
                }
                System.out.println();
            }
        }
    }


    // To do: LP4; return the number of topological orders of g
    public long enumerateTopological(boolean flag) {
        print = flag;
        int k = this.g.getVertexArray().length;
        Enumerate enumerate = new Enumerate(this.g.getVertexArray(),k,sel);
        enumerate.permute(k);
       return enumerate.count;
    }

    //-------------------static methods----------------------

    public static long countTopologicalOrders(Graph g) {
        EnumerateTopological et = new EnumerateTopological(g);
        return et.enumerateTopological(false);
    }

    public static long enumerateTopologicalOrders(Graph g) {
        EnumerateTopological et = new EnumerateTopological(g);
        return et.enumerateTopological(true);
    }

    public static void main(String[] args) throws Exception{
        int VERBOSE = 0;
//        if(args.length > 0) { VERBOSE = Integer.parseInt(args[0]); }
//        Graph g = Graph.readDirectedGraph(new java.util.Scanner(System.in));


        String string = "6 6   6 1 2   6 3 3   5 1 5   5 2 4   4 2 1   3 4 7 0";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

        // Read graph from input
        Graph g = Graph.readDirectedGraph(in);
        Graph.Timer t = new Graph.Timer();
        long result;
        if(VERBOSE > 0) {
            result = enumerateTopologicalOrders(g);
        } else {
            result = countTopologicalOrders(g);
        }
        System.out.println("\n" + result + "\n" + t.end());
    }

}
