package Structures;
import Exceptions.*;
//Clase por Leandro Lagos
public class DoubleLinkedList<E> implements DoubleList<E> {
    private DoubleNode<E> head;
    private DoubleNode<E> tail;
    private int size;

    public DoubleLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void insertFirst(E x) {
        DoubleNode<E> newNode = new DoubleNode<>(x);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        }
        size++;
    }

    @Override
    public void insertLast(E x) {
        DoubleNode<E> newNode = new DoubleNode<>(x);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        }
        size++;
    }

    @Override
    public E removeFirst() throws ExceptionIsEmpty {
        if (isEmpty()) throw new ExceptionIsEmpty("La lista está vacía");
        
        E data = head.getData();
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            head = head.getNext();
            head.setPrev(null);
        }
        size--;
        return data;
    }

    @Override
    public E removeLast() throws ExceptionIsEmpty {
        if (isEmpty()) throw new ExceptionIsEmpty("La lista está vacía");
        
        E data = tail.getData();
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            tail = tail.getPrev();
            tail.setNext(null);
        }
        size--;
        return data;
    }

    @Override
    public void remove(E x) throws ExceptionIsEmpty, ItemNotFoundException {
        if (isEmpty()) throw new ExceptionIsEmpty("La lista está vacía");

        DoubleNode<E> current = head;
        while (current != null && !current.getData().equals(x)) {
            current = current.getNext();
        }

        if (current == null) throw new ItemNotFoundException("Elemento no encontrado");

        if (current == head) {
            removeFirst();
        } else if (current == tail) {
            removeLast();
        } else {
            current.getPrev().setNext(current.getNext());
            current.getNext().setPrev(current.getPrev());
            size--;
        }
    }

    @Override
    public E get(int index) throws ExceptionIsEmpty, IndexOutOfBoundsException {
        if (isEmpty()) throw new ExceptionIsEmpty("La lista está vacía");
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Índice fuera de rango");

        DoubleNode<E> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.getPrev();
            }
        }
        return current.getData();
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        DoubleNode<E> current = head;
        int i = 0;
        while (current != null) {
            array[i++] = current.getData();
            current = current.getNext();
        }
        return array;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        DoubleNode<E> current = head;
        while (current != null) {
            sb.append(current.getData().toString());
            if (current.getNext() != null) sb.append(", ");
            current = current.getNext();
        }
        sb.append("]");
        return sb.toString();
    }
}