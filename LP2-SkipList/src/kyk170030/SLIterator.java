package kyk170030;

public interface SLIterator<T> {
    boolean hasNext();
    T next();
    void remove();
}