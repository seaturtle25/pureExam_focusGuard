package com.group14;

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
                System.out.println("攔截: 嘗試存取 AI 工具: " + blocked);
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