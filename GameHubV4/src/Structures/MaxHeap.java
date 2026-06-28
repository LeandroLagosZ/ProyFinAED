package Structures;
import Exceptions.ExceptionIsEmpty;

public class MaxHeap<T extends Comparable<T>> implements Heap<T> {
    private Object[] array;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public MaxHeap() {
        this.array = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @Override
    public void insert(T item) {
        if (size == array.length) {
            resize();
        }
        array[size] = item;
        size++;
        heapifyUp(size - 1);
    }

    @Override
    public T extractMax() throws ExceptionIsEmpty {
        if (isEmpty()) throw new ExceptionIsEmpty("El Heap está vacío");
        T max = (T) array[0];
        array[0] = array[size - 1];
        array[size - 1] = null;
        size--;
        heapifyDown(0);
        return max;
    }

    @Override
    public T getMax() throws ExceptionIsEmpty {
        if (isEmpty()) throw new ExceptionIsEmpty("El Heap está vacío");
        return (T) array[0];
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public int size() { return size; }

    private void heapifyUp(int index) {
        T temp = (T) array[index];
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            T parent = (T) array[parentIndex];
            if (temp.compareTo(parent) > 0) {
                array[index] = parent;
                index = parentIndex;
            } else {
                break;
            }
        }
        array[index] = temp;
    }

    private void heapifyDown(int index) {
        T temp = (T) array[index];
        int half = size / 2;
        while (index < half) {
            int leftChild = 2 * index + 1;
            int rightChild = leftChild + 1;
            int largerChild = leftChild;

            if (rightChild < size && ((T) array[rightChild]).compareTo((T) array[leftChild]) > 0) {
                largerChild = rightChild;
            }

            if (temp.compareTo((T) array[largerChild]) >= 0) {
                break;
            }
            array[index] = array[largerChild];
            index = largerChild;
        }
        array[index] = temp;
    }

    private void resize() {
        Object[] newArray = new Object[array.length * 2];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }
}