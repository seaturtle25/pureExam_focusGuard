package com.group14;

//資料型態
//統計攔截次數
public class ViolationRecord {

    private String name;
    private int count;

    public ViolationRecord() {
    }

    public ViolationRecord(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
