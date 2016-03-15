package chartdemo.cn.demo.mylibrary;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author  wangchenchen
 * CreateDate 2016/2/2.
 * Email wcc@jusfoun.com
 * Description
 */
public class TimeOut {

    public TimeOut(){
        Date date=new Date();
        System.out.println(date.getTime());
        long l=date.getTime();
        long l1=l/10000*10000;
        System.out.println(l1);
        Date date1=new Date(l1);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy.MM.DD hh:mm");
        String time=dateFormat.format(date);
        String time1=dateFormat.format(date1);
        System.out.println(time);
        System.out.println(time1);
    }

    public static void main(String[] args){
        Date date=new Date();
        System.out.println(date.getTime());
        long l=date.getTime();
        long l1=l/10000*10000;
        System.out.println(l1);
        Date date1=new Date(l1);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy.MM.DD hh:mm");
        String time=dateFormat.format(date);
        String time1=dateFormat.format(date1);
        System.out.println(time);
        System.out.println(time1);
    }
}
