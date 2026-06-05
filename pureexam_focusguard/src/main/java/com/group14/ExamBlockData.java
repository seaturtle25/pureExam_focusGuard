package com.group14;

import java.util.ArrayList;
import java.util.List;

// 建立考試資料json檔
public class ExamBlockData {

    private List<String> websites = new ArrayList<>();

    private List<String> apps = new ArrayList<>();

    private int min;

    public List<String> getWebsites() {
        return websites;
    }

    public void setWebsites(List<String> websites) {
        this.websites = websites;
    }

    public List<String> getApps() {
        return apps;
    }

    public void setApps(List<String> apps) {
        this.apps = apps;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}