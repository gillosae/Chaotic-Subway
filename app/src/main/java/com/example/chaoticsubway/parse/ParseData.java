package com.example.chaoticsubway.parse;


//일일 혼잡도 데이터 파싱해서 필요한 것들만 assets/parsed 디렉토리 저장
//2호선 성수지선, 신정지선 포함하기

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ParseData {
    static BufferedWriter bs = null;
    static int len;

    public static String[] compare(String a, String b, String[] station, String[] field) {
        //station에 호선, 상하선, 역 번호, 역 이름, 혼잡도 데이터 시간대별로 저장
        station[1] = b.substring(0);
        if(field[4].equals("상선") || field[4].equals("내선")) {
            station[4] = "U";
        }else {
            station[4] = "D";
        }
        //나머지 저장
        station[2] = field[2];
        station[3] = field[3];
        for(int i=6;i<field.length;i++) {
            station[i-1] = field[i];
        }
        return station;
    }

    public static void readCSV(String filepath){
        BufferedReader fr = null;
        String line;
        String splitBy = ",";
        String line_num;
        String[] station = new String[len];
        try{
            fr = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8" ));

            while((line = fr.readLine())!=null){
                System.out.println(line);
                String[] field = line.split(splitBy);
                //System.out.println(field[1]);
                line_num = field[1];
                //2,4,7,9호선 추출
                if(line_num.equals("2호선")) {
                    station = compare(line_num, "2호선", station, field);
                }else if(line_num.equals("4호선")) {
                    station = compare(line_num, "4호선", station, field);
                }else if(line_num.equals("7호선")) {
                    station = compare(line_num, "7호선", station, field);
                }else if(line_num.equals("9호선")){
                    station = compare(line_num, "9호선", station, field);
                }else {
                    continue;
                }
                writeCSV(station);
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedWriter createCSV(String filepath) throws IOException {
        bs = new BufferedWriter(new FileWriter(filepath, true));
        //,로 나눠서 csv 각 저장
        String[] entries = "요일코드, 호선,역번호,역명,구분,05:30 ~,06:00 ~,06:30 ~,07:00 ~,07:30 ~,08:00 ~,08:30 ~,09:00 ~,09:30 ~,10:00 ~,10:30 ~,11:00 ~,11:30 ~,12:00 ~,12:30 ~,13:00 ~,13:30 ~,14:00 ~,14:30 ~,15:00 ~,15:30 ~,16:00 ~,16:30 ~,17:00 ~,17:30 ~,18:00 ~,18:30 ~,19:00 ~,19:30 ~,20:00 ~,20:30 ~,21:00 ~,21:30 ~,22:00 ~,22:30 ~,23:00 ~,23:30 ~".split(",");

        len = entries.length;
        for(String e :entries) {
            bs.append(e);
            bs.append(",");
        }
        return bs;
    }
    public static void writeCSV(String[] station) throws IOException {
        bs.newLine();
        for(String element:station) {
            bs.append(element);
            bs.append(",");
        }
    }
    public static void close() throws IOException {
        //나머지 다 끝내고 bufferedwriter 닫기
        bs.flush();
        bs.close();
    }
    public static void main(String[] args) throws IOException {
        //새로운 csv파일 만들기 parsed 디렉토리 저장
        bs = createCSV("../../../../../assets/congestion/515_parse.csv");
        //기존 혼잡도 파일 (바꿔서 사용 가능) 이용해서 새로운 파일에 저장
        readCSV("../../../../../assets/congestion/200515_csv.csv");
        close();
    }

}
