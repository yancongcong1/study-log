package com.ycc.club.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created on 2018\7\6 0006 by yancongcong
 */
public class DirList3 {
    public static void main(final String[] args) {
        File file = new File("./");
        String[] list;

        if (args.length == 0) {
            list = file.list();
        } else {
            list = file.list(new FilenameFilter() {
                private Pattern pattern = Pattern.compile(args[0]);
                public boolean accept(File dir, String name) {
                    return false;
                }
            });
        }

        Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
        for (String name : list) {
            System.out.println(name);
        }
    }
}
