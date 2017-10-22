package zabbixagent.client;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Deque;
import java.util.Queue;

import static java.nio.charset.Charset.forName;
import static java.nio.file.Paths.get;

public class FileSort {


    public static class Entry {

        private static String delim = " ";
        private Long key;
        private String data;

        public Entry(String line) {
            String[] parts = line.split(delim);
            key = Long.valueOf(parts[0]);
            data = parts[1];
        }

        @Override
        public String toString() {
            return key + delim + data + "\n";
        }
    }


    static Charset utf8 = forName("UTF-8");

    public void test() throws Exception {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("res.txt"), StandardOpenOption.APPEND);
        BufferedReader br1 = Files.newBufferedReader(get("in1.xls"), utf8);
        BufferedReader br2 = Files.newBufferedReader(get("in2.xls"), utf8);
        String l1 = br1.readLine(), l2 = br2.readLine();
        while (l1 != null && l2 != null) {
            Entry e1 = new Entry(l1);
            Entry e2 = new Entry(l2);
            if (e1.key < e2.key) {
                writer.write(e1.toString());
                l1 = br1.readLine();
            } else {
                writer.write(e2.toString());
                l2 = br2.readLine();
            }
        }
        while ((l1 = br1.readLine()) != null) {
            writer.write(l1 + "\n");

        }
        while ((l2 = br2.readLine()) != null) {
            writer.write(l2 + "\n");
        }
        writer.flush();
        return null;
    }

}
