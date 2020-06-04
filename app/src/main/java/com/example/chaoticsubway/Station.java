package com.example.chaoticsubway;

import android.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Station {
    private String stationName; //이름
    private long id; //아이디(역번호)
    private List<Integer> line = new ArrayList<Integer>(); //호선

    List<Pair<String, Integer>> adjacentStation = new ArrayList<Pair<String, Integer>>(); //인접역, 인접역까지의 운행시간

    public Station(String stationName, int line /*long id,*/ /* Pair<String, Integer> adj*/) {
        System.out.println(line);
        this.stationName = stationName;
        this.line.add(line);
//        this.id = id;
//        adjacentStation.add(adj);
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public void setAdjacentStation(List<Pair<String, Integer>> adjacentStation) {
        this.adjacentStation = adjacentStation;
    }

    @Override
    public String toString(){
        final StringBuffer sb = new StringBuffer("Station{");
        sb.append("name='").append(stationName).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append(", line='").append(line).append('\'');
        sb.append(", isNode='").append(isNode()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}