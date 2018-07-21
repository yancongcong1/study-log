package com.ycc.club.file;

import java.io.*;
import java.util.ArrayList;

/**
 * Created on 2018\7\19 0019 by yancongcong
 */
public class TextFile extends ArrayList<String> {

    public static String read(String filename) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filename).getAbsoluteFile()));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                stringBuilder.append(s);
                stringBuilder.append("\n");
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws IOException {
        System.out.println(TextFile.read("socket/io-program/src/main/java/com/ycc/club/file/DirList3.java"));
    }
}
