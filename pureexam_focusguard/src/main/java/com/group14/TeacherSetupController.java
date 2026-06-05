package com.group14;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.Parent;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;

public class TeacherSetupController {

    @FXML private VBox blockContainer;
    @FXML private TextField timeField;
    
    private BlockController blockController; 

    @FXML
    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/block.fxml"));
            Node blockNode = loader.load();
            blockController = loader.getController();
            blockContainer.getChildren().add(blockNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExport() {
        // 驗證時間
        String timeStr = timeField.getText().trim();
        if (timeStr.isEmpty() || Integer.parseInt(timeStr) < 1) {
            System.out.println("時間格式錯誤！");
            return;
        }

        List<String> activeUrls = new ArrayList<>();
        List<String> activeApps = new ArrayList<>();

        // 掃描網址
        VBox urlContainer = blockController.getListContainer();
        for (Node node : urlContainer.getChildren()) {
            HBox item = (HBox) node;
            Label label = (Label) item.getChildren().get(0);
            ToggleButton toggle = (ToggleButton) item.getChildren().get(1);
            
            // 切換到 "ON" ，算入黑名單
            if (toggle.isSelected()) {
                activeUrls.add("\"" + label.getText() + "\"");
            }
        }

        // 掃描軟體
        FlowPane appContainer = blockController.getListContainer2();
        for (Node node : appContainer.getChildren()) {
            CheckBox checkBox = (CheckBox) node;
            
            // 有打勾的，才算入黑名單
            if (checkBox.isSelected()) {
                activeApps.add("\"" + checkBox.getText() + "\"");
            }
        }

        // 打包成 JSON 設定格式
        String jsonStr = String.format(
            "{\"time\": %s, \"urls\": %s, \"apps\": %s}", 
            timeStr, activeUrls.toString(), activeApps.toString()
        );

        // 加密成 .oasis 檔案，用 Base64 加密
        String encryptedData = Base64.getEncoder().encodeToString(jsonStr.getBytes(StandardCharsets.UTF_8));

        // 存檔彈窗
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("儲存考試設定檔 (.oasis)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Oasis Files (*.oasis)", "*.oasis"));
        
        File file = fileChooser.showSaveDialog(timeField.getScene().getWindow());
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(encryptedData);
                System.out.println("成功匯出考試設定檔！");
                Parent homeView = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
                timeField.getScene().setRoot(homeView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}