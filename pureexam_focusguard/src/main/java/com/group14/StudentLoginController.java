package com.group14;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


import com.google.gson.Gson;

public class StudentLoginController {
    
    @FXML private Button importBtn;
    @FXML private VBox infoBox;
    @FXML private Label examInfoLabel;

    private int examTime = 0;
    private List<String> appToBlock = new ArrayList<>();
    private List<String> urlToBlock = new ArrayList<>();

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
                Gson gson = new Gson();
                ExamBlockData loadedData = gson.fromJson(jsonStr, ExamBlockData.class);
                examTime = loadedData.getMin();
                appToBlock = loadedData.getApps();
                urlToBlock = loadedData.getWebsites();

                importBtn.setVisible(false);
                importBtn.setManaged(false);

                infoBox.setVisible(true);
                infoBox.setManaged(true);
                examInfoLabel.setText("考試時間" + examTime + " 分鐘\n已套用封鎖軟體列表。");

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
            PomodoroController.loadedExamMinutes = this.examTime;
            PomodoroController.examBlockedApps = this.appToBlock;
            PomodoroController.examBlockedUrls = this.urlToBlock;


            Parent examView = FXMLLoader.load(getClass().getResource("/fxml/pomodoro.fxml"));
            importBtn.getScene().setRoot(examView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            Parent homeView = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
            importBtn.getScene().setRoot(homeView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
