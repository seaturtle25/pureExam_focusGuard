package com.group14;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.concurrent.Worker;

public class BrowserTabController {

    @FXML private WebView webView;
    @FXML private ComboBox<String> historyCombo; // 裝歷史紀錄
    @FXML private TextField urlField;

    private WebEngine webEngine;
    private WebHistory history;
    private UrlInterceptor urlInterceptor = new UrlInterceptor();
    private Tab parentTab;
    private BrowserController mainController;
    private boolean isFirstLoad = true; // 紀錄是不是第一次載入

    public void setParentTab(Tab parentTab, BrowserController mainController) {
        this.parentTab = parentTab;
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        webEngine = webView.getEngine();
        history = webEngine.getHistory();

        webEngine.setUserStyleSheetLocation(getClass().getResource("/css/webview.css").toString());

        // 綁定攔截器
        webEngine.locationProperty().addListener((obs, oldUrl, newUrl) -> {
            if(newUrl != null){
                urlField.setText(newUrl);
                if (!urlInterceptor.isSafeUrl(newUrl)) {
                    javafx.application.Platform.runLater(() -> {
                        webEngine.load(urlInterceptor.getRedirectUrl());
                    });
                }
            }
        
        });

        // 注入JS腳本
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            //System.out.println("開始載入網頁");
            // 必須等網頁 "完全跑完" 才能抓到元素
            if (newState == Worker.State.SUCCEEDED) {
                //System.out.println("載入網頁完成");
                if(isFirstLoad) {
                    isFirstLoad = false;
                    webEngine.reload();
                    return;
                }
                String jsCode = """
                    function crushAIElements() {
                        let allElems = document.querySelectorAll('*');
                        for(let i=0; i<allElems.length; i++) {
                            var text = allElems[i].innerText;
                            if(text === 'AI 模式' || text === '相關問題' || text === 'People also ask') {
                                let target = allElems[i].closest('a, button, [role="button"], [role="link"], [jsname]');
                                if(!target) {
                                    target = allElems[i];
                                    if(target.parentElement) target = target.parentElement;
                                    if(target.parentElement) target = target.parentElement;
                                }
                                target.remove();
                                break; 
                            }
                        }
                        let divs = document.querySelectorAll('div');
                        for(let i=0; i<divs.length; i++) {
                            if(divs[i].innerText === 'AI 摘要') {
                                let target = divs[i];
                                for(let j=0; j<8; j++) {
                                    if(target.parentElement) target = target.parentElement;
                                }
                                target.remove();
                                break;
                            }
                        }
                    }
                    crushAIElements();
                    const observer = new MutationObserver(() => crushAIElements());
                    observer.observe(document.body, { childList: true, subtree: true });
                    """;
                // 呼叫 WebEngine 執行
                webEngine.executeScript(jsCode);

                if(parentTab != null) {
                    String title = webEngine.getTitle();
                    if(title != null && !title.isEmpty()) {
                        parentTab.setText(title.length() > 10 ? title.substring(0, 10) + "..." : title);
                    }
                }
                
                // 更新歷史紀錄
                historyCombo.getItems().clear();
                for(WebHistory.Entry entry : history.getEntries()) {
                    String entryTitle = entry.getTitle() != null ? entry.getTitle() : entry.getUrl();
                    historyCombo.getItems().add(entryTitle);
                }
                historyCombo.getSelectionModel().select(history.getCurrentIndex());
            }
        });

        // 處理歷史紀錄選單的點擊跳轉
        historyCombo.setOnAction(event -> {
            int selectedIndex = historyCombo.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex != history.getCurrentIndex()) {
                // 跳轉到指定的歷史紀錄
                history.go(selectedIndex - history.getCurrentIndex());
            }
        });

        webEngine.setCreatePopupHandler(config -> {
            if(mainController != null) {
                WebEngine newEngine = mainController.addNewTabAndGetEngine();
                return newEngine;
            }
            return null;
        });

        // 載入初始網頁
        webEngine.load("https://google.com");
    }

    public WebEngine getWebEngine() {
        return this.webEngine;
    }

    // 按鈕綁定
    @FXML
    public void goBack() {
        if (history.getCurrentIndex() > 0) {
            history.go(-1);
        }
    }

    @FXML
    public void goForward() {
        if (history.getCurrentIndex() < history.getEntries().size() - 1) {
            history.go(1);
        }
    }

    @FXML
    public void reload() {
        webEngine.reload();
    }

    @FXML
    public void handleUrlInput() {
        String input = urlField.getText().trim();
        if(input.isEmpty()) return;

        if(!input.startsWith("http://") && !input.startsWith("https://")) {
            if(!input.contains(".")) {
                input = "https://www.google.com/search?q=" + input;
            } else {
                input = "https://" + input;
            }
        }

        webEngine.load(input);
    }
}