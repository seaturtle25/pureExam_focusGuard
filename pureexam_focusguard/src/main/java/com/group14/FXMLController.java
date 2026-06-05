package com.group14;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FXMLController {

    @FXML private Button homeBtn;

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {

        showPage("timer.fxml");
    }

    @FXML
    private void showHome() {
        try {
            MainApp.setRoot("home", "Oasis");
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }

    @FXML
    private void showTimer() {

        showPage("timer.fxml");
    }    

    @FXML
    private void showSettings() {

        showPage("block.fxml");
    }

    @FXML
    private void showBrowser() {
        
        openNewWindow("browser.fxml");

    }
    
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