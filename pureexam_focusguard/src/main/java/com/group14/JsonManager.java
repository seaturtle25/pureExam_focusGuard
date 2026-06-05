package com.group14;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 將BlockData轉成json管理
// 將json檔轉成List<StudtSession>
public class JsonManager {

    private static final String FILE_NAME = "block_data.json";

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    // 將BlockData物件轉Json後寫入FILE_NAME = "block_data.json"
    public static void save(BlockData data) {

        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            
            gson.toJson(data, writer);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    // 回傳Json轉BlockData物件
    public static BlockData load() {

        try (FileReader reader = new FileReader(FILE_NAME)) {

            return gson.fromJson(reader, BlockData.class);

        } catch (Exception e) {

            return new BlockData();
        }
    }

    //讀取專注模式持續時間StudySession，輸出List<StudySession>
    public static List<StudySession> loadStudySessions() {

        Gson gson = new Gson();

        try (
            Reader reader = new FileReader("StudyRecord.json")) {
            StudySession[] sessions = gson.fromJson(reader, StudySession[].class);
            List<StudySession> sessionList = Arrays.asList(sessions);
            return sessionList;

        } catch (Exception e) {

            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /*
    //輸出考試建立json
    public static void saveExam(
        ExamBlockData data) {

    Gson gson =
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

    try(FileWriter writer = new FileWriter("exam.json")) {

        gson.toJson(data, writer);

    } catch(Exception e) {
        e.printStackTrace();
    }*/
}