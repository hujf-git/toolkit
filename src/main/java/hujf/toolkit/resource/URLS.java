package hujf.toolkit.resource;

import java.net.URL;
import java.util.*;

/**
 * @author zhaoyan.hjf
 * @since 2018-09-14
 */
public final class URLS {

    private URLS() {
    }


    /**
     * find resources of names under class root path
     */
    public static URL[] findResources(String... names) {
        Set<URL> urls = new HashSet<URL>();
        for (String name : names) {
            URL url = findResource(name);
            if (url != null) {
                urls.add(url);
            }
        }
        return urls.toArray(new URL[urls.size()]);

    }

    /**
     * find resource of name under class root path
     */
    public static URL findResource(String name) {
        if (name == null) {
            return null;
        }

        // 首先试着从当前线程的ClassLoader中查找。
        URL url = findResourceWithContextClassLoader(name);
        if (url != null) {
            return url;
        }

        // 如果没找到，试着从装入自己的ClassLoader中查找。
        url = findResourceWithSelfClassLoader(name);
        if (url != null) {
            return url;
        }

        // 最后的尝试: 在系统ClassLoader中查找(JDK1.2以上)，
        // 或者在JDK的内部ClassLoader中查找(JDK1.2以下)。
        return ClassLoader.getSystemResource(name);
    }

    private static URL findResourceWithContextClassLoader(String name) {
        return Thread.currentThread().getContextClassLoader().getResource(name);
    }

    private static URL findResourceWithSelfClassLoader(String name) {
        return URLS.class.getClassLoader().getResource(name);
    }
}
