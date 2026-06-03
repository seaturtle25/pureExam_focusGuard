package com.group14;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;

public class HomeController {

    //按鈕們
    @FXML private Button focusModeBtn;
    @FXML private Button examModeBtn;

    @FXML
    private void startFocusMode() throws IOException {
        PomodoroController.isExamMode = false;
        goToPomodoro();
    }

    @FXML
    private void startExamMode() throws IOException {
        PomodoroController.isExamMode = true;
        goToPomodoro();
    }

    //局部換頁
    private void goToPomodoro() throws IOException {
        //讀取番茄鐘的畫面
        Parent pomodoroView = FXMLLoader.load(getClass().getResource("/fxml/pomodoro.fxml"));
        
        //抓到整個視窗最底層的BorderPane
        BorderPane root = (BorderPane) focusModeBtn.getScene().getRoot();

        //把 BorderPane正中央的區塊抓出來
        StackPane contentArea = (StackPane) root.getCenter();
        
        if (contentArea != null) {
            contentArea.getChildren().clear(); // 清除大廳畫面
            contentArea.getChildren().add(pomodoroView); // 塞入番茄鐘畫面
        }
    }
}