package Structures;
//Clase por Leandro Lagos
public class DoubleNode<E> {
    private E data;
    private DoubleNode<E> next;
    private DoubleNode<E> prev;

    public DoubleNode(E data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }

    public E getData() { return data; }
    public void setData(E data) { this.data = data; }
    public DoubleNode<E> getNext() { return next; }
    public void setNext(DoubleNode<E> next) { this.next = next; }
    public DoubleNode<E> getPrev() { return prev; }
    public void setPrev(DoubleNode<E> prev) { this.prev = prev; }
}