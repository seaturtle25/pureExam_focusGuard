package com.group14;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class StudentLoginController {
    
    @FXML private Button importBtn;

    private int examTime = 0;
    private String urlsData = "";
    private String appsData = "";

    @FXML
    private void handleImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("選擇考試設定檔 (.oasis)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Oasis Files (*.oasis)", "*.oasis"));
        
        File file = fileChooser.showOpenDialog(importBtn.getScene().getWindow());
        if (file != null) {
            try {
                // 讀取 Base64 字串
                String encryptedData = Files.readString(file.toPath()).trim();
                
                // 還原回 JSON
                byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
                String jsonStr = new String(decodedBytes, StandardCharsets.UTF_8);
                
                System.out.println("解碼成功！原始 JSON: " + jsonStr);

                // 用 Gson 解析資料
                JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
                examTime = jsonObject.get("time").getAsInt();
                urlsData = jsonObject.get("urls").toString();
                appsData = jsonObject.get("apps").toString();

                // 成功提示
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setTitle("載入成功");
                alert.setContentText("考試時間: " + examTime + " 分鐘\n請點擊「開始考試」。");
                alert.getButtonTypes().setAll(ButtonType.OK);
                alert.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("錯誤");
                alert.setContentText("設定檔無效！");
                alert.showAndWait();
            }
        }
    }

    // 2. 點擊「開始考試」
    @FXML
    private void handleStartExam() {
        if (examTime < 1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("請先選擇並匯入有效的考試設定檔 (.oasis)!");
            alert.showAndWait();
            return;
        }

        try {
            // 傳給番茄鐘
            PomodoroController.isExamMode = true;

            Parent examView = FXMLLoader.load(getClass().getResource("/fxml/pomodoro.fxml"));
            importBtn.getScene().setRoot(examView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
