package com.group14;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

//臨時的測試用
//注意：測試的時候不要每次都搜尋一樣的東西，會被鎖（我不是機器人...）
public class BrowserTestApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("測試瀏覽器");

        //建立實體
        FocusBrowser browser = new FocusBrowser();

        //呼叫 getView() 取得畫面，塞進 Scene 裡
        Scene scene = new Scene(browser.getView(), 960, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}