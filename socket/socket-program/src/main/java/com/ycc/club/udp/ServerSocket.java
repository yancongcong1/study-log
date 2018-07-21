package com.ycc.club.udp;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created on 2018\7\5 0005 by yancongcong
 */
public class ServerSocket {
    DatagramSocket datagramSocket;

    {
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
