package com.group14;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import java.io.IOException;


public class HomeController {

    @FXML
    private Button focusBtn; 

    @FXML
    private void initialize() {

    }

    @FXML
    private void startFocus() {
        try {

            MainApp.setRoot("sidebar", "Oasis");

        } catch (IOException e) {

            System.err.println("無法載入 sidebar.fxml");
            e.printStackTrace();
        }
    }

    @FXML
    private void startTest() throws IOException {
       
        Stage dialog = new Stage();

        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(300, 150);

        Button createBtn = new Button("建立考試");
        Button enterBtn = new Button("進入考試");

        enterBtn.setOnAction(e -> {

        try {
            MainApp.setRoot("enterExam", "Oasis");
            dialog.close();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    });

    createBtn.setOnAction(e -> {

        try {
            MainApp.setRoot("createExam", "Oasis");
            dialog.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    });

        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(createBtn, enterBtn);

        root.getChildren().addAll(
            buttons
        );

        Scene scene = new Scene(root);

        dialog.setScene(scene);
        dialog.show();
    
    }


}

