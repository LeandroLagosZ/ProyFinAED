package Structures;
import Exceptions.ExceptionIsEmpty;

public interface Heap<T extends Comparable<T>> {
    void insert(T item);
    T extractMax() throws ExceptionIsEmpty;
    T getMax() throws ExceptionIsEmpty;
    boolean isEmpty();
    int size();
}