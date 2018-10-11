package hujf.toolkit.test;

import hujf.toolkit.resource.FileS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.List;

public class TestTemp {

    public static void main(String[] args) throws Exception{
        List<File> files = FileS.findFiles("/Users/jiafeng.hjf/.m2/repository/", true,
                new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
//                        return true;
                        return pathname != null && pathname.getName().endsWith(".pom");
                    }
                });

        for (File file : files) {
            FileReader sr = new FileReader(file);
            BufferedReader br = new BufferedReader(sr);
            String line;
            boolean isContain = false;
            while ((line = br.readLine()) != null) {
                if (line.contains("package-platform")) {
                    isContain = true;
                }
                if (isContain && line.contains("1.2.7")) {
                    System.out.println(file.getAbsolutePath());
                    break;
                }
            }
        }

    }

}
