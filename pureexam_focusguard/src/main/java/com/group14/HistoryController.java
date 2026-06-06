package com.group14;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HistoryController {

    @FXML
    private BarChart<String, Number> focusChart;

    @FXML
    private Label totalFocusLabel;

    @FXML
    private Label averageFocusLabel;

    @FXML
    private VBox violationContainer;
    
    private HistoryData historyData = new HistoryData();


    public void initialize() {
            loadChart();
            loadStatistics();
            loadViolations();
    }

    private void loadChart() {
        List<StudySession> sessions = JsonManager.loadStudySessions();
        List<FocusRecord> focusRecords = HistoryManager.countFocusTime(sessions);
        
        historyData.getFocusRecords().addAll(focusRecords);

        //new圖表
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("專注時間(分)");

        for (FocusRecord record : historyData.getFocusRecords()) {

            series.getData().add(
                new XYChart.Data<>(
                    record.getDate(),
                    record.getFocusMinutes()
                )
            );
        }

        focusChart.getData().clear();

        focusChart.getData().add(series);
    }

    private void loadStatistics() {

        int total = 0;
        int average = 0;

        for (FocusRecord record : historyData.getFocusRecords()) 
            total += record.getFocusMinutes();

        if (!historyData.getFocusRecords().isEmpty())
            average = total / historyData.getFocusRecords().size();

        if (total > 60)
            totalFocusLabel.setText("本週專注時間: " + total / 60 + "小時 " + total % 60 + "分鐘");
        else if (total < 60) 
            totalFocusLabel.setText("本週專注時間: " + total + "分鐘");

        if(average > 60)
            averageFocusLabel.setText("平均每日專注時間: " + average / 60  + "小時 " + average % 60 + "分鐘");
        else if (average < 60)
            averageFocusLabel.setText("平均每日專注時間: " + average + "分鐘");
    }

    private void loadViolations() {

        violationContainer.getChildren().clear();

        //讀csv(攔截紀錄)得到List<Record>
        List<Record> records = CsvManager.loadRecords();

        //把List<Record>處理成統計好的List<ViloationRecord>
        List<ViolationRecord> violations = HistoryManager.countViolations(records);

        historyData.getViolations().clear();
        historyData.getViolations().addAll(violations);

        for (ViolationRecord record : historyData.getViolations()) {
            Label label = new Label("• " + record.getName() + " (" + record.getCount() + "次)");
            violationContainer.getChildren().add(label);
        }
    }


}
