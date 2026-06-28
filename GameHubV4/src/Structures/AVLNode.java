package Structures;

public class AVLNode<T extends Comparable<T>> {

    private T dato;
    private int altura;

    private AVLNode<T> izquierdo;
    private AVLNode<T> derecho;

    public AVLNode(T dato) {
        this.dato = dato;
        this.altura = 1;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public AVLNode<T> getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(AVLNode<T> izquierdo) {
        this.izquierdo = izquierdo;
    }

    public AVLNode<T> getDerecho() {
        return derecho;
    }

    public void setDerecho(AVLNode<T> derecho) {
        this.derecho = derecho;
    }
}