package com.ycc.club.serialize;

import java.io.*;

/**
 * Created on 2018\7\23 0023 by yancongcong
 */
public class TestSerialize2 implements Serializable {

    private String a;
    private transient String b;

    public TestSerialize2(String a, String b) {
        this.a = "no transient " + a;
        this.b = "transient " + b;
    }

    @Override
    public String toString() {
        return "TestSerialize2{" +
                "a='" + a + '\'' +
                ", b='" + b + '\'' +
                '}';
    }

    /**
     * 重写以强制序列化transient字段
     * @param out
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(a);
        out.writeObject(b);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        a = (String) in.readObject();
        b = (String) in.readObject();
    }

    public static void main(String[] args) throws Exception {
        TestSerialize2 testSerialize = new TestSerialize2("ycc", "123456");
        System.out.println(testSerialize);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(testSerialize);
        objectOutputStream.close();

        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        TestSerialize2 testSerialize1 = (TestSerialize2) objectInputStream.readObject();
        System.out.println(testSerialize1);
        objectInputStream.close();
        byteArrayOutputStream.close();
    }
}
