package hujf.toolkit.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zhaoyan.hjf
 * @since 2018-09-12
 */
public final class MapS {

    public static final int MAX_POWER_OF_TWO = 1 << (Integer.SIZE - 2);

    private MapS() {
    }

    /**
     * create new HashMap with default capacity
     */
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    /**
     * create new HashMap with current map
     */
    public static <K, V> HashMap<K, V> newHashMap(
            Map<? extends K, ? extends V> map) {
        return new HashMap<K, V>(map);
    }

    /**
     * create new HashMap with expected size
     */
    public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(
            int expectedSize) {
        return new HashMap<K, V>(capacity(expectedSize));
    }

    /**
     * create new ConcurrentHashMap with default capacity
     */
    public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
        return new ConcurrentHashMap<K, V>();
    }


    static int capacity(int expectedSize) {
        if (expectedSize < 3) {
            return expectedSize + 1;
        }
        if (expectedSize < MAX_POWER_OF_TWO) {
            return expectedSize + expectedSize / 3;
        }
        return Integer.MAX_VALUE; // any large value
    }

}
