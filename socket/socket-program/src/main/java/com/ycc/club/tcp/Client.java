package com.ycc.club.tcp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created on 2018\7\5 0005 by yancongcong
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8888);

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        while(true){
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入：");
            String str = sc.next();
            out.writeUTF(str);

            String msg = in.readUTF();
            System.out.println("收到服务端信息" + msg);
        }
    }
}
