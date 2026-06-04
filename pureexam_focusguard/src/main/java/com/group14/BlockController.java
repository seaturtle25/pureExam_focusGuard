package com.group14;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
//import java.io.File;

public class BlockController {

    @FXML
    private TextField urlField;

    @FXML
    private TextField exeField;
 
    @FXML
    private VBox listContainer;

    @FXML
    private FlowPane listContainer2;

    @FXML
    public void initialize() {
        // 預設，讀初始json
        BlockData data = JsonManager.load();
        
        //讀取封鎖網站
        for(BlockItem item : data.getBlockedWebsites())
            addWebsite(item.getName(), item.isEnabled());
    
        //讀取封鎖APP
        for(BlockItem item : data.getBlockedApps()) {
            CheckBox checkBox = new CheckBox(item.getName());
            checkBox.setSelected(item.isEnabled());
            checkBox.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-padding: 8 12 8 12; -fx-background-radius: 10; -fx-text-fill: black;");
            listContainer2.getChildren().add(checkBox);
        }
        
        //讀取local端，效果不佳
        //loadLocalExe();
    }
    
    private void addWebsite(String url, boolean enabled) {

        Label urlLabel = new Label(url);
        urlLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

        ToggleButton toggle = new ToggleButton();
        toggle.setSelected(enabled);

        if (enabled) {
            toggle.setText("ON");
            toggle.setStyle("-fx-background-color: #90EE90;");
        } else {
            toggle.setText("OFF");
            toggle.setStyle("-fx-background-color: #ff0000;");
        }

        // 切換
        toggle.setOnAction(e -> {

            if (toggle.isSelected()) {
                toggle.setText("ON");
                toggle.setStyle("-fx-background-color: #90EE90;");
            } else {
                toggle.setText("OFF");
                toggle.setStyle("-fx-background-color: #ff0000;");
            }
        });

        HBox item = new HBox(5);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setStyle("-fx-background-color: #ffffff; -fx-padding: 8;");

        HBox.setHgrow(urlLabel, Priority.ALWAYS);
        urlLabel.setMaxWidth(Double.MAX_VALUE);

        item.getChildren().addAll(urlLabel, toggle);
        listContainer.getChildren().add(item);
    }

    // 加入初始軟體
    private void addCheckBox(String name) {
        
        CheckBox checkBox = new CheckBox(name);
        checkBox.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-padding: 8 12 8 12; -fx-background-radius: 10; -fx-text-fill: black;");
        listContainer2.getChildren().add(checkBox);
    }

    // 加入url
    @FXML
    private void urlAdd() {

        String url = urlField.getText();

        if (url.isEmpty()) return; // 避免空字串
        addWebsite(url, true);

        // 清空輸入框
        urlField.clear();
    }

    // 讀取user端自動加入
    /*private void loadLocalExe() {

        File folder = new File("C:\\Program Files"); // 讀取user端
        File[] files = folder.listFiles();

        if (files == null) return;

        for (File file : files) {

            if (file.isDirectory()) {
                String name = file.getName();
                addCheckBox(name + ".exe");
            }
        }
    }*/
    
    // 加入軟體
    @FXML
    private void exeAdd() {

        String exe = exeField.getText();

        if (exe.isEmpty()) return;  
        addCheckBox(exe);
        exeField.clear();
    }

    // 儲存封鎖設定
    @FXML
    private void handleSave() {

        BlockData data = new BlockData();

        // 網址
        for (Node node : listContainer.getChildren()) {
            HBox item = (HBox) node;
            Label label = (Label) item.getChildren().get(0);
            ToggleButton toggle = (ToggleButton) item.getChildren().get(1);
            
            // data加入label和toggle
            data.getBlockedWebsites().add(
                    new BlockItem(
                            label.getText(),
                            toggle.isSelected()
                    )
            );
        }

        // 軟體
        for (Node node : listContainer2.getChildren()) {
            CheckBox checkBox = (CheckBox) node;
            data.getBlockedApps().add(
                    new BlockItem(
                            checkBox.getText(),
                            checkBox.isSelected()
                    )
            );
        }
        
        // data放入JsonManager儲存成Json
        JsonManager.save(data);

    }
}
