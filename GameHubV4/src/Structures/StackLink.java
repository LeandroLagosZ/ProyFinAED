package Structures;

import Exceptions.*;

//Clase por Leandro Lagos
public class StackLink<E> implements Stack<E> {
    private Node<E> top;
    private int size;

    public StackLink() {
        this.top = null;
        this.size = 0;
    }

    @Override
    public void push(E x) {
        Node<E> nuevo = new Node<>(x);
        nuevo.setNext(top);
        top = nuevo;
        size++;
    }

    @Override
    public E pop() throws ExceptionIsEmpty {
        if (isEmpty()) {
            throw new ExceptionIsEmpty("La pila está vacía");
        }

        E dato = top.getData();
        top = top.getNext();
        size--;
        return dato;
    }

    @Override
    public E top() throws ExceptionIsEmpty {
        if (isEmpty()) {
            throw new ExceptionIsEmpty("La pila está vacía");
        }
        return top.getData();
    }

    @Override
    public boolean isEmpty() {
        return top == null;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public void clear() {
        this.top = null;
        this.size = 0;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "Pila vacía";
        }

        String resultado = "";
        Node<E> aux = top;
        while (aux != null) {
            resultado += aux.getData() + " ";
            aux = aux.getNext();
        }

        return resultado;
    }
    
    @Override
    public Object[] toArray() {
        Object[] array = new Object[this.size];
        Node<E> aux = top;
        int i = 0;
        while (aux != null) {
            array[i] = aux.getData();
            aux = aux.getNext();
            i++;
        }
        
        return array;
    }
}
