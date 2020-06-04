package com.example.chaoticsubway.getInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//testcode
public class TrainSchedule {
    //역코드 입력받는 코드 추가예정
    static String station_code = "SUB133";//서울역
    static String day_code = "01";
    static String up_down = "U";
    static int page_num;
    static String page;
    static int page_no;

    //상하선 U/D와 페이지 번호, 주일 주중 따라서 (station code, day code, up down code 필요)
    static String baseUrl = "http://openapi.tago.go.kr/openapi/service/SubwayInfoService/getSubwaySttnAcctoSchdulList?"
            +"serviceKey=VuzceYJ7gvgVq%2F5Ajaf%2FBtxHYW9AK187BWM2m1bHXdLL25f6JyQTaaUjUnlf%2BIRz1cAQV0KkOX9sXKVIPszb1w%3D%3D&"
            +"subwayStationId="+station_code+"&dailyTypeCode="+day_code+"&upDownTypeCode="+up_down+"&pageNo=";

    public static String getPageNumber() throws SAXException, IOException, ParserConfigurationException {
        URL path = new URL(baseUrl);
        String totalPage = null;
        //연결설정
        HttpURLConnection con = (HttpURLConnection)path.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept-language", "ko");

        //
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());
        boolean ok = false; // <resultCode>00</resultCode> 획득 여부

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
    public static String getUrl(int page_no) {
        return baseUrl+page_no;
    }
    //메인 함수
    public static void main(String[] args) throws Exception {
        page = getPageNumber();
        System.out.println(page);
        page_num = Integer.parseInt(page);
        //페이지를 나눠서
        if(page_num%10!=0) {
            page_num=page_num/10+1;
        }else {
            page_num=page_num/10;
        }
        for(int p=1;p<=page_num;p++) {
            System.out.println(p);
            URL path = new URL(getUrl(p));
            try{
                //연결설정
                HttpURLConnection con = (HttpURLConnection)path.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept-language", "ko");

                //
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());
                boolean ok = false; // <resultCode>00</resultCode> 획득 여부

                Element e;
                NodeList ns = doc.getElementsByTagName("header");
                if (ns.getLength() > 0)
                {
                    e = (Element) ns.item(0);
                    if (e.getElementsByTagName("resultCode").item(0).getTextContent().equals("00")) {
                        ok = true;// 성공 여부
                    }
                }

                if (ok)
                {   ns = doc.getElementsByTagName("item");
                    //페이지 값과 페이
                    for (int i = 0; i < ns.getLength(); i++){
                        e = (Element)ns.item(i);
                        NodeList childList = e.getChildNodes();
                        if(childList.getLength()>0) {

                            //출도착 시간과 해당 열차의 종착지 출력 (레이아웃에 맞춰서)
                            System.out.println("출발 시간: "+ childList.item(2).getTextContent());
                            System.out.println("도착 시간:"+ childList.item(0).getTextContent());
                            System.out.println("종착지:" + childList.item(4).getTextContent());
                        }
                    }
                }
            }catch(Exception e1){
                e1.printStackTrace();
                throw e1;
            }

        }

    }



}
