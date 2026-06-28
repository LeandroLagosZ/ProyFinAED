package Structures;

public class AVLTree<T extends Comparable<T>> {

    private AVLNode<T> raiz;

    public void insertar(T dato) {
        raiz = insertar(raiz, dato);
    }

    private AVLNode<T> insertar(AVLNode<T> nodo, T dato) {

        if (nodo == null) {
            return new AVLNode<>(dato);
        }

        if (dato.compareTo(nodo.getDato()) < 0) {
            nodo.setIzquierdo(insertar(nodo.getIzquierdo(), dato));
        } else if (dato.compareTo(nodo.getDato()) > 0) {
            nodo.setDerecho(insertar(nodo.getDerecho(), dato));
        } else {
            return nodo;
        }

        actualizarAltura(nodo);

        int balance = obtenerBalance(nodo);

        if (balance > 1 && dato.compareTo(nodo.getIzquierdo().getDato()) < 0) {
            return rotacionDerecha(nodo);
        }

        if (balance < -1 && dato.compareTo(nodo.getDerecho().getDato()) > 0) {
            return rotacionIzquierda(nodo);
        }

        if (balance > 1 && dato.compareTo(nodo.getIzquierdo().getDato()) > 0) {
            nodo.setIzquierdo(rotacionIzquierda(nodo.getIzquierdo()));
            return rotacionDerecha(nodo);
        }

        if (balance < -1 && dato.compareTo(nodo.getDerecho().getDato()) < 0) {
            nodo.setDerecho(rotacionDerecha(nodo.getDerecho()));
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    public void eliminar(T dato) {
        raiz = eliminar(raiz, dato);
    }

    private AVLNode<T> eliminar(AVLNode<T> nodo, T dato) {

        if (nodo == null) {
            return null;
        }

        int comparacion = dato.compareTo(nodo.getDato());

        if (comparacion < 0) {
            nodo.setIzquierdo(eliminar(nodo.getIzquierdo(), dato));
        } else if (comparacion > 0) {
            nodo.setDerecho(eliminar(nodo.getDerecho(), dato));
        } else {
            if (nodo.getIzquierdo() == null) {
                return nodo.getDerecho();
            } else if (nodo.getDerecho() == null) {
                return nodo.getIzquierdo();
            }

            AVLNode<T> sucesor = obtenerMinimo(nodo.getDerecho());
            nodo.setDato(sucesor.getDato());
            nodo.setDerecho(eliminar(nodo.getDerecho(), sucesor.getDato()));
        }

        actualizarAltura(nodo);
        int balance = obtenerBalance(nodo);

        if (balance > 1 && obtenerBalance(nodo.getIzquierdo()) >= 0) {
            return rotacionDerecha(nodo);
        }

        if (balance > 1 && obtenerBalance(nodo.getIzquierdo()) < 0) {
            nodo.setIzquierdo(rotacionIzquierda(nodo.getIzquierdo()));
            return rotacionDerecha(nodo);
        }

        if (balance < -1 && obtenerBalance(nodo.getDerecho()) <= 0) {
            return rotacionIzquierda(nodo);
        }

        if (balance < -1 && obtenerBalance(nodo.getDerecho()) > 0) {
            nodo.setDerecho(rotacionDerecha(nodo.getDerecho()));
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    private AVLNode<T> obtenerMinimo(AVLNode<T> nodo) {
        AVLNode<T> actual = nodo;
        while (actual.getIzquierdo() != null) {
            actual = actual.getIzquierdo();
        }
        return actual;
    }

    public boolean buscar(T dato) {
        return buscar(raiz, dato);
    }

    private boolean buscar(AVLNode<T> nodo, T dato) {

        if (nodo == null) {
            return false;
        }

        if (dato.compareTo(nodo.getDato()) == 0) {
            return true;
        }

        if (dato.compareTo(nodo.getDato()) < 0) {
            return buscar(nodo.getIzquierdo(), dato);
        }

        return buscar(nodo.getDerecho(), dato);
    }

    public void mostrarInOrden() {
        mostrarInOrden(raiz);
        System.out.println();
    }

    private void mostrarInOrden(AVLNode<T> nodo) {

        if (nodo != null) {
            mostrarInOrden(nodo.getIzquierdo());
            System.out.print(nodo.getDato() + " ");
            mostrarInOrden(nodo.getDerecho());
        }
    }

    private int altura(AVLNode<T> nodo) {

        if (nodo == null) {
            return 0;
        }

        return nodo.getAltura();
    }

    private void actualizarAltura(AVLNode<T> nodo) {
        nodo.setAltura(1 + Math.max(altura(nodo.getIzquierdo()), altura(nodo.getDerecho())));
    }

    private int obtenerBalance(AVLNode<T> nodo) {

        if (nodo == null) {
            return 0;
        }

        return altura(nodo.getIzquierdo()) - altura(nodo.getDerecho());
    }

    private AVLNode<T> rotacionDerecha(AVLNode<T> y) {

        AVLNode<T> x = y.getIzquierdo();
        AVLNode<T> temporal = x.getDerecho();

        x.setDerecho(y);
        y.setIzquierdo(temporal);

        actualizarAltura(y);
        actualizarAltura(x);

        return x;
    }

    private AVLNode<T> rotacionIzquierda(AVLNode<T> x) {

        AVLNode<T> y = x.getDerecho();
        AVLNode<T> temporal = y.getIzquierdo();

        y.setIzquierdo(x);
        x.setDerecho(temporal);

        actualizarAltura(x);
        actualizarAltura(y);

        return y;
    }

    public AVLNode<T> getRaiz() {
        return raiz;
    }

    public void setRaiz(AVLNode<T> raiz) {
        this.raiz = raiz;
    }

    public T buscarObjeto(T dato) {
        return buscarObjeto(raiz, dato);
    }

    private T buscarObjeto(AVLNode<T> nodo, T dato) {
        if (nodo == null) return null;
        int comparacion = dato.compareTo(nodo.getDato());
        if (comparacion == 0) return nodo.getDato();
        return (comparacion < 0) ? buscarObjeto(nodo.getIzquierdo(), dato) : buscarObjeto(nodo.getDerecho(), dato);
    }

    public Object[] extraerTopDescendente(int limite) {
        Object[] resultados = new Object[limite];
        int[] contador = {0};
        extraerTopDescendente(raiz, resultados, contador, limite);
        return resultados;
    }

    private void extraerTopDescendente(AVLNode<T> nodo, Object[] resultados, int[] contador, int limite) {
        if (nodo == null || contador[0] >= limite) return;
        extraerTopDescendente(nodo.getDerecho(), resultados, contador, limite);
        if (contador[0] < limite) {
            resultados[contador[0]] = nodo.getDato();
            contador[0]++;
            extraerTopDescendente(nodo.getIzquierdo(), resultados, contador, limite);
        }
    }
}
