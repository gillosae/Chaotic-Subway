package com.example.chaoticsubway;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.caverock.androidsvg.SVGImageView;
import com.example.chaoticsubway.main.TrainInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static BufferedReader br = null;
    private LocalTime localTime;

    private List<PointF> pointAs = new ArrayList<PointF>();
    private List<PointF> pointBs = new ArrayList<PointF>();
    private PointF pointA = new PointF(100, 100);
    private PointF pointB = new PointF(1000, 1000);

    private Station startStation;
    private Station endStation;
    private int startHour; //LocalTime.of(startHour, startMinute)
    private int startMinute;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getStationInfo();

        //Toolbar
        Toolbar myToolbar = (Toolbar)findViewById(R.id.start_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //FullScrollView
        FullScrollView scrollView = (FullScrollView)findViewById(R.id.scrollView);
        scrollView.scrollTo(1000, 0);

        //SVGImageView
        SVGImageView svgImageView = (SVGImageView)findViewById(R.id.map);
        svgImageView.setImageAsset("subway_map_small.svg");
        svgImageView.setMinimumWidth(10000);
        svgImageView.setMinimumHeight(10000);
//        svgImageView.setCSS("g text:hover{color:red}");

        //AutoCompleteTextView
        final AutoCompleteTextView depView = (AutoCompleteTextView) findViewById(R.id.start_station);
        final AutoCompleteTextView desView = (AutoCompleteTextView) findViewById(R.id.end_station);
        depView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,  STATIONS ));
        desView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,  STATIONS ));

        //TimeSet Button
        Button timeButton = (Button)findViewById(R.id.btn_time);
        timeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_TIME);
            }
        });

        //Search Button (For intent)
        Button searchButton = (Button)findViewById(R.id.search);
        searchButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Both start and end stations should be node station
//                startStation = allStations.get(depView.getText().toString());
//                endStation = allStations.get(desView.getText().toString());
//                if(!startStation.isNode()) nodeStations.put(startStation.getStationName(), startStation);
//                if(!endStation.isNode()) nodeStations.put(endStation.getStationName(), endStation);
//                beginSearch();

                String startStation = "";
                String endStation = "";

                if(depView.getText().toString().equals(""))
                    startStation = "학동";
                else
                    startStation = depView.getText().toString();

                if(desView.getText().toString().equals(""))
                    endStation = "선릉";
                else
                    endStation = desView.getText().toString();

                int dayCategory = 1; //평일
                LocalDateTime startTime = LocalDateTime.of(2020,6,22,7,20);

