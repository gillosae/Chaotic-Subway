package com.example.chaoticsubway.parse;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class TrainInfo {
    static String PATH = "../../../../../assets/";
    static BufferedWriter bw;
    //static String TRAIN;
    //static String LINE;
    //static int l;
    //static String DAY;
    //static String UPDOWN;
    static ArrayList<String> Trains;
    static ArrayList<String> STATIONS;
    static int num;

    private static void getStations(String l) throws IOException {// 열차 명 다 가져오기
        String file = "ordered/line_"+l+".csv";
        STATIONS = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(PATH + file), "utf-8"));
        //System.out.println("ok");
        String st =null;
        while((st = br.readLine())!=null){
            String arr[]  = st.split(",");
            if(arr[1].contains("Station")){
                //System.out.println("not this!");
                continue;
            }else{
                STATIONS.add(arr[1]);
            }
        }
        num = STATIONS.size();
    }

    //열차 번호 겹치는 것 없게 다 가져오기
    private static void getTrainNo(String line, String day, String up_down) throws IOException {
        Trains = new ArrayList<String>();
        String filename = "trainSchedule/"+line+"_"+day+"_"+up_down+".csv";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(PATH + filename), "utf-8"));
        String no = null;
        while((no = br.readLine())!=null){
            String[] arr = no.split(",");
            if(arr[0].equals("Line")){
                continue;
            }else{
                int len = (arr.length-4)/3;
                if(Trains.size()==0){
                    for(int i=0;i<len;i++){
                        if(arr[i*3+4].equals(" ")){
                            continue;
                        }else{
                            Trains.add(arr[i*3+4]);
                        }
                    }
                }else{
                    for(int i=0;i<len;i++) {
                        int n = 0;
                        for (int t = 0; t < Trains.size(); t++) {
                            if (arr[i * 3 + 4].equals(Trains.get(t))) {
                                n++;
                            } else {
                                continue;
                            }
                        }
                        //System.out.println(n);
                        if (n == 0) {
                            // System.out.println(n);
                            //System.out.println(arr[i*3+4]);
                            if(arr[i*3+4].equals(" ")){
                                continue;
                            }else{
                                System.out.println(arr[i*3+4]);
                                Trains.add(arr[i * 3 + 4]);
                            }
                        }
                    }
                }
            }
        }
    }
    private static void writeByName(String line, String day, String up_down,  String Train) throws IOException {
        bw.append(Train);
        bw.append(",");
        String filename = "trainSchedule/"+ line+"_"+day+"_"+up_down+".csv";

        for(int i=0;i<STATIONS.size();i++){
            //System.out.println(STATIONS.get(i));
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(PATH + filename), "utf-8"));
            String row = null;
            while((row = br.readLine())!=null){
                String arr[]  = row.split(",");
                if(arr[1].equals(STATIONS.get(i))){
                    //System.out.print(STATIONS.get(i));
                    int len = (arr.length-4)/3;
                    //System.out.println(len);
                    for(int a =0;a<len;a++){
                        //System.out.println(a);
                        if(arr[3*a+4].equals(Train)){
                            System.out.println(arr[3*a+5]);
                            bw.append(arr[3*a+5]);
                            bw.append(",");
                        }else{
                            //bw.append(" ");
                            //bw.append(",");
                            continue;
                        }
                    }
                }else{
                    continue;
                }
            }
        }
        /*
        while((row = br.readLine())!=null){ //파일의 전체 역과 그 줄을 모두 읽어들임
            String[] arr = row.split(",");
            if(arr[1].equals("Station")){
                continue;
            }else{
                for(int s=0;s<STATIONS.size();s++)
                {
                    if(arr[1].equals(STATIONS.get(s))){ //그중에서 역명이 STATIONS와 일치하는 경우 해당 row 넘겨줌
                        int len = ((arr.length)-4)/3;
                        for(int i=0;i<len;i++){
                            if(arr[i*3+4].equals(Train)){//해당 row의 해당 열차 번호가 전달받은 Trains와 일치하는 경우 해당 line에 열차 시간 저장
                                System.out.println("ok");
                                System.out.println(arr[i*3+5]);
                                bw.append(arr[i*3+5]);
                                bw.append(",");
                            }
                        }
                    }else{
                        continue;
                    }
                }
            }

        }
*/

    }
    // 역 모두 가져오기
    private static void writeHeader(String line, String day, String updown) throws IOException {
        String filename = line+"_"+day+"_"+updown+".csv";
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(PATH +"/TrainNo/"+ filename), "utf-8"));
        bw.append("Train No");
        bw.append(",");
        for(int i=0;i<num;i++){
            bw.append(STATIONS.get(i));
            bw.append(",");
        }
        bw.newLine();
        getTrainNo(line, day, updown);
    }

    //가져온 열차 번호에 따라서
    private static void parse(String line, String day, String up_down) throws IOException {

        //해당 파일의 가장 head 부분에 열차역 다 넣어주기
        writeHeader(line, day, up_down);
        // int len = ((arr.length)-4)/3;
        for(int a=0;a<Trains.size();a++){
            //System.out.println(Trains.get(a));
            writeByName(line, day, up_down, Trains.get(a));
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }

    public static void main(String[] args) throws IOException {
        for(int i=9;i<10;i++){
            String n= Integer.toString(i);
            getStations(n);
            parse(n, "1","1");
            parse(n, "1","2");
            parse(n, "2","1");
            parse(n, "2","2");
            parse(n, "3","1");
            parse(n, "3","2");
        }
    }
}
