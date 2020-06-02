package com.example.chaoticsubway.parse;
//연도별
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class ParseYearData {
    static BufferedWriter bs = null;
    static int len;

    public static String[] compare(String a, String b, String[] station, String[] field) {
        station[1] = b;
        if(field[2].equals("상선") || field[2].equals("내선")) {
            station[4] = "U";
        }else {
            station[4] = "D";
        }
        //나머지 저장
        station[3] = field[1];
        //station[2] = field[3];
        for(int i=3;i<field.length;i++) {
            station[i+2] = field[i];
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
                line_num = field[0];
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
        bs = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("파일명"), "utf-8"));
        String[] entries = "요일코드,호선,역번호,역명,구분,05:00 ~,06:00 ~,07:00 ~,08:00 ~,09:00 ~,10:00 ~,11:00 ~,12:00 ~,13:00 ~,14:00 ~,15:00 ~,16:00 ~,17:00 ~,18:00 ~,19:00 ~,20:00 ~,21:00 ~,22:00 ~,23:00 ~, 24:00~".split(",");
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
        bs.flush();
        bs.close();
    }
    public static void main(String[] args) throws IOException {
        String i = "a";
        //얘 변경해서 사용
        String filename = "2013_csv.csv";
        bs = createCSV("../../assets/congestion/"+i+".csv");
        readCSV("../../assets/congestion/"+filename);
        close();
    }
}