package com.example.chaoticsubway;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.chaoticsubway.MainActivity.context;

public class TimeTable {
    ArrayList<Station> route;
    List<Spot> allSpots;
    List<Spot> simpleSpots;

    LocalDateTime dateTime;

    private String timeTablePath = "TrainNo/";

    TimeTable(ArrayList<Station> simpleRoute, LocalDateTime startTime){
        System.out.println("TimeTable Instance Created");
        route = simpleRoute;
        allSpots = new ArrayList<Spot>();
        simpleSpots = new ArrayList<Spot>();
        this.dateTime = startTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDateTime strToTime(String str){
        if(str.equals("")) return null;
        String[] strArr = str.split(":");

        boolean atleastOneAlpha = str.matches(".*[a-zA-Z]+.*");;
        if(!atleastOneAlpha){
            if(Integer.parseInt(strArr[0]) < 24){
                return LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1]), Integer.parseInt(strArr[2])));
            }else if(Integer.parseInt(strArr[0]) >= 24){
                return LocalDateTime.of(dateTime.toLocalDate().plusDays(1), LocalTime.of(Integer.parseInt(strArr[0])-24, Integer.parseInt(strArr[1]), Integer.parseInt(strArr[2])));
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void TimeTableSearch(LocalDateTime startTime) throws IOException {
        System.out.println("///TimeTable Search : "+ route.get(0).getStationName() + " -> " + route.get(route.size()-1).getStationName() + " " + startTime.toLocalTime());
        for(int i=0; i<route.size(); i++){
            String stationName = route.get(i).getStationName();
            String nextStationName = route.get(i+1).getStationName();

            int lineNum = route.get(i).getLine().get(0);

            int stationColumn = -1;
            int nextStationColumn = -1;

            SearchTable(stationName, stationColumn, nextStationName, nextStationColumn, startTime, lineNum, 1, 0);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SearchTable(String stationName, int stationColumn, String nextStationName, int nextStationColumn, LocalDateTime startTime, final int lineNum, int sanghasun, int routeIndex) throws IOException {
        System.out.println("Searching... " + stationName + " " + stationColumn + " " + nextStationName + " " + nextStationColumn + " " +lineNum);
        BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("TrainNo/"+ lineNum + "_" + "1"  +"_" + sanghasun +".csv")));
        String firstLine = br.readLine();
        String[] firstLineArr = firstLine.split(",");
        for(int j=0; j<firstLineArr.length; j++){
            if(stationName.equals(firstLineArr[j])){
                stationColumn = j;
            }else if(nextStationName.equals(firstLineArr[j])){
                nextStationColumn = j;
            }
        }
        if(stationColumn > nextStationColumn) { //상선이 아니라면 하선으로 갓
            System.out.println("Go to Hasun");
            SearchTable(stationName, stationColumn, nextStationName,  nextStationColumn, startTime, lineNum, (sanghasun == 1) ? 2 : 1, routeIndex);
            return;
        }

        String line;
        while((line = br.readLine())!= null){
            //첫줄 예외처리 필요
            String[] arr = line.split(",");

            if(strToTime(arr[stationColumn]).isAfter(startTime)){
                for(int j=stationColumn; j<nextStationColumn; j++){
//                    allSpots.add(new Spot(new Station(firstLineArr[stationColumn],  new ArrayList<Integer>() {{ add(lineNum); }} ), strToTime(arr[stationColumn])));
                    allSpots.add(new Spot(firstLineArr[j], strToTime(arr[j])));
                    System.out.println(allSpots.get(allSpots.size()-1));
                }
                if(nextStationName != firstLineArr[nextStationColumn]){
                    if(route.get(route.size()-1).getStationName() == nextStationName){
                        System.out.println(allSpots);
                        return;
                    }
                    for (int k=0; k<route.get(routeIndex+1).getLine().size(); k++){
                        if(route.get(routeIndex+1).getLine().get(k) == lineNum){
                            route.get(routeIndex+1).getLine().remove(k);
                        }
                    }
                    SearchTable(nextStationName, 0, route.get(routeIndex+1).getStationName(), 0, strToTime(arr[nextStationColumn]).plusMinutes(2), route.get(routeIndex+1).getLine().get(0), 1, routeIndex+1);
                }
                return;
            }
        }
    }
}