package hujf.toolkit.resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @author zhaoyan.hjf
 * @since 2018-09-14
 */
public final class URLS {

    private URLS() {
    }

    public static URL findResourceByFilePath(String path) {
        if (path == null) {
            return null;
        }

        File file = new File(path);
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid file path of [" + path + "] ", e);
        }
    }

    /**
     * find resources under class root path
     */
    public static URL[] findResourcesByClassPath(String... paths) {
        Set<URL> urls = new HashSet<URL>();
        for (String path : paths) {
            URL url = findResourceByClassPath(path);
            if (url != null) {
                urls.add(url);
            }
        }
        return urls.toArray(new URL[urls.size()]);

    }

    /**
     * find resource under class root path
     */
    public static URL findResourceByClassPath(String path) {
        if (path == null) {
            return null;
        }

        // 首先试着从当前线程的ClassLoader中查找。
        URL url = findResourceWithContextClassLoader(path);
        if (url != null) {
            return url;
        }

        // 如果没找到，试着从装入自己的ClassLoader中查找。
        url = findResourceWithSelfClassLoader(path);
        if (url != null) {
            return url;
        }

        // 最后的尝试: 在系统ClassLoader中查找(JDK1.2以上)，
        // 或者在JDK的内部ClassLoader中查找(JDK1.2以下)。
        return ClassLoader.getSystemResource(path);
    }

    private static URL findResourceWithContextClassLoader(String name) {
        return Thread.currentThread().getContextClassLoader().getResource(name);
    }

    private static URL findResourceWithSelfClassLoader(String name) {
        return URLS.class.getClassLoader().getResource(name);
    }
}
