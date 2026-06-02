package com.group14;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//臨時的測試用
//注意：測試的時候不要每次都搜尋一樣的東西，會被鎖（我不是機器人...）
public class BrowserTestApp extends Application {

    public static void main(String[] args) {
        System.setProperty("http.protocols", "HTTP/1.1");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("測試瀏覽器");

        Scene scene = new Scene(loadFXML());

        primaryStage.setScene(scene);
        primaryStage.setWidth(650);
        primaryStage.setHeight(600);
        primaryStage.show();
    }

    private Parent loadFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/browser.fxml"));
        return fxmlLoader.load();
    }
}