package com.github;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class FileUtils {
    public static String read(String filename) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/test/java/com/github" + filename)));
        StringBuilder data = new StringBuilder();
        String cur = reader.readLine();
        while (cur != null) {
            data.append(cur).append("\n");
            cur = reader.readLine();
        }
        return data.toString();
    }

    public static BufferedReader readBuffer(String filename) throws Exception {
        return new BufferedReader(new InputStreamReader(new FileInputStream("src/test/java/com/github" + filename)));
    }

    public static void writeFile(String filename, String data) throws Exception {
        FileWriter w = new FileWriter("src/test/java/com/github" + filename);
        w.write(data);
        w.close();
    }

    public static String nextJson(BufferedReader reader) throws Exception {
        StringBuilder res = new StringBuilder();
        String cur = reader.readLine();
        while (!cur.equals("#")) {
            res.append(cur);
            cur = reader.readLine();
        }
        return res.toString();
    }
}
