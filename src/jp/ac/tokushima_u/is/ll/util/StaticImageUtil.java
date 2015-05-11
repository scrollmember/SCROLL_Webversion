/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.ac.tokushima_u.is.ll.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author li
 */
public class StaticImageUtil {
	
    public static byte[] getImageFileById(String imageUrl, Integer imagelevel){
        String append = "";
        if(Constants.NormalLevel.equals(imagelevel))
                append = "_800x600.png";
        else if(Constants.SmartPhoneLevel.equals(imagelevel))
        		append = "_320x240.png";
        else if(Constants.MailLevel.equals(imagelevel))
                append = "_160x120.png";
        else if(Constants.IconLevel.equals(imagelevel))
                append = "_80x60.png";
        DataInputStream in = null;
        String photoUrl = imageUrl + append;
        try {
            URL url = new URL(photoUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            in = new DataInputStream(connection.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
            byte[] buffer = new byte[2048];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            in.close();
            out.close();
            return out.toByteArray();
        }catch(Exception e){
            
        }finally{
            try{
                if(in!=null)
                 in.close();
            }catch(Exception e){
            }
        }
       return null;
    }
    
    public static boolean isFileExisting(String imageUrl, Integer imagelevel){
    	byte[] imageBytes = getImageFileById(imageUrl, imagelevel);
    	if(imageBytes != null && imageBytes.length > 0)
    		return true;
    	else
    		return false;
	}

}
