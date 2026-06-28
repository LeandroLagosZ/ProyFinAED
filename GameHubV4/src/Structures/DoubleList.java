package Structures;

import Exceptions.*;

//Clase por Leandro Lagos
public interface DoubleList<E> {
    void insertFirst(E x);
    void insertLast(E x);
    E removeFirst() throws ExceptionIsEmpty;
    E removeLast() throws ExceptionIsEmpty;
    
    void remove(E x) throws ExceptionIsEmpty, ItemNotFoundException;
    E get(int index) throws ExceptionIsEmpty, IndexOutOfBoundsException;
    
    boolean isEmpty();
    int size();
    void clear();
    Object[] toArray();
}