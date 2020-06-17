package com.example.chaoticsubway.main;

public class TrainInfo {
    String TRAIN_CODE;
    String LINE_NUM;
    String TIME;
    String TOWARDS;
    public TrainInfo(String code,  String time, String des){
        this.TRAIN_CODE = code;
        this.TIME = time;
        this.TOWARDS = des;
    }
}
