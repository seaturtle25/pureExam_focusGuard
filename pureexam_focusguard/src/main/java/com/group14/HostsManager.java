package com.group14;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HostsManager {
    // Windows 的 hosts 檔固定位置
    private static final String HOSTS_PATH = "C:\\Windows\\System32\\drivers\\etc\\hosts";
    private static final String OASIS_MARKER = "# --- OASIS FOCUS MODE ---";

    // 啟動專注模式：把黑名單寫入 hosts
    public static void enableFocusMode(List<String> blockedDomains) {
        // 先清理乾淨，避免重複寫入
        disableFocusMode(); 

        if (blockedDomains == null || blockedDomains.isEmpty()) return;

        try (FileWriter fw = new FileWriter(HOSTS_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            
            out.println();
            out.println(OASIS_MARKER); // 留下 OASIS 記號，免得刪到其他系統設定檔
            
            for (String domain : blockedDomains) {
                // 將網域導向本機 127.0.0.1，就連不上了
                // IPv4
                out.println("127.0.0.1 " + domain);
                out.println("127.0.0.1 www." + domain);
            
                // IPv6
                out.println("::1" + domain);
                out.println("::1 www." + domain);
            }
            
            out.println(OASIS_MARKER);
            System.out.println("專注模式已啟動，網站已封鎖！");
            Runtime.getRuntime().exec("ipconfig /flushdns");
            
        } catch (IOException e) {
            System.err.println("修改 Hosts 失敗！請確認是否有系統管理員權限！");
        }
    }

    // 結束專注模式：把 OASIS 加的設定刪掉，恢復網路
    public static void disableFocusMode() {
        File hostsFile = new File(HOSTS_PATH);
        List<String> safeLines = new ArrayList<>();
        boolean isInsideOasisBlock = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(hostsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 遇到我們的記號，切換狀態
                if (line.equals(OASIS_MARKER)) {
                    isInsideOasisBlock = !isInsideOasisBlock;
                    continue; 
                }
                
                // 如果不是我們加的行，就把它保留下來
                if (!isInsideOasisBlock) {
                    safeLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // 把安全的行數重新寫回 hosts 檔
        try (PrintWriter writer = new PrintWriter(new FileWriter(hostsFile))) {
            for (String safeLine : safeLines) {
                writer.println(safeLine);
            }
            System.out.println("專注模式已解除，網路已恢復！");
            Runtime.getRuntime().exec("ipconfig /flushdns");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 確保 hosts 可以寫入(沒被設為唯讀)
    public static boolean isHostsWritable() {
        File hostsFile = new File(HOSTS_PATH);
        return hostsFile.exists() && hostsFile.canWrite();
    }
}
