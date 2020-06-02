package com.example.chaoticsubway.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.example.chaoticsubway.getInfo.GetTime;

public class ClosestTrain {
    static String start_code;
    static String end_code;
    static String day_code;
    static String updown;
    static BufferedReader start_br;
    static BufferedReader end_br;
    static String time;

    //assets/timetable 폴더에 파싱해놓은 시간표 csv 파일 이용해서 시작역, 도착역 정보 받아오기
    public static void getFile() throws FileNotFoundException {
        start_br = new BufferedReader(new InputStreamReader(new FileInputStream("../../../../assets/timetable/" + start_code + "_" + day_code + "_" + updown + ".csv")));
        end_br = new BufferedReader(new InputStreamReader(new FileInputStream("../../../../assets/timetable//" + end_code + "_" + day_code + "_" + updown + ".csv")));
    }

    private static void getParam() {

    }

    private static String parseTime() {
        GetTime cur_time = new GetTime();
        String date_time = cur_time.dateTime();
        date_time = date_time.substring(8);
        //System.out.println(date_time);
        return date_time;
    }

    public static int calculate(String a) {
        int time_diff;
        int a_time = Integer.valueOf(time);
        int b_time = Integer.valueOf(a);
        time_diff = a_time - b_time;
        return time_diff;
    }


    private static String getTrainNum(ArrayList<Integer> time, int n) {
        String train = null;
        int a, b, c;
        for (int i = 1; i < n - 1; i++) {
            a = Integer.valueOf(time.get(i - 1));
            b = Integer.valueOf(time.get(i));
            c = Integer.valueOf(time.get(i + 1));

            if (b > 60 && c < 0) {
                //System.out.println(time.get(i-1));
                train = Integer.toString(i + 2);//좀더 명확하고 상세한 계산을 통해 열차 추천 필요
                //System.out.println(train);
                break;
            }
        }
        return train;
    }

    public static void main(String[] args) throws IOException {
        ArrayList<String> arrival_time = new ArrayList<String>();
        String line = null;
        String train_num = null;
        time = parseTime();
        getFile();
        int n = 0;
        while ((line = start_br.readLine()) != null) {
            String[] e = line.split(",");
            arrival_time.add(e[7]);
            n++;
        }

        ArrayList<Integer> time_d = new ArrayList<>();
        for (int i = 1; i < n; i++) {
            time_d.add(calculate(arrival_time.get(i)));
            System.out.println(time_d.get(i - 1));
        }
        train_num = getTrainNum(time_d, time_d.size());

        System.out.println(train_num);
    }
}
