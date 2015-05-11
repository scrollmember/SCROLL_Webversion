/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ac.tokushima_u.is.ll.util;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author li
 */
public class ConvertUtil {
    public static String  timeToString(Time t, String f){
        if(f==null||f.length()<=0)
            f = "HH:mm:ss";
        if(t==null)
            return "";
        else{
            DateFormat format = new SimpleDateFormat(f);
            return format.format(t);
        }
    }

    public static Time stringToTime(String s){
        if(s==null||s.length()<=0)
            return null;
        try{
            return Time.valueOf(s);
        }catch(Exception e){
            return null;
        }
    }
}
