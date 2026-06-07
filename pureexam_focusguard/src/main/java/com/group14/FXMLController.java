package com.group14;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FXMLController {

    @FXML private Button homeBtn;

    @FXML
    private StackPane contentArea;

    private Parent timerPage; // 修:記住當下時鐘，避免切換畫面被重置

    @FXML
    public void initialize() {

        showTimer();
        //修:預設載入時，直接呼叫專屬的方法
    }

    @FXML
    private void showHome() {
        if (PomodoroController.isRunning) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("注意");
            alert.setHeaderText(null);
            alert.setContentText("目前正在專注計時中，請先重置計時器後再返回首頁！");
            alert.showAndWait();
            return; // 修:攔截，提早結束方法，不執行下面的 setRoot
        }
        try {
            MainApp.setRoot("home", "Oasis");
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }

    @FXML
    private void showTimer() {
        try {
            //修:如果已經載入過，就直接使用記住的頁面，避免重置
            if(timerPage == null) {
                timerPage = FXMLLoader.load(getClass().getResource("/fxml/pomodoro.fxml"));
            }
            contentArea.getChildren().clear();
            contentArea.getChildren().add(timerPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    

    @FXML
    private void showSettings() {

        showPage("block.fxml");
    }

    @FXML
    private void showBrowser() {
        
        openNewWindow("browser.fxml");

    }
    
    @FXML
    private void showHistory() {
        
        showPage("history.fxml");
    }

    private void showPage(String fxml) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/" + fxml)
            );

            Parent page = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(page);

        } catch (Exception e) {
            System.err.println("無法載入");
            e.printStackTrace();

        }
    }

    private void openNewWindow(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/" + fxml)
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
}