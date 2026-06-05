package com.group14;

//專注時間，一天的統計結果
//日期、總時間
public class FocusRecord {

    private String date;
    private int focusMinutes;

    public FocusRecord() {
    }

    public FocusRecord(String date, int focusMinutes) {
        this.date = date;
        this.focusMinutes = focusMinutes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFocusMinutes() {
        return focusMinutes;
    }

    public void setFocusMinutes(int focusMinutes) {
        this.focusMinutes = focusMinutes;
    }
}
