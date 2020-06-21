package com.example.chaoticsubway.main;

import android.app.DatePickerDialog;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import com.example.chaoticsubway.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
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
    static int year, month, day, day_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            getStationList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final AutoCompleteTextView dep = (AutoCompleteTextView) findViewById(R.id.start_station);
        final AutoCompleteTextView des = (AutoCompleteTextView) findViewById(R.id.end_station);

        // AutoCompleteTextView 에 아답터를 연결한다.
        dep.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,  STATIONS ));
        des.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,  STATIONS ));

        dep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                station_name = ((TextView)view).getText().toString();
                Toast.makeText(SearchActivity.this, station_name, Toast.LENGTH_SHORT).show();
            }
        });
        des.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destination = ((TextView)view).getText().toString();
                Toast.makeText(SearchActivity.this, destination, Toast.LENGTH_SHORT).show();
            }
        });

        Button b1 = (Button)findViewById(R.id.btn_time);//시간 받아오는 버튼
        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_TIME);
            }
        });
    }


    //역명 모두 가져오기
    private void getStationList() throws IOException {
        STATIONS = new ArrayList<String>();
        int n=0;
        for(int i=1;i<10;i++){
            String path = "stations/line_" + i +".csv";
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open(path)));
            String line;
            while((line=br.readLine())!=null){
                String[] arr = line.split(",");
                if(arr[0].equals("Line")){
                    continue;
                }else{
                    int same =0;
                    for(int a=0;a<STATIONS.size();a++){
                        if(STATIONS.get(a).equals(arr[1])){
                            same++;
                        }else{
                            continue;
                        }
                    }
                    if(same==0){
                        STATIONS.add(arr[1]);
                    }
                    //System.out.println("station:"+STATIONS.get(n));
                    n++;
                }
            }
        }
    }
