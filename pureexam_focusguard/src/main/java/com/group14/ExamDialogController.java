package com.group14;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ExamDialogController {

    @FXML private Button btnTeacher; // 用來抓取目前視窗的媒介
    
    private HomeController homeController;

    // 把 HomeController 傳進來，讓首頁換頁
    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    @FXML
    private void handleTeacher() {
        closeDialog();
        if (homeController != null) {
            homeController.switchPage("/fxml/teacherSetup.fxml");
        }
    }

    @FXML
    private void handleStudent() {
        closeDialog();
        if (homeController != null) {
            homeController.switchPage("/fxml/studentLogin.fxml");
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog(); // 單純關閉，什麼都不做
    }

    private void closeDialog() {
        Stage stage = (Stage) btnTeacher.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }
}