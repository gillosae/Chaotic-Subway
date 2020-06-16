package com.example.chaoticsubway.getInfo;

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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//key = 6e6943477a7373773737504670736d
//전체 환승 역 모두 포함


public class Transfer {
    static String key = "6e6943477a7373773737504670736d";
    static String base_url = "http://swopenapi.seoul.go.kr/api/subway/"+key+"/xml/fastTransfer/0/100/";
    static BufferedReader br = null;
    static BufferedWriter bw = null;

    private static String getURL(String station) throws UnsupportedEncodingException {
        station = URLEncoder.encode(station, "utf-8");
        String url = base_url+station;
        System.out.println(url);
        return url;
    }
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        br = new BufferedReader(new InputStreamReader(new FileInputStream("../../../../../assets/stations/station.csv"), "utf-8"));
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("../../../../../assets/stations/transfer.csv"), "utf-8"));
        String[] header = "호선, 역 코드, 역이름, 방면,U/D,  환승 호선, 환승 후 방면, 빠른 환승 칸, 출입문 위치".split(",");
        for(int i=0;i<header.length;i++){
            bw.append(header[i]);
            bw.append(",");
        }

        bw.newLine();
        String line = br.readLine();
        while(line !=null){
            LineNumberReader line_num = new LineNumberReader(br);
            while((line=line_num.readLine())!=null) {
                int n = line_num.getLineNumber();
                String[] arr = line.split(",");
                if (n > 0) {
                    String get_url = getURL(arr[1]);
                    URL path = new URL(get_url);
                    try {
                        HttpURLConnection con = (HttpURLConnection) path.openConnection();
                        con.setRequestMethod("GET");
                        con.setRequestProperty("Accept-language", "ko");

                        //
                        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());
                        boolean ok = false; // <resultCode>00</resultCode> 획득 여부

                        Element e;
                        NodeList ns = doc.getElementsByTagName("row");
                        System.out.println(ns.getLength());


                        //System.out.println(ns.item(0).getTextContent());
                        //NodeList child = e.getChildNodes();

                        if (ns.getLength()==0) {
                            continue;
                        } else {
                            //System.out.println("ok");
                            String[] row = new String[9];
                            System.out.println(arr[1]);
                            for (int i = 0; i < ns.getLength(); i++) {
                                e = (Element) ns.item(i);
                                NodeList childList = e.getChildNodes();
                                if (childList.getLength() > 0) {
                                        System.out.println("역코드:" + childList.item(3).getTextContent().substring(7, 10));
                                        row[1] = childList.item(3).getTextContent().substring(7, 10);
                                        System.out.println("역이름:" + childList.item(4).getTextContent());
                                        row[2] = childList.item(4).getTextContent();
                                        row[0] = childList.item(6).getTextContent();
                                        row[3] = childList.item(8).getTextContent();
                                        if (row[3].contains("내선") || row[3].contains("상선")) {
                                            row[4] = "U";
                                        } else {
                                            row[4] = "D";
                                        }
                                        row[5] = childList.item(9).getTextContent().substring(3)+"호선";
                                        row[6] = childList.item(10).getTextContent();
                                        row[7] = childList.item(11).getTextContent();
                                        row[8] = childList.item(12).getTextContent();
                                        for (int a = 0; a < row.length; a++) {
                                            bw.append(row[a]);
                                            bw.append(",");
                                        }
                                        bw.newLine();
                                }else{
                                    continue;
                                }
                            }

                        }

                    } catch (Exception e1) {
                        e1.printStackTrace();
                        throw e1;
                    }
                }

            }
        }
        bw.flush();

    }

}