//얘만 다시 손보기
    public void getDateNum(int y, int m, int d) {

        int total_days = 0, weekday;

        total_days += (y - 1900) * 365;
        total_days += (y - 1900) / 4;

        if ((((y % 4 == 0) && (y % 100 != 0) || (y % 400 == 0)) && (m == 1 || m == 2)) == true) {
            total_days--;
        }
        if (m >= 3) {
            total_days += 28 + 31 + 31 * ((m / 2) - 1) + 30 * (Math.round((double) m / 2) - 2) + d;
        } else if (m == 1) {
            total_days += d;
        } else if (m == 2) {
            total_days += 31 + d;
        }
        if (m >= 9) {
            total_days++;
        }
        weekday = total_days % 7;
        System.out.println(weekday);
        int num;
        switch (weekday) {
            case 6:
                num = 02;
                break;
            case 0:
                num = 03;
                break;
            default:
                num = 01;

        }
        day_num = Integer.toString(num);
        System.out.println(day_num);
    }
    //요일 코드
    public void day(){
        Calendar cal = Calendar.getInstance();
        //default 값
        year = cal.get(Calendar.YEAR);
        month =  cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DATE);
        day_code = cal.get(Calendar.DAY_OF_WEEK);
        System.out.println("현재시간:"+year+"년"+month+"월"+day+"일");

    }

    //출발역 - 인코딩 문제 나중에 다시 확인
    public void curStation(String station) throws IOException {

        String cur =station;//검색으로 받아오기;

        cur_station = cur;
        //System.out.println(cur_station);
        getCode(cur_station, line_num, station_codes);
    }

    //도착역
    private void desStation(String station) throws IOException {
        //System.out.println(station);
        getCode(station, des_line, des_codes);
    }

    //현재시간 - 동작 확인 완료
    public String time() {
        Calendar cur_time = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(cur_time.getTime());
    }


    //역명에 따라 역 코드 받아오기 -완료
    public void getCode(String station, ArrayList<String> add_line, ArrayList<String> codes) throws IOException {
        System.out.println(station);
        String row;
        String[] info;
        String line;
        for(int i=1;i<10;i++){
            String filename = "stations/line_"+i+".csv";
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open(filename)));
            while((row=br.readLine())!=null){
                info = row.split(",");
                if(info[1].equals(station)){
                    System.out.println(info[3]);
                    codes.add(info[3]);
                    line = Integer.toString(i);
                    add_line.add(line);
                }else{
                    continue;
                }
            }
        }
    }

    //시간표의 총 길이 가져오기
    public int getLen(String path) throws IOException {
        BufferedReader len = new BufferedReader(new InputStreamReader(getAssets().open(path)));
        String get_len = len.readLine();
        String[] arr = get_len.split(",");
        int n = arr.length;
        // System.out.println("arr_len:" + n);
        return n;
    }

    //시간 초단위 계산해서 가져오기
    public static int getSum(String a){
        String[] a_hr = a.split(":");
        int a_sum = (Integer.valueOf(a_hr[0])-12)*3600+ Integer.valueOf(a_hr[1])*60 + Integer.valueOf(a_hr[2]);
        //System.out.println(a_sum);
        return a_sum;
    }

    public int calculate(int a) {
        int time_diff;
        int a_time = getSum(time);
        time_diff = a_time - a;
        return time_diff;
    }

    //가장 가까운 열차 확인할 수 있는 인덱스값 가져오기
    public int getTrainNum(ArrayList<Integer> time, int n) {
        int index=0;
        int b, c;
        for (int i = 1; i < n - 1; i++) {
            b = time.get(i);
            c = time.get(i+1);

            if (b> 0  && c < 0) {
                index = (i + 2);//좀더 명확하고 상세한 계산을 통해 열차 추천 필요
                break;
            }
        }
        return index;
    }

    public void getCloseTrain(String[] schedule) throws IOException {
        int closest_index;
        String train;
        int index = (schedule.length-4)/3;

        ArrayList<String> time = new ArrayList<>();
        for(int j=0;j<index;j++){
            String t = schedule[j*3+5];
            if(t.equals(" ")){
                break;
            }
            time.add(t);
        }

        ArrayList<Integer> time_d = new ArrayList<>();
        for(int i=0;i<time.size();i++){
            time_d.add(calculate(getSum(time.get(i))));
           // System.out.println(time_d.get(i));
        }
        closest_index = getTrainNum(time_d, time_d.size());
       // System.out.println(closest_index);
        train = schedule[(closest_index-1)*3+4];
        train_num.add(train);
        towards.add(schedule[(closest_index-1)*3+6]);//방향정보 넣기
        dep_time.add(schedule[(closest_index-1)*3+5]);//시간정보 넣기
    }

    //line 정해서 내부정보 싹다 파싱해서 가장 가까운 시간대 열차번호 가져오기
    public void closestTrain(String line, int n) throws IOException {

        String[] up = getInfo(line, "1", n);
        String[] down = getInfo(line, "2", n);
        getCloseTrain(up);//상행
        getCloseTrain(down);//하행
    }

    public String[] getInfo(String line, String i, int n) throws IOException {
        String filename = "trainSchedule/"+line+"_"+day_num+"_"+i+".csv";
        //String filename = line+"_"+day_num+"_"+i+".csv";
        BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open(filename)));
        String schedule;
        String[] train = new String[getLen(filename)];
        //해당 역의 시간표 모두
        while((schedule=br.readLine())!=null){
            train = schedule.split(",");
            if(train[3].equals(station_codes.get(n))){
                break;
            }
        }
        return train;
    }

    // 필수 정보들 받아오기
    void getParams(String station) throws IOException {
        curStation(station);
        desStation(destination);
        time = time();
        if(!set_time.equals(time)){
            time = set_time; }
        //day();
    }

    //이 모든것을 실행하고 받은 시간표에 따라 리스트로 정리
    public void info(String station) throws IOException {
        getParams(station);//시간과 날짜변수 받아오기
        //System.out.println("a");
        //출발역에서 갈 수 있는 방향의 열차 코드 모두 받아오기
        for(int i=0;i<line_num.size();i++){
            closestTrain(line_num.get(i), i);
        }

        int num_of_trains = dep_time.size();

        for(int i=0;i<num_of_trains;i++){
            System.out.println("시간: "+dep_time.get(i));
            System.out.println("열차번호: "+train_num.get(i));
            System.out.println("종착지: "+towards.get(i));
            arr.add(new TrainInfo(train_num.get(i),dep_time.get(i), towards.get(i)));
        }

    }
    /////////////////////////////////////
    ///////////////////////////////////////
    //////////////////////////////////////////
    /////////////////////////////////////////


   // private SearchView search;
    final int DIALOG_TIME =2;
    final int DIALOG_DATE =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            getStationList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
   final AutoCompleteTextView dep = (AutoCompleteTextView) findViewById(R.id.start_station);
        final AutoCompleteTextView des = (AutoCompleteTextView) findViewById(R.id.end_station);

        // AutoCompleteTextView 에 아답터를 연결한다.
        dep.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,  STATIONS ));
        des.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,  STATIONS ));

        dep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                station_name = ((TextView)view).getText().toString();
                Toast.makeText(SearchActivity.this, station_name, Toast.LENGTH_SHORT).show();
