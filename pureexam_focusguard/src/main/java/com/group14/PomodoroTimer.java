package com.group14;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class PomodoroTimer {
    private Timeline timeline;
    private int totalSeconds;
    private int initialMinutes; // 紀錄一開始設定的分鐘數
    private Runnable onCompleteAction; //存結束要執行的動作

    //定義JSON紀錄的資料結構
    public static class StudyRecord {
        String timestamp;
        int durationMinutes;

        public StudyRecord(String timestamp, int durationMinutes) {
            this.timestamp = timestamp;
            this.durationMinutes = durationMinutes;
        }
    }
     
    public void start(Label timerLabel, int minutes) {
        //如果已經在跑了，就不要重複啟動 (防止越跑越快)
        if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
            return;
        }
        //如果是被暫停的，從暫停的地方繼續播放
        if (timeline != null && timeline.getStatus() == Animation.Status.PAUSED) {
            timeline.play();
            return;
        }
        this.initialMinutes = minutes;
        this.totalSeconds = minutes * 60;
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            totalSeconds--;
            updateLabel(timerLabel); //更新畫面

            if (totalSeconds <= 0) {
                timeline.stop();
                handleTimerComplete();
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.playFromStart();
        updateLabel(timerLabel);
    }
    public void pause(){
        if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
            timeline.pause();
            System.out.println("計時已暫停");
        }
    }
    public void reset(Label timerLabel, int minutes) {
        if(timeline != null) {
            timeline.stop();
        }
        this.totalSeconds = minutes * 60;
        updateLabel(timerLabel); // 重置時也要更新一次畫面
    }

    public void setOnCompleteAction(Runnable action) {  
        this.onCompleteAction = action;
    }

    private void updateLabel(Label timerLabel) {
        int min = totalSeconds / 60;
        int sec = totalSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", min, sec));
    }
    
    private void logStudySessionToJson(int minutes) {
        String filePath = "StudyRecord.json";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<StudyRecord> records = new ArrayList<>();
        File file = new File(filePath);

        // 如果檔案已經存在，先把舊紀錄讀出來
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Type listType = new TypeToken<ArrayList<StudyRecord>>(){}.getType();
                List<StudyRecord> existingRecords = gson.fromJson(reader, listType);
                if (existingRecords != null) {
                    records.addAll(existingRecords);
                }
            } catch (Exception e) {
                System.err.println("讀取紀錄失敗：" + e.getMessage());
            }
        }

        //取得當下時間並新增這一筆紀錄
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        records.add(new StudyRecord(now, minutes));

        //將包含了新紀錄的列表，轉成 JSON 並寫回檔案
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(records, writer);
            System.out.println("專注紀錄已儲存至 " + filePath);
        } catch (Exception e) {
            System.err.println("寫入紀錄失敗：" + e.getMessage());
        }
    }

    private void handleTimerComplete() {
        //播放結束提示音 (使用獨立執行緒避免卡住 UI，發出連續三聲嗶聲)
        new Thread(() -> {
            for(int i = 0; i < 3; i++) {
                java.awt.Toolkit.getDefaultToolkit().beep();
                try { Thread.sleep(500); } catch (Exception e) {}
            }
        }).start();
        
        //寫入 StudyRecord.json 紀錄 // 修:考試時不寫進去
        if(!PomodoroController.isExamMode){
            logStudySessionToJson(initialMinutes);
        }
        
        //確保背景的進程監控(防作弊)被關閉
        ProcessMonitorTest.stop();

        if (onCompleteAction != null) {
            javafx.application.Platform.runLater(onCompleteAction);
        }
    }
}
