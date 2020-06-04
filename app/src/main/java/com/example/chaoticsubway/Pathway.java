package com.example.chaoticsubway;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

public class Pathway { //환승이 없는 일자경로
    private List<Station> stations = new ArrayList<Station>(); //경로상의 모든 역
    private List<LocalTime> localTimes = new ArrayList<LocalTime>(); //경로상 역마다 도착시간 (첫 역은 출발시간)
    //묶을까?

    public Pathway(Station depStation, Station arrStation) { //첫역(노드)과 마지막역(노드)만 받아와서 생성
        stations.add(depStation);
        stations.add(arrStation);

        //Get Full station list


//        this.stations = stations;
//        this.localTimes = localTimes;
//        System.out.println(arrStation.toString());
//        System.out.println(depStation.toString());
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public List<LocalTime> getLocalTimes() {
        return localTimes;
    }

    public void setLocalTimes(List<LocalTime> localTimes) {
        this.localTimes = localTimes;
    }

    public Station getDepStation(){ //Get departure station
        return stations.get(0);
    }

    public Station getArrStation(){ //Get arrival station
        return stations.get(stations.size() - 1); //추후수정
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDepTime(){ //Get departure time
        return LocalTime.now().format(DateTimeFormatter.ofPattern("kk:mm"));
//        return localTimes.get(0).format(DateTimeFormatter.ofPattern("kk:mm"));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getArrTime(){ //Get arrival time
        return LocalTime.now().format(DateTimeFormatter.ofPattern("kk:mm"));
//        return localTimes.get(localTimes.size() - 1).format(DateTimeFormatter.ofPattern("kk:mm"));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Pathway{");
        sb.append("From='").append(getDepStation().getStationName()).append('\'');
        sb.append(", at='").append(getDepTime()).append('\'');
        sb.append(", To='").append(getArrStation().getStationName()).append('\'');
        sb.append(", at='").append(getArrTime()).append('\'');
        sb.append(", Total '").append(stations.size()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}