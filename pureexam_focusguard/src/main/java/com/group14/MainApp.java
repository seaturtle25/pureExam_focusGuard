package com.group14;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class MainApp extends Application {
    private static Stage stage;

    @Override
    public void start(@SuppressWarnings("exports") Stage s) throws IOException {
        // 清掉可能殘留的網頁封鎖
        HostsManager.disableFocusMode();

        stage=s;
        setRoot("home" ,"Oasis");
        stage.setWidth(650);
        stage.setHeight(600);   
        stage.show();
    }

    static void setRoot(String fxml, String title) throws IOException {
        Scene scene = new Scene(loadFXML(fxml));
        scene.getStylesheets().add(
            MainApp.class
                .getResource("/css/style.css")
                .toExternalForm()
        );
        stage.setTitle(title);
        stage.setScene(scene);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/"+ fxml + ".fxml"));
        return fxmlLoader.load();
    }

    //攔截視窗右上角的叉叉
    @Override
    public void stop() throws Exception {
        System.out.println("OASIS 系統關閉，清理檔案中...");
        
        HostsManager.disableFocusMode();
        ProcessMonitorTest.stop();

        System.exit(0);
    }
    
    public static void main(String[] args) {
        // 攔截被工作管理員強制終止、當機的情況，以防 host 卡住
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("JVM準備關閉，清理檔案中...");
            HostsManager.disableFocusMode();
            ProcessMonitorTest.stop();
        }));

        launch(args);
    }

}
