package com.example.chaoticsubway.main;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


//출발역, 출발요일, 출발시간을 파라미터로 넣어서 몇시출발 무슨행 몇번열차의 리스트를 반환하는 함수형태로 만들어줘
//어떤식으로 어떻게 넘겨주는지 정하기
public class FindTrain {
    static String day_num;//요일 코드
    static String time;//현재 시간
    static ArrayList<String> des_line = new ArrayList<>();//도착역 호선
    static String station_name;//도착역 이름
    static ArrayList<String> line_num = new ArrayList<>();//출발역 호선 (환승역일 수 있으므로)
    static String cur_station;//출발역 이름
    static ArrayList<String> train_num = new ArrayList<>();//열차 번호 리스트로
    static ArrayList<String> station_codes = new ArrayList<>();//출발역 코드
    static ArrayList<String> codes = new ArrayList<>();
    static ArrayList<String> dep_time = new ArrayList<>();
    static ArrayList<String> towards = new ArrayList<>();
    //static ArrayList<ArrayList<String>> result = new ArrayList<>();
    static List<TrainInfo> arr = new ArrayList<TrainInfo>();
    static InputStream input;

    public FindTrain() throws IOException {

    }

    //요일 코드
    public  String day() {
        Calendar cal = Calendar.getInstance();

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int num;
        switch(dayOfWeek){
            case 6:
                num = 02;
                break;
            case 7:
                num = 03;
                break;
            default:
                num = 01;
        }
        day_num = Integer.toString(num);
        return day_num;
    }

    //출발역 - 인코딩 문제 나중에 다시 확인
    public void curStation(String station) throws IOException {

        String cur =station;//검색으로 받아오기;

        cur_station = cur;
        System.out.println(cur_station);
        getCode(cur_station, line_num);
    }

    //도착역
    private void desStation(String station) throws IOException {

        String destination =station;//검색으로 받아오기

        station_name = destination;
        System.out.println(station_name);
        getCode(station_name, des_line);
    }

    //현재시간 - 동작 확인 완료
    public  String time() {
        Calendar cur_time = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        //time = "15:40:32";
        return dateFormat.format(cur_time.getTime());
    }


    //역명에 따라 역 코드 받아오기 -완료
    public void getCode(String station, ArrayList<String> add_line) throws IOException {
        System.out.println(station);
        String row;
        String[] info;
        String line;
        for(int i=1;i<10;i++){
            String filename = "../../../../../assets/stations/line_"+i+".csv";
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            while((row=br.readLine())!=null){
                info = row.split(",");
                if(info[1].equals(station)){
                    System.out.println(info[3]);
                    station_codes.add(info[3]);
                    line = Integer.toString(i);
                    add_line.add(line);
                }else{
                    continue;
                }
            }
        }
        for(int i=0;i<add_line.size();i++){
            System.out.println(add_line.get(i));
        }
    }

    //시간표의 총 길이 가져오기
    public int getLen(String path) throws IOException {
        BufferedReader len = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        String get_len = len.readLine();
        String[] arr = get_len.split(",");
        int n = arr.length;
       // System.out.println("arr_len:" + n);
        return n;
    }

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

            if (b > 60 && c < 0) {
                index = (i + 2);//좀더 명확하고 상세한 계산을 통해 열차 추천 필요
                break;
            }
        }
        return index;
    }
    //-미완 : 목적지, 노드에 따른 경로 등 고려 필요
    public void getCloseTrain(String[] schedule) throws IOException {
        int closest_index;
        String train;
        int index = (schedule.length-4)/3;
        //System.out.println(index);
        ArrayList<String> time = new ArrayList<>();
        int n =0;
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
        }
        closest_index = getTrainNum(time_d, time_d.size());
        System.out.println(closest_index);
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
        String filename = "../../../../../assets/trainSchedule/"+line+"_"+day_num+"_"+i+".csv";
        //String filename = line+"_"+day_num+"_"+i+".csv";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
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
    void getParams() throws IOException {
        //curStation(station);
        //desStation();
        time();
        day();
    }

    //이 모든것을 실행하고 받은 시간표에 따라 리스트로 정리
    public List<TrainInfo> info(String station) throws IOException {
        getParams();//시간과 날짜변수 받아오기
        //System.out.println("a");
        //출발역에서 갈 수 있는 방향의 열차 코드 모두 받아오기
        for(int i=0;i<line_num.size();i++){
            System.out.println(line_num.get(i));
            closestTrain(line_num.get(i), i);
        }

        int num_of_trains = dep_time.size();

        for(int i=0;i<num_of_trains;i++){
            System.out.println("시간"+dep_time.get(i));
            //System.out.println("호선"+line_num.get(i));
            System.out.println("열차번호: "+train_num.get(i));
            System.out.println("종착지:"+towards.get(i));
        }

        for(int i=0;i<num_of_trains;i++){

            arr.add(new TrainInfo(towards.get(i), dep_time.get(i),  train_num.get(i)));
            System.out.println(towards.get(i));
        }
        return arr;
    }
}
