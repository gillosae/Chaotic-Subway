package com.example.chaoticsubway.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class CalculateCongestion {
    static String code;
    static String up_down;
    static String day;
    static String cur_time;
    static BufferedReader br = null;


    private static void print(String con) throws UnsupportedEncodingException {

        if(con.equals("�л�")){
            System.out.println("�л�");
        }else if(con.equals("ȥ��")){
            System.out.println("ȥ��");
        }else if(con.equals("����")){
            System.out.println("����");
        }else if(con.equals("�γ�")){
            System.out.println("�γ�");
        }
    }
    private static void getRate(int p_num) throws UnsupportedEncodingException {

        int mul = p_num*65;
        int cal = (mul*100)/10500;

        if(cal>=150){
            print("�л�");
        }else if(cal>=130 && cal<150){
            print("ȥ��");
        }else if(cal>=80&&cal<130){
            print("����");
        }else if(cal<80){
            print("�γ�");
        }
    }
/*
    private static void encode(){
        if()
    }*/

    private static int getPeopleNum() throws IOException {
        int t = Integer.valueOf(cur_time);
        int people =0;
        String line = null;

        while((line = br.readLine())!=null) {
            String[] arr = line.split(",");

            if (arr[2].contains(code) && arr[0].equals(day)&&arr[4].equals(up_down)) {
                people = Integer.valueOf(arr[t]);
                //System.out.print(people);
                break;
            }
        }
        return people;
    }
    //���� �ǽð� �� �������� ������ ���� �ʿ�

    private static void getParams() throws UnsupportedEncodingException {
        //�ð��� ��������
        //���� get time Ŭ������ �����Ͽ� �ǽð� �ð� �ҷ��� �� <��> �κи� �����Ͽ� ��
        //GetTime time = new GetTime();
        cur_time ="08";//time.dateTime();
        //System.out.println(cur_time);

        //cur_time = cur_time.substring(8, 10);
        //System.out.println(cur_time);
        //��¥ (����=01, �����=02, �Ͽ���=03 ) �ڵ� ��������
        day="01" ;//time.getDay();
        //System.out.println(Arrays.toString(day.getBytes(StandardCharsets.UTF_8)));
        //�� �ڵ� ��������(������ ���� ����. ���Ŀ� �˻� �� �ش� �˻���� �� �ڵ� �ҷ����� station code�� �����Ͽ� ��������)
        code = "226";
        //������ ���� ���� (���� ���� ���� ���� ����)
        up_down = "D";
    }


    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(new FileInputStream("../../../../../assets/parsed_congestion/num_of_people.csv")));
        getParams();
        getRate( getPeopleNum());


    }
}
