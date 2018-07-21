package com.ycc.club.input;

import java.io.StringReader;

/**
 * Created on 2018\7\19 0019 by yancongcong
 */
public class MemoryInput {

    public static void main(String[] args) throws Exception {
        StringReader stringReader = new StringReader("laladema西亚");
        int c;
        while ((c = stringReader.read()) != -1) {
            System.out.println((char) c);
        }
    }
}
