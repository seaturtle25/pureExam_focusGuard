package com.group14;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UrlInterceptor {

    //網址包含這些關鍵字就封鎖
    private static final List<String> AI_BLACKLIST = Arrays.asList(
        "chatgpt.com",
        "chat.openai.com",
        "claude.ai",
        "gemini.google.com",
        "copilot.microsoft.com",
        "perplexity.ai",
        "poe.com",
        "deepseek.com",
        "phind.com"
    );

    /**
    檢查網址是否為AI網站
    url = 準備載入的網址
    return true 通過
           false 封鎖*/
    public boolean isSafeUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        
        // 轉小寫方便比對
        String lowerCaseUrl = url.toLowerCase();

        //遍歷黑名單，檢查網址
        for (String blocked : AI_BLACKLIST) {
            if (lowerCaseUrl.contains(blocked)) {
                return false; 
            }
        }

        //修:沒攔截到網址(專注和考試模式都要檢查)，再檢查動態網址
        List<String> dynamicUrls = new ArrayList<>();
        if (PomodoroController.isExamMode){
            // 考試模式：讀取老師出卷時設定的網址
            if (PomodoroController.examBlockedUrls != null) {
                for(String u : PomodoroController.examBlockedUrls) {
                     dynamicUrls.add(u.toLowerCase());
                }
            }
        }
        else {
            // 一般專注模式：讀取學生自己設定的 BlockData JSON
            BlockData data = JsonManager.load();
            if (data != null && data.getBlockedWebsites() != null) {
                for (BlockItem item : data.getBlockedWebsites()) {
                    if (item.isEnabled()) {
                        dynamicUrls.add(item.getName().toLowerCase());
                    }
                }
            }
        }
        //檢查是否有踩到動態黑名單
        for (String blockedUrl : dynamicUrls) {
            if (lowerCaseUrl.contains(blockedUrl)) {
                return false;
            }
        }

        return true; //通過
    }

    /*重新導向回Google*/
    public String getRedirectUrl() {
        return "https://www.google.com"; 
    }
}