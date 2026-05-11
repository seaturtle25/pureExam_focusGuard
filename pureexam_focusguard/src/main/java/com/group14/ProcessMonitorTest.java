package com.group14;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

public class ProcessMonitorTest {
    private static SystemInfo systemInfo = new SystemInfo();
    private static OperatingSystem os = systemInfo.getOperatingSystem();
    private static List<String> blackList = Arrays.asList("notepad", "chrome", "firefox");
    private static final String LOG_FILE_PATH = "History.csv";
    public static void main(String[] args) { 
        System.out.println("Checking for prohibited processes...");
        ScheduledExecutorService executor = java.util.concurrent.Executors.newSingleThreadScheduledExecutor();
        Runnable monitorTask = () -> {
            List<OSProcess> processes = os.getProcesses();
            boolean found = false;
            for(OSProcess process : processes){
                String pName = process.getName().toLowerCase();
                for(String blockApp : blackList){
                    if(pName.contains(blockApp)){
                        System.out.println("Found and closing: " + process.getName());
                        found = true;
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
