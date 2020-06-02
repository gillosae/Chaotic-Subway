package com.example.chaoticsubway.getInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GetTime {
    static int day_num;
    static String time;
    public static void getDay() {
        Calendar cal = Calendar.getInstance();

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        switch(dayOfWeek){
            case 6:
                day_num = 02;
                break;
            case 7:
                day_num = 03;
                break;
            default:
                day_num = 01;
        }
        System.out.println(day_num);
    }
    public static String dateTime() {
        Calendar cur_time = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
        time = dateFormat.format(cur_time.getTime());
        //System.out.println(time);
        return time;
    }
    public static void main(String[] args){
        getDay();
        String cur_time = dateTime();
        System.out.println(cur_time);
    }
}
