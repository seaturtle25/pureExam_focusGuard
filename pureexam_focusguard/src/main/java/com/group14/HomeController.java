package com.group14;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private Button focusBtn; 

    @FXML
    private void startFocus() throws IOException {
        PomodoroController.isExamMode = false;
        switchPage("/fxml/sidebar.fxml");
        //goToPomodoro();
    }

    @FXML
    private void startTest() throws IOException {
        PomodoroController.isExamMode = true;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/examDialog.fxml"));
            Parent root = loader.load();

            ExamDialogController dialogController = loader.getController();
            dialogController.setHomeController(this);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("OASIS 考試模式");
            dialogStage.initModality(Modality.APPLICATION_MODAL); // 鎖定後方視窗
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);
            
            dialogStage.showAndWait(); // 亮相並等待

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchPage(String fxml) {
        try{
            Parent newView = FXMLLoader.load(getClass().getResource(fxml));
            focusBtn.getScene().setRoot(newView);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("無法導向頁面" + fxml);
        }
        
    }

    /*
    //局部換頁
    private void goToPomodoro() throws IOException {
        //讀取番茄鐘的畫面
        Parent pomodoroView = FXMLLoader.load(getClass().getResource("/fxml/pomodoro.fxml"));
        
        //抓到整個視窗最底層的BorderPane
        BorderPane root = (BorderPane) focusModeBtn.getScene().getRoot();

        } catch (IOException e) {

            System.err.println("無法載入 sidebar.fxml");
            e.printStackTrace();
        }
    }*/
}
