package com.example.chaoticsubway.getInfo;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.LineNumberReader;
import java.net.URLEncoder;

public class Congestion {
    static BufferedReader data = null;
    static BufferedWriter number = null;
    static String sun;
    static String sat;
    static String weekday;
    static String up_1, up_2;
    static String down_1, down_2;


    private static void getPeopleNum() throws IOException {
        String line = null;
        LineNumberReader line_num =  new LineNumberReader(data);
        while((line=line_num.readLine())!=null){
            int n = line_num.getLineNumber();

            String[] header = line.split(",");
            //System.out.println(header[1]);
            if(n >1){
                if(header.length ==1) {//한칸 건너뛰는 등 25의 인자 모두 가지고 있지 않은 경우
                    continue;
                }else {
                    for(int i=0;i<5;i++) {
                        number.append(header[i]);
                        number.append(",");
                    }
                    for(int i=0;i<20;i++){
                        int percent = Integer.valueOf(header[i+5]);
                        int people = (percent* (10500/100))/65;
                        //System.out.println(people);
                        number.append(Integer.toString(people));
                        number.append(",");
                    }
                }

            }else{
                for(int i=0;i<header.length;i++){
                    number.append(header[i]);
                    number.append(",");
                }
            }
            number.newLine();
        }

    }

    public static void main(String[] args) throws IOException {
        //받아올 파일과 다시 저장할 파일 지정, utf-8로 인코딩 에러 방지
        data = new BufferedReader(new InputStreamReader(new FileInputStream("../../../../../assets/parsed_congestion/2015_1.csv"), "utf-8"));
        number = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../../../../../assets/parsed_congestion/num_of_people.csv"),"utf-8"));
        getPeopleNum();
    }
}
