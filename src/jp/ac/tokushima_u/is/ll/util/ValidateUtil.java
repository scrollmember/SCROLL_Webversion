/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ac.tokushima_u.is.ll.util;

/**
 *
 * @author lemonrain
 */
public class ValidateUtil {

    public static boolean isEmpty(Object o){
        if(o == null)
            return true;
        else if(o.toString()==null||o.toString().length()<=0)
            return true;
        else
            return false;
    }


}
