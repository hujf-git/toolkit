package hujf.toolkit.resource;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.List;

public final class FileS {

    private FileS() {
    }

    public static List<File> findFiles(String path, boolean isRecursive) {
        if (path == null) {
            return new LinkedList<File>();
        }

        List<File> filteredFiles = new LinkedList<File>();
        findFiles0(filteredFiles, new File(path), isRecursive, null);
        return filteredFiles;
    }

    public static List<File> findFiles(String path, boolean isRecursive, FileFilter fileFilter) {
        if (path == null) {
            return new LinkedList<File>();
        }

        List<File> filteredFiles = new LinkedList<File>();
        findFiles0(filteredFiles, new File(path), isRecursive, fileFilter);
        return filteredFiles;
    }

    private static void findFiles0(final List<File> filteredFiles, File file, boolean isRecursive, FileFilter fileFilter) {
        if (filteredFiles == null) {
            throw new IllegalArgumentException("filteredFiles can not be null");
        }

        if (file == null) {
            return;
        }

        if (!file.isDirectory()) {
            if (fileFilter.accept(file)) {
                filteredFiles.add(file);
            }
            return;
        }

        File[] subFiles = file.listFiles(fileFilter);
        if (subFiles != null && file.length() > 0) {
            for (File fileUnderDir : subFiles) {
                filteredFiles.add(fileUnderDir);
            }
        }

        if (isRecursive) {
            File[] subDirs = file.listFiles(DirFileFilter.instance);
            if (subDirs != null && subDirs.length > 0) {
                for (File subDir : subDirs) {
                    findFiles0(filteredFiles, subDir, true, fileFilter);
                }
            }
        }
    }



    private static class DirFileFilter implements FileFilter {

        static DirFileFilter instance = new DirFileFilter();

        @Override
        public boolean accept(File file) {
            return file.isDirectory();
        }
    }

}