//                RouteSearcher rs = null;
//                try {
//                    rs = new RouteSearcher(getApplicationContext(), startTime);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    rs.RouteSearch(startStation, endStation, startTime, new ArrayList<Node>());
//                    System.out.println(rs.successPath.get(0));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                finally{
//                    System.out.println("성공한 길찾기 경로 개수는 " + rs.successPath.size());
//                }
//
                Intent resultIntent = new Intent(v.getContext(), ResultActivity.class);
                startActivity(resultIntent);
                depView.setText("");
                desView.setText("");

            }
        });

    }
    static List<String> STATIONS = new ArrayList<>();
    List <TrainInfo> result;
    //AssetManager assets = getApplicationContext().getAssets();
    static String station_name;
    static String day_num;//요일 코드
    static String time;//현재 시간
    static ArrayList<String> des_line = new ArrayList<>();//도착역 호선
    static String destination;//도착역 이름
    static ArrayList<String> line_num = new ArrayList<>();//출발역 호선 (환승역일 수 있으므로)
    static String cur_station;//출발역 이름
    static ArrayList<String> train_num = new ArrayList<>();//열차 번호 리스트로
    static ArrayList<String> station_codes = new ArrayList<>();//출발역 코드
    static ArrayList<String> des_codes = new ArrayList<>();//도착역 코드
    static ArrayList<String> dep_time = new ArrayList<>();
    static ArrayList<String> towards = new ArrayList<>();
    //static ArrayList<ArrayList<String>> result = new ArrayList<>();
    static List<TrainInfo> arr = new ArrayList<TrainInfo>();
    static InputStream input;
    static String set_time;

    public String getCurrentTime() {
        Calendar cur_time = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(cur_time.getTime());
    }

    final int DIALOG_TIME = 2;
    protected Dialog onCreateDialog(int id){ //버튼 클릭시 시간 선택화면 등장 (디폴트는 현재시간, 사용자 지정 가능)
        if(id==DIALOG_TIME){
            TimePickerDialog tpd = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    set_time = Integer.toString(hourOfDay) + ":"+minute + ":00";
                    Toast.makeText(getApplicationContext(), hourOfDay +"시 " + minute+"분 을 선택했습니다", Toast.LENGTH_SHORT).show();
                    startHour = hourOfDay;
                    startMinute = minute;
                } // 값설정시 호출될 리스너 등록
            }, Integer.valueOf(getCurrentTime().substring(0,2)),Integer.valueOf(getCurrentTime().substring(3,5)), false); // 기본값 시분 등록
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    public void getStationInfo(){
        try {
            getStationList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Station> nodeStations = new HashMap<String, Station>(); //Transfer stations
    public static Map<String, Station> allStations = new HashMap<String, Station>(); //Station name, Station class

    private void getStationList() throws IOException { //Get Station from csv
        STATIONS = new ArrayList<String>();
        int n = 0;
        for(int i = 1; i < 10; i++){
            String path = "orderedstations.csv";
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open(path)));
            String line;
            while((line = br.readLine()) != null){
                String[] arr = line.split(",");
                if(allStations.containsKey(arr[1])){
                    allStations.get(arr[1]).setLine(Integer.parseInt(arr[0]));
                    for(int j=3; j<arr.length; j++){
                        allStations.get(arr[1]).setAdjacentStation(new Pair(arr[j], Integer.parseInt(arr[0])));
                    }
                    if(!nodeStations.containsKey(arr[1])){
                        nodeStations.put(arr[1], allStations.get(arr[1]));
                    }
                }else{
                    allStations.put(arr[1], new Station(arr[1], Integer.parseInt(arr[0]), Integer.parseInt(arr[2])));
                    for(int j=3; j<arr.length; j++){
                        allStations.get(arr[1]).setAdjacentStation(new Pair(arr[j], Integer.parseInt(arr[0])));
                    }
                    int same = 0;
                    for(int a=0; a < STATIONS.size(); a++){
                        if(STATIONS.get(a).equals(arr[1])) same++;
                        else continue;
                    }
                    if(same == 0){
                        STATIONS.add(arr[1]);
                    }
                }
            }
        }
    }

    int timecount = 0;

    public void beginSearch(List<String> passedStations, String currentStation, String endStation, int line, int time) {
        if (allStations.get(currentStation).time > timecount) {
            allStations.get(currentStation).time = timecount;
            passedStations.add(currentStation);
            System.out.println("///" + currentStation + line);
            System.out.println("//////////////////////" + passedStations.size());
        } else {
            System.out.println("Depre");
            return; //Deprecate
        }

        if (currentStation.equals(endStation)) {
            System.out.println("Finish" + currentStation);
            System.out.println(time);
            return; //Finish
        }

        for (int k = 0; k < allStations.get(currentStation).adjacentStation.size(); k++) {
            String adjName = allStations.get(currentStation).adjacentStation.get(k).first;
            Integer adjLine = allStations.get(currentStation).adjacentStation.get(k).second;

            int transTime = 4;
            if (allStations.get(adjName).getLine().contains(line)) {
                transTime = 0;
            }
            //List<String> passedStations, String currentStation, String endStation, int line, int time

            List<String> copiedList = new ArrayList<String>();
            copiedList.addAll(passedStations);

            beginSearch(copiedList, adjName.toString(), endStation.toString(), adjLine, (3 + time + transTime));
        }
    }
}
