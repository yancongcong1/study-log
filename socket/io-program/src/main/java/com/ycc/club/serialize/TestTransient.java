package com.ycc.club.serialize;

import java.io.*;
import java.util.Date;

/**
 * Created on 2018\7\23 0023 by yancongcong
 */
public class TestTransient implements Serializable {

    private String username;
    private transient String password;

    public TestTransient(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "TestTransient{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static void main(String[] args) throws Exception {
        TestTransient testTransient = new TestTransient("ycc", "123456");
        System.out.println(testTransient);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(testTransient);
        objectOutputStream.close();

        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        TestTransient testTransient1 = (TestTransient) objectInputStream.readObject();
        System.out.println(testTransient1);
        objectInputStream.close();
        byteArrayOutputStream.close();
    }
}
