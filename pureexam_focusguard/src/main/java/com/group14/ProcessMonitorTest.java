package com.group14;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.google.gson.Gson;

import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

public class ProcessMonitorTest {
    private static SystemInfo systemInfo = new SystemInfo();
    private static OperatingSystem os = systemInfo.getOperatingSystem();
    private static final String LOG_FILE_PATH = "History.csv";
    private static ScheduledExecutorService executor;
    public static void start(){ 
        if (executor != null && !executor.isShutdown()) return;
        List<String> dynamicBlackList = new ArrayList<>();
        File settingFile = new File("exam_block_data.json");
        if (settingFile.exists()) {
            try (FileReader reader = new FileReader(settingFile)) {
                Gson gson = new Gson();
                //用BlockData的結構來解析考試設定
                BlockData examData = gson.fromJson(reader, BlockData.class);
                if (examData != null && examData.getBlockedApps() != null) {
                    for (BlockItem item : examData.getBlockedApps()) {
                        if (item.isEnabled()) {
                            //把"chrome.exe"轉小寫並去掉".exe"以符合系統程序名稱
                            String appName = item.getName().toLowerCase().replace(".exe", "");
                            dynamicBlackList.add(appName);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("讀取考試設定檔失敗：" + e.getMessage());
            }
        }
        // 如果考試設定檔不存在或裡面沒勾任何軟體，就啟用寫死的預設名單
        if (dynamicBlackList.isEmpty()) {
            dynamicBlackList.addAll(Arrays.asList("chrome", "msedge", "firefox", "brave", "opera", "safari", "vivaldi", "tor", "chromium", "edge"));
        }
        executor = Executors.newSingleThreadScheduledExecutor();
        Runnable monitorTask = () -> {
            List<OSProcess> processes = os.getProcesses();
            for(OSProcess process : processes){
                String pName = process.getName().toLowerCase();
                for(String blockApp : dynamicBlackList){
                    if(pName.contains(blockApp)){
                        killProcess(blockApp + ".exe");
                        logToCSV(blockApp + ".exe");
                        break;
                    }
                }
            }
            /*if(!found){
                System.out.println("No prohibited processes found.");

                System.out.println("--- 系統前 30 個進程清單 (除錯用) ---");
                for(int i = 0; i < Math.min(30, processes.size()); i++){
                    System.out.println(processes.get(i).getName());
                }
            }*/
        };
        
        executor.scheduleAtFixedRate(monitorTask, 0, 3, java.util.concurrent.TimeUnit.SECONDS);
    }
    
    public static void stop() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow(); // 強制停止背景計時器
            System.out.println("背景進程監控已停止。");
        }
    }

    private static void killProcess(String processName){
        try {
            String command = "taskkill /F /IM " + processName;
            Runtime.getRuntime().exec(command);
            System.out.println("Force closing: " + processName);
        } 
        catch (Exception e) {
            System.err.println("No need to close: " + processName + ": " + e.getMessage());
        }
    }

    private static void logToCSV(String processName){
        try(FileWriter writer = new FileWriter(LOG_FILE_PATH, true);
            PrintWriter printer = new PrintWriter(writer)){
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = now.format(formatter);

            printer.println(formattedTime + "," + processName + ",Blocked");
        }
        catch(IOException e){
            System.err.println("Error logging to CSV: " + e.getMessage());
        }
    }
}
