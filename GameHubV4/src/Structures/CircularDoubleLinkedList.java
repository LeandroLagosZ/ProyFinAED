package Structures;
//Creada por Leandro Lagos
import Exceptions.ExceptionIsEmpty;

public class CircularDoubleLinkedList<T> {
    
    private DoubleNode<T> head;
    private int size;
    private DoubleNode<T> cursor; 

    public CircularDoubleLinkedList() {
        this.head = null;
        this.cursor = null;
        this.size = 0;
    }

    public void insert(T data) {
        DoubleNode<T> newNode = new DoubleNode<>(data);
        if (head == null) {
            head = newNode;
            head.setNext(head);
            head.setPrev(head);
            cursor = head;
        } else {
            DoubleNode<T> tail = head.getPrev();
            tail.setNext(newNode);
            newNode.setPrev(tail);
            newNode.setNext(head);
            head.setPrev(newNode);
        }
        size++;
    }

    public T getNextRecommended() throws ExceptionIsEmpty {
        if (head == null) throw new ExceptionIsEmpty("No hay recomendaciones");
        cursor = cursor.getNext();
        return cursor.getData();
    }

    public T getPrevRecommended() throws ExceptionIsEmpty {
        if (head == null) throw new ExceptionIsEmpty("No hay recomendaciones");
        cursor = cursor.getPrev();
        return cursor.getData();
    }
    
    public T getCurrentRecommended() throws ExceptionIsEmpty {
        if (cursor == null) throw new ExceptionIsEmpty("No hay recomendaciones");
        return cursor.getData();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
    
    public void clear() {
        head = null;
        cursor = null;
        size = 0;
    }
}