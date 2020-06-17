package com.example.chaoticsubway.parse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class GetNodes {
    static String url = "http://openapi.seoul.go.kr:8088/sample/xml/SearchSTNTimeTableByFRCodeService/1/5/132/1/1/";
    public GetNodes(){
        this.nodes();
    }
    private static void nodes() {
        try{
            URL path = new URL(url);
            HttpURLConnection con = (HttpURLConnection)path.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept-language", "ko");

            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(con.getInputStream());
            Element e;

            if(true){
                NodeList ns = doc.getElementsByTagName("row");
                for (int i = 0; i < ns.getLength(); i++){
                    e = (Element)ns.item(i);
                    //NodeList childList = e.getChildNodes();
                    System.out.println(e.getElementsByTagName("FR_CODE").item(0).getTextContent());
                    //System.out.println(childList.item(4).getTextContent()); //열차번호
                   // System.out.println(childList.item(10).getTextContent());
                }
            }
        } catch (SAXException | ProtocolException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
