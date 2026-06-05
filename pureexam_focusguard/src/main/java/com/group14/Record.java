package com.group14;

import java.time.LocalDateTime;

//攔截紀錄(like 2026-06-02 00:29:45,chrome.exe,Blocked)
//History.csv -> CsvManager -> List<Record> -> HistoryManager.java統計 -> List<ViolationRecord>
public class Record {

    private LocalDateTime time;
    private String target;
    private String status;

    public Record(LocalDateTime time, String target, String status) {
        this.time = time;
        this.target = target;
        this.status = status;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getTarget() {
        return target;
    }

    public String getStatus() {
        return status;
    }

}
