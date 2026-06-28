package Structures;

import Exceptions.*;

//Eduardo Motta
public class QueueCustom<T extends Comparable<T>> implements Queue<T> {
    private Node<T> front;
    private Node<T> back;
    private int size;

    public QueueCustom() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }
    
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node<T> actual = front;
        int i = 0;
        while (actual != null) {
            array[i++] = actual.getData();
            actual = actual.getNext();
        }
        return array;
    }

    @Override
    public void enqueue(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            front = newNode;
        } else {
            back.setNext(newNode);
        }
        back = newNode;
        size++;
    }

    @Override
    public T dequeue() throws ExceptionIsEmpty {
        if (isEmpty()) throw new ExceptionIsEmpty("Error ~Intentando extraer de una cola vacía");
        T data = front.getData();
        front = front.getNext();
        if (front == null) back = null;
        size--;
        return data;
    }

    @Override
    public T front() throws ExceptionIsEmpty {
        if (isEmpty()) throw new ExceptionIsEmpty("Error ~Cola vacía");
        return front.getData();
    }

    @Override
    public T back() throws ExceptionIsEmpty {
        if (isEmpty()) throw new ExceptionIsEmpty("Error ~Cola vacía");
        return back.getData();
    }

    @Override
    public boolean isEmpty() {
        return front == null;
    }
    
    @Override
    public int getSize() {
        return size;
    }
}