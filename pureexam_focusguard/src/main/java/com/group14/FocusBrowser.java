package com.group14;

//import javafx.application.Application;
//import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.scene.control.Button;
//import javafx.stage.Stage;
import javafx.concurrent.Worker;

public class FocusBrowser/* extends Application */{
    /*public static void main(String[] args) {
        launch(args);
    }*/

    //public void start(Stage primaryStage) {
    public VBox getView() {
        //primaryStage.setTitle("Google");

        WebView webView = new WebView();

        UrlInterceptor urlInterceptor = new UrlInterceptor();

        WebEngine webEngine = webView.getEngine();

        webEngine.locationProperty().addListener((obs, oldUrl, newUrl) -> {
            if(newUrl != null && !urlInterceptor.isSafeUrl(newUrl)){
                javafx.application.Platform.runLater(() -> {
                    webEngine.load(urlInterceptor.getRedirectUrl());
                });
            }
        });

        webEngine.load("https://www.google.com"); 
        WebHistory history = webEngine.getHistory();
        
        Button back = new Button("<-");
        Button next = new Button("->");

        back.setOnAction(event -> {
            if(history.getCurrentIndex() > 0) {
                history.go(-1);
            }
        });

        next.setOnAction(event -> {
            if(history.getCurrentIndex() < history.getEntries().size() - 1) {
                history.go(1);
            }
        });

        HBox hBox = new HBox(back, next);

        webView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            //System.out.println("開始載入網頁");
            // 必須等網頁 "完全跑完" 才能抓到元素
            if (newState == Worker.State.SUCCEEDED) {
                //System.out.println("載入網頁完成");
                String jsCode = """
                    function crushAIElements() {
                        //刪除「AI 模式」按鈕
                        let allElems = document.querySelectorAll('*');
                        for(let i=0; i<allElems.length; i++) {
                            if(allElems[i].innerText === 'AI 模式') {
                                //從文字往上抓最接近的「可點擊/按鈕容器」
                                let target = allElems[i].closest('a, button, [role="button"], [role="link"], [jsname]');
                                
                                // 如果沒抓到特徵外框,往上找2層
                                if(!target) {
                                    target = allElems[i];
                                    if(target.parentElement) target = target.parentElement;
                                    if(target.parentElement) target = target.parentElement;
                                }
                                
                                //拔掉
                                target.remove();
                                break; 
                            }
                        }
                        
                        //刪除「AI 摘要」大區塊
                        let divs = document.querySelectorAll('div');
                        for(let i=0; i<divs.length; i++) {
                            if(divs[i].innerText === 'AI 摘要') {
                                let target = divs[i];
                                //整包拆掉(實測大概8層)
                                for(let j=0; j<8; j++) {
                                    if(target.parentElement) target = target.parentElement;
                                }
                                target.remove(); //直接拔掉(不用display: none)
                                break;
                            }
                        }
                    }

                    //載入網頁跑一次
                    crushAIElements();

                    //google有更動,就再掃一次
                    const observer = new MutationObserver(() => crushAIElements());
                    observer.observe(document.body, { childList: true, subtree: true });
                    """;


                // 呼叫 WebEngine 執行
                webView.getEngine().executeScript(jsCode);
            }
        });

        VBox vBox = new VBox(hBox, webView);
        VBox.setVgrow(webView, Priority.ALWAYS);
        //Scene scene = new Scene(vBox, 960, 600);

        //primaryStage.setScene(scene);
        //primaryStage.show();
        return vBox;
    }
}
