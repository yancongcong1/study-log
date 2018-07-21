package com.ycc.club.input;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created on 2018\7\19 0019 by yancongcong
 */
public class TestEOF {

    public static void main(String[] args) {
        new TestEOF().testFormatted();
        System.out.println("-------------");
        new TestEOF().testEOF();
    }

    void testFormatted() {
        new FormattedMemoryInput().read();
    }

    class FormattedMemoryInput {
        public void read() {
            DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream("laladema西亚".getBytes()));

            try {
                while (true) {
                    System.out.print((char) inputStream.readByte());
                }
            } catch (IOException e) {
                System.out.println();
                System.out.println("End of stream");
            }
        }
    }

    void testEOF() {
        new EOF().read();
    }

    class EOF {
        public void read() {
            DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream("laladema西亚".getBytes()));

            try {
                while (inputStream.available() != 0)
                    System.out.print((char) inputStream.readByte());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
