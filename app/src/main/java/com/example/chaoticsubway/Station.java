package com.example.chaoticsubway;

import android.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Station {
    private String stationName; //이름
//    private int id; //아이디(역번호)
    private List<Integer> line = new ArrayList<Integer>(); //호선

    public int time = Integer.MAX_VALUE;

    public List<Pair<String, Integer>> adjacentStation = new ArrayList<Pair<String, Integer>>(); //인접역, 호선


    public Station(String stationName, List<Integer> line) {
        System.out.println(line);
        this.stationName = stationName;
        this.line.addAll(line);
//        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public List<Integer> getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line.add(line);
    }

    public boolean isNode() { //Whether the station is used for route search
        return (adjacentStation.size() == 1) || (adjacentStation.size() > 2); //Terminal stations, Fork stations
    }

    public List<Pair<String, Integer>> getAdjacentStation() {
        return adjacentStation;
    }

    public void setAdjacentStation(Pair<String, Integer> adjacentStation) {
        this.adjacentStation.add(adjacentStation);
//        System.out.println(getStationName() + adjacentStation.second + adjacentStation.first);
    }

    @Override
    public String toString(){
        final StringBuffer sb = new StringBuffer("Station{");
        sb.append("name='").append(stationName).append('\'');
//        sb.append(", id='").append(id).append('\'');
        sb.append(", line='").append(line).append('\'');
//        sb.append(", isNode='").append(isNode()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}