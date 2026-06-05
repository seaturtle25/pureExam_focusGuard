package com.group14;

import java.util.ArrayList;
import java.util.List;

//紀錄統整Focus、Violation Record，方便HistoryController.java、history.fxml使用
public class HistoryData {

    private List<FocusRecord> focusRecords = new ArrayList<>();

    private List<ViolationRecord> violations = new ArrayList<>();

    public List<FocusRecord> getFocusRecords() {
        return focusRecords;
    }

    public List<ViolationRecord> getViolations() {
        return violations;
    }
}
