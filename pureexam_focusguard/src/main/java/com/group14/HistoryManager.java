package com.group14;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

//統計攔截次數
public class HistoryManager {

    public static List<ViolationRecord> countViolations(List<Record> records) {

        Map<String,Integer> countMap = new HashMap<>();

        for (Record record : records) {

            String target = record.getTarget();

            countMap.put(
                    target,
                    countMap.getOrDefault(target,0)
                    + 1
            );
        }

        List<ViolationRecord> result = new ArrayList<>();

        for (Map.Entry<String,Integer> entry
                : countMap.entrySet()) {

            result.add(
                    new ViolationRecord(
                            entry.getKey(),
                            entry.getValue()
                    )
            );
        }

        return result;
    }

    public static List<FocusRecord> countFocusTime(List<StudySession> sessions) {

        Map<String,Integer> focusMap = new LinkedHashMap<>();

        LocalDate today = LocalDate.now();
        DateTimeFormatter labelFormatter = DateTimeFormatter.ofPattern("E", Locale.ENGLISH);

        // 建立最近7天
        for (int i = 6; i >= 0; i--) {

            String dateLabel = today.minusDays(i).format(labelFormatter);
            focusMap.put(dateLabel, 0);
        }

        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 統計資料
        for (StudySession session : sessions) {

            LocalDate date = LocalDateTime.parse(session.getTimestamp(), parser).toLocalDate();

            // 超過7天直接忽略
            if (date.isBefore(today.minusDays(6)) || date.isAfter(today)) {
            continue;
            }

            String dateLabel = date.format(labelFormatter);

            focusMap.put(
                dateLabel,
                focusMap.get(dateLabel)
                + session.getDurationMinutes()
            );
        }

        List<FocusRecord> result = new ArrayList<>();

        for (Map.Entry<String,Integer> entry : focusMap.entrySet()) {

            result.add(new FocusRecord(entry.getKey(), entry.getValue()));
        }

        return result;
    }
}