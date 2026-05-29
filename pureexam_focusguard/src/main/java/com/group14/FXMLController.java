package com.group14;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

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
}