package com.group14;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.web.WebEngine;

import java.io.IOException;

public class BrowserController {

    @FXML private TabPane tabPane;

    private int tabCount = 1;

    @FXML
    public void initialize() {
        addNewTabAndGetEngine();
    }

    @FXML
    public WebEngine addNewTabAndGetEngine() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/browserTab.fxml"));
            Node tabContent = loader.load();

            BrowserTabController tabController = loader.getController();

            Tab newTab = new Tab("新分頁 " + tabCount++);
            newTab.setContent(tabContent);

            //讓tabController可以改標題
            tabController.setParentTab(newTab, this);

            tabPane.getTabs().add(newTab);
            tabPane.getSelectionModel().select(newTab);

            return tabController.getWebEngine();
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        
    }
}