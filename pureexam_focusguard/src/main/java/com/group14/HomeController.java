package com.group14;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class HomeController {

    @FXML private Spinner<Integer> timeSpinner; // 綁定妳的滾輪
    @FXML private Label timerLabel;
    @FXML private Label statusLabel;
    @FXML private Button focusModeBtn;
    @FXML private Button examModeBtn;
    @FXML private Button pauseBtn;
    @FXML private Button resetBtn;

    private PomodoroTimer timer;
    private boolean isExamMode = false;

    @FXML
    public void initialize() {
        timer = new PomodoroTimer();

        //初始化滾輪：最小 1 分鐘，最大 180 分鐘，預設 25 分鐘
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 180, 25);
        timeSpinner.setValueFactory(valueFactory);
        
        //當滾輪滑動時，大數字跟著變
        timeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            timer.reset(timerLabel, newValue);
        });
    }

    @FXML
    private void startFocusMode() {
        isExamMode = false;
        int minutes = timeSpinner.getValue();
        timer.start(timerLabel, minutes);
        
        statusLabel.setText("狀態：讀書專注中 (可暫停/停止)");
        timeSpinner.setDisable(true); //開始後不能偷改滾輪時間
        
        //專注模式：解鎖暫停與停止按鈕
        pauseBtn.setDisable(false);
        resetBtn.setDisable(false);
    }

    @FXML
    private void startExamMode() {
        int minutes = timeSpinner.getValue();
        
        //考試時間不得 < 1 分鐘
        if (minutes < 1) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("時間設定錯誤");
            alert.setHeaderText(null);
            alert.setContentText("請輸入考試時間，不得小於 1 分鐘！");
            alert.showAndWait();
            return;
        }

        isExamMode = true;
        timer.start(timerLabel, minutes);
        statusLabel.setText("狀態：嚴格監考中 (防作弊已啟動！)");
        
        // 💡 考試模式核心邏輯：物理封印所有按鈕，不給暫停也不給重置！
        timeSpinner.setDisable(true);
        pauseBtn.setDisable(true);   // 鎖死暫停鍵
        resetBtn.setDisable(true);   // 鎖死重置鍵
        focusModeBtn.setDisable(true);
        examModeBtn.setDisable(true);

        // 啟動視窗鎖死 + 呼叫妳寫好的底層瀏覽器獵殺器
        enableAntiCheatLock();
        ProcessMonitorTest.start(); 
    }

    @FXML
    private void handlePause() {
        if (!isExamMode) {
            timer.pause();
            statusLabel.setText("狀態：已暫停");
        }
    }

    @FXML
    private void handleReset() {
        int minutes = timeSpinner.getValue();
        timer.reset(timerLabel, minutes);
        statusLabel.setText("狀態：等待選擇模式");
        
        // 恢復所有按鈕的點擊功能
        timeSpinner.setDisable(false);
        pauseBtn.setDisable(false);
        resetBtn.setDisable(false);
        focusModeBtn.setDisable(false);
        examModeBtn.setDisable(false);
        
        disableAntiCheatLock();
        ProcessMonitorTest.stop();
    }

    // --- 動態防作弊鎖定機制 ---
    private void enableAntiCheatLock() {
        Stage stage = (Stage) timerLabel.getScene().getWindow();
        if (stage != null) {
            stage.setAlwaysOnTop(true);
            stage.setOnCloseRequest(event -> {
                System.out.println("⚠️ 嚴格監考中，禁止關閉程式！");
                event.consume(); // 攔截右上角的 X
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