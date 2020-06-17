package com.example.chaoticsubway.parse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class ParseByLine_1 {
    private static String path = "../../../../../assets/stations/all_station.csv";
    static BufferedReader br = null;
    static BufferedWriter bw1 = null;
    static BufferedWriter bw2 = null;
    static BufferedWriter bw3 = null;
    static BufferedWriter bw4 = null;
    static BufferedWriter bw5 = null;
    static BufferedWriter bw6 = null;
    static BufferedWriter bw7 = null;
    static BufferedWriter bw8 = null;
    static BufferedWriter bw9 = null;


    private static void ReadCsv(String path) throws FileNotFoundException, UnsupportedEncodingException {
        br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
    }
    private static void WriteCsv() throws IOException {
        String[] entries = "Line, Station, Station Code, FR Code".split(",");

        bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../../../../../assets/stations/line_1.csv")));
        bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../../../../../assets/stations/line_2.csv")));
        bw3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../../../../../assets/stations/line_3.csv")));
        bw4 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../../../../../assets/stations/line_4.csv")));
        bw5 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../../../../../assets/stations/line_5.csv")));
        bw6 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../../../../../assets/stations/line_6.csv")));
        bw7 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../../../../../assets/stations/line_7.csv")));
        bw8 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../../../../../assets/stations/line_8.csv")));
        bw9 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../../../../../assets/stations/line_9.csv")));

        for(String e :entries){
            bw1.append(e);
            bw1.append(",");
            bw2.append(e);
            bw2.append(",");
            bw3.append(e);
            bw3.append(",");
            bw4.append(e);
            bw4.append(",");
            bw5.append(e);
            bw5.append(",");
            bw6.append(e);
            bw6.append(",");
            bw7.append(e);
            bw7.append(",");
            bw8.append(e);
            bw8.append(",");
            bw9.append(e);
            bw9.append(",");
        }
    }

    private static void add(String[] station, BufferedWriter bw) throws IOException {
        bw.newLine();
        bw.append(station[2]);
        bw.append(",");
        bw.append(station[1]);
        bw.append(",");
        bw.append(station[0]);
        bw.append(",");
        bw.append(station[3]);
        bw.append(",");
    }

    public static void main(String[] args) throws IOException {
        ReadCsv(path);
        WriteCsv();

        String line;
        String[] station;
        try{
            while((line = br.readLine())!=null) {
                station = line.split(",");

                if (station[2].equals("1")) {
                    add(station, bw1);
                } else if (station[2].equals("2")) {
                    add(station, bw2);
                } else if (station[2].equals("3")) {
                    add(station, bw3);
                } else if (station[2].equals("4")) {
                    add(station, bw4);
                } else if (station[2].equals("5")) {
                    add(station, bw5);
                } else if (station[2].equals("6")) {
                    add(station, bw6);
                } else if (station[2].equals("7")) {
                    add(station, bw7);
                } else if (station[2].equals("8")) {
                    add(station, bw8);
                } else if (station[2].equals("9")) {
                    add(station, bw9);
                } else {
                    continue;
                }
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bw1.flush();
        bw2.flush();
        bw3.flush();
        bw4.flush();
        bw5.flush();
        bw6.flush();
        bw7.flush();
        bw8.flush();
        bw9.flush();
    }
}
