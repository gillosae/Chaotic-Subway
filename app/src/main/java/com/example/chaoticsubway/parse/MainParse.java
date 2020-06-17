package com.example.chaoticsubway.parse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainParse {
    static String baseUrl = "http://openapi.tago.go.kr/openapi/service/SubwayInfoService/getKwrdFndSubwaySttnList?serviceKey=VuzceYJ7gvgVq%2F5Ajaf%2FBtxHYW9AK187BWM2m1bHXdLL25f6JyQTaaUjUnlf%2BIRz1cAQV0KkOX9sXKVIPszb1w%3D%3D&subwayStationName=";
    String station_key;
    static BufferedWriter bs = null;
    static String page;
    static int page_num;

    public MainParse(){
        this.Parse();
    }

    private static BufferedWriter createCSV(String filepath) throws IOException {
        bs = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), "utf-8"));
        String[] entries = "Line, Station, Station Code, Distance, Total Distance, Time, Total Time".split(",");
        for(String e :entries){
            bs.append(e);
            bs.append(",");
        }
        return bs;
    }

    private static void putStation(String line, String name, String key) throws IOException{
        bs.newLine();
        String[] station = new String[3];

        station[0] = line;
        station[1] = name;
        station[2] = key.substring(3);

        for(int i=0;i<station.length;i++) {
            System.out.println(station[i]);
            bs.append(station[i]);
            bs.append(",");
        }
    }

    private static void close() throws IOException {
        bs.flush();
        bs.close();
    }

    private static String getUrl(int page_no) {
        return baseUrl+"&pageNo="+page_no;
    }

    private static String getPageNumber() throws SAXException, IOException, ParserConfigurationException {
        URL path = new URL(baseUrl);
        String totalPage = null;
        //연결설정
        HttpURLConnection con = (HttpURLConnection)path.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept-language", "ko");

        //
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());

        Element e;
        NodeList n = doc.getElementsByTagName("body");
        if (n.getLength() > 0)
        {
            e = (Element) n.item(0);
            totalPage = e.getElementsByTagName("totalCount").item(0).getTextContent();
        }
        return totalPage;
    }
    public static void Parse(){
        //파일 위치 추후 변경
        try {
            bs = createCSV("../../../../../assets/stations/station.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            page = getPageNumber();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        System.out.println(page);
        page_num = Integer.parseInt(page);
        //페이지를 나눠서
        if(page_num%10!=0) {
            page_num=page_num/10+1;
        }else {
            page_num=page_num/10;
        }
        for(int p=0;p<=page_num;p++) {
            System.out.println(p);
            URL newp = null;
            try {
                newp = new URL(getUrl(p));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try{
                //연결설정
                HttpURLConnection con = (HttpURLConnection) newp.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept-language", "ko");

                //
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());

                Element e;

                NodeList ns = doc.getElementsByTagName("header");

                if (true)
                {
                    NodeList s = doc.getElementsByTagName("item");
                    for (int i = 0; i < s.getLength(); i++)
                    {
                        e = (Element)s.item(i);
                        NodeList childList = e.getChildNodes();

                        if(childList.getLength()>0) {
                            String line = childList.item(0).getTextContent();
                            String station_name = childList.item(2).getTextContent();
                            String station_key = childList.item(1).getTextContent();

                            //전호선 데이터 정보 받아오기
                            if(station_key.contains("SUB1")&&(station_key.length()==6) || station_key.contains("SUB11")&&(station_key.length()==7) || station_key.contains("SUB11")&&(station_key.length()==7)){
                                putStation(line, station_name, station_key);
                            }else if(station_key.contains("SUB2")&&(station_key.length()==6)) {
                                putStation(line, station_name, station_key);
                            }else if(station_key.contains("SUB3")&&(station_key.length()==6)) {
                                putStation(line, station_name, station_key);
                            }else if(station_key.contains("SUB4")&&(station_key.length()==6)) {
                                putStation(line, station_name, station_key);
                            }else if(station_key.contains("SUB5")&&(station_key.length()==6)) {
                                putStation(line, station_name, station_key);
                            }else if(station_key.contains("SUB6")&&(station_key.length()==6)) {
                                putStation(line, station_name, station_key);
                            }else if(station_key.contains("SUB7")&&(station_key.length()==6)) {
                                putStation(line, station_name, station_key);
                            }else if(station_key.contains("SUB8")&&(station_key.length()==6)) {
                                putStation(line, station_name, station_key);
                            }else if(station_key.contains("SUB9")&&(station_key.length()==6)) {
                                putStation(line, station_name, station_key);
                            }else {
                                continue;
                            }
                        }
                    }

                }
            }catch(Exception e1){
                e1.printStackTrace();
                try {
                    throw e1;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}