//
            }
        });
        des.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destination = ((TextView)view).getText().toString();
                Toast.makeText(SearchActivity.this, destination, Toast.LENGTH_SHORT).show();
//
            }
        });


        Button b1 = (Button)findViewById(R.id.btn_time);//시간 받아오는 버튼
        Button b2 = (Button)findViewById(R.id.btn_date);//날짜받아오는 버튼

        Button start = (Button) findViewById(R.id.search_s);//출발역 검색하는 버튼
        Button end = (Button) findViewById(R.id.search_d);//도착역 검색하는 버튼

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SearchActivity.this, "선택된 출발역:"+station_name, Toast.LENGTH_LONG).show();
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SearchActivity.this, "선택된 도착역: "+destination, Toast.LENGTH_LONG).show();
            }
        });
        b1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                showDialog(DIALOG_TIME);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });
    }

    //버튼 클릭시 시간 선택화면 등장 (디폴트는 현재시간, 사용자 지정 가능)
    protected Dialog onCreateDialog(int id){
        switch (id){
            case DIALOG_TIME:
                TimePickerDialog tpd =
                        new TimePickerDialog(SearchActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view,
                                                          int hourOfDay, int minute) {
                                        set_time = Integer.toString(hourOfDay)+":"+minute + ":00";
                                        Toast.makeText(getApplicationContext(),
                                                hourOfDay +"시 " + minute+"분 을 선택했습니다",
                                                Toast.LENGTH_SHORT).show();
                                        try {
                                            info(station_name);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        //잘 돌아가는지 테스트용 - 출발
                                        String str = "";
                                        str = str+"열차코드: "+arr.get(1).TRAIN_CODE + "\n시간: "+arr.get(1).TIME +"\n방향: "+arr.get(1).TOWARDS ;
                                        TextView data_1 = (TextView) findViewById(R.id.txt1);
                                        data_1.setText(str);
                                        //테스트용 - 도착
                                        String d = "";
                                        d = d+"코드: "+des_codes.get(0)+"\n호선:"+des_line.get(0);
                                        TextView data_2 = (TextView) findViewById(R.id.txt2);
                                        data_2.setText(d);
                                    }
                                }, // 값설정시 호출될 리스너 등록
                                Integer.valueOf(time().substring(0,2)),Integer.valueOf(time().substring(3,5)), false);
                return tpd;// 기본값 시분 등록

            case DIALOG_DATE:
                day();
                DatePickerDialog dpd = new DatePickerDialog
                        (SearchActivity.this, // 현재화면의 제어권자
                                new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear, int dayOfMonth) {
                                        Toast.makeText(getApplicationContext(),
                                                year+"년 "+(monthOfYear+1)+"월 "+dayOfMonth+"일 을 선택했습니다",
                                                Toast.LENGTH_SHORT).show();
                                        getDateNum(year, monthOfYear+1, dayOfMonth);
                                    }
                                }
                                , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                                //    호출할 리스너 등록
                                year, month, day); // 기본값 연월일
                return dpd;

                // true : 24 시간(0~23) 표시
                // false : 오전/오후 항목이 생김

        }
        return super.onCreateDialog(id);
    }
}


