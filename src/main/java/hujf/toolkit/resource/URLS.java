package hujf.toolkit.resource;

import java.io.File;
import java.io.IOException;
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
     * find resource under class root path
     */
    public static List<URL> findResourceByClassPath(String path) throws IOException {
        if (path == null) {
            return new ArrayList<URL>();
        }

        // 首先试着从当前线程的ClassLoader中查找。
        List<URL> urls = findResourceWithContextClassLoader(path);
        if (!urls.isEmpty()) {
            return urls;
        }

        // 如果没找到，试着从装入自己的ClassLoader中查找。
        urls = findResourceWithSelfClassLoader(path);
        if (!urls.isEmpty()) {
            return urls;
        }

        // 最后的尝试: 在系统ClassLoader中查找(JDK1.2以上)，
        // 或者在JDK的内部ClassLoader中查找(JDK1.2以下)。
        return Collections.list(ClassLoader.getSystemResources(path));
    }

    private static List<URL> findResourceWithContextClassLoader(String name) throws IOException {
        return Collections.list(
                Thread.currentThread().getContextClassLoader().getResources(name)
        );
    }

    private static List<URL> findResourceWithSelfClassLoader(String name) throws IOException {
        return Collections.list(
                URLS.class.getClassLoader().getResources(name)
        );
    }
}
