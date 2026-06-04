package com.group14;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.Node;


public class CreateExamController {
   @FXML
   private TextField websiteField;

   @FXML
   private TextField appField;

   @FXML
   private TextField minuteField;

   @FXML
   private VBox websiteContainer;

   @FXML
   private FlowPane appContainer;
   
    @FXML
    private void initialize() {    
    }

   @FXML
   private void addWebsite() {

    String url = websiteField.getText();

    if(url.isEmpty())
        return;

    Label label = new Label(url);
    label.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

    HBox item = new HBox(5);
    item.setAlignment(Pos.CENTER_LEFT);
    item.setStyle("-fx-background-color: #ffffff; -fx-padding: 8;");

    HBox.setHgrow(label, Priority.ALWAYS);
    label.setMaxWidth(Double.MAX_VALUE);

    item.getChildren().addAll(label);
    websiteContainer.getChildren().add(item);
    websiteField.clear();
}

@FXML
private void addApp() {

    String app = appField.getText();

    if(app.isEmpty())
        return;

    Label label = new Label(app);
    label.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

    appContainer.getChildren().add(label);
    appField.clear();
}

// 輸出json檔
@FXML
private void saveExam() {

    ExamBlockData data = new ExamBlockData();

    try {

        int min = Integer.parseInt(minuteField.getText());

        if(min <= 0) {
            throw new NumberFormatException();
        }

        data.setMin(min);

    } catch(NumberFormatException e) {

        new Alert(
            Alert.AlertType.ERROR,
            "請輸入正確的分鐘數"
        ).showAndWait();

        return;
    }

    // 網址
    for (Node node : websiteContainer.getChildren()) {

        HBox item = (HBox) node;

        Label label = (Label) item.getChildren().get(0);

        data.getWebsites().add(label.getText());
    }

    // 軟體
    for (Node node : appContainer.getChildren()) {

        Label label = (Label) node;

        data.getApps().add(label.getText());
    }

    JsonManager.saveExam(data);
}
    
   // 返回home
   @FXML
   private void goBackHome() {
        try {
            MainApp.setRoot("home", "Oasis");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
   }

}
