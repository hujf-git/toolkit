package hujf.toolkit.collection;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author zhaoyan.hjf
 * @since 2018-09-12
 */
public final class ListS {

    private ListS() {
    }

    /**
     * create new ArrayList with default capacity
     */
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    /**
     * create new ArrayList with elements
     */
    public static <E> ArrayList<E> newArrayList(E... elements) {
        int capacity = computeArrayListCapacity(elements.length);
        ArrayList<E> list = new ArrayList<E>(capacity);
        Collections.addAll(list, elements);
        return list;
    }

    /**
     * create new ArrayList with expected size
     */
    public static <E> ArrayList<E> newArrayListWithExpectedSize(
            int estimatedSize) {
        return new ArrayList<E>(computeArrayListCapacity(estimatedSize));
    }



    static int computeArrayListCapacity(int arraySize) {
        long value = 5L + arraySize + (arraySize / 10);
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) value;
    }


}
