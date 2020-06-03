package com.example.chaoticsubway.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class CalculateCongestion {
    static String code;
    static String up_down;
    static String day;
    static String cur_time;
    static BufferedReader br = null;


    private static void print(String con) throws UnsupportedEncodingException {

        if(con.equals("압사")){
            System.out.println("압사");
        }else if(con.equals("혼잡")){
            System.out.println("혼잡");
        }else if(con.equals("보통")){
            System.out.println("보통");
        }else if(con.equals("널널")){
            System.out.println("널널");
        }
    }
    private static void getRate(int p_num) throws UnsupportedEncodingException {

        int mul = p_num*65;
        int cal = (mul*100)/10500;

        if(cal>=150){
            print("압사");
        }else if(cal>=130 && cal<150){
            print("혼잡");
        }else if(cal>=80&&cal<130){
            print("보통");
        }else if(cal<80){
            print("널널");
        }
    }
/*
    private static void encode(){
        if()
    }*/

    private static int getPeopleNum() throws IOException {
        int t = Integer.valueOf(cur_time);
        int people =0;
        String line = null;

        while((line = br.readLine())!=null) {
            String[] arr = line.split(",");

            if (arr[2].contains(code) && arr[0].equals(day)&&arr[4].equals(up_down)) {
                people = Integer.valueOf(arr[t]);
                //System.out.print(people);
                break;
            }
        }
        return people;
    }
    //추후 실시간 값 가져오는 것으로 변경 필요

    private static void getParams() throws UnsupportedEncodingException {
        //시간대 가져오기
        //이후 get time 클래스와 연동하여 실시간 시간 불러온 후 <시> 부분만 추출하여 비교
        //GetTime time = new GetTime();
        cur_time ="08";//time.dateTime();
        //System.out.println(cur_time);

        //cur_time = cur_time.substring(8, 10);
        //System.out.println(cur_time);
        //날짜 (평일=01, 토요일=02, 일요일=03 ) 코드 가져오기
        day="01" ;//time.getDay();
        //System.out.println(Arrays.toString(day.getBytes(StandardCharsets.UTF_8)));
        //역 코드 가져오기(지금은 임의 설정. 차후에 검색 시 해당 검색어로 역 코드 불러오는 station code와 연동하여 가져오기)
        code = "226";
        //상하행 열차 선택 (현재 임의 설정 위와 동일)
        up_down = "D";
    }


    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(new FileInputStream("../../../../../assets/parsed_congestion/num_of_people.csv")));
        getParams();
        getRate( getPeopleNum());


    }
}
