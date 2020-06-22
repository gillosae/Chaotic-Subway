package com.example.chaoticsubway;

import android.content.Context;
import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RouteSearcher {
    private Map<String, Station> stations = new HashMap<String, Station>(); //Station name, Station class
    private String stationInfoPath = "orderedstations.csv";
    private Context context;

    private LocalDateTime dateTime;

    RouteSearcher(Context context, LocalDateTime dateTime) throws IOException {
        this.context = context;
        this.dateTime = dateTime;
        initializeStationList();
    }

    private void initializeStationList() throws IOException { //Get Station from csv
        for(int i = 1; i < 10; i++){
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(stationInfoPath)));
            String line;
            while((line = br.readLine()) != null){
                String[] arr = line.split(",");
                if(stations.containsKey(arr[1])){
                    if(!stations.get(arr[1]).getLine().contains(Integer.parseInt(arr[0])))
                        stations.get(arr[1]).setLine(Integer.parseInt(arr[0]));
                    for(int j=3; j<arr.length; j++){
                        stations.get(arr[1]).setAdjacentStation(new Pair(arr[j], Integer.parseInt(arr[0])));
                    }
                }else{
                    stations.put(arr[1], new Station(arr[1], Integer.parseInt(arr[0]), Integer.parseInt(arr[2])));
                    for(int j=3; j<arr.length; j++){
                        stations.get(arr[1]).setAdjacentStation(new Pair(arr[j], Integer.parseInt(arr[0])));
                    }
                }
            }
        }
    }

    //결과 루트를 저장하는 리스트가 있어야한다. 시간과 역을. 열차번호와 무슨행인지 열차정보도.
    public ArrayList<ArrayList<Node>> successPath = new ArrayList<ArrayList<Node>>();

//    private ArrayList<Station> passedStations = new ArrayList<Station>();

//    private String startStation = "온수";
//    private String endStation = "과천";
//    private LocalTime startTime = LocalTime.of(7, 30);
    private int dayCategory = 1; //평일
    private int transferMinute = 4;//환승시간

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

    public void RouteSearch(String startStation, String endStation, LocalDateTime startTime, ArrayList<Node> passedStations) throws IOException {
        System.out.println("/// Route Search : "+ startStation + " -> " + endStation + " " + startTime.toLocalTime());
        String currentStation = startStation;

        for(int i=0; i<stations.get(startStation).getLine().size(); i++){ //해당 역에서 탈 수 있는 호선 전부 돌기
            for(int sh=1; sh<=2; sh++){ //상선 하선 모두 돌기
                int currentLine = stations.get(startStation).getLine().get(i);
                System.out.println(currentLine + "호선 " + ((sh == 1)? "상선" : "하선"));

                BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("TrainNo/"+currentLine + "_" + dayCategory+"_" + sh +".csv")));
                String line;

                //startStation의 컬럼 인덱스 찾기
                int columnOfStation = 0;
                String firstLine = br.readLine();
                String[] firstArr = firstLine.split(",");
                for(int j=0; j<firstArr.length; j++){
                    if(startStation.equals(firstArr[j])){
                        columnOfStation = j;
                        break;
                    }
                }

                //둘째줄부터 쭉 훑어가며 startStation 컬럼이 최초로 현재시간에 근접한 로우(시간) 찾기
                while((line = br.readLine()) != null) {
                    String[] arr = line.split(",");
                    if(arr.length <= columnOfStation) continue; //뒤에 싹 빈 콤마는 무시하는듯.
                    if(arr[columnOfStation].equals("") || strToTime(arr[columnOfStation])==null) continue; //null이거나 첫줄이라 데이트타임포맷이 아닌 경우에 대한 예외처리

                    if(strToTime(arr[columnOfStation]).isAfter(startTime)){ //근접한 시간을 찾았다면 (가장 빨리 오는 열차의 열과 행을 찾았다면)
                        int k = columnOfStation + ((sh == 1 ) ? -1 : 1);
                        while(k >= 1 || k <= arr.length - 1) { //찾은 줄에서 columnOfStation 위치부터 하나하나 인덱스 옮겨가며 역과 시간 확인
                            if (arr[k] == null || strToTime(arr[k]) == null)  //컬럼이 빈칸이거나 숫자타입이 아니라면
                                continue; //갈 수 없으므로 컨티뉴. 지선으로 연결될 수도 있으므로 반복문을 빠져나가지 않고 검색은 계속 해준다
                            if (passedStations.contains(firstArr[k])/* && !(arr[k].equals(""))*/) //보고있는 컬럼의 역이 이미 지나친 역인데다 현재 탄 열차가 그 역을 거쳐가면
                                break; //이 경로는 아예 의미가 없으므로 반복문 탈출

                            currentStation = firstArr[k]; //현재역을 보고있는 컬럼으로 지정 후
                            passedStations.add(new Node(currentStation, strToTime(arr[k]))); //passedStation에 저장.
                            System.out.println(currentStation);

                            if (currentStation.equals(endStation)) { //이때 currentStation과 endStation이 같으면 길찾기 성공
                                System.out.println("//////////////////// 길찾기 성공");
                                successPath.add(passedStations);
                                System.out.println(passedStations);
                                break; //이 반복문을 탈출하여 새로운 경로를 탐색한다
                            }

                            if (stations.get(currentStation).getLine().size() > 1) { //현재 역이 환승역이라면
                                System.out.println(passedStations);
                                System.out.println(stations.get(currentStation));
                                ArrayList<Node> copiedStations = new ArrayList<Node>();
                                for(int f=0; f<passedStations.size(); f++){
                                    copiedStations.add(new Node(passedStations.get(f).getStation(), passedStations.get(f).getTime()));
                                }
                                RouteSearch(currentStation, endStation, strToTime(arr[k])/*.plusMinutes(transferMinute)*/, copiedStations); //환승기회 제공
                            }

                            if(sh == 1) k--; //상선일 경우 인덱스 하나 낮추기
                            else k++; //하선일 경우 인덱스 하나 올리기
                        }
                    }
                }
            }
        }
    }
}