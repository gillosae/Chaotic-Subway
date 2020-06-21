package com.example.chaoticsubway.parse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TimeTableByLine {
    static BufferedReader br = null;
    static BufferedWriter bw = null;
    static String baseUrl = "http://openapi.seoul.go.kr:8088/766a6f517773737736374163504c43/xml/SearchSTNTimeTableByFRCodeService/1/450/";
    static String basePath = "../../../../../assets/ordered/";
    static String station_code[];
    static String line_num;
    static String thisFile;

    // inout_tag = 1/2 ; week_tag = 1/2/3;
    private static void ReadCsv(String path) throws IOException {
        br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
        int i=0;
        String line=null;
        station_code = new String[getStationNum()];
        while((line=br.readLine())!=null){
            System.out.println(line);

            System.out.println(line);
            String[] station = line.split(",");
            if(station[2].contains("Station")){
                continue;
            }else{
                station_code[i] = station[2];
                System.out.println(station[2]);
                i++;
            }
        }
    }

    private static Integer getStationNum() throws IOException {
        int num=0;
        while(br.readLine()!=null){
            num++;
        }
        return num;
    }

    private static String newUrl(String base, String code, String up_down, String week ){
        String url = base + code +"/" + week + "/" + up_down;
        return url;
    }

    private static void WriteCsv(String path, int len) throws IOException {
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"));
        String[] entries = "Line, Station, Code".split(",");
        for(String e: entries){
            bw.append(e);
            bw.append(",");
        }
        for(int i=0;i<len;i++){
            String num = Integer.toString(i);
            bw.append(num);
            bw.append(",");
            bw.append(num);
            bw.append(",");
            bw.append(num);
            bw.append(",");
        }
        bw.newLine();
    }

    private static void getByLine(String file) throws IOException, ParserConfigurationException {
        thisFile = file;
        line_num = file.substring(0,6);
        ReadCsv(basePath+file);

        for(int i=0;i<station_code.length;i++){
            parseData(station_code[i], "1", "1");//상선 평일
            parseData(station_code[i], "1", "2");//상선 토요일
            parseData(station_code[i], "1", "3");//상선 일요일
            parseData(station_code[i], "2", "1");//하선 평일
            parseData(station_code[i], "2", "2");//하선 토요일
            parseData(station_code[i], "2", "3");//하선 주말
        }
    }

    private static void parseData(String code, String up_down, String week) throws IOException, ParserConfigurationException {
        String url = newUrl(baseUrl, code, up_down, week);
        URL path = new URL(url);

        try{
            HttpURLConnection con = (HttpURLConnection)path.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept-language", "ko");

            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());
            Element e;

            if(true){
                NodeList ns = doc.getElementsByTagName("row");
                String[] time_table = new String[(ns.getLength()*3)+3];
                WriteCsv(basePath+line_num+"_"+up_down+"_"+week+".csv", ns.getLength());
                for (int i = 0; i < ns.getLength(); i++){
                    String line = br.readLine();
                    System.out.println(line);
                    String[] info = line.split(",");
                    // 출발시간 +  방향,종점 + 열차번호
                    for(int a=0;a<3;a++){
                        time_table[a] = info[a];
                    }
                    e = (Element)ns.item(i);
                    NodeList childList = e.getChildNodes();
                    System.out.println(childList.item(6).getTextContent());
                    time_table[i*3+3] = childList.item(4).getTextContent(); //열차번호
                    time_table[i*3+4] = childList.item(6).getTextContent(); //도착시간
                    time_table[i*3+5] = childList.item(10).getTextContent(); //종착지

                    }
                for(int j=0;j<time_table.length;j++){
                    bw.append(time_table[j]);
                    bw.append(",");
                }
                bw.flush();
                bw.newLine();
            }
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException {

        getByLine("line_1.csv");
        getByLine("line_2.csv");
        getByLine("line_3.csv");
        getByLine("line_4.csv");
        getByLine("line_5.csv");
        getByLine("line_6.csv");
        getByLine("line_7.csv");
        getByLine("line_8.csv");
        getByLine("line_9.csv");

        }

    }
