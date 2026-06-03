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

        showPage("pomodoro.fxml");
    }

    @FXML
    private void showHome() throws IOException {

        Parent homeView = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
        homeBtn.getScene().setRoot(homeView);
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