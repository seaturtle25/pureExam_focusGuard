package com.group14;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

//讀取csv檔將每筆資料存成Record，放入List<Record>
public class CsvManager {

    private static final String FILE = "History.csv";

    public static List<Record> loadRecords() {

        List<Record> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {

            String line;

            while ((line = br.readLine()) != null) { //一行一行讀取

                String[] parts = line.split(",");
                
                //字串轉時間
                LocalDateTime time = LocalDateTime.parse(parts[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                //每一行存成Record放入records arraylist
                records.add(new Record(time, parts[1], parts[2]));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }
}
