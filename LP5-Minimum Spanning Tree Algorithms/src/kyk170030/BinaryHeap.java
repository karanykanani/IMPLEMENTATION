// Starter code for LP5

// Change to your netid
package kyk170030;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class BinaryHeap<T extends Comparable<? super T>> {
    Comparable[] pq;
    int size;
    int totalSize;

    // Constructor for building an empty priority queue using natural ordering of T
    public BinaryHeap(int maxCapacity) {
        pq = new Comparable[maxCapacity];
        size = 0;
        totalSize=pq.length;
    }

    // add method: resize pq if needed
    public boolean add(T x) {
        if(size==totalSize){
            resize();
        }
        move(size, x);
        percolateUp(size);
        size++;
        return true;
    }



    public boolean offer(T x) {
        return add(x);
    }

    // throw exception if pq is empty
    public T remove() throws NoSuchElementException {
        T result = poll();
        if(result == null) {
            throw new NoSuchElementException("Priority queue is empty");
        } else {
            return result;
        }
    }

    // return null if pq is empty
    public T poll() {
        if (size > 0) {
            Comparable min = pq[0];
            move(0, pq[size-1]);
            size--;
            percolateDown(0);
            return (T)min;
        }
        else {
            return null;
        }
    }

    public T min() {
        return peek();
    }

    // return null if pq is empty
    public T peek() {
        if (size > 0) {
            return (T)pq[0];
        }
        else {
            return null;
        }
    }

    int parent(int i) {
        return (i-1)/2;
    }

    int leftChild(int i) {
        return 2*i + 1;
    }

    /** pq[index] may violate heap order with parent */
    void percolateUp(int i) {
        Comparable x = pq[i];
        int parent = parent(i);
        while(i>0 && x.compareTo(pq[parent]) < 0) {
            move(i, pq[parent]);
            i = parent;
            parent = parent(i);
        }
        move(i, x);
    }

    /** pq[index] may violate heap order with children */
    void percolateDown(int i) {
        Comparable x = pq[i];
        int c = leftChild(i);
        while (c <= size-1 ) {
            //Get the smaller child
            if (c<size-1 && pq[c].compareTo(pq[c+1]) > 0) {
                c++; // Right child
            }

            //Check the node and smaller child priorities

            if(x.compareTo(pq[c]) <= 0) { //Constraint OK
                break;
            }

            move(i, pq[c]);
            i = c;
            c = leftChild(i);
        }
        move(i, x);
    }

    void move(int dest, Comparable x) {
        pq[dest] = x;
    }

    int compare(Comparable a, Comparable b) {
        return ((T) a).compareTo((T) b);
    }

    /** Create a heap.  Precondition: none. */
    void buildHeap() {
        for(int i=parent(size-1); i>=0; i--) {
            percolateDown(i);
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    // Resize array to double the current size
    void resize() {
        int oldSize = pq.length;
        Comparable[] oldArr = pq;

        pq = new Comparable[oldSize*2];
        System.arraycopy(oldArr, 0, pq, 0, oldSize);
    }

    public interface Index {
        public void putIndex(int index);
        public int getIndex();
    }

    public static class IndexedHeap<T extends Index & Comparable<? super T>> extends BinaryHeap<T> {

        /**
         * Build a priority queue with a given array
         * @param capacity
         */
        IndexedHeap(int capacity) {
            super(capacity);
        }

        /**
         * Restores heap order property after the priority of x has decreased
         * @param x
         */
        void decreaseKey(T x) {
            //Decrease x key
            int i = x.getIndex();
            percolateUp(i);
        }

        /**
         * Moves x to index i
         * Updates the index of the item x
         */
        @Override
        void move(int i, Comparable x) {
            super.move(i, x);
            T item = (T) x;
            item.putIndex(i);
        }
    }

    public static void main(String[] args) {
        Integer[] arr = {0,9,7,5,3,1,8,6,4,2};
        BinaryHeap<Integer> h = new BinaryHeap(arr.length);

        System.out.print("Before:");
        for(Integer x: arr) {
            h.offer(x);
            System.out.print(" " + x);
        }
        System.out.println();

        for(int i=0; i<arr.length; i++) {
            arr[i] = h.poll();
        }

        System.out.print("After :");
        for(Integer x: arr) {
            System.out.print(" " + x);
        }
        System.out.println();
    }
}