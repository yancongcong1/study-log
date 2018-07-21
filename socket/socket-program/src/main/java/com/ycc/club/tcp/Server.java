package com.ycc.club.tcp;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created on 2018\7\5 0005 by yancongcong
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("tcp监听端口：8888");
        Socket socket = serverSocket.accept();

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        while (true) {
            String msg = in.readUTF();
            System.out.println("收到客户端信息" + msg);

            Scanner sc = new Scanner(System.in);
            System.out.println("请输入：");
            String str = sc.next();
            out.writeUTF(str);
        }
    }
}
