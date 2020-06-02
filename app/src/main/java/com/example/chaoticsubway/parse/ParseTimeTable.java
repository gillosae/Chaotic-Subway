package com.example.chaoticsubway.parse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParseTimeTable {

    //역코드 입력받는 코드 추가예정
    static String station_code;//서울역
    static String day_code;
    static String up_down;
    static int page_num;
    static String page;
    static int page_no;
    static BufferedWriter bw = null;
    static BufferedReader br = null;
    static String s[] = new String[9];

    //상하선 U/D와 페이지 번호, 주일 주중 따라서 (station code, day code, up down code 필요)
    static String baseUrl = "http://openapi.tago.go.kr/openapi/service/SubwayInfoService/getSubwaySttnAcctoSchdulList?"
            +"serviceKey=VuzceYJ7gvgVq%2F5Ajaf%2FBtxHYW9AK187BWM2m1bHXdLL25f6JyQTaaUjUnlf%2BIRz1cAQV0KkOX9sXKVIPszb1w%3D%3D&";

    //각 인자값에 값 전달
    public static void getParam(String code, String ud, String day) throws IOException {

        station_code = getCode(code);
        System.out.println(station_code);
        day_code = day;
        up_down = ud;
    }

    //인자값 포함해서 url 맅
    public static String getUrl(String url) {
        String new_url = url +"subwayStationId="+station_code+"&dailyTypeCode="+day_code+"&upDownTypeCode="+up_down+"&pageNo=";
        System.out.println(new_url);
        return new_url;
    }

    //역 코드에 SUB 붙여서 넘기기. 이렇게 넣어야 가능
    public static String getCode(String code) {
        String new_code = "SUB"+code;
        return new_code;
    }

    //url 값에 따라 페이지 값 돌려받음. 이거 인자로 넣어서 모든 값 파싱 가능
    public static String getNumber() throws SAXException, IOException, ParserConfigurationException {
        URL path = new URL(getUrl(baseUrl));
        String totalPage = null;
        //연결설정
        HttpURLConnection con = (HttpURLConnection)path.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept-language", "ko");

        //
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());
        Element e;
        NodeList ns = doc.getElementsByTagName("body");
        if (ns.getLength() > 0)
        {
            e = (Element) ns.item(0);
            totalPage = e.getElementsByTagName("totalCount").item(0).getTextContent();
        }
        return totalPage;
    }

    //페이지 넘겨서 파싱
    public static String turnPage(int page_no) {
        return getUrl(baseUrl)+page_no;
    }

    public static int getPageNum() throws SAXException, IOException, ParserConfigurationException {
        page = getNumber();
        System.out.println(page);
        page_num = Integer.parseInt(page);
        //페이지를 나눠서
        if(page_num%10!=0) {
            page_num=page_num/10+1;
        }else {
            page_num=page_num/10;
        }
        return page_num;
    }

    public static void writeCSV(String[] station) throws SAXException, IOException, ParserConfigurationException {
        page_num = getPageNum();
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../../../../../assets/trainSchedule/"+station_code+"_"+day_code+"_"+up_down+".csv"), "utf-8"));
        String[] entries = "Train No., Line, Station Code, Station, U/D, Day Code, Departure Time, Arrival Time, Destination".split(",");
        for(String e :entries) {
            bw.append(e);
            bw.append(",");
        }
        bw.newLine();
        int train=0;
        for(int p=1;p<=page_num;p++) {
            System.out.println(p);
            URL path = new URL(turnPage(p));
            try{
                //연결설정
                HttpURLConnection con = (HttpURLConnection)path.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept-language", "ko");

                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());
                Element e;
                NodeList ns = doc.getElementsByTagName("header");


                if (true)
                {   ns = doc.getElementsByTagName("item");
                    //페이지 값과 페이지
                    for (int i = 0; i < ns.getLength(); i++){
                        train++;
                        String[] time_table = new String[9];
                        e = (Element)ns.item(i);
                        NodeList childList = e.getChildNodes();
                        if(childList.getLength()>0) {
                            time_table[0] = Integer.toString(train);
                            time_table[1] = station[0];
                            System.out.println(station[0]);
                            time_table[2] = station[1];
                            time_table[3] = station[2];
                            time_table[4] = up_down;
                            time_table[5] = day_code;
                            time_table[6] = childList.item(2).getTextContent();
                            time_table[7] = childList.item(0).getTextContent();
                            time_table[8] = childList.item(4).getTextContent();

                            for(int j=0;j<9;j++) {
                                bw.append(time_table[j]);
                                bw.append(",");
                            }

                            //출도착 시간과 해당 열차의 종착지 출력 (레이아웃에 맞춰서)
	                        	/*
	                            System.out.println("출발 시간: "+ childList.item(2).getTextContent());
	                            System.out.println("도착 시간:"+ childList.item(0).getTextContent());
	                            System.out.println("종착지:" + childList.item(4).getTextContent());
	                        	*/
                        }
                        bw.newLine();
                    }
                    bw.flush();
                }
            }catch(Exception e1){
                e1.printStackTrace();
                throw e1;
            }
        }
    }
    //메인 함수
    public static void main(String[] args) throws Exception {

        br = new BufferedReader(new InputStreamReader(new FileInputStream("../../../../../assets/stations/station.csv"), "utf-8"));
        String line = null;

        while((line=br.readLine())!=null){
            String[] station = line.split(",");
            //String encoded = URLEncoder.encode(station[1], "utf-8");

            getParam(station[2],"U", "01");
            writeCSV(station);
            getParam(station[2],"U", "02");
            writeCSV(station);
            getParam(station[2],"U", "03");
            writeCSV(station);
            getParam(station[2],"D", "01");
            writeCSV(station);
            getParam(station[2],"D", "02");
            writeCSV(station);
            getParam(station[2],"D", "03");
            writeCSV(station);

            //br.newLine();
        }

        br.close();

    }

}
