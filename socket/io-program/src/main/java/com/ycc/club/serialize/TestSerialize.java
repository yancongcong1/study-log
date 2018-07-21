package com.ycc.club.serialize;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

/**
 * Created on 2018\7\21 0021 by yancongcong
 */
public class TestSerialize implements Serializable {

    private static Random random = new Random(47);

    class Data implements Serializable {

        private int n;

        public Data(int n) {
            this.n = n;
        }

        @Override
        public String toString() {
            return Integer.toString(n);
        }
    }

    class Worm implements Serializable {

        private Data[] data = {
                new Data(random.nextInt(10)),
                new Data(random.nextInt(10)),
                new Data(random.nextInt(10))
        };

        private Worm next;
        private char c;

        public Worm() {
            System.out.println("default construction");
        }

        public Worm(int i, char x) {
            System.out.println("worm construction " + i);
            c = x;
            if (--i > 0) {
                next = new Worm(i, (char) (x + 1));
            }
        }

        @Override
        public String toString() {
            return "Worm{" +
                    "data=" + Arrays.toString(data) +
                    ", next=" + next +
                    ", c=" + c +
                    '}';
        }
    }

    public void test() {
        Worm w1 = new Worm(6, 'a');
        System.out.println("w1 " + w1);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("worm.out"));
            objectOutputStream.writeObject(w1);
            objectOutputStream.close();

            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("worm.out"));
            Worm w2 = (Worm) objectInputStream.readObject();
            System.out.println("w2 " + w2);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream1.writeObject(w1);

            ObjectInputStream objectInputStream1 = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            Worm w3 = (Worm) objectInputStream1.readObject();
            System.out.println("w3 " + w3);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TestSerialize().test();
    }

}
