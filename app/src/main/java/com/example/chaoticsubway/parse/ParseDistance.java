package com.example.chaoticsubway.parse;


//역 거리
//7，9호선 아직 완성 못함. 이후에 다른 api를 통해 추가 필요
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;


import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ParseDistance {
    static String key = "6f56486c4973737736386765525a72";
    static String base_url = "http://openapi.seoul.go.kr:8088/"+key+"/xml/StationDstncReqreTimeHm/1/";

    static String[] n = new String[2];
    static String[] line_num={"2","4"};

    static BufferedReader br = null;
    static BufferedWriter bw = null;
    //static int sum=0;

    public static String getURL(String base_url, String num, String line) {
        String new_url = base_url + num +"/"+line;
        return new_url;
    }

    public static void writeHead() throws IOException {

        String[] entries = "Line, Station Code, Station , Distance, Total Distance, Time".split(",");
        for(String e :entries) {
           // System.out.println(e);
            bw.append(e);
            bw.append(",");
        }
    }

    public static void writeCSV(URL path) throws Exception{
        bw.newLine();
        String[] station = new String[6];
        try{
            //연결설정
            HttpURLConnection con = (HttpURLConnection) path.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept-language", "ko");
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());

            Element e;
            NodeList ns = doc.getElementsByTagName("row");

            String line = null;

            for (int i = 0; i < ns.getLength(); i++)
            {	e = (Element)ns.item(i);//row 하나씩 가져오기

                NodeList childList = e.getChildNodes();
                br = new BufferedReader(new InputStreamReader(new FileInputStream("../../../../../assets/stations/station.csv"), "utf-8"));
                while((line = br.readLine())!=null){
                    String[] newLine = line.split(",");
                    String station_line = URLEncoder.encode(childList.item(1).getTextContent(), "euc-kr");
                    String name = URLEncoder.encode(childList.item(3).getTextContent(), "euc-kr");

                    if(URLEncoder.encode(newLine[0], "euc-kr").contains(station_line) && URLEncoder.encode(newLine[1], "euc-kr").contains(name)){

                        station[0] = newLine[0];
                        station[1] = newLine[2];
                        station[2] = newLine[1];

                        String distance = childList.item(5).getTextContent();
                        String t_distance = childList.item(7).getTextContent();
                        String time = childList.item(9).getTextContent();

                        station[3]= distance;
                        station[4] = t_distance;
                        station[5] = time;
                        for(int a=0;a<6;a++) {
                            System.out.println(station[a]);
                            bw.append(station[a]);
                            bw.append(",");
                        }
                        bw.newLine();
                        break;
                    }

                }
            }

        }catch(Exception e1){
            e1.printStackTrace();
            throw e1;
        }
    }

    public static void getParam() throws IOException, SAXException, ParserConfigurationException
    {
        for(int i=0;i<2;i++) {
            //연결설정
            URL path = new URL(base_url+"10/"+line_num[i]);
            HttpURLConnection con = (HttpURLConnection) path.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept-language", "ko");
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());

            NodeList ns = doc.getElementsByTagName("list_total_count");
            String count = ns.item(0).getTextContent();
            System.out.println(count);
            n[i] = count;
        }
    }

    public static void main(String[] args) throws Exception {
        getParam();
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../../../../../assets/stations/final.csv"), "utf-8"));
        for(int i=0;i<2;i++) {
            String url = getURL(base_url, n[i], line_num[i]);
            System.out.println(url);
            URL path = new URL(url);
            writeHead();
            writeCSV(path);
            //bw.flush();
        }
        bw.flush();
        bw.close();
    }

}