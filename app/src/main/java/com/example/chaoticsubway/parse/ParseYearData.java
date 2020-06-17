package com.example.chaoticsubway.parse;
//연도별
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class ParseYearData {
    static BufferedWriter bs = null;
    static int len;

    public static String[] compare( String b, String[] station, String[] field) {
        if(field[0].equals("평일")){
            station[0] = "01";
        }else if(field[0].equals("토요일")){
            station[0] = "02";
        }else if(field[0].equals("일요일")){
            station[0] = "03";
        }
        station[1] = b;
        if(field[2].equals("상선") || field[2].equals("내선")) {
            station[4] = "U";
        }else {
            station[4] = "D";
        }
        //나머지 저장
        station[3] = field[3];
        station[2] = field[4];
        //System.out.println(field.length);
        for(int i=5;i<len;i++) {
            station[i] = field[i];
        }
        return station;
    }

    private static void checkType(String a) throws UnsupportedEncodingException {
        /*
        System.out.println("utf-8 -> euc-kr        : " + new String(word.getBytes("utf-8"), "euc-kr"));

        System.out.println("utf-8 -> ksc5601       : " + new String(word.getBytes("utf-8"), "ksc5601"));

        System.out.println("utf-8 -> x-windows-949 : " + new String(word.getBytes("utf-8"), "x-windows-949"));

        System.out.println("utf-8 -> iso-8859-1    : " + new String(word.getBytes("utf-8"), "iso-8859-1"));

        System.out.println("iso-8859-1 -> euc-kr        : " + new String(word.getBytes("iso-8859-1"), "euc-kr"));

        System.out.println("iso-8859-1 -> ksc5601       : " + new String(word.getBytes("iso-8859-1"), "ksc5601"));

        System.out.println("iso-8859-1 -> x-windows-949 : " + new String(word.getBytes("iso-8859-1"), "x-windows-949"));

        System.out.println("iso-8859-1 -> utf-8         : " + new String(word.getBytes("iso-8859-1"), "utf-8"));

        System.out.println("euc-kr -> utf-8         : " + new String(word.getBytes("euc-kr"), "utf-8"));

        System.out.println("euc-kr -> ksc5601       : " + new String(word.getBytes("euc-kr"), "ksc5601"));

        System.out.println("euc-kr -> x-windows-949 : " + new String(word.getBytes("euc-kr"), "x-windows-949"));

        System.out.println("euc-kr -> iso-8859-1    : " + new String(word.getBytes("euc-kr"), "iso-8859-1"));

        System.out.println("ksc5601 -> euc-kr        : " + new String(word.getBytes("ksc5601"), "euc-kr"));

        System.out.println("ksc5601 -> utf-8         : " + new String(word.getBytes("ksc5601"), "utf-8"));

        System.out.println("ksc5601 -> x-windows-949 : " + new String(word.getBytes("ksc5601"), "x-windows-949"));

        System.out.println("ksc5601 -> iso-8859-1    : " + new String(word.getBytes("ksc5601"), "iso-8859-1"));

        System.out.println("x-windows-949 -> euc-kr     : " + new String(word.getBytes("x-windows-949"), "euc-kr"));

        System.out.println("x-windows-949 -> utf-8      : " + new String(word.getBytes("x-windows-949"), "utf-8"));

        System.out.println("x-windows-949 -> ksc5601    : " + new String(word.getBytes("x-windows-949"), "ksc5601"));

        System.out.println("x-windows-949 -> iso-8859-1 : " + new String(word.getBytes("x-windows-949"), "iso-8859-1"));

*/



        String charSet[] = { "UTF-8", "euc-kr", "ksc5601", "iso-8859-1", "ascii", "x-windows-949" };

        String name = a;

        for (int i = 0; i < charSet.length; i++) {

            for (int j = 0; j < charSet.length; j++) {

                if (i == j){

                    continue;

                }

                System.out.println(charSet[i] + " : " + charSet[j] + " :" + new String(name.getBytes(charSet[i]), charSet[j]));

            }

        }


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
                String[] field = line.split(splitBy);
                if(field.length ==0){
                    continue;
                }else{

                    line_num = field[1];

                    if(line_num.equals("2호선")) {
                        //System.out.println("ok");
                        station = compare("2호선", station, field);
                    }else if(line_num.equals("4호선")) {
                        station = compare( "4호선", station, field);
                    }else if(line_num.equals("7호선")) {
                        station = compare( "7호선", station, field);
                    }else if(line_num.equals("9호선")){
                        station = compare( "9호선", station, field);
                    }else {
                        continue;
                    }
                    writeCSV(station);
                }

            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedWriter createCSV(String filepath) throws IOException {
        bs = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), StandardCharsets.UTF_8));
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
        bs.flush();
    }
    public static void close() throws IOException {
        bs.flush();
        bs.close();
    }
    public static void main(String[] args) throws IOException {
        String i = "a";
        //얘 변경해서 사용
        String filename = "2015_1.csv";
        bs = createCSV("../../../../../assets/parsed_congestion/"+filename);
        readCSV("../../../../../assets/congestion/"+filename);
        close();
    }
}