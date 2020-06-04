package com.example.chaoticsubway.getInfo;


//역 코드 받아오기
//csv 파일에 넣기
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilderFactory;

//역 이름 검색 시 입력받아서 역 코드 확인
public class StationCode{
    static String baseUrl = "http://openapi.tago.go.kr/openapi/service/SubwayInfoService/getKwrdFndSubwaySttnList?serviceKey=VuzceYJ7gvgVq%2F5Ajaf%2FBtxHYW9AK187BWM2m1bHXdLL25f6JyQTaaUjUnlf%2BIRz1cAQV0KkOX9sXKVIPszb1w%3D%3D&subwayStationName=";

    String station_key;
    static String station_name = null;


    public static String encodeUrl(String name) throws  UnsupportedEncodingException {
        String new_name = URLEncoder.encode(name, "UTF-8");
        return new_name;
    }

    public static void main(String[] args) throws Exception{
        URL path= new URL(baseUrl+encodeUrl(station_name));

        //xml Parser 로 station code 받기
        try{
            //연결설정
            HttpURLConnection con = (HttpURLConnection) path.openConnection();
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
                if (e.getElementsByTagName("resultCode").item(0).getTextContent().equals("00"))
                    ok = true; // 성공 여부
            }

            if (ok)
            {
                ns = doc.getElementsByTagName("item");
                for (int i = 0; i < ns.getLength(); i++)
                {
                    e = (Element)ns.item(i);
                    NodeList childList = e.getChildNodes();
                    if(childList.getLength()>0) {
                        System.out.println("역이름:"+childList.item(2).getTextContent());
                        System.out.println("역코드:"+childList.item(1).getTextContent().substring(3));
                    }
                }
            }
        }catch(Exception e1){
            e1.printStackTrace();
            throw e1;
        }
    }
}
