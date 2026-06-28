package Structures;
import Exceptions.*;
//Clase por Leandro Lagos
public interface Stack<E> {
    void push(E x);
    E pop() throws ExceptionIsEmpty;
    E top() throws ExceptionIsEmpty;
    boolean isEmpty();
    int size();
    void clear();
    Object[] toArray();
}
