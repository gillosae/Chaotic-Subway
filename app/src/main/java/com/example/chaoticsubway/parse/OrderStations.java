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
import java.io.UnsupportedEncodingException;

public class OrderStations {

    static BufferedReader br;
    static BufferedWriter bw;
    static BufferedReader br2;

    private static void writeHead() throws IOException {
        bw.append("Line");
        bw.append(",");
        bw.append("Stations");
        bw.append(",");
        bw.append("Station Code");
        bw.append(",");
        bw.append("FR Code");
        bw.append(",");
        bw.newLine();
    }

    private static void order(String line) throws IOException {
        br = new BufferedReader(new InputStreamReader(new FileInputStream("../../../../../assets/stations/ordered_stations.csv"), "utf-8"));
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../../../../../assets/ordered/line_"+line+".csv")));
       // header(line);
        String station = null;
        String original = null;
        writeHead();
        while((station= br.readLine())!=null){
            //original = br2.readLine();
            br2 = new BufferedReader(new InputStreamReader(new FileInputStream("../../../../../assets/stations/line_"+line+".csv"), "utf-8"));
            String[] arr = station.split(",");
            //System.out.println(arr.length);
            if(arr.length==0){
                break;
            }else{
                if(arr[0].equals(line)){
                    //System.out.println("ok");
                    while((original = br2.readLine())!=null){
                        String[] a = original.split(",");
                        //System.out.println(a[1]);
                        if(arr[1].equals(a[1])){
                            System.out.println("ok");
                            for(int i=0;i<a.length;i++){
                               // System.out.println(arr[1]);
                                System.out.println(a[i]);
                                bw.append(a[i]);
                                bw.append(",");
                            }
                            bw.newLine();
                        }
                    }
                }else{
                    continue;
                }
            }

        }
        bw.flush();
        bw.close();
    }

    public static void main(String[] args) throws IOException {
        order("1");
        order("2");
        order("3");
        order("4");
        order("5");
        order("6");
        order("7");
        order("8");
        order("9");
    }
}
