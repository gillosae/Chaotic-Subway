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
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ParseNine {
    static BufferedReader br = null;
    static BufferedWriter bw = null;
    static String baseUrl = "http://openapi.tago.go.kr/openapi/service/SubwayInfoService/getSubwaySttnAcctoSchdulList?serviceKey=VuzceYJ7gvgVq%2F5Ajaf%2FBtxHYW9AK187BWM2m1bHXdLL25f6JyQTaaUjUnlf%2BIRz1cAQV0KkOX9sXKVIPszb1w%3D%3D&";
    static String basepath = "../../../../../assets/ordered/";
    static ArrayList<String> station_code;
    static String[] station_info = new String[4];
    static String file;
    static int largestNum;
    static int page;

    private static void OpenCsv(String line, String week, String up_down) throws IOException, ParserConfigurationException {
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../../../../../assets/trainSchedule/"+line+"_"+week+"_"+up_down+".csv"),"utf-8"));
        br = new BufferedReader(new InputStreamReader(new FileInputStream(basepath+file), "utf-8"));
        String[] entries = br.readLine().split(",");

        getLargestNum(up_down, week);
        System.out.println(largestNum);
        for(int i=0;i<largestNum+4;i++){
            if(i<4){
                bw.append(entries[i]);
                bw.append(",");
            }else{
                String a =  Integer.toString(i-3);
                bw.append(a+" Train Code");
                bw.append(",");
                bw.append(a+" Arrive Time");
                bw.append(",");
                bw.append(a+" Towards");
                bw.append(",");
            }
        }
        bw.newLine();
        ParseData(line, week, up_down);
    }

    private static String newUrl(String base, String code, String up_down, String week, int n){
        String ud;
        if(up_down.equals("1")){
            ud = "U";
        }else{
            ud = "D";
        }
        String url = "subwayStationId=SUB"+code+"&dailyTypeCode=0"+week+"&upDownTypeCode="+ud+"&pageNo="+n;
        return url;
    }

    private static void ReadCsv() throws IOException {
        station_code = new ArrayList<String >();
        br = new BufferedReader(new InputStreamReader(new FileInputStream(basepath+file),"utf-8"));
        String line = null;
       // station_code = new String[(len-1)];
        int i=0;
        while((line = br.readLine())!=null){
            String[] station = line.split(",");
            if(station[0].equals("Line")){
                continue;
            }else{
                station_code.add(station[3]);
                i++;
            }
        }
    }

    private static void stationVar(String line) throws IOException, ParserConfigurationException {
        OpenCsv(line, "1", "1");//주일 상선
        OpenCsv(line, "2", "1");//토요일 상선
        OpenCsv(line, "3", "1");//일요일 상선
        OpenCsv(line, "1", "2");//주일 하선
        OpenCsv(line, "2", "2");//토요일 하선
        OpenCsv(line, "3", "2");//일요일 하선
    }

    private static void getStationInfo(int n) throws IOException {
        br = new BufferedReader(new InputStreamReader(new FileInputStream(basepath+file),"utf-8"));
        String line = br.readLine();
        while(line!=null){
            LineNumberReader num = new LineNumberReader(br);
            while((line=num.readLine())!=null) {
                int a = num.getLineNumber();
                String[] arr = line.split(",");
                if(a == (n+1)){
                    station_info[0] = arr[0];
                    station_info[1] = arr[1];
                    station_info[2] = arr[2];
                    station_info[3] = arr[3];
                }else{
                    continue;
                }
            }
        }
    }

    private static void getLargestNum(String up_down, String week) throws IOException, ParserConfigurationException {
        int largest=0;
        for (int i = 0; i < station_code.size(); i++) {
            //getStationInfo(i);
            String url = newUrl(baseUrl, station_code.get(i), up_down, week, 1);
            URL path = new URL(url);
            System.out.println(path);
            try {
                HttpURLConnection con = (HttpURLConnection) path.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept-language", "ko");

                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());
                Element e;
                NodeList ns = doc.getElementsByTagName("tbody");
                String len = null;
                String row = null;
                if (ns.getLength() > 0) {
                    e = (Element) ns.item(0);
                    len = e.getElementsByTagName("totalCount").item(0).getTextContent();
                    row = e.getElementsByTagName("numOfRows").item(0).getTextContent();
                    System.out.println(len);
                    System.out.println(row);
                    page = Integer.valueOf(len)/Integer.valueOf(row)+1;
                    System.out.println(page);
                }else{
                    continue;
                }
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
        largestNum = largest;
        System.out.println(largestNum);
    }
    private static void ParseData(String line, String week, String up_down) throws IOException {
        br = new BufferedReader(new InputStreamReader(new FileInputStream(basepath+file),"utf-8"));
        for(int i=0;i<station_code.size();i++){
            getStationInfo(i);
            for(int n=1;n<page+1;n++){
                String url = newUrl(baseUrl, station_code.get(i), up_down, week, n);
                URL path = new URL(url);
                Boolean TF = false;
                try{
                    HttpURLConnection con = (HttpURLConnection)path.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept-language", "ko");

                    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());
                    Element e;
                    NodeList ns = doc.getElementsByTagName("SearchArrivalTimeOfLine2SubwayByFRCodeService");
                    String len=null;
                    if (ns.getLength() > 0) {
                        e = (Element) ns.item(0);
                        len = e.getElementsByTagName("list_total_count").item(0).getTextContent();
                        TF = true;
                    }

                    if(TF){
                        //int n = Integer.valueOf(len);
                        ns = doc.getElementsByTagName("items");
                        String[] time_table = new String[(largestNum*3)+4];

                        time_table[0] = station_info[0];
                        time_table[1] = station_info[1];
                        time_table[2] = station_info[2];
                        time_table[3] = station_info[3];

                        for (int a = 0; a < ns.getLength(); a++){
                            // 출발시간 +  방향,종점 + 열차번
                            e = (Element)ns.item(a);
                            NodeList childlist = e.getChildNodes();
                            // String[] time;
                            // int k;
                            //System.out.println("열차코드:"+e.getElementsByTagName("TRAINCODE").item(0).getTextContent());
                            time_table[a*3+4] = childlist.item(2).getTextContent(); //열차번호
                            if(e.getElementsByTagName("ARRIVETIME").item(0).getTextContent().equals("00:00:00")) {
                                time_table[a*3+5] = e.getElementsByTagName("LEFTTIME").item(0).getTextContent();
                            }else{
                                time_table[a*3+5] =e.getElementsByTagName("ARRIVETIME").item(0).getTextContent();
                            }
                            time_table[a*3+6] = e.getElementsByTagName("DESTSTATION_NAME").item(0).getTextContent(); //종착지
                        }
                        for(int a = ns.getLength(); a < largestNum; a++){
                            time_table[a*3+4] = " ";
                            time_table[a*3+5] = " ";
                            time_table[a*3+6] = " ";
                        }
                        for(int j=0;j<time_table.length;j++){
                            bw.append(time_table[j]);
                            bw.append(",");
                        }
                    }else{
                        continue;
                    }
                } catch (SAXException | IOException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
                bw.newLine();
            }
            bw.flush();
        }

        bw.close();
    }

    private static void getTimeTable(int num, String line) throws IOException, ParserConfigurationException {
        br = new BufferedReader(new InputStreamReader(new FileInputStream(basepath+file),"utf-8"));// 각 호선 읽을 파일 열기
        ReadCsv(); //station code 들 읽어오기
        stationVar(line);
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException {
        file = "line_9.csv";
        getTimeTable(31, "9");
    }
}
