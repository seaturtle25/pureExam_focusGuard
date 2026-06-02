package com.group14;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        //番茄鐘
        PomodoroTimer timer = new PomodoroTimer();

        Label titleLabel = new Label("FocusGuard X PureExam");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: #ffffff;");

        //建立顯示計時的 Label(簡易模擬 UI 概念圖的部分)
        Label timerLabel = new Label("25:00");
        timerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        timerLabel.setStyle("-fx-text-fill: #3e3eb4ff; -fx-background-color: #ffffff; -fx-padding: 2 10; -fx-background-radius: 5;");

        Label statusLabel = new Label("waiting for start...");
        statusLabel.setFont(Font.font("Arial", 14));
        statusLabel.setStyle("-fx-text-fill: #eeeeee;");

        Button startBtn = new Button("開始");
        Button pauseBtn = new Button("暫停");
        Button resetBtn = new Button("重置");
        String btnStyle = "-fx-font-size: 16px; -fx-padding: 8 15; -fx-cursor: hand;";
        startBtn.setStyle(btnStyle);
        pauseBtn.setStyle(btnStyle);
        resetBtn.setStyle(btnStyle);
        startBtn.setOnAction(e -> {
            timer.start(timerLabel);
            statusLabel.setText("狀態：專注模式中...");
        });
        pauseBtn.setOnAction(e -> {
            timer.pause();
            statusLabel.setText("狀態：已暫停");
        });
        resetBtn.setOnAction(e -> {
            timer.reset(timerLabel);
            statusLabel.setText("狀態：等待開始...");
        });
        
        //將 Label 置中
        HBox topBar = new HBox(20, titleLabel, timerLabel, statusLabel, startBtn, pauseBtn, resetBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setStyle("-fx-background-color: #2c3e50;");

        BrowserController browser = new BrowserController();
        
        BorderPane root = new BorderPane();
        root.setTop(topBar); //計時
        root.setCenter(browser.getView()); //瀏覽器

        Scene scene = new Scene(root, 1024, 768);
        stage.setScene(scene);
        stage.setTitle("FocusGuard X PureExam - 系統防護專注模式");

        //視窗置頂
        stage.setAlwaysOnTop(true);
        //禁止縮放視窗大小 (讓學生不能把視窗縮小藏起來)
        stage.setResizable(false);
        //阻止視窗關閉
        stage.setOnCloseRequest(event -> {
            System.out.println("專注模式結束前禁止關閉程式！");
            event.consume(); 
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}