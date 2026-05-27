package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;

// 將BlockData轉成Json管理
public class JsonManager {

    private static final String FILE_NAME = "block_data.json";

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static void save(BlockData data) {

        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            
            // 將BlockData物件轉Json後寫入FILE_NAME
            gson.toJson(data, writer);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static BlockData load() {

        try (FileReader reader = new FileReader(FILE_NAME)) {

            // 回傳FILE_NAME裡的Json轉BlockData物件
            return gson.fromJson(reader, BlockData.class);

        } catch (Exception e) {

            return new BlockData();
        }
    }

    public static BlockData loadJson() {

        try (FileReader reader = new FileReader(FILE_NAME)) {

            // 回傳FILE_NAME裡的Json轉BlockData物件
            return gson.fromJson(reader, BlockData.class);

        } catch (Exception e) {

            return new BlockData();
        }
    }

}