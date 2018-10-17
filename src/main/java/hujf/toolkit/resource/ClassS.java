package hujf.toolkit.resource;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
public final class ClassS {

    private static final String CLASS_EXTENSION_NAME = ".class";

    private ClassS() {
    }

    public static Set<Class<?>> findClasses(String classPath, boolean isRecursive) {
        if (classPath == null) {
            return new TreeSet<Class<?>>();
        }

        Set<Class<?>> classes = new TreeSet<Class<?>>(ClassNameComparator.instance);
        try {
            String packageName = formatPath(classPath);
            List<URL> urls = URLS.findResourceByClassPath(packageName);
            for (URL url : urls) {
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    doScanPackageClassesByFile(classes, packageName, filePath, isRecursive);
                } else if ("jar".equals(protocol)) {
                    doScanPackageClassesByJar(packageName, url, isRecursive, classes);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classes;
    }

    private static String formatPath(final String classPath) {
        String formatPath = classPath;
        if (formatPath.endsWith(".")) {
            formatPath = formatPath.substring(0, formatPath.lastIndexOf('.'));
        }
        formatPath = formatPath.replace('.', '/');
        return formatPath;
    }


    /**
     * 以文件的方式扫描包下的所有Class文件.
     *
     * @param packageName the package name for scanning.
     * @param packagePath the package path for scanning.
     * @param recursive   whether to search recursive.
     * @param classes     set of the found classes.
     */
    private static void doScanPackageClassesByFile(Set<Class<?>> classes, String packageName, String packagePath, final boolean recursive) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles(ClassFileFilter.instance);
        if (null == files || files.length == 0) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                doScanPackageClassesByFile(classes, packageName + "." + file.getName(), file.getAbsolutePath(), recursive);
            } else {
                String className = file.getName().substring(0, file.getName().length() - CLASS_EXTENSION_NAME.length());
                try {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    log.error("LoadClass exception: ===>" + className, e);
                } catch (NoClassDefFoundError error) {
                    log.error("LoadClass error: ===>" + className, error);
                }
            }
        }
    }

    /**
     * 以jar的方式扫描包下的所有Class文件<br>.
     *
     * @param basePackage eg：michael.utils.
     * @param url         the url.
     * @param recursive   whether to search recursive.
     * @param classes     set of the found classes.
     */
    private static void doScanPackageClassesByJar(String basePackage, URL url, final boolean recursive, Set<Class<?>> classes) {
        String package2Path = basePackage.replace('.', '/');
        JarFile jar;
        try {
            jar = ((JarURLConnection) url.openConnection()).getJarFile();
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!name.startsWith(package2Path) || entry.isDirectory()) {
                    continue;
                }
                // 判断是否递归搜索子包
                if (!recursive && name.lastIndexOf('/') != package2Path.length()) {
                    continue;
                }

                String classSimpleName = name.substring(name.lastIndexOf('/') + 1);
                // 判定是否符合过滤条件
                if (isClassName(classSimpleName)) {
                    String className = name.replace('/', '.');
                    className = className.substring(0, className.length() - 6);
                    try {
                        classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
                    } catch (ClassNotFoundException e) {
                        log.error("LoadClass Exception:URL is ===>" + url.getPath() + " , Class ===> " + className, e);
                    } catch (NoClassDefFoundError error) {
                        log.error("LoadClass error:URL is ===>" + url.getPath() + " , Class ===> " + className, error);
                    }
                }
            }
        } catch (IOException e) {
            log.error("IOException error:URL is ===>" + url.getPath(), e);
        } catch (Throwable e) {
            log.error("ScanPackageClassesByJar error:URL is ===>" + url.getPath(), e);
        }
    }


    private static class ClassNameComparator implements Comparator<Class<?>> {

        static final ClassNameComparator instance = new ClassNameComparator();

        @Override
        public int compare(Class<?> o1, Class<?> o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }

    private static class ClassFileFilter implements FileFilter {

        static final ClassFileFilter instance = new ClassFileFilter();

        @Override
        public boolean accept(File pathname) {
            return isClassName(pathname.getName());
        }
    }

    private static boolean isClassName(String className) {
        if (!className.endsWith(CLASS_EXTENSION_NAME)) {
            return false;
        }
        return true;
    }

}
