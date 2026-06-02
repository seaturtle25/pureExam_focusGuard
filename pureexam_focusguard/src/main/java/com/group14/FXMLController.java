package com.group14;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMLController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {

        showPage("home.fxml");
    }

    @FXML
    private void showHome() {

        showPage("home.fxml");
    }

    @FXML
    private void showBlock() {

        showPage("block.fxml");
    }

    @FXML
    private void showBrowser() {
        
        openNewWindow("browser.fxml");
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