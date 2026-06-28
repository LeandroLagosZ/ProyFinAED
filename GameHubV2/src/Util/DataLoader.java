package Util;

import Structures.*;

public interface DataLoader<T extends Comparable <T>> {
    Queue<T> loadData(String filePath);
}