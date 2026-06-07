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
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;

public class PomodoroController {

    //用來接收HomeController傳過來的模式設定
    public static boolean isExamMode = false;
    public static int loadedExamMinutes = 0;
    public static List<String> examBlockedApps = new ArrayList<>();
    public static List<String> examBlockedUrls = new ArrayList<>();

    public static boolean isRunning = false; //修:紀錄當前倒計時是否正在運行，讓外部也能讀取

    @FXML private Button backBtn;
    @FXML private Spinner<Integer> timeSpinner;
    @FXML private Button startBtn;
    @FXML private Label timerLabel;
    @FXML private Label statusLabel;
    @FXML private Button pauseBtn;
    @FXML private Button resetBtn;
    @FXML private Button browserBtn;

    private PomodoroTimer timer;

    @FXML
    public void initialize() {
        timer = new PomodoroTimer();
        
        timer.setOnCompleteAction(() -> {
            isRunning = false; //修:倒計時結束後更新狀態
            if (isExamMode) {
                // 解除防作弊鎖
                disableAntiCheatLock();
                
                // 彈出考試結束視窗
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("考試結束");
                alert.setHeaderText(null);
                alert.setContentText("考試時間到！即將返回首頁。");
                alert.showAndWait();
                
                // 把畫面切回首頁
                try {
                    Parent homeView = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
                    timerLabel.getScene().setRoot(homeView);
                } catch (IOException e) { e.printStackTrace(); }
            } else {
                // 一般專注模式，只彈出休息提示
                Alert alert = new Alert(AlertType.INFORMATION);
                // 關掉 hosts 封鎖
                HostsManager.disableFocusMode();
                alert.setHeaderText(null);
                alert.setContentText("專注時間結束！休息一下吧。");
                alert.show();
            }
        });

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
            
            if (loadedExamMinutes > 0) {
                timeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 120, loadedExamMinutes));
                timer.reset(timerLabel, loadedExamMinutes);
            }
            javafx.application.Platform.runLater(() -> handleStart());

        } else {
            statusLabel.setText("請設定時間並按下開始計時");
        }
    }

    @FXML
    private void handleStart() {
        if (!HostsManager.isHostsWritable()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("系統權限錯誤");
            alert.setContentText("無法啟動防護！請確認您已使用「系統管理員身分」執行，且系統 hosts 檔案未被設為唯讀。");
            alert.showAndWait();
            return; // 直接擋下，不給開始計時！
        }

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
        isRunning = true; //修:更新狀態為正在運行
        
        // 放要鎖的網址
        List<String> domainsToBlock = new ArrayList<>();

        if (isExamMode) {
            statusLabel.setText("狀態：嚴格監考中 (防作弊已啟動！)");
            pauseBtn.setDisable(true);
            resetBtn.setDisable(true);
            //backBtn.setDisable(true); //考試中不准回首頁
            
            if (PomodoroController.examBlockedUrls != null) {
                domainsToBlock.addAll(PomodoroController.examBlockedUrls);
            }

            //啟動視窗鎖死
            enableAntiCheatLock();
        } else {
            statusLabel.setText("狀態：讀書專注中");
            pauseBtn.setDisable(false);
            resetBtn.setDisable(false);

            BlockData blockData = JsonManager.load();
            if (blockData != null && blockData.getBlockedWebsites() != null) {
                for (BlockItem item : blockData.getBlockedWebsites()) {
                    if (item.isEnabled()) {
                        domainsToBlock.add(item.getName());
                    }
                }
            }
            //backBtn.setDisable(true); //計時中先不讓學生亂切換畫面
        }
        
        HostsManager.enableFocusMode(domainsToBlock);
        ProcessMonitorTest.start();

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
        //backBtn.setDisable(false);
        pauseBtn.setDisable(true);
        resetBtn.setDisable(true);
        
        //解除鎖定與防作弊進程
        disableAntiCheatLock();
        HostsManager.disableFocusMode();
        ProcessMonitorTest.stop();
        isRunning = false; //修:更新狀態為未運行
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
    
    @FXML
    private void openBrowser() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/browser.fxml")
            );
            Parent page = loader.load();

            Stage newStage = new Stage();
            newStage.setTitle("Oasis");

            Scene scene = new Scene(page, 1024, 768);
            newStage.setScene(scene);

            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //外部呼叫判斷visible
    public void setButtonVisible(boolean visible) {
        browserBtn.setVisible(visible);
        browserBtn.setManaged(visible);
    }
}