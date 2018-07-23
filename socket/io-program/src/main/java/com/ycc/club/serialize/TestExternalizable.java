package com.ycc.club.serialize;

import java.io.*;

/**
 * Created on 2018\7\23 0023 by yancongcong
 */
public class TestExternalizable implements Externalizable {

    private int i;
    private String s;

    public TestExternalizable() {
        System.out.println("default constructor");
    }

    public TestExternalizable(int i, String s) {
        System.out.println("parameter constructor");
        this.i = i;
        this.s = s;
    }

    @Override
    public String toString() {
        return "TestExternalizable{" +
                "i=" + i +
                ", s='" + s + '\'' +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(s);
//        out.writeInt(i);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        s = (String) in.readObject();
//        i = in.readInt();
    }

    public static void main(String[] args) throws Exception {
        TestExternalizable testExternalizable = new TestExternalizable(1, "ycc");
        System.out.println(testExternalizable);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(testExternalizable);
        objectOutputStream.close();

        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        TestExternalizable testExternalizable1 = (TestExternalizable) objectInputStream.readObject();
        System.out.println(testExternalizable1);
        objectInputStream.close();
        byteArrayOutputStream.close();
    }
}
