package com.group14;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;

public class PomodoroController {

    //用來接收HomeController傳過來的模式設定
    public static boolean isExamMode = false; 

    @FXML private Button backBtn;
    @FXML private Spinner<Integer> timeSpinner;
    @FXML private Button startBtn;
    @FXML private Label timerLabel;
    @FXML private Label statusLabel;
    @FXML private Button pauseBtn;
    @FXML private Button resetBtn;

    private PomodoroTimer timer;

    @FXML
    public void initialize() {
        timer = new PomodoroTimer();
        
        // 設定滾輪：最小 1 分，最大 120 分，預設 25 分
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 120, 25);
        timeSpinner.setValueFactory(valueFactory);
        
        //讓大數字跟著滾輪動態變化
        timeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            timer.reset(timerLabel, newValue);
        });

        //初始狀態下，鎖定暫停與重置按鈕
        pauseBtn.setDisable(true);
        resetBtn.setDisable(true);

        //根據進來時的模式，給予不同的提示與介面調整
        if (isExamMode) {
            statusLabel.setText("目前為考試模式，開始後將無法暫停與跳出！");
        } else {
            statusLabel.setText("請設定時間並按下開始計時");
        }
    }

    @FXML
    private void handleStart() {
        int minutes = timeSpinner.getValue();
        
        // 考試模式下的防呆機制
        if (isExamMode && minutes < 1) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("考試時間不得小於 1 分鐘！");
            alert.showAndWait();
            return;
        }

        //啟動底層番茄鐘計時器
        timer.start(timerLabel, minutes);
        
        //點擊開始後，鎖定時間滾輪與開始按鈕，不給偷改
        startBtn.setDisable(true);
        timeSpinner.setDisable(true);

        if (isExamMode) {
            statusLabel.setText("狀態：嚴格監考中 (防作弊已啟動！)");
            pauseBtn.setDisable(true);
            resetBtn.setDisable(true);
            backBtn.setDisable(true); //考試中不准回首頁
            
            //啟動視窗鎖死
            enableAntiCheatLock();
            ProcessMonitorTest.start(); 
        } else {
            statusLabel.setText("狀態：讀書專注中");
            pauseBtn.setDisable(false);
            resetBtn.setDisable(false);
            backBtn.setDisable(true); //計時中先不讓學生亂切換畫面
        }
    }

    @FXML
    private void handlePause() {
        //只有非考試模式才能暫停
        if (!isExamMode) {
            timer.pause();
            statusLabel.setText("狀態：已暫停");
        }
    }

    @FXML
    private void handleReset() {
        int minutes = timeSpinner.getValue();
        timer.reset(timerLabel, minutes);
        statusLabel.setText("狀態：已重置，請重新開始");
        
        //恢復所有按鈕原本的狀態
        startBtn.setDisable(false);
        timeSpinner.setDisable(false);
        backBtn.setDisable(false);
        pauseBtn.setDisable(true);
        resetBtn.setDisable(true);
        
        //解除鎖定與防作弊進程
        disableAntiCheatLock();
        ProcessMonitorTest.stop();
    }

    /*@FXML
    private void handleBack() throws IOException {
        //讀取大廳的畫面
        Parent homeView = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
        
        //抓到整個視窗最底層的外殼
        BorderPane root = (BorderPane) backBtn.getScene().getRoot();
        
        //抓出正中央的 StackPane
        StackPane contentArea = (StackPane) root.getCenter();
        
        if (contentArea != null) {
            contentArea.getChildren().clear(); // 清除番茄鐘畫面
            contentArea.getChildren().add(homeView); // 塞回大廳畫面
        }
    }*/

    // --- 動態防作弊鎖定機制 ---
    private void enableAntiCheatLock() {
        Stage stage = (Stage) timerLabel.getScene().getWindow();
        if (stage != null) {
            stage.setAlwaysOnTop(true);
            stage.setOnCloseRequest(event -> {
                System.out.println("監考中，禁止關閉程式！");
                event.consume(); //攔截右上角的 X 按鈕
            });
        }
    }

    private void disableAntiCheatLock() {
        Stage stage = (Stage) timerLabel.getScene().getWindow();
        if (stage != null) {
            stage.setAlwaysOnTop(false);
            stage.setOnCloseRequest(null); 
        }
    }
}