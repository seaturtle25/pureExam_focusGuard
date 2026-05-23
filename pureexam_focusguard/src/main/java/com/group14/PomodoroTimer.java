package com.group14;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class PomodoroTimer {
    private Timeline timeline;
    private int seconds;
    private static final int DEFAULT_TIME = 25 * 60; // 25分鐘


    public PomodoroTimer() {
        this.seconds = DEFAULT_TIME;
    }
     
    public void start(Label timerLabel) {
        //如果已經在跑了，就不要重複啟動 (防止越跑越快)
        if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
            return;
        }
        //如果是被暫停的，從暫停的地方繼續播放
        if (timeline != null && timeline.getStatus() == Animation.Status.PAUSED) {
            timeline.play();
            return;
        }

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            seconds--;
            updateLabel(timerLabel); //更新畫面
            
            if (seconds <= 0) {
                timeline.stop();
                System.out.println("⏰ 專注結束！");
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.playFromStart();
    }
    public void pause(){
        if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
            timeline.pause();
            System.out.println("計時已暫停");
        }
    }
    public void reset(Label timerLabel){
        if(timeline != null) {
            timeline.stop();
        }
        this.seconds = DEFAULT_TIME;
        updateLabel(timerLabel); // 重置時也要更新一次畫面
        System.out.println("計時器已重置");
    }

    private void updateLabel(Label timerLabel) {
        int min = seconds / 60;
        int sec = seconds % 60;
        timerLabel.setText(String.format("%02d:%02d", min, sec));
    }
}
