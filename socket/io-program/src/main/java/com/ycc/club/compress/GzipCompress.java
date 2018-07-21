package com.ycc.club.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created on 2018\7\20 0020 by yancongcong
 */
public class GzipCompress {

    public static void main(String[] args) throws Exception {
        byte[] byt = new byte[1024];

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("README.md"));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream("test.gz")));

        int c;
        while ((c = bufferedInputStream.read(byt)) != -1) {
            bufferedOutputStream.write(byt);
            byt = new byte[1024];
        }
        bufferedOutputStream.close();
        bufferedInputStream.close();
    }

}
