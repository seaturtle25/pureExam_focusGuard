package com.group14;

//專注模式持續時間紀錄
//資料型態
//StudyRecord.json -> JsonManager.loadStudySessions()輸出List<StudySession> 
// -> HistoryManager.countFocusTime()輸出List<FocusRecord> -> HistoryData
public class StudySession {

    private String timestamp;
    private int durationMinutes;

    public StudySession() {}

    public String getTimestamp() {
        return timestamp;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }
}
