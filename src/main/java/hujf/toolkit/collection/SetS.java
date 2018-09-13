package hujf.toolkit.collection;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * @author zhaoyan.hjf
 * @since 2018-09-12
 */
public final class SetS {

    private SetS() {
    }

    /**
     * create new LinkedHashSet with default capacity
     */
    public static <E> LinkedHashSet<E> newLinkedHashSet() {
        return new LinkedHashSet<E>();
    }

    /**
     * create new HashSet with default capacity
     */
    public static <E> HashSet<E> newHashSet() {
        return new HashSet<E>();
    }

    /**
     * create new HashSet with elements
     */
    public static <E> HashSet<E> newHashSet(E... elements) {
        HashSet<E> set = newHashSetWithExpectedSize(elements.length);
        Collections.addAll(set, elements);
        return set;
    }

    /**
     * create new HashSet with expected size
     */
    public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
        return new HashSet<E>(MapS.capacity(expectedSize));
    }

}
